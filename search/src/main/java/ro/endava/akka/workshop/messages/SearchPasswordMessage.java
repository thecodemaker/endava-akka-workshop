package ro.endava.akka.workshop.messages;

import java.io.Serializable;

/**
 * Created by cosmin on 4/9/14.
 * Class holding the message for searching passwords.
 * Should not have query because we just want to retrieve all passwords
 * Pagination features
 */
public class SearchPasswordMessage implements Serializable {
    private Long from;
    private Long size;
    private PasswordType passwordType;

    //TODO add sort

    public SearchPasswordMessage(PasswordType passwordType, Long from, Long size) {
        this.from = from;
        this.size = size;
        this.passwordType = passwordType;
    }

    public Long getFrom() {
        return from;
    }

    public Long getSize() {
        return size;
    }


    public PasswordType getPasswordType() {
        return passwordType;
    }
}
