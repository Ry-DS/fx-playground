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
    @JacksonXmlElementWrapper(/*useWrapping = false*/localName = "Students")
//states this variable isn't wrapped (isn't enclosed within another variable), represents whole collection
    @JacksonXmlProperty(localName = "Student")
//the name each of these elements will hold in the doc, represents single items
    private List<Student> students = new ArrayList<>();//array holding students in the xml doc
    @JacksonXmlElementWrapper(localName = "Users")
    @JacksonXmlProperty(localName = "User")
    private List<User> users = new ArrayList<>();

    public Table(User... users) {
        this.users = Arrays.asList(users);

    }

    public Table() {
    }

    public List<Student> getStudents() {
        return students == null ? students = new ArrayList<>() : students;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public boolean removeStudent(Student student) {
        return students.remove(student);
    }

    @Override
    public String toString() {
        return students + " " + users;
    }
}
