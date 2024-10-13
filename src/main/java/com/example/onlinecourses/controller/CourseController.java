package com.example.onlinecourses.controller;

import com.example.onlinecourses.model.Course;
import com.example.onlinecourses.model.Student;
import com.example.onlinecourses.model.Enrollment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api") // Added a base path for the API
public class CourseController {

    private final List<Course> courses = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private final List<Enrollment> enrollments = new ArrayList<>();

    // Get all courses
    @GetMapping("/courses")
    public List<Course> getCourses() {
        return courses;
    }

    // Get all students
    @GetMapping("/students")
    public List<Student> getStudents() {
        return students;
    }

    // Get all enrollments
    @GetMapping("/enrollments")
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    // Add a new course
    @PostMapping("/courses")
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        courses.add(course);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    // Add a new student
    @PostMapping("/students")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        students.add(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    // Enroll a student in a course
    @PostMapping("/enrollments")
    public ResponseEntity<Enrollment> enrollStudent(@RequestBody Enrollment enrollment) {
        enrollments.add(enrollment);
        return new ResponseEntity<>(enrollment, HttpStatus.CREATED);
    }
}
