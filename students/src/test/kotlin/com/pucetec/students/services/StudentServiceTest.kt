package com.pucetec.students.services

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.entities.Student
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.repositories.StudentRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class StudentServiceTest {

    @Mock lateinit var studentRepository: StudentRepository
    @InjectMocks lateinit var studentService: StudentService

    @Test
    fun `createStudent lanza BlankNameException si nombre vacio`() {
        val request = StudentRequest(name = "", email = "test@test.com")
        assertThrows<BlankNameException> { studentService.createStudent(request) }
    }

    @Test
    fun `createStudent guarda correctamente`() {
        val request = StudentRequest(name = "Ana", email = "ana@test.com")
        val student = Student(id = 1L, name = "Ana", email = "ana@test.com")

        `when`(studentRepository.save(any())).thenReturn(student)

        val result = studentService.createStudent(request)
        assertEquals("Ana", result.name)
    }

    @Test
    fun `getAllStudents retorna lista`() {
        val student = Student(id = 1L, name = "Ana")
        `when`(studentRepository.findAll()).thenReturn(listOf(student))

        val result = studentService.getAllStudents()
        assertEquals(1, result.size)
    }

    @Test
    fun `getStudentById lanza excepcion si no existe`() {
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<StudentNotFoundException> { studentService.getStudentById(99L) }
    }

    @Test
    fun `getStudentById retorna estudiante si existe`() {
        val student = Student(id = 1L, name = "Ana")
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        val result = studentService.getStudentById(1L)
        assertEquals("Ana", result.name)
    }

    @Test
    fun `updateStudent lanza BlankNameException si nombre vacio`() {
        val request = StudentRequest(name = "   ", email = "test@test.com")
        assertThrows<BlankNameException> { studentService.updateStudent(1L, request) }
    }

    @Test
    fun `updateStudent lanza excepcion si no existe`() {
        val request = StudentRequest(name = "Ana", email = "test@test.com")
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows<StudentNotFoundException> { studentService.updateStudent(99L, request) }
    }

    @Test
    fun `updateStudent actualiza exitosamente`() {
        val request = StudentRequest(name = "Ana Actualizada", email = "ana2@test.com")
        val existing = Student(id = 1L, name = "Ana Vieja")
        val updated = Student(id = 1L, name = "Ana Actualizada", email = "ana2@test.com")

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(studentRepository.save(any())).thenReturn(updated)

        val result = studentService.updateStudent(1L, request)
        assertEquals("Ana Actualizada", result.name)
    }

    @Test
    fun `deleteStudent lanza excepcion si no existe`() {
        `when`(studentRepository.existsById(99L)).thenReturn(false)
        assertThrows<StudentNotFoundException> { studentService.deleteStudent(99L) }
    }

    @Test
    fun `deleteStudent borra exitosamente`() {
        `when`(studentRepository.existsById(1L)).thenReturn(true)
        studentService.deleteStudent(1L)
        verify(studentRepository).deleteById(1L)
    }

    private fun <T> any(): T = org.mockito.Mockito.any()
}