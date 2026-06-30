package com.pucetec.students.services

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.dto.EnrollmentUpdateRequest
import com.pucetec.students.entities.Enrollment
import com.pucetec.students.exceptions.EnrollmentNotFoundException
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.exceptions.SubjectNotFoundException
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.EnrollmentRepository
import com.pucetec.students.repositories.StudentRepository
import com.pucetec.students.repositories.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val studentRepository: StudentRepository,
    private val subjectRepository: SubjectRepository
) {
    private val logger = LoggerFactory.getLogger(EnrollmentService::class.java)

    fun createEnrollment(request: EnrollmentRequest): EnrollmentResponse {
        logger.info("Creating enrollment for student ${request.studentId} in subject ${request.subjectId}")

        val student = studentRepository.findById(request.studentId).orElseThrow {
            StudentNotFoundException("No se encontró el estudiante con ID: ${request.studentId}")
        }

        val subject = subjectRepository.findById(request.subjectId).orElseThrow {
            SubjectNotFoundException("No se encontró la materia con ID: ${request.subjectId}")
        }

        val savedEnrollment = enrollmentRepository.save(request.toEntity(student, subject))
        return savedEnrollment.toResponse()
    }

    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Getting all enrollments")
        return enrollmentRepository.findAll().map { it.toResponse() }
    }

    fun getEnrollmentById(id: Long): EnrollmentResponse {
        logger.info("Getting enrollment by id: $id")
        val enrollment = enrollmentRepository.findById(id).orElseThrow {
            EnrollmentNotFoundException("No se encontró la inscripción con ID: $id")
        }
        return enrollment.toResponse()
    }

    fun updateEnrollmentStatus(id: Long, request: EnrollmentUpdateRequest): EnrollmentResponse {
        logger.info("Updating enrollment status with id: $id")
        val existingEnrollment = enrollmentRepository.findById(id).orElseThrow {
            EnrollmentNotFoundException("No se encontró la inscripción con ID: $id")
        }

        val updatedEnrollment = Enrollment(
            id = existingEnrollment.id,
            createdAt = existingEnrollment.createdAt,
            status = request.status,
            student = existingEnrollment.student,
            subject = existingEnrollment.subject
        )
        return enrollmentRepository.save(updatedEnrollment).toResponse()
    }

    fun deleteEnrollment(id: Long) {
        logger.info("Deleting enrollment with id: $id")
        if (!enrollmentRepository.existsById(id)) {
            throw EnrollmentNotFoundException("No se encontró la inscripción con ID: $id")
        }
        enrollmentRepository.deleteById(id)
    }
}