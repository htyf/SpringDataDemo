package zyf.springdata.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.RepositoryDefinition;

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
}
