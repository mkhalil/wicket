package com.wct.models;

import org.apache.wicket.model.Model;

import com.wct.dao.CustomerDAO;
import com.wct.dao.DAOException;
import com.wct.entities.Customer;

public class CustomerModel extends Model<Customer> {

	private Long id;
	private transient Customer customer;
	
	public CustomerModel() {
	}
	
	public CustomerModel(Customer customer) {
		setObject(customer);
	}
	
	public CustomerModel(Long id) {
		this.id = id;
	}
	
	@Override
	public Customer getObject() {
		System.out.println("----------------GET OBJECT-------------");
		System.out.println("------CUSTOMRE FRPOM getObject ------------- " + customer);
		if (customer != null) {
			return customer;
		} 
		if (id == null) {
			customer = new Customer();
		} else {
			try {
				CustomerDAO customerDAO = new CustomerDAO();
				customer = customerDAO.findById(id);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				customer = null;
			}
		}
		return customer;
	}
	
	@Override
	public void setObject(Customer customer) {
		this.customer = customer;
		id = (customer == null) ? null:customer.getId();
	}
	
	@Override
	public void detach() {
		System.out.println("------DETACH--------");
		this.customer = null;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	
}
