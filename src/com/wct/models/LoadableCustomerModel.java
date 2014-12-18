package com.wct.models;

import org.apache.wicket.model.LoadableDetachableModel;

import com.wct.dao.CustomerDAO;
import com.wct.dao.DAOException;
import com.wct.entities.Customer;

public class LoadableCustomerModel extends LoadableDetachableModel<Customer> {

	private Long id;
	
	public LoadableCustomerModel() {
		super();
	}
	
	public LoadableCustomerModel(Customer customer) {
		super(customer);
		id = customer.getId();
	}
	
	public LoadableCustomerModel(Long id) {
		super();
		this.id = id;
	}
	
	@Override
	protected Customer load() {
		System.out.println("--------LOAD model--------------");
		if (id != null) {
			try {
				CustomerDAO customerDAO = new CustomerDAO();
				System.out.println("--------LOAD Customer-----------");
				return customerDAO.findById(id);
			} catch (DAOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return new Customer();
		}
		
	}
	
	@Override
	protected void onDetach() {
	   super.onDetach();	    
	}

}
