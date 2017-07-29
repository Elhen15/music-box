package com.finalproject.elhen15.musicbox.Model;

/**
 * Created by Elhen15 on 29/07/2017.
 */

public class User extends Entity {
    private String email;
    private String password;
    private boolean isAdmin;

    public User(){
        super();
    }

    public User(String email, String password, boolean isAdmin, String id){
        super(id);
        this.setEmail(email);
        this.setPassword(password);
        this.setIsAdmin(isAdmin);
    }

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

    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }
}
