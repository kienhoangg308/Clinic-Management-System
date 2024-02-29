package com.clinic.admin.user;

import com.clinic.common.entity.Role;
import com.clinic.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    @Query("Select u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("email") String email);

    public Long countById(Integer id);

    @Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ',"
            + " u.lastName) LIKE %?1%")
    public Page<User> findAll(String keyword, Pageable pageable);

//    @Query("SELECT u FROM User u WHERE CONCAT(CAST(u.id AS string), ' ', u.email, ' ', u.firstName, ' ',"
//            + " u.lastName) LIKE %?1%")
//    public Page<User> findAll(String keyword, Pageable pageable);

    @Query("Update User u SET u.enabled =?2 WHERE u.id=?1")
    @Modifying
    void updateUserStatus(Integer id, boolean enabled);

    //Page<User> findByKeywordAndRole(String keyword, String role, Pageable pageable);

    Page<User> findByRole(Role role, Pageable pageable);

//    @Query("SELECT u FROM User u WHERE ( u.email LIKE %?1% OR u.firstName LIKE %?1% OR u.lastName LIKE %?1%) AND u.role.name = ?2")
//    Page<User> findByKeywordAndRole(String keyword, String roleName, Pageable pageable);


    @Query("SELECT u FROM User u WHERE (str(u.id) LIKE %?1% OR u.email LIKE %?1% OR u.firstName LIKE %?1% OR u.lastName LIKE %?1%OR u.degree LIKE %?1%) AND u.role.name = ?2")
    Page<User> findByKeywordAndRole(String keyword, String roleName, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role.name = ?1")
    List<User> findByRoleName(String roleName);
}
