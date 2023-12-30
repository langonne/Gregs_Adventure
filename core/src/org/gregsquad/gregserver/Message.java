package org.gregsquad.gregserver;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Message<T> implements Serializable {

    private static final List<String> VALID_TYPES = Arrays.asList("CONNEXION", "CHAT", "ANSWER","GAME");
    private static final List<String> VALID_PURPOSES = Arrays.asList("","NAME","DRAW_DONJON_CARD");

    private UUID id;
    private String sender;
    private String type;
    private String purpose;
    private T content;
    private Class<? extends Serializable> typeOfContent;

    public Message(String sender, String type, String purpose,T content, Class<? extends Serializable> typeOfContent) {
        if (!isValidType(type)) {
            throw new IllegalArgumentException("[MESSAGE] Invalid type: " + type);
        }
        if (!isValidPurpose(purpose)) {
            throw new IllegalArgumentException("[MESSAGE] Invalid purpose: " + purpose);
        }
        this.id = UUID.randomUUID();
        this.sender = sender;
        this.type = type;
        this.purpose = purpose;
        this.content = content;
        this.typeOfContent = typeOfContent;
    }

    public Message(UUID id, String sender, String type, String purpose,T content, Class<? extends Serializable> typeOfContent) {
        if (!isValidType(type)) {
            throw new IllegalArgumentException("[MESSAGE] Invalid type: " + type);
        }
        if (!isValidPurpose(purpose)) {
            throw new IllegalArgumentException("[MESSAGE] Invalid purpose: " + purpose);
        }
        this.id = id;
        this.sender = sender;
        this.type = type;
        this.purpose = purpose;
        this.content = content;
        this.typeOfContent = typeOfContent;
    }

    public boolean isOfType(Class<?> typeOfContent) {
        return this.typeOfContent.equals(typeOfContent);
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

    public UUID getId() {
        return id;
    }

    public static boolean isValidType(String type) {
        return VALID_TYPES.contains(type);
    }

    public static boolean isValidPurpose(String purpose) {
        return VALID_PURPOSES.contains(purpose);
    }

}