package com.clinic.admin.customer;


import com.clinic.admin.user.UserNotFoundException;
import com.clinic.common.entity.Customer;
import com.clinic.common.entity.Role;
import com.clinic.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.NoSuchElementException;

@Service
@Transactional
public class CustomerService {

    public static final int CUSTOMER_PER_PAGE = 4;


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMER_PER_PAGE, sort);

        if (keyword != null) {
            return customerRepository.findAll(keyword, pageable);
        }

        return customerRepository.findAll(pageable);
    }

    public Customer save(Customer user){
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {
            Customer existingUser = customerRepository.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }

        } else {
            encodePassword(user);
        }
        return customerRepository.save(user);
    }


    public void encodePassword(Customer user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id,String email) {
        Customer userByEmail = customerRepository.findByCustomerEmail(email);

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


    public Customer get(Integer id) throws CustomerNotFoundException {
        try{
            Customer user = customerRepository.findById(id).get();
            return user;
        }catch(NoSuchElementException ex){
            throw new CustomerNotFoundException("Can not find user with id: " + id);
        }

    }

    public void delete(Integer id) throws CustomerNotFoundException{
        Long countById = customerRepository.countById(id);
        if(countById == null || countById == 0){
            throw new CustomerNotFoundException("Can not find user with id: " + id);
        }
        customerRepository.deleteById(id);

    }

    public void updateUserStatus(Integer id, boolean enabled) {
        customerRepository.updateCustomerStatus(id, enabled);
    }
}
