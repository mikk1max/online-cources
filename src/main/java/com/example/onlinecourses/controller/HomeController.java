package com.example.onlinecourses.controller;

import com.example.onlinecourses.model.Course;
import com.example.onlinecourses.model.Student;
import com.example.onlinecourses.model.Enrollment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class HomeController {

    private static final String STUDENT_FILE_PATH = "students.txt"; // Путь к файлу

    @GetMapping("/")
    public String home(Model model) {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Spring Boot Basics", "Learn the basics of Spring Boot", 10));
        courses.add(new Course("React Native", "Build mobile apps using React Native", 8));

        List<Student> students = new ArrayList<>();
        students.add(new Student("John Doe", 22));
        students.add(new Student("Jane Doe", 21));
        students.add(new Student("Milena Runets", 21));
        students.add(new Student("Max Shepeta", 20));
        students.add(new Student("Leanid Shaveika", 21));
        students.add(new Student("Oleg Nowak", 25));
        students.add(new Student("Oleg Nowak", 21));
        students.add(new Student("John Doe", 22));

        // Запись студентов в файл
        writeStudentsToFile(students);

        // Чтение студентов из файла
        List<Student> studentsFromFile = readStudentsFromFile();

        List<Student> streamStudents = students.stream()
                .filter(student -> student.getName().contains("a"))
                .collect(Collectors.toList());

        // Подсчет повторяющихся имен студентов
        Map<String, Long> nameCount = students.stream()
                .collect(Collectors.groupingBy(Student::getName, Collectors.counting()));

        // Выводим только повторяющиеся имена
        Map<String, Long> duplicateNames = nameCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        long amountDuplicateNames = duplicateNames.size();

        // Стрим только имена
        Stream<String> streamStudent = students.stream().map(Student::getName);
        List<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(new Enrollment(students.get(0), courses.get(0)));
        enrollments.add(new Enrollment(students.get(1), courses.get(1)));

        model.addAttribute("courses", courses);
        model.addAttribute("students", students);
        model.addAttribute("studentsFromFile", studentsFromFile); // Добавляем считанных студентов
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("filteredStudents", streamStudents);
        model.addAttribute("duplicateNames", duplicateNames);
        model.addAttribute("amountDuplicateNames", amountDuplicateNames);
        model.addAttribute("streamStudent", streamStudent);
        return "index";
    }

    // Метод для записи студентов в файл
    private void writeStudentsToFile(List<Student> students) {
        List<String> studentLines = students.stream()
                .map(student -> student.getName() + "," + student.getAge()) // Формат записи: имя,возраст
                .collect(Collectors.toList());
        try {
            Files.write(Paths.get(STUDENT_FILE_PATH), studentLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для чтения студентов из файла
    private List<Student> readStudentsFromFile() {
        List<Student> students = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(STUDENT_FILE_PATH))) {
            lines.forEach(line -> {
                String[] parts = line.split(","); // Предполагаем, что данные в формате: имя,возраст
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
