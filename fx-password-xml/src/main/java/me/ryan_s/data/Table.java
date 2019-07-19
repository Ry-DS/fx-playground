package me.ryan_s.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SimplyBallistic on 16/07/2019
 *
 * @author SimplyBallistic
 **/
//states this is the root element, the beginning of our xml document
@JacksonXmlRootElement
public class Table {
    @JacksonXmlElementWrapper(localName = "Members")
//states this variable isn't wrapped (isn't enclosed within another variable), represents whole collection
    @JacksonXmlProperty(localName = "Member")
//the name each of these elements will hold in the doc, represents single items
    private List<Member> members = new ArrayList<>();//array holding members in the xml doc
    @JacksonXmlElementWrapper(localName = "Users")
    @JacksonXmlProperty(localName = "User")
    private List<User> users = new ArrayList<>();

    public Table(User... users) {
        this.users = Arrays.asList(users);

    }

    public Table() {
    }

    //methods to interact with data
    public List<Member> getMembers() {
        return members == null ? members = new ArrayList<>() : members;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addMember(Member member) {
        members.add(member);
    }

    public boolean removeMember(Member member) {
        return members.remove(member);
    }

    @Override
    public String toString() {
        return members + " " + users;
    }
}
