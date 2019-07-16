package me.ryan_s.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class Controller {


    public void initialize() throws IOException {
        File testFile = new File(System.getProperty("user.dir") + File.separator + "file2.xml");
        testFile.createNewFile();
        ObjectMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
       /* xmlMapper.writeValue(testFile, new Table(new Student(2,"F","d","d",'E'),
                new Student(2,"F","d","d",'E'),
                new Student(2,"F","d","d",'E')));*/
        Table table = xmlMapper.readValue(testFile, Table.class);
        System.out.println(table);

    }
}
