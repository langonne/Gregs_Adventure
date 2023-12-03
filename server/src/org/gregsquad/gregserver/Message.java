package org.gregsquad.gregserver;

import java.io.Serializable;

public class Message<T> implements Serializable {
    private String sender;
    private T content;

    public Message(String sender, T content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public T getContent() {
        return content;
    }
}