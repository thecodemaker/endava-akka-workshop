package com.en_workshop.webcrawlerakka.akka.requests.persistence;

import com.en_workshop.webcrawlerakka.akka.requests.MessageRequest;

/**
 * Request the list of all domains.
 *
 * @author Radu Ciumag
 */
public class ListDomainsRequest extends MessageRequest implements PersistenceRequest{

    /**
     * The default constructor
     */
    public ListDomainsRequest() {
        super(System.currentTimeMillis());
    }
}
