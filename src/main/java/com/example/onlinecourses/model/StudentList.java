package com.example.onlinecourses.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;


import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "students")
public class StudentList {
    private List<Student> students = new ArrayList<>();

    @XmlElement(name = "student")
    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
