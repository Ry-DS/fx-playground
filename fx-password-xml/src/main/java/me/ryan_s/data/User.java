package me.ryan_s.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Created by SimplyBallistic on 17/07/2019
 *
 * @author SimplyBallistic
 **/
public class User {
    @JacksonXmlProperty(isAttribute = true)
    private String username;
    @JacksonXmlProperty(isAttribute = true)
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getUsername().equals(user.getUsername()) &&
                getPassword().equals(user.getPassword());
    }


}
