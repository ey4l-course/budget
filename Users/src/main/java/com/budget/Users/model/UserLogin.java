package com.budget.Users.model;

public class UserLogin {
    private String email;
    private String password;
    private String ip;

    public UserLogin(String email, String password, String ip) {
        this.email = email;
        this.password = password;
        this.ip = ip;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
