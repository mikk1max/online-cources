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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class HomeController {

    private static final String STUDENT_FILE_PATH = "students.txt"; // Ścieżka do pliku

    @GetMapping("/")
    public String home(Model model) {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Spring Boot Basics", "Poznaj podstawy Spring Boot", 10));
        courses.add(new Course("React Native", "Twórz aplikacje mobilne za pomocą React Native", 8));

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
        List<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(new Enrollment(students.get(0), courses.get(0)));
        enrollments.add(new Enrollment(students.get(1), courses.get(1)));

        model.addAttribute("courses", courses);
        model.addAttribute("students", students);
        model.addAttribute("studentsFromFile", studentsFromFile); // Dodajemy odczytanych studentów
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("filteredStudents", streamStudents);
        model.addAttribute("duplicateNames", duplicateNames);
        model.addAttribute("amountDuplicateNames", amountDuplicateNames);
        model.addAttribute("streamStudent", streamStudent);
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
}
