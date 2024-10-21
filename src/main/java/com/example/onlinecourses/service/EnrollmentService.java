package com.example.onlinecourses.service;

import com.example.onlinecourses.model.Course;
import com.example.onlinecourses.model.Enrollment;
import com.example.onlinecourses.model.Student;
import org.springframework.stereotype.Service;

import javax.xml.stream.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.example.onlinecourses.controller.HomeController.findCourseByTitle;
import static com.example.onlinecourses.controller.HomeController.findStudentByName;

@Service
public class EnrollmentService {
    private static final String ENROLLMENT_FILE_PATH = "./src/main/resources/data/enrollments.txt";
    private static final String ENROLLMENT_FILE_XML_PATH = "./src/main/resources/data/enrollments.xml";


    public List<Enrollment> readEnrollmentsFromFile() {
        List<Enrollment> enrollments = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(ENROLLMENT_FILE_PATH))) {
            lines.forEach(line -> {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String title = parts[1];
//                    int duration = Integer.parseInt(parts[2]);

                    Student student = findStudentByName(name);
                    Course course = findCourseByTitle(title);
                    if (student != null && course != null) {
                        enrollments.add(new Enrollment(student, course));
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    public void writeEnrollmentsToFile(List<Enrollment> enrollmentStream) {
        List<String> enrollmentLines = enrollmentStream.stream()
                .filter(enrollment -> enrollment.getCourse().getDuration() > 6)
                .sorted(Comparator.comparingInt((Enrollment e) -> e.getCourse().getDuration()).reversed())
                .map(enrollment -> enrollment.getStudent().getName() + "," + enrollment.getCourse().getTitle() + "," + enrollment.getCourse().getDuration())
                .toList();

        try {
            Files.write(Paths.get(ENROLLMENT_FILE_PATH), enrollmentLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveEnrollmentsToXML (List<Enrollment> enrollments) throws XMLStreamException, IOException {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        try (FileOutputStream outputStream = new FileOutputStream(ENROLLMENT_FILE_XML_PATH)) {
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(outputStream);
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("enrollments");

            for (Enrollment enrollment : enrollments) {
                writer.writeStartElement("enrollment");

                writer.writeStartElement("student");
                writer.writeCharacters(enrollment.getStudent().getName());
                writer.writeEndElement();

                writer.writeStartElement("course");
                writer.writeCharacters(enrollment.getCourse().getTitle());
                writer.writeEndElement();

                writer.writeEndElement();
            }

            writer.writeEndElement();
            writer.writeEndDocument();
        }
    }

    public List<Enrollment> readEnrollmentsFromXML() {
        List<Enrollment> enrollments = new ArrayList<>();
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();

        try (FileInputStream inputStream = new FileInputStream(ENROLLMENT_FILE_XML_PATH)) {
            XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream);

            String studentName = null;
            String courseTitle = null;

            while (reader.hasNext()) {
                reader.next();

                if (reader.isStartElement()) {
                    if ("student".equals(reader.getLocalName())) {
                        studentName = reader.getElementText();
                    } else if ("course".equals(reader.getLocalName())) {
                        courseTitle = reader.getElementText();
                    }
                }

                if (reader.isEndElement() && "enrollment".equals(reader.getLocalName())) {
                    try {
                        Student student = findStudentByName(studentName);
                        Course course = findCourseByTitle(courseTitle);
                        if (student == null || course == null) {
                            throw new Exception("Invalid data for enrollment: " + studentName + ", " + courseTitle);
                        }
                        enrollments.add(new Enrollment(student, course));
                    } catch (Exception e) {
                        System.err.println("Błąd: " + e.getMessage());
                    }

                    studentName = null;
                    courseTitle = null;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return enrollments;
    }


}