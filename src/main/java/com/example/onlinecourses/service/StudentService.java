package com.example.onlinecourses.service;

import com.example.onlinecourses.model.Student;
import org.springframework.stereotype.Service;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {

    private static final String STUDENT_FILE_PATH = "./src/main/resources/data/students.txt";
    private static final String STUDENT_FILE_XML_PATH = "./src/main/resources/data/students.xml";

    public void writeStudentsToFile(List<Student> students) {
        List<String> studentLines = students.stream()
                .filter(student -> student.getAge() >= 18) // Filter students who are 18 or older
                .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName())) // Sort students by name
                .map(student -> student.getName() + "," + student.getAge()) // Map each student to a string
                .collect(Collectors.toList()); // Collect all strings into a list
        try {
            Files.write(Paths.get(STUDENT_FILE_PATH), studentLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Student> readStudentsFromFile() {
        List<Student> students = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(STUDENT_FILE_PATH))) {
            lines.forEach(line -> {
                String[] parts = line.split(","); // Split each line by comma
                if (parts.length == 2) {
                    String name = parts[0];
                    int age = Integer.parseInt(parts[1]);
                    students.add(new Student(name, age));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    public void saveStudentsToXML(List<Student> students) {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        try (FileOutputStream outputStream = new FileOutputStream(STUDENT_FILE_XML_PATH)) {
            XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(outputStream);
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
            xmlStreamWriter.writeStartElement("students");

            for (Student student : students) {
                xmlStreamWriter.writeStartElement("student");

                xmlStreamWriter.writeStartElement("name");
                xmlStreamWriter.writeCharacters(student.getName());
                xmlStreamWriter.writeEndElement();

                xmlStreamWriter.writeStartElement("age");
                xmlStreamWriter.writeCharacters(String.valueOf(student.getAge()));
                xmlStreamWriter.writeEndElement();

                xmlStreamWriter.writeEndElement(); // End student
            }

            xmlStreamWriter.writeEndElement(); // End students
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();

        } catch (IOException | XMLStreamException e) {
            System.err.println("Error while saving students to XML: " + e.getMessage());
        }
    }

    public List<String> readStudentsFromXMLToStrings() {
        List<String> studentData = new ArrayList<>();
        String currentElement = "";
        StringBuilder studentInfo = new StringBuilder();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new FileInputStream(STUDENT_FILE_XML_PATH));

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    currentElement = startElement.getName().getLocalPart(); // Get the name of the current element
                }

                if (event.isCharacters()) {
                    Characters characters = event.asCharacters();
                    if (!characters.isWhiteSpace()) {
                        if ("name".equals(currentElement)) {
                            studentInfo.append("Name: ").append(characters.getData()).append("\n");
                        } else if ("age".equals(currentElement)) {
                            studentInfo.append("Age: ").append(characters.getData()).append("\n");
                        }
                    }
                }

                if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("student")) {
                    studentData.add(studentInfo.toString());
                    studentInfo.setLength(0); // Reset StringBuilder
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentData;
    }

    // Read students from XML file into a list of objects, with exception handling
    public List<Student> readStudentsFromXMLToObjects() throws Exception {
        List<Student> students = new ArrayList<>();
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(STUDENT_FILE_XML_PATH));

        String name = null;
        int age = -1;

        while (xmlStreamReader.hasNext()) {
            xmlStreamReader.next();

            if (xmlStreamReader.isStartElement()) {
                if ("name".equals(xmlStreamReader.getLocalName())) {
                    name = xmlStreamReader.getElementText();
                } else if ("age".equals(xmlStreamReader.getLocalName())) {
                    age = Integer.parseInt(xmlStreamReader.getElementText());
                }
            }

            if (xmlStreamReader.isEndElement() && "student".equals(xmlStreamReader.getLocalName())) {
                if (name != null && age >= 0) {
                    students.add(new Student(name, age));
                } else {
                    throw new Exception("Invalid student data: name or age is missing");
                }
                name = null;
                age = -1;
            }
        }

        xmlStreamReader.close();
        return students;
    }
}