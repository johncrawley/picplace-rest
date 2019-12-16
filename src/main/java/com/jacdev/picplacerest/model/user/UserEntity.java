package com.jacdev.picplacerest.model.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name="users")
public class UserEntity implements UserDetails {
	
	@Id
	private String username;
	private boolean enabled;
	private String email, password, firstname, lastname, gender, countryCode;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="authorities",
					 joinColumns = {
							 @JoinColumn(name = "username", referencedColumnName = "username")
					 })
	private Set<UserRole> authorities = new HashSet<>();
	

	public UserEntity() {
		
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	
	public void setAuthorities(Set<UserRole> authorities) {this.authorities = authorities;
			System.out.println("UserEntity authorities retrieved count: " + authorities.size());
			authorities.stream().map(a -> "retrieved--->> a.getAuthority())").forEach(System.out::println);
			
	}
	public Set<UserRole> getAuthorities(){return this.authorities;}
	
	@Basic public boolean isEnabled() {return this.enabled;}
    @Basic public String getEmail() 	{ return email; }
    @Basic public String getFirstname() { return firstname; }
    @Basic public String getLastname() 	{ return lastname; }
    @Basic public String getGender() 	{ return gender; }
    @Basic
    @Column(name="country_code")
    public String getCountrycode(){ return this.countryCode; }
    
	public void addRole(String role) {authorities.add(new UserRole(role));}
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}
	public void setEmail(String email) {this.email = email; System.out.println("User Entity...Setting email: " + email);}
	public void setEnabled(boolean enabled) {this.enabled = enabled;}
    public void setCountrycode(String countryCode) {this.countryCode = countryCode;}
    public void setGender(String gender) {this.gender = gender;}
    public void setFirstname(String firstName) {this.firstname = firstName;}
    public void setLastname(String lastName) {this.lastname = lastName;}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

}
