package com.clinic.admin.user;

import com.clinic.common.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(String rolename);
}
