package com.example.ucs067.studentmanagement.controller;

import java.util.List;

import com.example.ucs067.studentmanagement.Student;
import com.example.ucs067.studentmanagement.repository.StudentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
public class HelloController {

    private final StudentRepository studentRepository;

    public HelloController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }

    @GetMapping("/student")
    public Student getStudent() {
        return new Student(
                1,
                "Ranjit",
                "ranjit@example.com"
        );
    }

    // GET all students
    @GetMapping("/students")
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    // POST - add a new student
    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // GET student by ID
    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT - update an existing student
    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable int id,
            @RequestBody Student updatedStudent) {

        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(updatedStudent.getName());
                    student.setEmail(updatedStudent.getEmail());

                    Student savedStudent = studentRepository.save(student);

                    return ResponseEntity.ok(savedStudent);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - delete a student
    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {

        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        studentRepository.deleteById(id);

        return ResponseEntity.ok("Student deleted successfully");
    }
    @GetMapping("/students/search/name/{name}")
    public List<Student> searchByName(@PathVariable String name) {
        return studentRepository.findByName(name);
    }
    @GetMapping("/students/search/name/contains/{keyword}")
    public List<Student> searchByNameContaining(@PathVariable String keyword) {
        return studentRepository.findByNameContaining(keyword);
    }

    @GetMapping("/students/search/email/{email}")
    public List<Student> searchByEmail(@PathVariable String email) {
        return studentRepository.findByEmail(email);
    }
    @GetMapping("/students/page")
    public Page<Student> getStudentsPage(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }
    // GET students sorted by name
    @GetMapping("/students/sort/name")
    public List<Student> getStudentsSortedByName() {
        return studentRepository.findAll(Sort.by("name"));
    }



}
