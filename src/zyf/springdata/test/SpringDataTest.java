package zyf.springdata.test;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import zyf.springdata.dao.PersonJpaRepository;
import zyf.springdata.dao.PersonJpaSpecificationExcutor;
import zyf.springdata.dao.PersonPagingAndSortingRepository;
import zyf.springdata.dao.PersonRepository;
import zyf.springdata.entity.Person;
import zyf.springdata.service.PersonService;

public class SpringDataTest {
	private ApplicationContext cxt=null;
	private PersonRepository pr= null;
	private PersonService ps;
	private PersonPagingAndSortingRepository ppsr;
	private PersonJpaRepository pjr;
	private PersonJpaSpecificationExcutor pjse;
	private List<Person> persons = null;

	{
		cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
		pr= cxt.getBean(PersonRepository.class);
		ps=cxt.getBean(PersonService.class);
		ppsr=cxt.getBean(PersonPagingAndSortingRepository.class);
		pjr = cxt.getBean(PersonJpaRepository.class);
		pjse = cxt.getBean(PersonJpaSpecificationExcutor.class);
	}
	/**
	 * 测试数据源是否配置成功
	 * @throws SQLException
	 */
	@Test
	public void testDataSource() throws SQLException {
		DataSource ds = cxt.getBean(DataSource.class);
		System.out.println(ds.getConnection());
	}
	@Test
	public void testJPA() throws SQLException {
	}

	@Test
	public void testKeyWords(){
		List<Person> persons = pr.getByLastNameStartingWithAndIdLessThan("x", 4);
		System.out.println(persons);

		persons = pr.getByLastNameEndingWithAndIdLessThan("wu", 4);
		System.out.println(persons);

		persons = pr.getByEmileInOrBirthLessThan(Arrays.asList("wangwu@qq.com","lisi@qq.com","zhaoliu@126.com"), new Date());
		System.out.println(persons);
	}

	@Test
	public void testKeyWords2(){
		List<Person> persons = pr.getByAdderssIdGreaterThan(1);
		System.out.println(persons);

	}

	@Test
	public void testHelloSpringData(){
		Person person = pr.getByLastName("zhangsan");
		System.out.println(person);
	}

	@Test
	public void testQueryAnnotationParam(){
		Person person = pr.getMaxIdPerson();
		System.out.println(person);

		persons = pr.testQueryAnnotationParam("x_wangwu", "wangwu@sina.com");
		System.out.println(persons);
		persons = pr.testQueryAnnotationParam2( "wangwu@sina.com","x_wangwu");
		System.out.println(persons);
	}

	@Test
	public void testQueryAnnotationLikeParam(){
		persons = pr.testQueryAnnotationLikeParam("%wangwu%", "%@sina.com%");
		persons = pr.testQueryAnnotationLikeParam2("wangwu", "@sina.com");
		persons = pr.testQueryAnnotationLikeParam3("@sina.com", "wangwu");
		System.out.println(persons);
	}

	@Test
	public void testSQL(){
		long totalCount = pr.getTotalCount();
		System.out.println(totalCount);
	}

	@Test
	public void testModifying(){
		//		 pr.updatePersonEmail("zhsan@2334.com",1);//不可以
		ps.updatePersonEmail("zhsan@2334.com",1);
	}
	@Test
	public void testCrudRepository(){
		List<Person> persons = new ArrayList<>();
		for(int i='a';i<='z';i++){
			Person p = new Person();
			p.setAddresssId(i+1);
			p.setBirth(new Date());
			p.setEmile((char)i+""+(char)i+"@12306.com");
			p.setLastName((char)i+""+(char)i);
			persons.add(p);
		}
		ps.savePersons(persons);
	}
	@Test
	public void testPersonPagingAndSortingRepository(){
		//pageNo从0开始
		int pageNo=3;//代表第四页
		int pageSize=5;//每页显示的记录数
		//Pageable接口通常使用的是其PageRequest子类  其中封装了需要分页的信息
		//排序相关的 Sort封装了排序的信息
		//Order是根据某一属性是升序还是降序
		Order order1 = new Order(Direction.DESC, "id");
		Order order2 = new Order(Direction.ASC, "emile");
		Sort sort = new Sort(order1,order2);//Sort封装了排序的信息
		//		PageRequest pageable = new PageRequest(pageNo, pageSize);
		PageRequest pageable = new PageRequest(pageNo, pageSize,sort);
		Page<Person> page = ppsr.findAll(pageable);
		System.out.println("总记录数:"+page.getTotalElements());
		System.out.println("当前页码："+(page.getNumber()+1));
		System.out.println("总页数："+page.getTotalPages());
		System.out.println("当前页面的List:"+page.getContent());
		System.out.println("当前页码的记录数："+page.getNumberOfElements());
	}
	@Test
	public void testPersonJpaRepository(){
		Person p = new Person();
		p.setBirth(new Date());
		p.setEmile("XyZ@123.COM");
		p.setLastName("XyZ");
		Person person = pjr.saveAndFlush(p);
		System.out.println(p==person);
	}
	/**
	 * 实现带查询条件的分页---》  id>5的条件
	 * 调用JpaSpecificationExcutor的  方法：
	 * Specification：封装了JPA Criteria查询的查询条件
	 * Pageable：封装了请求分页的信息。如pageNo pageSize  Sort 
	 * public Page<Person> findAll(Specification<Person> arg0, Pageable arg1);
	 */
	@Test
	public void testPersonJpaSpecificationExcutor(){
		int pageNo=3-1;
		int pageSize = 5;
		PageRequest pageable = new PageRequest(pageNo, pageSize);
		//通常使用Specification的匿名内部类
		Specification<Person > specification =new Specification<Person>() {
			/**
			 * @param *root:代表查询的实体类
			 * @param query:可以从中得到root对象，即高中JPA Criteria查询要
			 * 查询哪一个实体类，还可以来添加查询条件，还可以结合EntityManager对象得到最终
			 * 查询的TypeQuery对象
			 * @param *cb:CriteriaBuilder对象，用于创建Criteria相关对象的工厂，可以
			 * 从中获取到Predicate对象
			 * @return:Predicate类型，代表一个查询条件
			 */
			@Override
			public Predicate toPredicate(Root<Person> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				//目标：id>5
				Path path = root.get("id");
				Predicate Predicate=cb.gt(path, 5);
				
				return Predicate;
			}
		};
		
		Page<Person> page = pjse.findAll(specification,pageable);
		System.out.println("总记录数:"+page.getTotalElements());
		System.out.println("当前页码："+(page.getNumber()+1));
		System.out.println("总页数："+page.getTotalPages());
		System.out.println("当前页面的List:"+page.getContent());
		System.out.println("当前页码的记录数："+page.getNumberOfElements());
	}
}
