package zyf.springdata.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import zyf.springdata.dao.PersonRepository;
import zyf.springdata.entity.Person;

public class SpringDataTest {
	private ApplicationContext cxt=null;
	PersonRepository pr= null;

	{
		cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
		pr= cxt.getBean(PersonRepository.class);
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
}
