package sms.plateserv.InnoComm.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sms.plateserv.InnoComm.entitys.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query(value = "select * from role where role_name=:roleName", nativeQuery = true)
	public Set<Role> findByRoleName(String roleName);
}
