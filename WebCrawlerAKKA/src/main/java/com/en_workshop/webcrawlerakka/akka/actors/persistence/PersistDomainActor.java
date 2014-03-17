package com.en_workshop.webcrawlerakka.akka.actors.persistence;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.en_workshop.webcrawlerakka.akka.actors.BaseActor;
import com.en_workshop.webcrawlerakka.akka.requests.persistence.PersistDomainRequest;

/**
 * Request to persist a domain.
 *
 * Created by roxana on 3/13/14.
 */
public class PersistDomainActor extends BaseActor {

    private final LoggingAdapter LOG = Logging.getLogger(getContext().system(), this);

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof PersistDomainRequest) {
            PersistDomainRequest persistDomainRequest = (PersistDomainRequest) message;
            LOG.info("Received domain to persist: " + persistDomainRequest.getDomain().getName());

        } else {
            LOG.error("Unknown message: " + message);
            unhandled(message);
        }

    }
}
