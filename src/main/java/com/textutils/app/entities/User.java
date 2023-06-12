package com.textutils.app.entities;

public class User {
    private String name;
    private String emailId;
    private String password;
    private String confirmpassword;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmailId() {
        return emailId;
    }
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmpassword() {
        return confirmpassword;
    }
    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
    
}
