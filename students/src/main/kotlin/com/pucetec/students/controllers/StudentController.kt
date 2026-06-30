package com.pucetec.students.controllers

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.services.StudentService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/students")
class StudentController(
    val studentService: StudentService
) {
    val logger = LoggerFactory.getLogger(StudentController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createStudent(@RequestBody request: StudentRequest): StudentResponse {
        return studentService.createStudent(request)
    }

    @GetMapping
    fun getAllStudents(): List<StudentResponse> {
        logger.info("Request to get all students")
        return studentService.getAllStudents()
    }

    @GetMapping("/{id}")
    fun getStudentById(@PathVariable id: Long): StudentResponse {
        return studentService.getStudentById(id)
    }

    @PutMapping("/{id}")
    fun updateStudent(
        @PathVariable id: Long,
        @RequestBody request: StudentRequest
    ): StudentResponse {
        return studentService.updateStudent(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStudent(@PathVariable id: Long) {
        studentService.deleteStudent(id)
    }
}