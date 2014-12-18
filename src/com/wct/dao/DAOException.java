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


/**
 * Exception implementation to signal a initialization failure.
 * 
 * @author Frederik Van Hecke <frederik@cluttr.be>
 */
public class DAOException extends Exception
{
    //-----------------------------------------------------------------
    // CONSTRUCTORS
    //-----------------------------------------------------------------

    /**
     * Constructor for the DAOException class.
     */
    public DAOException()
    {
        super();
    }

    /**
     * Constructor for the DAOException class.
     *
     * @param   message     Message providing more info about
     *                      the cause of the exception
     */
    public DAOException(String message)
    {
        super(message);
    }

    /**
     * Constructor for the DAOException class.
     *
     * @param   message     Message providing more info about
     *                      the cause of the exception
     * @param   cause       The cause of the exception
     */
    public DAOException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor for the DAOException class.
     *
     * @param   cause       The cause of the exception
     */
    public DAOException(Throwable cause)
    {
        super(cause);
    }
}