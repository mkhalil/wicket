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


import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.*;


/**
 * The AbstractDAO class is an abstract implementation
 * providing a general implementation of the CRUD methods.
 *
 * .
 * An extension of the AbstractDAO should be provided for
 * every individual entity. It's then also up to that extension
 * to implement other entity-specific methods and functionality.
 *
 *
 * @author  Khalil Mlika       <khalil.mlika@whitecapetech.com>
 * @author  Frederik Van Hecke <frederik@cluttr.be>
 * @author  Jawhar Turki       <jawhar.turki@whitecapetech.com>
 */
public abstract class AbstractDAO<EntityType, EntityKeyType>
{
    //-----------------------------------------------------------------
    // INSTANCE VARIABLES
    //-----------------------------------------------------------------

    // The EntityManager instance to be used by all
    // DAO subclasses. This instance is given by the
    // Singleton class EntityManagerInstance
    private EntityManager entityManager;

    // The Class of the entity for the
    // specific DAO implementation
    private Class<EntityType> entityClass;
    
    //-----------------------------------------------------------------
    // CONSTRUCTORS
    //-----------------------------------------------------------------

    /**
     * Constructor for the AbstractDAO class.
     *
     * @throws  DAOException            If initializing the DAO failed
     */
    public AbstractDAO()
            throws DAOException
    {
        // Retreive the unique EntityManager
        // instance as created and provided by the
        // EntityManagerInstance class
        entityManager = EntityManagerInstance.getInstance();
        // Retreive target entity class from generic parameters
        this.entityClass = (Class<EntityType>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


    /**
     * Persist an entity to the database.
     *
     * @param   entity      The entity to be saved
     *
     * @throws  DAOException If saving the entity failed
     */
    public void create(EntityType entity)
            throws DAOException
    {
        // If the entity manager instance is closed
        // throw the appropriate exception
        if(!entityManager.isOpen())
            throw new DAOException("Unable to persist the requested entity ('" +
                                   entity.getClass().getSimpleName() + "') due to " +
                                   "a closed entity manager instance");

        // Declare the transaction instance
        EntityTransaction trans = null;
        // Try initialize the transaction
        try{ trans = entityManager.getTransaction(); }
        // If transactions are handled by JTA
        catch(IllegalStateException e)
        {
            throw new DAOException("Failed to get transaction instance while "+
                                   "trying to persist entity '" + 
                                   entity.getClass().getSimpleName()+"' ", e);
        }
        // Try to start transaction
        try{ trans.begin(); }
        // If initializing the transaction failed,
        // throw the appropriate exception
        //
        // EntityManager state doesn't allow transaction;
        // may be due to a closed EntityManager state
        // or the transaction is already active and can't perform Begin action
        catch(IllegalStateException e)
        {
            throw new DAOException("Failed to start transaction while trying to persist entity '"
                                   +entity.getClass().getSimpleName()+"' ", e);
        }

        // Ask EntityManager to persist the entity passed in parameter
        // and commit it to database, as the transaction is already initiated
        try
        {
            entityManager.persist(entity);
            trans.commit();
        }
        // A similar entity, with same identifier, is already persisted
        catch (EntityExistsException ex)
        {
            throw new DAOException("Failed to persist entity '"+entity.getClass().getSimpleName()
                                  +"' : Entity already exists in context", ex);
        }
        // EntityManager state doesn't allow persisting or committing
        // may be due to a closed EntityManager state
        catch (IllegalStateException ex)
        {
            throw new DAOException("Failed to persist entity '"+entity.getClass().getSimpleName()
                                  +"' : EntityManager state doesn't allow persisting", ex);
        }
        // Failed to commit;
        // need to rollback if transaction is active
        catch (RollbackException ex)
        {
            // Check if transaction exists and active
            if (trans != null && trans.isActive())
            {
                trans.rollback();
            }
            System.out.println("--GET MESSAGE--"+ex.getMessage());
            throw new DAOException("Failed to persist entity; "+
                                   "Roll Back Exception : " + 
                                   ex.getMessage(), ex.getCause());
        }
        // Any other possible exception (no transaction initiated, bad entity object, ... )
        catch (RuntimeException ex)
        {
            throw new DAOException("Failed to persist entity '" +
                                   entity.getClass().getSimpleName() +"' : "+
                                   ex.getMessage(), ex);
        }
     }

     /**
     * Try to update entity in the database.
     *
     * @param entity The entity to be updated into database
     * @throws DAOException If failed to execute the update action
     */
     public void update(EntityType entity)
            throws DAOException
     {
        // If the entity manager instance is closed
        // throw the appropriate exception
        if(!entityManager.isOpen())
            throw new DAOException("Unable to update the requested entity ('" +
                                   entity.getClass().getSimpleName() + "') due to " +
                                   "a closed entity manager instance");

        EntityTransaction tans = null;
        // Try to merge the entity parameter within a transaction
        try
        {
            // Try to create and start a transaction before persisting
            tans = entityManager.getTransaction();
            tans.begin();
        }
        // EntityManager state doesn't allow transaction;
        // may be due to a closed EntityManager state
        // or the transaction is already active and can't perform Begin action
        catch (RuntimeException ex)
        {
            throw new DAOException("Failed to start transaction while trying to merge entity '"
                                   +entity.getClass().getSimpleName()+"' ", ex);
        }

        // Ask EntityManager to merge the state of the 
        // given entity in current persistence context
        // and commit it to database
        try
        {
            entityManager.merge(entity);
            tans.commit();
        }
        // EntityManager state doesn't allow merginig or committing
        // may be due to a closed EntityManager state
        catch (IllegalStateException ex)
        {
            throw new DAOException("Failed to merge entity '"+entity.getClass().getSimpleName()
                                  +"' : EntityManager state doesn't allow merging", ex);
        }
        // Failed to commit;
        // need to rollback if transaction is active
        catch (RollbackException ex)
        {
            // Check if transaction exists and active
            if (tans != null && tans.isActive())
            {
                tans.rollback();
            }
            throw new DAOException("Roll Back Exception : "
                                   + ex.getMessage(), ex.getCause());
        }
        // Any other possible exception (no transaction initiated, bad entity object, ... )
        catch (RuntimeException ex)
        {
            throw new DAOException("Failed to merge entity '"
                                    +entity.getClass().getSimpleName()+"'", ex);
        }
    }

    /**
     * Try to delete entity from the database.
     *
     * @param entity The entity to be removed from database
     * @throws DAOException If failed to execute delete action
     */
    public void delete(EntityType entity)
            throws DAOException
    {
        // If the entity manager instance is closed
        // throw the appropriate exception
        if(!entityManager.isOpen())
            throw new DAOException("Unable to delete the requested entity ('" +
                                   entity.getClass().getSimpleName() + "') due to " +
                                   "a closed entity manager instance");
        EntityTransaction trans = null;
        // Try to delete the entity parameter within a transaction
        try
        {
            // Try to create and start a transaction before persisting
            trans = entityManager.getTransaction();
            trans.begin();
        }
        // EntityManager state doesn't allow transaction;
        // may be due to a closed EntityManager state
        // or the transaction is already active and can't perform Begin action
        catch (RuntimeException ex)
        {
            throw new DAOException("Failed to start transaction while trying to remove entity '"
                                   +entity.getClass().getSimpleName()+"' ", ex);
        }
        // Try to remove the given entity from 
        // the current persistence context
        // and commit it to database
        try
        {
            entityManager.remove(entity);
            trans.commit();
        }
        // EntityManager state doesn't allow removing or committing
        // may be due to a closed EntityManager state
        catch (IllegalStateException ex)
        {
            throw new DAOException("Failed to remove entity '"+entity.getClass().getSimpleName()
                                  +"' : EntityManager state doesn't allow deleting", ex);
        }
        // Failed to commit;
        // need to rollback if transaction is active
        catch (RollbackException ex)
        {
            // Check if transaction exists and active
            if (trans != null && trans.isActive())
            {
                trans.rollback();
            }
            throw new DAOException("Roll Back Exception : "
                                   + ex.getMessage(), ex.getCause());
        }
        // Any other possible exception (no transaction initiated, bad entity object, ... )
        catch (RuntimeException ex)
        {
            throw new DAOException("Failed to remove entity '"
                                    +entity.getClass().getSimpleName()+"'", ex);
        }
    }

    /**
     * Try to find entity by ID
     * 
     * @param entityKey The primary key field (ID) value.
     * @return          Entity instance having the target ID 
     *                  or null if the ID does not exist.
     * 
     * @throws DAOException If failed to execute search.
     */
    public EntityType findById(EntityKeyType entityKey)
            throws DAOException 
    {
        // If the entity manager instance is closed
        // throw the appropriate exception
        if(!entityManager.isOpen())
            throw new DAOException("Unable to find entity with id ('" +
                                   entityKey+ "') due to " +
                                   "a closed entity manager instance");
        // Ask EntityManager to find entity by id
        try
        {
            return entityManager.find(entityClass, entityKey);
        }
        // EntityManager state doesn't allow to fetch entity by id
        // may be due to a closed EntityManager state
        catch (IllegalStateException ex)
        {
            throw new DAOException("Failed to find entity with id '"+entityKey
                                  +"' : bad EntityManager state", ex);
        }
        // Any other possible exception (no transaction initiated, bad entity id, ... )
        catch (RuntimeException ex)
        {
             throw new DAOException("Failed to find entity with id '"
                                    +entityKey+"'", ex);
        }
    }

    /**
     * Retrive the list of all entities.
     * 
     * @return The List of all persisted entities
     * @throws DAOException If failed to execute search query
     */
    public List<EntityType> findAll()
            throws DAOException
    {
        // The select query.
        String selectQuery = "SELECT e FROM " + entityClass.getSimpleName()+" e ";
        // Try to execute the select query.
        return this.executeSelectQuery(selectQuery, null, 0, 0);
    }  
    
    /**
     * Execute the given Namedquery amongst EntityType's named queries.   
     * 
     * Target NamedQuery is identified by its unique name.
     * The parameters' values for the JPQL statement will be extracted from 
     * the given parameters Map and set accordingly. 
     * The parametersMap and JPQL statements must much, otherwise 
     * an exception will be thrown.
     * 
     * @param  queryName       Name of named query
     * @param  parametersMap   Map of parameters :each element is a parameter
     *                         the key of element is name of parameter
     *                         the value of element is value of parameter 
     * @return List<EntityType>
     * @throws DAOException    If namedQuery is not defined, error in name 
     *                         of parameter, error in query,...
     */
    protected List<EntityType> executeNamedQuery(String queryName, Map<String,Object> parametersMap)
            throws DAOException
    {
        
        // If the entity manager instance is closed
        // throw the appropriate exception
        if(!entityManager.isOpen())
            throw new DAOException("Unable to find entities list ('" +
                                   entityClass.getSimpleName()+ "') due to " +
                                   "a closed entity manager instance");
        Query query = null;
        // Create instance of query with the name queryName
        try
        {            
            query = entityManager.createNamedQuery(queryName);
        }
        // The namedQuery is not defined in JPA entity
        catch(IllegalArgumentException e)
        {
            throw new DAOException("Failed to create Query '" + queryName +
                                   "' : this namedQuery is not defined", e);
        }
        // The EntityManager has been closed
        catch(IllegalStateException e)
        {
            throw new DAOException("Failed to create Query  '" + queryName +
                                   "' : due to a closed entity manager instance", e);
        }
        // Test parametersMap not empty.
        // if parametersMap not empty then params will be set to query.
        if(parametersMap!=null && parametersMap.size()>0)
        {
            // Set params to query
            // Map iterator
            Iterator it = parametersMap.entrySet().iterator();
            // if iteration has new element
            while(it.hasNext())
            {
                // get element Map.Entry
                Map.Entry element = (Map.Entry)it.next();
                // Try to set paramater to the query
                try
                {   
                    query.setParameter((String)element.getKey(), element.getValue());
                }
                // The paramName does not correspond to parameter in Namedquery
                catch(IllegalArgumentException e)
                {
                    throw new DAOException("Failed to execute Query; no parameter named '" + 
                                           (String)element.getKey() + "' is found", e);
                }
            }
        }            
        // Try to execute query and return results list
        try
        {
            return query.getResultList();
        }
        // The type of NamedQuery is Update or delete
        catch(IllegalStateException e)
        {
            throw new DAOException("The type of NamedQuery is Update or Delete ",e);
        }
    }

    /**
     * Execute a SELECT query and return the query results as a List
     * 
     * @param selectQuery The query to be executed
     * @param queryParams map of key/values, parameter name and parameter value
     * @param page        Number of results page (starting from 1); 
     *                    if set to zero, then all results are returned 
     *                    without limiting to page and page size.       
     * @param page_size   The number of results per page.  
     * 
     * @return List of EntityType
     * 
     * @throws DAOException If failed to execute query.
     */
    protected List<EntityType> executeSelectQuery(String selectQuery, Map<String,Object> queryParams, int page, int page_size)
            throws DAOException
    {
        return (List<EntityType>) this.executeSelectQuery(selectQuery, queryParams, page, page_size, true);
    }
    
    /**
     * Execute a SELECT query that returns a single result.
     * 
     * @param selectQuery The query to be executed
     * @param queryParams Map of key/values, parameter name and parameter value
     * 
     * @return single result (object instance, number, string value ...)
     * 
     * @throws DAOException If failed to execute query. 
     */
    protected Object executeSelectQuery(String selectQuery, Map<String,Object> queryParams)
            throws DAOException
    {
        return this.executeSelectQuery(selectQuery, queryParams, 0, 0, false);
    }
    
    /**
     * Execute a select query that returns single result or list, depend on boolean parameter "list".
     * If the value of parameter "list" is true then we get result in list else we get a single result.
     * 
     * @param queryString The query in select form to be executed.
     * @param list        Boolean to indicate whether the function returns single result or results as a List.
     *                    if the value of resulList is false then parameters(page and page_size)
     *                    are unused.
     * @param queryParams Map of key/values
     *                    the key is the parameter in query
     *                    the value is the value of parameter
     * @param page        Number of results page (starting from 1); 
     *                    if set to zero, then all results are returned 
     *                    without limiting to page and page size.
     * @param page_size   The number of results per page.
     * 
     * @return            Object instance that represent the result of select query.
     * 
     * @throws DAOException If failed to execute query.
     */
    private Object executeSelectQuery(String selectQuery, Map<String,Object> queryParams, int page, int page_size, boolean list)
            throws DAOException
    {
        // If the entity manager instance is closed
        // throw the appropriate exception
        if(!entityManager.isOpen())
            throw new DAOException("Unable to find entities list ('" +
                                   entityClass.getSimpleName()+ "') due to " +
                                   "a closed entity manager instance");
        // Create query
        Query query;
        try
        {
            // Create query 
            query = entityManager.createQuery(selectQuery);
        }
        // Entity Manager has been closed
        catch (IllegalStateException e)
        {
            throw new DAOException("Failed to execute query : The EntityManager has been closed.", e);
        }
        // Query string is not valid
        catch (IllegalArgumentException e)
        {
            throw new DAOException("Failed to execute query : The query string is not valid", e);
        }
        if(queryParams!=null && queryParams.size()>0)
        {
            // Set parameters into query
            for(String key : queryParams.keySet())
            {
                try
                {
                    query.setParameter(key, queryParams.get(key));
                }
                // If parameter name does not correspond to parameter in query string or argument is of incorrect type
                // throw the appropriate IllegalArgumentException
                catch(IllegalArgumentException e)
                {
                    throw new DAOException("Failed to execute query : Paramater name '"+key+"' does "
                            + "not correspond to parameter in query string",e);
                }
            }
        }                    
        // Check single result or list.   
        if(list)
        {
            // Check if number of page is provided.
            if(page >= 0)
            { 
               try
               {
            	   System.out.println("page = " + page);
                   // Specify the offset.
                   query.setFirstResult(page);
                   // Specify the number of results per page.
                   query.setMaxResults(page_size);
               }
               // If argument is negative,
               // throw the appropriate IllegalArgumentException
               catch(IllegalArgumentException e)
               {
                    throw new DAOException("Failed to execute query : page_size argument is negative ",e);
               }
            }
            // Try to execute a SELECT query and return the query results as a List.
            try
            {
                return query.getResultList();
            }
            // If the select query failed due to invalid jpql statement.
            // throw the appropriate IllegalArgumentException
            catch(IllegalArgumentException e)
            {
                throw new DAOException("Failed to execute query : The query is not SELECT type",e);
            }
        }
        else
        {
            // Try to execute a SELECT query that returns a single result. 
            try
            {
                return query.getSingleResult();
            }
            // If there is no result,
            // throw the appropriate NoResultException 
            catch(NoResultException e)
            {
                return null;
            }
            // If more than one result,
            // throw the appropriate NonUniqueResultException
            catch(NonUniqueResultException e)
            {
                 throw new DAOException("Failed to execute query : There is No unique result",e);
            }
            // If called for a JPQL UPDATE or DELETE statement, 
            // throw the appropriate IllegalArgumentException
            catch(IllegalArgumentException e)
            {
                throw new DAOException("Failed to execute query : The query is not SELECT type",e);
            }
        }
    }
    
    /**
     * Retrieve list of entities.
     * 
     * @param page        Number of results page (starting from 1); 
     *                    if set to zero, then all results are returned 
     *                    without limiting to page and page size.
     * @param page_size   The number of results per page.
     * @param sort_field  Field name of the sorting field.
     * @param ascending   Specifies Ascending/Descending results order.
     * @return            List of EntityType.
     * 
     * @throws DAOException  If an error occured in DAO layer.
     * 
     */
    public List<EntityType> list(int page, int page_size,String sort_field,boolean ascending)
            throws DAOException
    {
        return this.search(null, page, page_size, sort_field, ascending);
    }

    /**
     * Search and return EntityType entities list 
     * respecting the given search criteria.
     * 
     * @param parameters  Couples of query parameters names and values 
     * @param page        Number of results page (starting from 1); 
     *                    if set to zero, then all results are returned 
     *                    without limiting to page and page size.
     * @param page_size   The number of results per page.
     * @param sort_field  Field name of the sorting field.
     * @param ascending   Specifies Ascending/Descending results order.
     * @return            List of EntityType entities respecting 
     *                    the given criteria. 
     * @throws DAOException  If an error occured in DAO layer.
     * @throws IllegalArgumentException  If a given parameter (key of parameters map)
     *                                   is not a valid field name.
     */
    public List<EntityType> search(Map<String,Object> parameters,int page, int page_size, String sort_field, boolean ascending)
            throws DAOException
    {
        StringBuilder queryString = new StringBuilder();
        // Add SELECT Clause
        queryString.append("SELECT e FROM ").append(entityClass.getSimpleName()).append(" e WHERE 1 = 1 ");
        if(parameters != null && !parameters.isEmpty())
        {
            //Add WHERE clause
            for (String key:parameters.keySet())
            {
                this.verifiedField(key);
                queryString.append("AND e.").append(key).append(" LIKE :").append(key);
            }
        }
        // Add Order by clause
        this.verifiedField(sort_field);
        queryString.append(" ORDER BY ").append(sort_field);
        queryString.append(" ").append((ascending ? "ASC" : "DESC"));    
        System.out.println("---QUERY STRING---"+queryString.toString());
        System.out.println("---- page = "+page + " ---- page_size = " + page_size);

        return this.executeSelectQuery(queryString.toString(),parameters,page, page_size);
    }
    
    /**
     * Verify if the name of the field exist in entity.
     * 
     * @param field_name The name of the field
     * 
     * @throws DAOException if field with the given name does not exist 
     */
    public void verifiedField(String field_name)
            throws DAOException
    {
        try 
        {
            // Try to find the given field with the specified name.
            this.entityClass.getDeclaredField(field_name);
        }
        // If the field is not found
        // throw the appropriate DAOException
        catch (NoSuchFieldException e)
        {
            throw new DAOException("Field with the name '"+field_name+"' is not found", e);
        } 
        // If field name is null, access denied or other exception cause,
        // throw the appropriate DAOException
        catch (Exception e) 
        {
            throw new DAOException("Failed to execute query; ", e);
        }
    }
}