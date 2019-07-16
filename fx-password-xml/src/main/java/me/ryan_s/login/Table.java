package me.ryan_s.login;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

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
    @JacksonXmlElementWrapper(useWrapping = false)
//states this variable isn't wrapped (isn't enclosed within another variable), represents whole collection
    @JacksonXmlProperty(localName = "Students")
//the name each of these elements will hold in the doc, represents single items
    private List<Student> students;//array holding students in the xml doc

    public Table(Student... students) {
        this.students = Arrays.asList(students);

    }

    public Table() {
    }

    public List<Student> getStudents() {
        return students;
    }

    @Override
    public String toString() {
        return "Table{" +
                "students=" + students +
                '}';
    }
}
