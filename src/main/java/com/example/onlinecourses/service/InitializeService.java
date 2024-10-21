package com.example.onlinecourses.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.onlinecourses.model.Course;
import com.example.onlinecourses.model.Enrollment;
import com.example.onlinecourses.model.Student;

@Service
public class InitializeService {
    public List<Course> initializeCourses() {
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

    public List<Student> initializeStudents() {
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

    public List<Enrollment> createEnrollments(List<Student> students, List<Course> courses) {
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
}
