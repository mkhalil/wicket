package com.wct.dao;

import com.wct.entities.Person;

public class PersonDao extends AbstractDAO<Person, Integer> {

	public PersonDao() throws DAOException {
		super();
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
        String selectQuery = "SELECT COUNT(p) FROM Person p";
        return (Long)this.executeSelectQuery(selectQuery, null);
    }
    
}
