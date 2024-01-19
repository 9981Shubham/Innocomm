package sms.plateserv.InnoComm.entitys;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Proxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//@Table(catalog = "innocomDB", schema = "user_schema", name = "USER")
@Entity(name = "_EMPLOYEE")
@Proxy(lazy = false)
public class User implements UserDetails {

	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "user_seq", allocationSize = 1)
	private Long id;

	@NaturalId
	@Column(name = "EMAIL", unique = true)
	@NotBlank(message = "User email cannot be null")
	private String email;

	@Column(name = "USERNAME", unique = true)
	private String username;

	@Column(name = "PASSWORD")
	@NotNull(message = "Password cannot be null")
	private String password;

	@Column(name = "IS_ACTIVE", nullable = false)
	private Boolean active;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@JoinTable(name = "USER_AUTHORITY",joinColumns = {
			@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID") }, inverseJoinColumns = {
					@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID") })
	private Set<Role> roles = new HashSet<>();

	public User() {
		super();
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USERWALLET_ID", referencedColumnName = "Wallet_ID", insertable = true, updatable = true)
	private UserWallet userWallet;

	public UserWallet getUserWallet() {
		return userWallet;
	}

	public void setUserWallet(UserWallet userWallet) {
		this.userWallet = userWallet;
	}

	public User(User user) {
		if (user != null) {
			id = user.getId();
			username = user.getUsername();
			password = user.getPassword();
			email = user.getEmail();
			active = user.getActive();
			roles = user.getRoles();
		}
	}

	public void addRole(Role role) {
		roles.add(role);
		role.getUserList().add(this);
	}

	public void addRoles(Set<Role> roles) {
		roles.forEach(this::addRole);
	}

	public void removeRole(Role role) {
		roles.remove(role);
		role.getUserList().remove(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> authorities) {
		roles = authorities;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", email='" + email + '\'' + ", username='" + username + '\'' + ", password='"
				+ password + '\'' + ", active=" + active + ", roles=" + roles + '}';
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}