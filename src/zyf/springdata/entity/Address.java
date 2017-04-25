package zyf.springdata.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="jpa_address")
@Entity
public class Address {
	private Integer id;
	private String province;
	private String city ;
	
	@GeneratedValue
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Address(Integer id, String province, String city) {
		super();
		this.id = id;
		this.province = province;
		this.city = city;
	}
	@Override
	public String toString() {
		return "Address [id=" + id + ", province=" + province + ", city="
				+ city + "]";
	}
	

}
