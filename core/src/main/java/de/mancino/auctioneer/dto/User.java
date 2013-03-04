package de.mancino.auctioneer.dto;

import java.io.Serializable;


public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String userName;
    private String password;
    private String fullName;
    private String roles;
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getFullName() {
        return fullName;
    }
    
    public void setRoles(String roles) {
        this.roles = roles;
    }
    
    public String getRoles() {
        return roles;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
