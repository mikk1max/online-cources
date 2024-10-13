package com.example.onlinecourses.controller;

import com.example.onlinecourses.model.Course;
import com.example.onlinecourses.model.Student;
import com.example.onlinecourses.model.Enrollment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CourseController {

    @GetMapping("/courses")
    public List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Spring Boot Basics", "Learn the basics of Spring Boot", 10));
        courses.add(new Course("React Native", "Build mobile apps using React Native", 8));
        return courses;
    }

    @GetMapping("/students")
    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student("John Doe", 22));
        students.add(new Student("Jane Doe", 21));
        return students;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/enrollments")
    public List<Enrollment> getEnrollments() {
        Student student = new Student("John Doe", 22);
        Course course = new Course("Spring Boot Basics", "Learn the basics of Spring Boot", 10);
        List<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(new Enrollment(student, course));
        return enrollments;
    }
}
