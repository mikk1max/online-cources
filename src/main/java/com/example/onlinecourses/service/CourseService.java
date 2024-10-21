package com.example.onlinecourses.service;

import com.example.onlinecourses.model.Course;
import org.springframework.stereotype.Service;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CourseService {

    private static final String COURSE_FILE_PATH = "./src/main/resources/data/courses.txt";
    private static final String COURSE_IO_FILE_PATH = "./src/main/resources/data/coursesIO.txt";
    private static final String XML_COURSES_FILE_PATH = "./src/main/resources/data/courses.xml";

    // Metoda do zapisu kursow do pliku
    public void writeCoursesToFile(List<Course> courses) {
        List<String> coursesLines = courses.stream()
                .filter(course -> course.getDuration() >= 8) // Filter courses with duration of 8 hours or more
                .sorted(Comparator.comparingInt(Course::getDuration)) // Sort courses by duration
                .map(course -> course.getTitle() + "-" + course.getDuration()) // Map each course to a string
                .collect(Collectors.toList());
        try {
            Files.write(Paths.get(COURSE_FILE_PATH), coursesLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metoda do odczytu kursow z pliku
    public List<Course> readCoursesFromFile() {
        List<Course> courses = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(COURSE_FILE_PATH))) {
            lines.forEach(line -> {
                String[] parts = line.split("-"); // Split each line by hyphen
                if (parts.length == 2) {
                    String title = parts[0];
                    int duration = Integer.parseInt(parts[1]);
                    courses.add(new Course(title, "", duration));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courses;
    }

    //metoda zapisu JavaIO
    public void writeCoursesToFileJavaIO(List<Course> courses) {
        File file = new File(COURSE_IO_FILE_PATH);
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            for (int i = 0; i < courses.size(); i++) {
                fileWriter.append(courses.get(i).getTitle() + "-" + courses.get(i).getDuration() + "\n");
            }
            fileWriter.close();
        } catch (IOException ex) {
            System.err.println(ex.getCause());
        }
    }

    //metoda odczytu JavaIO
    public List<Course> readCoursesFromFileJavaIO() {
        List<Course> courses = new ArrayList<>(); // Assuming courses is locally defined, change it if it's class-level

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(COURSE_IO_FILE_PATH))) {
            String linia;
            while ((linia = bufferedReader.readLine()) != null) {
                String[] parts = linia.split("-"); // Assuming the format is title-duration
                if (parts.length == 2) {
                    String title = parts[0];
                    int duration;
                    try {
                        duration = Integer.parseInt(parts[1].trim()); // Trim to remove any spaces
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid duration format for line: " + linia);
                        continue; // Skip this line and continue with the next one
                    }
                    courses.add(new Course(title, "", duration));
                } else {
                    System.out.println("Invalid line format: " + linia);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found: " + COURSE_IO_FILE_PATH);
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error reading file: " + COURSE_IO_FILE_PATH);
            ex.printStackTrace();
        }

        return courses;
    }


    public void saveToXMLCourses(List<Course> courses) {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        try (FileOutputStream outputStream = new FileOutputStream(XML_COURSES_FILE_PATH)) {
            XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(outputStream);
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
            xmlStreamWriter.writeStartElement("courses"); // Start main element

            for (Course course : courses) {
                xmlStreamWriter.writeStartElement("course"); // Start element "course"

                xmlStreamWriter.writeStartElement("title");
                xmlStreamWriter.writeCharacters(course.getTitle()); // Course title
                xmlStreamWriter.writeEndElement();

                xmlStreamWriter.writeStartElement("description");
                xmlStreamWriter.writeCharacters(course.getDescription()); // Course description
                xmlStreamWriter.writeEndElement();

                xmlStreamWriter.writeStartElement("duration");
                xmlStreamWriter.writeCharacters(String.valueOf(course.getDuration())); // Course duration
                xmlStreamWriter.writeEndElement();

                xmlStreamWriter.writeEndElement(); // End element "course"
            }

            xmlStreamWriter.writeEndElement(); // End main element
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
        } catch (IOException | XMLStreamException e) {
            System.err.println("Error while saving courses to XML: " + e.getMessage());
        }
    }

    public List<String> readCourseFromXML() {
        List<String> courseData = new ArrayList<>();
        String currentElement = "";
        StringBuilder courseInfo = new StringBuilder();

        try {
            // Creating XML input factory and event reader
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new FileInputStream(XML_COURSES_FILE_PATH));

            // Parsing XML events
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    currentElement = startElement.getName().getLocalPart();
                }

                if (event.isCharacters()) {
                    Characters characters = event.asCharacters();

                    if (!characters.isWhiteSpace()) {
                        switch (currentElement) {
                            case "title":
                                courseInfo.append("Title: ").append(characters.getData()).append("\n");
                                break;
                            case "description":
                                courseInfo.append("Description: ").append(characters.getData()).append("\n");
                                break;
                            case "duration":
                                courseInfo.append("Duration: ").append(characters.getData()).append(" hours\n");
                                break;
                        }
                    }
                }

                // If it's the closing tag of the course element, add the course info to the list
                if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("course")) {
                    courseData.add(courseInfo.toString());
                    courseInfo.setLength(0); // Reset StringBuilder
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return courseData;
    }


    public List<Course> CoursesFromXMLtoObjects() {
        List<Course> courses = new ArrayList<>();
        Course currentCourse = null;
        String currentElement = "";
        String title = "";
        String description = "";
        int duration = 0;

        try {
            // Creating XML input factory and event reader
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new FileInputStream(XML_COURSES_FILE_PATH));

            // Parsing XML events
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                // If the event is the start of an element
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    currentElement = startElement.getName().getLocalPart();

                    if ("course".equals(currentElement)) {
                        // New course element, create a new Course object
                        currentCourse = new Course("", "", 0);
                    }
                }

                // If it is a text between the tags
                if (event.isCharacters()) {
                    Characters characters = event.asCharacters();

                    if (!characters.isWhiteSpace()) {
                        switch (currentElement) {
                            case "title":
                                title = characters.getData();
                                break;
                            case "description":
                                description = characters.getData();
                                break;
                            case "duration":
                                duration = Integer.parseInt(characters.getData());
                                break;
                        }
                    }
                }

                // If it is the end of an element
                if (event.isEndElement()) {
                    String endElement = event.asEndElement().getName().getLocalPart();

                    if ("course".equals(endElement)) {
                        // Finished parsing a course element, add it to the list
                        currentCourse = new Course(title, description, duration);
                        courses.add(currentCourse);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return courses;
    }
}