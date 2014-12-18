/**
 *---------------------------------------------------------------------
 *
 * Copyright (C) 2012 Cluttr BVBA. All rights reserved.
 *
 *
 * THIS DOCUMENT CONTAINS CONFIDENTIAL INFORMATION PROPRIETARY TO ITS
 * AUTHOR, CLUTTR BVBA. IT IS THEREFORE STRICTLY FORBIDDEN TO PUBLISH
 * CITE OR MAKE PUBLIC IN ANY WAY THIS DOCUMENT OR ANY PART THEREOF
 * WITHOUT THE EXPRESS WRITTEN PERMISSION BY THE AUTHOR.
 *
 * UNDER NO CIRCUMSTANCE THIS DOCUMENT MAY BE COMMUNICATED TO OR PUT
 * AT THE DISPOSAL OF THIRD PARTIES; PHOTOCOPYING OR DUPLICATING IT IN
 * ANY OTHER WAY IS STRICTLY PROHIBITED. DISREGARDING THE CONFIDENTIAL
 * NATURE OF THIS DOCUMENT MAY CAUSE IRREMEDIABLE DAMAGE TO THE AUTHOR.
 *
 *---------------------------------------------------------------------
 */


package com.wct.dao;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Provides the functionality to create an entity manager
 * instance for use by the DAO implementations.
 *
 * @author  Khalil Mlika <khalil.mlika@whitecapetech.com>
 * @author  Frederik Van Hecke <frederik@cluttr.be>
 */
public class EntityManagerInstance
{
    //-----------------------------------------------------------------
    // PRIVATE STATIC VARIABLES
    //-----------------------------------------------------------------

    // Entity manager instance
    private static EntityManager entityManager;


    //-----------------------------------------------------------------
    // ENTITY MANAGER METHODS
    //-----------------------------------------------------------------

    /**
     * Retrieve an EntityManager instance.
     *
     * @return  An EntityManager instance
     *
     * @throws  DAOException            If creating the EntityManager
     *                                  instance failed
     */
    public static EntityManager getInstance()
            throws DAOException
    {
        // If the entity manager instance has not yet
        // been initialized, try to create a new one
        if (entityManager == null)
        {
            try
            {
                // Initialize the factory instance
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("WicketAutoFormation");
                // Create the entity manager
                entityManager = emf.createEntityManager();
            }
            // If creating an entity manager instance fails,
            // throw the appropriate exception
            catch (IllegalStateException e)
            {
                throw new DAOException("Failed to create an EntityManager instance", e);
            }
        }
        // Return the entity manager instance
        return entityManager;
    }
}