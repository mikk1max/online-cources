package com.example.onlinecourses.controller;
import com.example.onlinecourses.model.Course;
import com.example.onlinecourses.model.Student;
import com.example.onlinecourses.model.Enrollment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
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

    private static final String STUDENT_FILE_PATH = "students.txt";
    private static final String ENROLLMENT_FILE_PATH = "enrollments.txt";

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
    public String home(Model model) {

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





        model.addAttribute("courses", courses);

        model.addAttribute("students", students);
        model.addAttribute("filteredStudents", streamStudents);
        model.addAttribute("duplicateNames", duplicateNames);
        model.addAttribute("amountDuplicateNames", amountDuplicateNames);
        model.addAttribute("studentsFromFile", studentsFromFile);

        model.addAttribute("enrollments", enrollments);
        model.addAttribute("streamEnrollmentsContainsSth", streamEnrollmentsContainsSth);
        model.addAttribute("studentEnrollmentCounts", studentEnrollmentCounts);
        model.addAttribute("enrollmentsFromFile", enrollmentsFromFile);

        return "index";
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
        }  catch (IOException e) {
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

}
