package com.wct.provider;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebSession;

import com.wct.dao.CustomerDAO;
import com.wct.dao.DAOException;
import com.wct.entities.Customer;

public class CustomerSortableDataProvider extends
		SortableDataProvider<Customer, String> {

	public CustomerSortableDataProvider() {
		// set default sort
		setSort("id", SortOrder.ASCENDING);
	}
	
	@Override
	public Iterator<? extends Customer> iterator(long first, long count) {
		// TODO Auto-generated method stub
		System.out.println("----Sort Page ------------ " + getSort());
		System.out.println("--------first--------" + first);

		System.out.println("--------COUNT--------" + count);
		try {
			System.out.println("-----------------" + WebSession.get().getAttribute("searchword"));
			HashMap<String, Object> params = new HashMap<String, Object>();
			if (WebSession.get().getAttribute("searchword") != null) {
				params.put("firstName", WebSession.get().getAttribute("searchword"));
			}
			CustomerDAO customerDAO = new CustomerDAO();
			List<Customer> listCustomers = customerDAO.search(params , (int) first, (int) count, getSort().getProperty(), getSort().isAscending());
			System.out.println("List Customers = " + listCustomers);
			return listCustomers.iterator();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}
	}

	@Override
	public long size() {
		System.out.println("-------SIZE -----------");
		long size = 0;
		try {
			CustomerDAO customerDAO = new CustomerDAO();
			size = customerDAO.size();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return size;
	}

	@Override
	public IModel<Customer> model(Customer object) {
		// TODO Auto-generated method stub
		return new Model<Customer>(object);
	}

}
