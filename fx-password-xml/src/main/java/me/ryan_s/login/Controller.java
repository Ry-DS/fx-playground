package me.ryan_s.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class Controller {


    public void initialize() throws IOException {
        File testFile = new File(System.getProperty("user.dir") + File.separator + "file.xml");
        testFile.createNewFile();
        ObjectMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.writeValue(testFile, new Persons(new Person("John", 33, "Coder", 200000),
                new Person("John", 33, "Coder", 200000), new Person("John", 33, "Coder", 200000), new Person("John", 33, "Coder", 200000)));
    }
}
