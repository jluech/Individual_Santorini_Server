package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@JsonIgnoreProperties(value={"password"}, allowSetters = true)
public class User implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private String lastname;

	private String firstname;

	@Column(nullable = false)
	private Date birthdate;

	private String birthdateStr;
	
	@Column(nullable = false, unique = true) 
	private String username;

	@Column(nullable = false)
	@JsonProperty("password")
	private String password;

	//TODO: attach currentPassword to methods (create, update, get)
	//@Column(nullable = false)
	//private String currentPassword;
	
	@Column(nullable = false, unique = true) 
	private String token;

	@Column(nullable = false)
	private UserStatus status;

	@Column(nullable = false)
	private Date creationDate;

	private String creationDateStr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) { this.id = id; }

	public String getLastName() {
		return lastname;
	}

	public void setLastName(String newlastname) {
		this.lastname = newlastname;
	}

	public String getFirstName() { return firstname; }

	public void setFirstName(String newfirstname) {
		this.firstname = newfirstname;
	}

	public Date getBirthdate() { return birthdate; }

	public void setBirthdate(Date birthDate) { this.birthdate = birthDate; }

	public String getBirthdateStr() { return birthdateStr; }

	public void setBirthdateStr(String birthdateStr) { this.birthdateStr = birthdateStr; }

	public String getUsername() { return username; }

	public void setUsername(String username) { this.username = username; }

	//TODO: security reason: add currentPassword field for which to check against before assigning new password value
	//@JsonIgnore
	public String getPassword() { return password; }

	//@JsonProperty
	public void setPassword(String password) { this.password = password; }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public Date getCreationDate() { return creationDate; }

	public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

	public String getCreationDateStr() { return creationDateStr; }

	public void setCreationDateStr(String creationDateStr) { this.creationDateStr = creationDateStr; }

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) {
			return false;
		}
		User user = (User) o;
		return this.getId().equals(user.getId());
	}
}
