package com.ecomm.productcatalog.controller;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    // Default constructor
    public LoginRequest() {}

    // Parameterized constructor
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /** @return the username */
    public String getUsername() {
        return username;
    }

    /** @param username to set */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return the password */
    public String getPassword() {
        return password;
    }

    /** @param password to set */
    public void setPassword(String password) {
        this.password = password;
    }
}
