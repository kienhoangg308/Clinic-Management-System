package com.clinic.admin.user;

import com.clinic.common.entity.Role;
import com.clinic.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    public static final int USERS_PER_PAGE = 4;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    public List<User> listAll(){
        return (List<User>) userRepository.findAll();
    }


    public User save(User user, String roleName){
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {
            User existingUser = userRepository.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }

        } else {
            encodePassword(user);
        }
        Role role = roleRepository.findByName(roleName);
        user.setRole(role);
        return userRepository.save(user);
    }



    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword, String rolename) {
        Sort sort = Sort.by(sortField);

        Role role = roleRepository.findByName(rolename);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);

        if (keyword != null) {
            return userRepository.findByKeywordAndRole(keyword, rolename, pageable);
        }

        return userRepository.findByRole(role, pageable);
    }

    public void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id,String email) {
        User userByEmail = userRepository.getUserByEmail(email);

        if (userByEmail == null) return true;

        boolean isCreatingNew = (id == null);

        if (isCreatingNew) {
            if (userByEmail != null) return false;
        } else {
            if (userByEmail.getId() != id) {
                return false;
            }
        }

        return true;
    }

    public User get(Integer id) throws UserNotFoundException{
        try{
            User user = userRepository.findById(id).get();
            return user;
        }catch(NoSuchElementException ex){
            throw new UserNotFoundException("Can not find user with id: " + id);
        }

    }

    public void delete(Integer id) throws UserNotFoundException{
        Long countById = userRepository.countById(id);
        if(countById == null || countById == 0){
            throw new UserNotFoundException("Can not find user with id: " + id);
        }
        userRepository.deleteById(id);

    }

    public void updateUserStatus(Integer id, boolean enabled) {
        userRepository.updateUserStatus(id, enabled);
    }

    public List<User> listAllDoctor() {
        Role role = roleRepository.findById(2).get();
        return userRepository.findByRoleName("Doctor");
    }

    public List<User> listAllStaff() {
        Role role = roleRepository.findById(3).get();
        return userRepository.findByRoleName("Staff");
    }

    public User getByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }


    public User updateAccount(User userInForm) {
        User userInDB = userRepository.findById(userInForm.getId()).get();

        if (!userInForm.getPassword().isEmpty()) {
            userInDB.setPassword(userInForm.getPassword());
            encodePassword(userInDB);
        }

        if (userInForm.getPhotos() != null) {
            userInDB.setPhotos(userInForm.getPhotos());
        }

        userInDB.setFirstName(userInForm.getFirstName());
        userInDB.setLastName(userInForm.getLastName());


        return userRepository.save(userInDB);
    }
}
