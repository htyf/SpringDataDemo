package zyf.springdata.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import zyf.springdata.entity.Person;

/**
 * 1.Repository是一个空接口，即是一个标记接口
 * 2.若我们定义的接口继承了Repository，则该接口会被IOC容器
 * 识别为一个Repository Bean  纳入IOC容器中，进而可以在该
 * 接口中定义满足一定规范的方法。
 * 
 * 3.可以通过@RepositoryDefinition注解来替代继承Repository接口
 * 
 * Repository<Person, Integer>:第一个类型是需要处理对象的类型
 * 								第二个类型是主键的类型
 * @author yanfangzhang
 *
 */

@RepositoryDefinition(domainClass = Person.class, idClass = Integer.class)
public interface PersonRepository/* extends Repository<Person, Integer>*/{
	/**
	 * 根据lastName来获取对应的person
	 * @param lastName
	 * @return
	 */
	Person getByLastName(String lastName);
	/**
	 * Repository及其子接口中定义方法的规范：
	 * 1.不能随便声明，而需要符合一定的规范
	 * 2.查询方法一find|read|get开头
	 * 3.涉及条件查询时，条件的属性用条件关键字连接
	 * 4.要注意的是：条件属性以首字母大写
	 * 5.支持属性的级联查询,如果当前类有符合条件的属性，
	 * 则优先使用，而不使用级联属性，若需要使用级联属性，
	 * 则属性直接使用_进行连接
	 * 这种方式麻烦且不灵活，推荐使用@Query注解
	 */
	//where lastName LIKE ?% And id<?
	List<Person> getByLastNameStartingWithAndIdLessThan(String lastName,Integer id);

	//where lastName LIKE %? And id<?
	List<Person> getByLastNameEndingWithAndIdLessThan(String lastName,Integer id);

	//where email In(?,?,?) OR birth<?
	List<Person> getByEmileInOrBirthLessThan(List<String> emails,Date birth);

	//WHERE a.id>? 
	List<Person> getByAdderssIdGreaterThan(Integer id);
	/**
	 * 使用@Query 注解可以自定义JPQL语句以实现更灵活的查询
	 * 注意：JPQL语句中用到的是字段和类名
	 * @return
	 */
	@Query("select p from Person p where p.id= (select max(p2.id) from Person p2)")
	Person getMaxIdPerson();
	
	/**
	 * 使用@Query注解时传递参数的方式：1.使用占位符
	 * 注意：顺序必须一致   占位符后面的数字代表的是由参数列表中的第几个参数替代 
	 * 如果写成where p.lastName=?2 and p.emile=?1")，则参数列表也要跟着变为(String emile,String lastName);
	 * @return
	 */
	@Query("select p from Person p where p.lastName=?1 and p.emile=?2")
	List<Person> testQueryAnnotationParam(String lastName,String emile);
	/**
	 * 使用@Query注解时传递参数的方式：2.命名参数的方式
	 * 注：这种方式参数列表可以不按顺序，只需要在相应参数前面使用@Param声明即可
	 * 
	 * 推荐使用这种方式
	 * @return
	 */
	@Query("select p from Person p where p.lastName=:lastName and p.emile=:emile")
	List<Person> testQueryAnnotationParam2(@Param("emile") String emile,@Param("lastName") String lastName);
	
	/**
	 * 第一种方式：传参的时候通配符也要传入
	 * @return
	 */
	@Query("select p from Person p where p.lastName like ?1 and p.emile like ?2")
	List<Person> testQueryAnnotationLikeParam(String lastName,String emile);
	/**
	 * springdata 允许在占位符上添加通配符%  也可以是命名参数
	 * 注：spring-data-jpa版本必须在3.1以上
	 * @return
	 */
	@Query("select p from Person p where p.lastName like %?1% and p.emile like %?2%")
	List<Person> testQueryAnnotationLikeParam2(String lastName,String emile);
	/**
	 * springdata 允许在占位符上添加通配符%  也可以是命名参数
	 * @return
	 */
	@Query("select p from Person p where p.lastName like %:lastName% and p.emile like %:email%")
	List<Person> testQueryAnnotationLikeParam3(@Param("email") String emile,@Param("lastName") String lastName);
	
	/**
	 * 设置nativeQuery=true 就可以使用原生sql查询
	 * @return
	 */
	@Query(value="select count(id) from jpa_parson",nativeQuery=true)
	long getTotalCount();
	
	/**
	 * 可以通过自定义JPQL完成update/delete操作
	 * 注意：JPQL不支持insert操作
	 * 在@Query注解中拼写JPQL语句，但是必须用@Modifying进行修饰，以便
	 * 通知springdata 这是一个update/delete操作，需要使用事务，所以
	 * 需要定义service层，在事务层的方法上添加事务操作
	 * 默认情况下，springdata的方法上都有一个只读的事务，不能完成修改操作
	 * @param email
	 * @param id
	 */
	@Modifying
	@Query("update Person p set p.emile=:email where id=:id")
	void updatePersonEmail(@Param("email") String email,@Param("id")Integer id);
	
}
