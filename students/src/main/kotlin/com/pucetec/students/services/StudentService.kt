package com.pucetec.students.services

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.entities.Student
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.StudentRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val repository: StudentRepository
) {
    private val logger = LoggerFactory.getLogger(StudentService::class.java)

    fun createStudent(request: StudentRequest): StudentResponse {
        if (request.name.isBlank()) throw BlankNameException("El nombre del estudiante no puede estar en blanco")

        logger.info("Creating student ${request.name}")
        val savedStudent = repository.save(request.toEntity())
        return savedStudent.toResponse()
    }

    fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")
        return repository.findAll().map { it.toResponse() }
    }

    fun getStudentById(id: Long): StudentResponse {
        logger.info("Getting student by id: $id")
        val student = repository.findById(id).orElseThrow {
            StudentNotFoundException("No se encontró el estudiante con ID: $id")
        }
        return student.toResponse()
    }

    fun updateStudent(id: Long, request: StudentRequest): StudentResponse {
        if (request.name.isBlank()) throw BlankNameException("El nombre del estudiante no puede estar en blanco")

        logger.info("Updating student with id: $id")
        val existingStudent = repository.findById(id).orElseThrow {
            StudentNotFoundException("No se encontró el estudiante con ID: $id")
        }

        val updatedStudent = Student(
            id = existingStudent.id,
            name = request.name,
            email = request.email
        )
        return repository.save(updatedStudent).toResponse()
    }

    fun deleteStudent(id: Long) {
        logger.info("Deleting student with id: $id")
        if (!repository.existsById(id)) {
            throw StudentNotFoundException("No se encontró el estudiante con ID: $id")
        }
        repository.deleteById(id)
    }
}