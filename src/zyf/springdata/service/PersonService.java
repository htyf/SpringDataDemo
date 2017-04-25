package zyf.springdata.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zyf.springdata.dao.PersonCrudRepository;
import zyf.springdata.dao.PersonRepository;
import zyf.springdata.entity.Person;

@Service
public class PersonService {

	@Resource
	private PersonRepository personRepository;
	@Resource
	private PersonCrudRepository personCrudRepository;
	@Transactional
	public void savePersons(List<Person> persons){
		personCrudRepository.save(persons);
	}

	@Transactional
	public void updatePersonEmail(String email ,Integer id){
		personRepository.updatePersonEmail(email, id);
	}

}
