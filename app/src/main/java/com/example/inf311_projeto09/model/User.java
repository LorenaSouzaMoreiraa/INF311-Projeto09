package com.example.inf311_projeto09.model;

import java.util.List;
import java.util.Map;

public class User {
    private int id;
    private String name;
    private UserRole type;
    private String email;
    private String cpf;
    private String school;
    private String password;
    private boolean enableNotifications;
    private String imageUrl;
    private List<Notification> notifications;

    public User(final int id, final String name, final UserRole type,
                final String email, final String cpf, final String school,
                final String password, final boolean enableNotifications,
                final String imageUrl, final List<Notification> notifications) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.email = email;
        this.cpf = cpf;
        this.school = school;
        this.password = password;
        this.enableNotifications = enableNotifications;
        this.imageUrl = imageUrl;
        this.notifications = notifications;
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public UserRole getType() {
        return this.type;
    }

    public void setType(final UserRole type) {
        this.type = type;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getCPF() {
        return this.cpf;
    }

    public void setCPF(final String cpf) {
        this.cpf = cpf;
    }

    public String getSchool() {
        return this.school;
    }

    public void setSchool(final String school) {
        this.school = school;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public boolean getEnableNotifications() {
        return this.enableNotifications;
    }

    public void setEnableNotifications(final boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Notification> getNotifications() {
        return this.notifications;
    }

    public void setNotifications(final List<Notification> notifications) {
        this.notifications = notifications;
    }

    public enum UserRole {
        USER("User"),
        ADMIN("Admin");

        private final String identifier;

        UserRole(final String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return this.identifier;
        }
    }

    public record RawUserResponse(String id, String nome, Map<String, Object> emails,
                                  String cpf, String escolaOrigem, String imagem,
                                  List<Object> camposPersonalizados) {
    }
}



