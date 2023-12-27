package org.gregsquad.gregserver;

import java.io.Serializable;

public class Message<T> implements Serializable {
    private String sender;
    private String type;
    private String purpose;
    private T content;

    public Message(String sender, String type, String purpose,T content) {
        this.sender = sender;
        this.type = type;
        this.purpose = purpose;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getType() {
        return type;
    }

    public String getPurpose() {
        return purpose;
    }

    public T getContent() {
        return content;
    }

}