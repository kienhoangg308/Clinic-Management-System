package com.clinic.customer;

import com.clinic.common.entity.Customer;
import com.clinic.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class CustomerService {


	@Autowired private CustomerRepository customerRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;


	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}

	public void save(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(true);
		customerRepo.save(customer);
	}

	public void encodePassword(Customer customer){
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}
}