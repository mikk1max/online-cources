package com.example.onlinecourses.controller;

import com.example.onlinecourses.model.Course;
import com.example.onlinecourses.model.Student;
import com.example.onlinecourses.model.Enrollment;
import com.example.onlinecourses.model.StudentList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.xml.stream.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class HomeController {

    private static final String STUDENT_FILE_PATH = "./src/main/resources/data/students.txt";
    private static final String COURSE_FILE_PATH = "./src/main/resources/data/courses.txt";
    private static final String ENROLLMENT_FILE_PATH = "./src/main/resources/data/enrollments.txt";
    private static final String STUDENT_FILE_XML_PATH = "./src/main/resources/data/students.xml";
    private static final String STUDENT_FILE_XML_PATH2 = "./src/main/resources/data/students2.xml";
    private List<Course> courses;
    private List<Student> students;
    private List<Enrollment> enrollments;

    private List<Course> initializeCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Spring Boot Basics", "Poznaj podstawy Spring Boot", 10));
        courses.add(new Course("React Native", "Twórz aplikacje mobilne za pomocą React Native", 8));
        courses.add(new Course("Advanced Java", "Master advanced concepts of Java programming", 12));
        courses.add(new Course("Node.js & Express", "Build backend services using Node.js and Express", 9));
        courses.add(new Course("HTML & CSS Fundamentals", "Learn the basics of web development with HTML and CSS", 6));
        courses.add(new Course("JavaScript ES6+", "Explore modern JavaScript features from ES6 and beyond", 7));
        courses.add(new Course("Database Management", "Understand relational databases and SQL", 10));
        courses.add(new Course("Firebase Integration", "Integrate Firebase services in your mobile and web apps", 5));
        courses.add(new Course("React.js", "Build interactive UIs with React.js", 8));
        courses.add(new Course("REST API Development", "Design and implement RESTful APIs", 6));
        return courses;
    }

    private List<Student> initializeStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student("Milena Runets", 21));
        students.add(new Student("Max Shepeta", 20));
        students.add(new Student("Leanid Shaveika", 21));
        students.add(new Student("John Doe", 22));
        students.add(new Student("Oleg Nowak", 21));
        students.add(new Student("Jane Doe", 21));
        students.add(new Student("Justin Quinn", 15));
        students.add(new Student("Rayan Goslindg", 13));
        students.add(new Student("Oleg Nowak", 25));
        students.add(new Student("John Doe", 22));
        students.add(new Student("Adam Kim", 55));
        students.add(new Student("Nicholas Jones", 15));
        return students;
    }

    private List<Enrollment> createEnrollments(List<Student> students, List<Course> courses) {
        List<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(new Enrollment(students.get(0), courses.get(0)));
        enrollments.add(new Enrollment(students.get(1), courses.get(1)));
        enrollments.add(new Enrollment(students.get(0), courses.get(2)));
        enrollments.add(new Enrollment(students.get(1), courses.get(3)));
        enrollments.add(new Enrollment(students.get(2), courses.get(4)));
        enrollments.add(new Enrollment(students.get(2), courses.get(5)));
        enrollments.add(new Enrollment(students.get(3), courses.get(2)));
        enrollments.add(new Enrollment(students.get(3), courses.get(6)));
        enrollments.add(new Enrollment(students.get(4), courses.get(7)));
        enrollments.add(new Enrollment(students.get(5), courses.get(8)));
        enrollments.add(new Enrollment(students.get(1), courses.get(7)));
        enrollments.add(new Enrollment(students.get(5), courses.get(9)));
        enrollments.add(new Enrollment(students.get(6), courses.get(5)));
        enrollments.add(new Enrollment(students.get(7), courses.get(6)));
        enrollments.add(new Enrollment(students.get(8), courses.get(4)));
        enrollments.add(new Enrollment(students.get(9), courses.get(3)));
        enrollments.add(new Enrollment(students.get(10), courses.get(1)));
        enrollments.add(new Enrollment(students.get(11), courses.get(0)));
        enrollments.add(new Enrollment(students.get(1), courses.get(0)));
        return enrollments;
    }

    @GetMapping("/")
    public String home(Model model) throws XMLStreamException, FileNotFoundException {

        courses = initializeCourses();
        students = initializeStudents();
        enrollments = createEnrollments(students, courses);


        // !Students part
        // Zapisz studentów do pliku w porządku alfabetycznym i tylko tych, którzy mają 18 lat lub więcej
        writeStudentsToFile(students);

        // Odczytaj studentów z pliku
        List<Student> studentsFromFile = readStudentsFromFile();

        List<Student> streamStudents = students.stream()
                .filter(student -> student.getName().contains("a"))
                .collect(Collectors.toList());

        // Zliczanie powtarzających się imion studentów
        Map<String, Long> nameCount = students.stream()
                .collect(Collectors.groupingBy(Student::getName, Collectors.counting()));

        // Wyświetlaj tylko powtarzające się imiona
        Map<String, Long> duplicateNames = nameCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        long amountDuplicateNames = duplicateNames.size();

        // Strumień tylko imion
        Stream<String> streamStudent = students.stream().map(Student::getName);

        //!Courses part
        //Filtrowanie po ciagu znakow kluczowemu
        Stream<Course> streamCoursesWithJava = courses.stream()
                .filter(course -> course.getTitle().contains("Java"));

        //Zliczanie powtarzajacych sie godzin trwania kursow
        Map<Integer, Long> courseCount = courses.stream()
                .collect(Collectors.groupingBy(Course::getDuration, Collectors.counting()));

        Map<Integer, Long> duplicateDurationsOfCourses = courseCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        long amountOfDuplicateCourses = duplicateDurationsOfCourses.size();

        //Wyswietlanie czasu trwania kursow w minutach
        Stream<String> streamCourseDurationsInMinutes = courses.stream()
                .map(course -> course.getTitle() + " - " + (course.getDuration() * 60) + " minutes");

        //dodanie do pliku
        writeCoursesToFile(courses);
        //odczytanie z pliku
        List<Course> coursesFromFile = readCoursesFromFile();

        //zapis do pliku JavaIO
        writeCoursesToFileJavaIO(courses);

        //odczyt z pliku JavaIO
        List<Course> coursesFromFileJavaIO = readCoursesFromFileJavaIO();


        // !Enrollments part
        // Create a stream and use filter on it
        Stream<Enrollment> streamEnrollmentsContainsSth = enrollments.stream()
                .filter(enrollment -> enrollment.getStudent().getName().contains("Shepeta"));

        // Stream and count repeating students
        Map<String, Long> studentEnrollmentCounts = enrollments.stream()
                .collect(Collectors.groupingBy(
                        enrollment -> enrollment.getStudent().getName(), // Group by student name
                        Collectors.counting() // Count occurrences
                ));

        // Create a Stream from another stream
        Stream<String> streamWithMap = enrollments.stream().map(enrollment -> enrollment.getStudent().getName() + " enrolled in " + enrollment.getCourse().getTitle());

        // Write to the file
        writeEnrollmentsToFile(enrollments);

        // Read from the file
        List<Enrollment> enrollmentsFromFile = readEnrollmentsFromFile();


        //!Lista Studentów XML
        try {
            // Tworzenie listy studentów
            List<Student> studentList = new ArrayList<>();
            studentList.add(new Student("John Snow", 20));
            studentList.add(new Student("Adam Smith", 22));
            studentList.add(new Student("Mikel John", 21));

            // Zapis studentów do XML-a
            StudentList studentxml = new StudentList();
            studentxml.setStudents(studentList);
            saveToXMLStudents(studentxml);

            // Odczyt studentów z XML-a
            StudentList loadedStudents = readFromXMLStudents();
            model.addAttribute("studentxml", loadedStudents);


        } catch (Exception e) {
            e.printStackTrace();
        }

        Student student1 = new Student("Jorge Pepa", 30);
        Student student2 = new Student("Kirill Adamow", 23);
        Student student3 = new Student("Francua Debua", 41);

        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        try (FileOutputStream outputStream = new FileOutputStream(STUDENT_FILE_XML_PATH2)) {
            XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(outputStream);

            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
            xmlStreamWriter.writeStartElement("students");
            saveStudentXML(xmlStreamWriter, student1);
            saveStudentXML(xmlStreamWriter, student2);
            saveStudentXML(xmlStreamWriter, student3);

            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();

            xmlStreamWriter.flush();
            xmlStreamWriter.close(); // Zamykamy writer
        } catch (IOException | XMLStreamException e) {
            System.err.println("Błąd zapisu studentów " + e.getMessage());
        }

        // Odczyt studentów z xml
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(STUDENT_FILE_XML_PATH2));
        List<Student> zad2 = new ArrayList<>();
       while (xmlStreamReader.hasNext()) {

           if (xmlStreamReader.isStartElement() && "student".equals(xmlStreamReader.getLocalName())) {
               Student student = readStudent(xmlStreamReader); // Używamy metodę do odzytu studentów

               // Sprawdzenie tworzenia studenta
               if (student == null) {
                  System.out.println("Błąd podczas tworzenia studenta");
               }
               else {
                   zad2.add(student);
               }
           }
           xmlStreamReader.next();
       }

        model.addAttribute("courses",courses);
        model.addAttribute("streamCoursesWithJava",streamCoursesWithJava);
        model.addAttribute("duplicateDurationsOfCourses",duplicateDurationsOfCourses);
        model.addAttribute("amountOfDuplicateCourses",amountOfDuplicateCourses);
        model.addAttribute("streamCourseDurationsInMinutes",streamCourseDurationsInMinutes);
        model.addAttribute("coursesFromFile",coursesFromFile);
        model.addAttribute("coursesFromFileJavaIO",coursesFromFileJavaIO);
        model.addAttribute("zad2", zad2);

        model.addAttribute("students",students);

        model.addAttribute("filteredStudents",streamStudents);
        model.addAttribute("duplicateNames",duplicateNames);
        model.addAttribute("amountDuplicateNames",amountDuplicateNames);
        model.addAttribute("studentsFromFile",studentsFromFile);

        model.addAttribute("enrollments",enrollments);
        model.addAttribute("streamEnrollmentsContainsSth",streamEnrollmentsContainsSth);
        model.addAttribute("studentEnrollmentCounts",studentEnrollmentCounts);
        model.addAttribute("enrollmentsFromFile",enrollmentsFromFile);

        return"index";
}

//metoda zapisu JavaIO
public void writeCoursesToFileJavaIO(List<Course> courses) {
    File file = new File("./src/main/resources/data/CoursesIO.txt");
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

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader("./src/main/resources/data/CoursesIO.txt"))) {
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
        System.out.println("Plik nie odnaleziono!");
        ex.printStackTrace();
    } catch (IOException ex) {
        System.out.println("Błąd odczytu pliku spowodowany:");
        ex.printStackTrace();
    }

    // Process courses if needed or return them if the method signature allows
    return courses;
}

// Metoda do zapisu kursow do pliku
private void writeCoursesToFile(List<Course> courses) {
    List<String> coursesLines = courses.stream()
            .filter(course -> course.getDuration() >= 8) // Filtrujemy kursy, którzy trwaja 8 godzin lub więcej
            .sorted(Comparator.comparingInt(Course::getDuration)) // Sortujemy po czasu trwania
            .map(course -> course.getTitle() + "-" + course.getDuration()) // Format zapisu: nazwa-czas
            .collect(Collectors.toList());
    try {
        Files.write(Paths.get(COURSE_FILE_PATH), coursesLines);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

// Metoda do odczytu kursow z pliku
private List<Course> readCoursesFromFile() {
    List<Course> courses = new ArrayList<>();
    try (Stream<String> lines = Files.lines(Paths.get(COURSE_FILE_PATH))) {
        lines.forEach(line -> {
            String[] parts = line.split("-"); // Zakładamy, że dane są w formacie: nazwa-czas
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

// Metoda do zapisu studentów do pliku
private void writeStudentsToFile(List<Student> students) {
    List<String> studentLines = students.stream()
            .filter(student -> student.getAge() >= 18) // Filtrujemy studentów, którzy mają 18 lat lub więcej
            .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName())) // Sortujemy po imieniu
            .map(student -> student.getName() + "," + student.getAge()) // Format zapisu: imię,wiek
            .collect(Collectors.toList());
    try {
        Files.write(Paths.get(STUDENT_FILE_PATH), studentLines);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void writeEnrollmentsToFile(List<Enrollment> enrollmentStream) {
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

// Metoda do odczytu studentów z pliku
private List<Student> readStudentsFromFile() {
    List<Student> students = new ArrayList<>();
    try (Stream<String> lines = Files.lines(Paths.get(STUDENT_FILE_PATH))) {
        lines.forEach(line -> {
            String[] parts = line.split(","); // Zakładamy, że dane są w formacie: imię,wiek
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


private List<Enrollment> readEnrollmentsFromFile() {
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

private Student findStudentByName(String name) {
    return students.stream()
            .filter(student -> student.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null); // Return null if not found
}

private Course findCourseByTitle(String title) {
    return courses.stream()
            .filter(course -> course.getTitle().equalsIgnoreCase(title))
            .findFirst()
            .orElse(null); // Return null if not found
}


public static void saveToXMLStudents(StudentList students) {//zapisanie listy studentów
    XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

    try (FileOutputStream outputStream = new FileOutputStream(STUDENT_FILE_XML_PATH)) {
        XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(outputStream);
        xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
        xmlStreamWriter.writeStartElement("students"); // Początek elementu głównego

        for (Student student : students.getStudents()) {
            saveStudentXML(xmlStreamWriter, student);
        }

        xmlStreamWriter.writeEndElement(); // Koniec elementu głównego
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.flush();
    } catch (IOException | XMLStreamException e) {
        System.err.println("Błąd podczas zapisywania danych studentów do XML: " + e.getMessage());
    }
}

private static void saveStudentXML(XMLStreamWriter xmlStreamWriter, Student student) throws XMLStreamException {
    xmlStreamWriter.writeStartElement("student"); // Начало элемента "student"

    xmlStreamWriter.writeStartElement("name");
    xmlStreamWriter.writeCharacters(student.getName()); // Имя студента
    xmlStreamWriter.writeEndElement(); // Конец элемента "name"

    xmlStreamWriter.writeStartElement("age");
    xmlStreamWriter.writeCharacters(String.valueOf(student.getAge())); // Возраст студента
    xmlStreamWriter.writeEndElement(); // Конец элемента "age"

    xmlStreamWriter.writeEndElement(); // Конец элемента "student"
}


// Odczyt listy studentów z XML-a
private static StudentList readFromXMLStudents() throws Exception {
    StudentList studentList = new StudentList();
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

            if (name != null && age != -1) {
                students.add(new Student(name, age));
            } else {
                System.out.println("Błąd podczas tworzenia studenta");
            }
            name = null;
            age = -1;
        }
    }
    xmlStreamReader.close();
    studentList.setStudents(students);
    return studentList;
}


private static Student readStudent(XMLStreamReader xmlStreamReader) throws XMLStreamException {
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
            break; // Kończymy odczyt elementu "student"
        }
    }

    // Sprawdzamy, czy wszystkie wymagane dane zostały odczytane
    if (name != null && age > 0) {

        return new Student(name, age);
    } else {
        return null; // Zwracamy null, jeśli nie udało się utworzyć obiektu
    }
}





}
