package com.example.onlinecourses.controller;

import com.example.onlinecourses.model.Course;
import com.example.onlinecourses.model.Student;
import com.example.onlinecourses.model.Enrollment;

import com.example.onlinecourses.service.CourseService;
import com.example.onlinecourses.service.InitializeService;
import com.example.onlinecourses.service.EnrollmentService;

import com.example.onlinecourses.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class HomeController {
    private static List<Course> courses;
    private static List<Student> students;

    private final InitializeService initializeService;
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final StudentService studentService;

    public HomeController(InitializeService initializeService, EnrollmentService enrollmentService, CourseService courseService, StudentService studentService) {
        this.initializeService = initializeService;
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String home(Model model) throws Exception {
        courses = initializeService.initializeCourses();
        students = initializeService.initializeStudents();
        List<Enrollment> enrollments = initializeService.createEnrollments(students, courses);


        // !Students part
        // Filter students by name containing 'a'
        List<Student> streamStudents = students.stream()
                .filter(student -> student.getName().contains("a"))
                .collect(Collectors.toList());

        // Calculate the number of students with the same name
        Map<String, Long> nameCount = students.stream()
                .collect(Collectors.groupingBy(Student::getName, Collectors.counting()));

        // Only duplicate names
        Map<String, Long> duplicateNames = nameCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        long amountDuplicateNames = duplicateNames.size();

        // Only names stream from another stream with map
        Stream<String> streamStudent = students.stream().map(Student::getName);

        studentService.writeStudentsToFile(students); // Write to file
        List<Student> studentsFromFile = studentService.readStudentsFromFile(); // Read from file

        // *LAB 2
        studentService.saveStudentsToXML(students); // Save students to XML

        List<String> studentStrings = studentService.readStudentsFromXMLToStrings(); // Read from XML to string
        List<Student> studentObjects = studentService.readStudentsFromXMLToObjects(); // Read from XML to objects





        //!Courses part
        // Filter courses with Java in the title
        Stream<Course> streamCoursesWithJava = courses.stream()
                .filter(course -> course.getTitle().contains("Java"));

        // Calculate the number of courses with the same duration
        Map<Integer, Long> courseCount = courses.stream()
                .collect(Collectors.groupingBy(Course::getDuration, Collectors.counting()));

        // Only duplicate durations
        Map<Integer, Long> duplicateDurationsOfCourses = courseCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        long amountOfDuplicateCourses = duplicateDurationsOfCourses.size();

        // Time of courses in minutes
        Stream<String> streamCourseDurationsInMinutes = courses.stream()
                .map(course -> course.getTitle() + " - " + (course.getDuration() * 60) + " minutes");

        // *Using Java NIO
        courseService.writeCoursesToFile(courses); // Write to file JavaNIO
        List<Course> coursesFromFile = courseService.readCoursesFromFile(); // Read from file JavaNIO

        // *Using Java IO
        courseService.writeCoursesToFileJavaIO(courses); // Write to file JavaIO
        List<Course> coursesFromFileJavaIO = courseService.readCoursesFromFileJavaIO(); // Read from file JavaIO


        // *LAB 2
        courseService.saveToXMLCourses(courses); // Write to XML

        List<String> readedCourses = courseService.readCourseFromXML(); // Read from XML to string
        List<Course> loadedCourses = courseService.CoursesFromXMLtoObjects(); // Read from XML to objects





        // !Enrollments part
        // Filter enrollments with student name containing 'Shepeta'
        Stream<Enrollment> streamEnrollmentsContainsSth = enrollments.stream()
                .filter(enrollment -> enrollment.getStudent().getName().contains("Shepeta"));

        // Count enrollments for each student
        Map<String, Long> studentEnrollmentCounts = enrollments.stream()
                .collect(Collectors.groupingBy(
                        enrollment -> enrollment.getStudent().getName(), // Group by student name
                        Collectors.counting() // Count occurrences
                ));

        // Create a Stream from another stream with map
        Stream<String> streamWithMap = enrollments.stream().map(enrollment -> enrollment.getStudent().getName() + " enrolled in " + enrollment.getCourse().getTitle());

        enrollmentService.writeEnrollmentsToFile(enrollments); // Write to file
        List<Enrollment> enrollmentsFromFile = enrollmentService.readEnrollmentsFromFile(); // Read from file

        // *LAB 2
        enrollmentService.saveEnrollmentsToXML(enrollments); // Save enrollments to XML
        List<Enrollment> enrollmentsFromXML = enrollmentService.readEnrollmentsFromXML(); // Read enrollments from XML


        // Add all attributes to the model
        try {
            model.addAttribute("courses",courses);
            model.addAttribute("streamCoursesWithJava",streamCoursesWithJava);
            model.addAttribute("duplicateDurationsOfCourses",duplicateDurationsOfCourses);
            model.addAttribute("amountOfDuplicateCourses",amountOfDuplicateCourses);
            model.addAttribute("streamCourseDurationsInMinutes",streamCourseDurationsInMinutes);
            model.addAttribute("coursesFromFile",coursesFromFile);
            model.addAttribute("coursesFromFileJavaIO",coursesFromFileJavaIO);
            model.addAttribute("StringCoursesFromXML", readedCourses);
            model.addAttribute("ObjectsCoursesFromXML",loadedCourses);


            model.addAttribute("students",students);
            model.addAttribute("filteredStudents",streamStudents);
            model.addAttribute("duplicateNames",duplicateNames);
            model.addAttribute("amountDuplicateNames",amountDuplicateNames);
            model.addAttribute("studentsFromFile",studentsFromFile);
            model.addAttribute("studentStrings", studentStrings);
            model.addAttribute("studentObjects", studentObjects);


            model.addAttribute("enrollments", enrollments);
            model.addAttribute("streamEnrollmentsContainsSth",streamEnrollmentsContainsSth);
            model.addAttribute("studentEnrollmentCounts",studentEnrollmentCounts);
            model.addAttribute("enrollmentsFromFile",enrollmentsFromFile);
            model.addAttribute("enrollmentsFromXML",enrollmentsFromXML);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "index";
}

    public static Student findStudentByName(String name) {
        return students.stream()
                .filter(student -> student.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static Course findCourseByTitle(String title) {
        return courses.stream()
                .filter(course -> course.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

}
