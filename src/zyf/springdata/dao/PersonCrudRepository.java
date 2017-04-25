package zyf.springdata.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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

public interface PersonCrudRepository extends CrudRepository<Person, Integer>{
	
}
