/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sms.plateserv.InnoComm.entitys;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sms.plateserv.InnoComm.enums.RoleName;

/**
 * The type Role. Defines the role and the list of users who are associated with
 * that role
 */
@Entity(name = "ROLE")
//@Table(catalog = "innocomDB", name = "ROLE")
public class Role {

	@Id
	@Column(name = "ROLE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@NaturalId
	@Column(name = "ROLE_NAME")
	@Enumerated(EnumType.STRING)
	private RoleName role;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@JsonIgnore
	private Set<User> userList = new HashSet<>();

	public Role(RoleName role) {
		this.role = role;
	}

	public Role() {

	}

	public boolean isAdminRole() {
		return null != this && this.role.equals(RoleName.ROLE_ADMIN);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleName getRole() {
		return role;
	}

	public void setRole(RoleName role) {
		this.role = role;
	}

	public Set<User> getUserList() {
		return userList;
	}

	public void setUserList(Set<User> userList) {
		this.userList = userList;
	}
}
