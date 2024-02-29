package com.clinic.user;

import com.clinic.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.role.name = ?1")
    List<User> findByRoleName(String roleName);
}
