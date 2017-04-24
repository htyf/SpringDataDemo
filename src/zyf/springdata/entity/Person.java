package zyf.springdata.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name="jpa_parson")
@Entity
public class Person {
	private Integer id;
	private String lastName;
	private String emile;
	private Date birth;
	private Address adderss;
	private Integer addresssId;
	@GeneratedValue
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmile() {
		return emile;
	}
	public void setEmile(String emile) {
		this.emile = emile;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
	@JoinColumn(name="address_id")
	@ManyToOne
	public Address getAdderss() {
		return adderss;
	}
	public void setAdderss(Address adderss) {
		this.adderss = adderss;
	}
	
	
	public Integer getAddresssId() {
		return addresssId;
	}
	public void setAddresssId(Integer addresssId) {
		this.addresssId = addresssId;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", lastName=" + lastName + ", emile="
				+ emile + ", birth=" + birth + ", adderss=" + adderss + "]";
	}
	
	
	

}
