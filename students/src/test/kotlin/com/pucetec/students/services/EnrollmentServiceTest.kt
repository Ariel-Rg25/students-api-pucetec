package com.pucetec.students.services

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentUpdateRequest
import com.pucetec.students.entities.Enrollment
import com.pucetec.students.entities.Student
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.EnrollmentNotFoundException
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.exceptions.SubjectNotFoundException
import com.pucetec.students.repositories.EnrollmentRepository
import com.pucetec.students.repositories.StudentRepository
import com.pucetec.students.repositories.SubjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class EnrollmentServiceTest {

    @Mock lateinit var enrollmentRepository: EnrollmentRepository
    @Mock lateinit var studentRepository: StudentRepository
    @Mock lateinit var subjectRepository: SubjectRepository
    @InjectMocks lateinit var enrollmentService: EnrollmentService

    @Test
    fun `createEnrollment lanza StudentNotFoundException si estudiante no existe`() {
        val request = EnrollmentRequest(studentId = 99L, subjectId = 1L)
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows<StudentNotFoundException> { enrollmentService.createEnrollment(request) }
    }

    @Test
    fun `createEnrollment lanza SubjectNotFoundException si materia no existe`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 99L)
        val student = Student(id = 1L, name = "Ariel")

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows<SubjectNotFoundException> { enrollmentService.createEnrollment(request) }
    }

    @Test
    fun `createEnrollment retorna respuesta exitosa`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 1L)
        val student = Student(id = 1L, name = "Ariel")
        val subject = Subject(id = 1L, name = "Arquitectura", code = "AE-1", professor = com.pucetec.students.entities.Professor())
        val enrollment = Enrollment(id = 1L, student = student, subject = subject)

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))
        `when`(enrollmentRepository.save(any())).thenReturn(enrollment)

        val result = enrollmentService.createEnrollment(request)
        assertEquals("INSCRITO", result.status)
        assertEquals("Ariel", result.student.name)
    }

    @Test
    fun `getAllEnrollments retorna lista`() {
        val student = Student(id = 1L, name = "Ariel")
        val subject = Subject(id = 1L, name = "Arquitectura", code = "AE-1", professor = com.pucetec.students.entities.Professor())
        val enrollment = Enrollment(id = 1L, student = student, subject = subject)

        `when`(enrollmentRepository.findAll()).thenReturn(listOf(enrollment))
        val result = enrollmentService.getAllEnrollments()
        assertEquals(1, result.size)
    }

    @Test
    fun `getEnrollmentById lanza excepcion si no existe`() {
        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<EnrollmentNotFoundException> { enrollmentService.getEnrollmentById(99L) }
    }

    @Test
    fun `getEnrollmentById retorna datos si existe`() {
        val student = Student(id = 1L, name = "Ariel")
        val subject = Subject(id = 1L, name = "Arquitectura", code = "AE-1", professor = com.pucetec.students.entities.Professor())
        val enrollment = Enrollment(id = 1L, student = student, subject = subject)

        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment))
        val result = enrollmentService.getEnrollmentById(1L)
        assertEquals(1L, result.id)
    }

    @Test
    fun `updateEnrollmentStatus lanza excepcion si no existe`() {
        val request = EnrollmentUpdateRequest(status = "APROBADO")
        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<EnrollmentNotFoundException> { enrollmentService.updateEnrollmentStatus(99L, request) }
    }

    @Test
    fun `updateEnrollmentStatus actualiza correctamente`() {
        val request = EnrollmentUpdateRequest(status = "APROBADO")
        val student = Student(id = 1L, name = "Ariel")
        val subject = Subject(id = 1L, name = "Arquitectura", code = "AE-1", professor = com.pucetec.students.entities.Professor())
        val existingEnrollment = Enrollment(id = 1L, student = student, subject = subject, status = "INSCRITO", createdAt = LocalDateTime.now())
        val updatedEnrollment = Enrollment(id = 1L, student = student, subject = subject, status = "APROBADO", createdAt = existingEnrollment.createdAt)

        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(existingEnrollment))
        `when`(enrollmentRepository.save(any())).thenReturn(updatedEnrollment)

        val result = enrollmentService.updateEnrollmentStatus(1L, request)
        assertEquals("APROBADO", result.status)
    }

    @Test
    fun `deleteEnrollment lanza excepcion si no existe`() {
        `when`(enrollmentRepository.existsById(99L)).thenReturn(false)
        assertThrows<EnrollmentNotFoundException> { enrollmentService.deleteEnrollment(99L) }
    }

    @Test
    fun `deleteEnrollment elimina exitosamente`() {
        `when`(enrollmentRepository.existsById(1L)).thenReturn(true)
        enrollmentService.deleteEnrollment(1L)
        verify(enrollmentRepository).deleteById(1L)
    }

    private fun <T> any(): T = org.mockito.Mockito.any()
}