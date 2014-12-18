package com.wct.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.wct.entities.Customer;

public class CustomerDAO extends AbstractDAO<Customer, Long> {

	public CustomerDAO() throws DAOException {
		super();
	}
	
	public List<Customer> list() {
		try {
			EntityManager em = EntityManagerInstance.getInstance();
			TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c",  Customer.class);
			return query.getResultList();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		/*em = EntityManagerInstance.getInstance();
		TypedQuery<User> query = em.createQuery("SELECT u FROM User u",  User.class);
		System.out.print("--------------"+ query.getResultList());
		return query.getResultList();*/
	}

	/**
     * Count users assigned to the profile.
     * 
     * @param id The id of the UserProfile.
     * @return number of users.
     * @throws DAOException If an error ocured.
     */
    public Long size()
            throws DAOException
    {
        String selectQuery = "SELECT COUNT(c) FROM Customer c";
        return (Long)this.executeSelectQuery(selectQuery, null);
    }
    
    public List<Customer> iterator(int page, int count) 
    {
    	
		try {
			return this.search(new HashMap<String, Object>() , page, count, "id", true);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	
    }
    
}
