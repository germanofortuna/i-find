package br.com.ifind.model;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission implements GrantedAuthority, Serializable{
	
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //auto incremental
	private Long id;
	
	@Column
	private String descripiton;
	
	public Permission() {
	}

	@Override
	public String getAuthority() {
		return this.descripiton;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripiton() {
		return descripiton;
	}

	public void setDescripiton(String descripiton) {
		this.descripiton = descripiton;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descripiton, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permission other = (Permission) obj;
		return Objects.equals(descripiton, other.descripiton) && Objects.equals(id, other.id);
	}
}
