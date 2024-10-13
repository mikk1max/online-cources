package com.example.onlinecourses.controller;

import com.example.onlinecourses.model.Course;
import com.example.onlinecourses.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

//    @RequestMapping( method = RequestMethod.GET, value = "/")
//    public String getWelcomeMessage() {
//        return "Greetings from Spring Boot! Hello World!";
//    }

    private final List<Course> courses = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("courses", courses);
        model.addAttribute("students", students);
        return "index"; // This will look for 'index.html' in the 'resources/templates' folder
    }

    @PostMapping("/courses")
    public String addCourse(@RequestParam String title,
                            @RequestParam String description,
                            @RequestParam int duration) {
        courses.add(new Course(title, description, duration));
        return "redirect:/"; // Redirect back to the home page
    }

    @PostMapping("/students")
    public String addStudent(@RequestParam String name,
                             @RequestParam int age) {
        students.add(new Student(name, age));
        return "redirect:/"; // Redirect back to the home page
    }
}
