package com.example.editionlpgm.dtos;

public class AgentLoginRequest {
    private String email;
    private String password;

    public AgentLoginRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
