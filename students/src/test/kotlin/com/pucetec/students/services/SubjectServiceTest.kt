package com.pucetec.students.services

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.entities.Professor
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.ProfessorNotFoundException
import com.pucetec.students.exceptions.SubjectNotFoundException
import com.pucetec.students.repositories.ProfessorRepository
import com.pucetec.students.repositories.SubjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class SubjectServiceTest {

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var subjectService: SubjectService

    @Test
    fun `createSubject lanza BlankNameException cuando el nombre esta vacio`() {
        val request = SubjectRequest(name = "", code = "AE-101", professorId = 1L)
        assertThrows<BlankNameException> { subjectService.createSubject(request) }
    }

    @Test
    fun `createSubject lanza BlankNameException cuando el codigo esta vacio`() {
        val request = SubjectRequest(name = "Arquitectura", code = "", professorId = 1L)
        assertThrows<BlankNameException> { subjectService.createSubject(request) }
    }

    @Test
    fun `createSubject lanza ProfessorNotFoundException si el profesor no existe`() {
        val request = SubjectRequest(name = "Arquitectura", code = "AE-101", professorId = 99L)
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows<ProfessorNotFoundException> { subjectService.createSubject(request) }
    }

    @Test
    fun `createSubject retorna SubjectResponse cuando los datos son validos`() {
        val request = SubjectRequest(name = "Arquitectura", code = "AE-101", professorId = 1L)
        val professor = Professor(id = 1L, name = "Dr. Garcia", email = "garcia@puce.edu.ec")
        val subject = Subject(id = 1L, name = "Arquitectura", code = "AE-101", professor = professor)

        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any())).thenReturn(subject)

        val result = subjectService.createSubject(request)

        assertEquals("Arquitectura", result.name)
        assertEquals("AE-101", result.code)
        assertEquals("Dr. Garcia", result.professor.name)
    }

    @Test
    fun `getAllSubjects retorna lista de materias`() {
        val professor = Professor(id = 1L, name = "Dr. Garcia", email = "garcia@puce.edu.ec")
        val subjects = listOf(
            Subject(id = 1L, name = "Arquitectura", code = "AE-101", professor = professor),
            Subject(id = 2L, name = "Bases de Datos", code = "BD-101", professor = professor)
        )
        `when`(subjectRepository.findAll()).thenReturn(subjects)

        val result = subjectService.getAllSubjects()

        assertEquals(2, result.size)
        assertEquals("Arquitectura", result[0].name)
    }

    @Test
    fun `getSubjectById lanza SubjectNotFoundException cuando la materia no existe`() {
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<SubjectNotFoundException> { subjectService.getSubjectById(99L) }
    }

    @Test
    fun `getSubjectById retorna la materia cuando existe`() {
        val professor = Professor(id = 1L, name = "Dr. Garcia", email = "garcia@puce.edu.ec")
        val subject = Subject(id = 1L, name = "Arquitectura", code = "AE-101", professor = professor)
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        val result = subjectService.getSubjectById(1L)

        assertEquals(1L, result.id)
        assertEquals("Arquitectura", result.name)
    }

    @Test
    fun `updateSubject lanza SubjectNotFoundException si la materia no existe`() {
        val request = SubjectRequest(name = "Arquitectura Avanzada", code = "AE-201", professorId = 1L)
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows<SubjectNotFoundException> { subjectService.updateSubject(99L, request) }
    }

    @Test
    fun `updateSubject lanza ProfessorNotFoundException si el profesor no existe`() {
        val request = SubjectRequest(name = "Arquitectura Avanzada", code = "AE-201", professorId = 99L)
        val professor = Professor(id = 1L, name = "Dr. Garcia", email = "garcia@puce.edu.ec")
        val existingSubject = Subject(id = 1L, name = "Arquitectura", code = "AE-101", professor = professor)

        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject))
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows<ProfessorNotFoundException> { subjectService.updateSubject(1L, request) }
    }

    @Test
    fun `updateSubject retorna SubjectResponse actualizado cuando es valido`() {
        val request = SubjectRequest(name = "Arquitectura Avanzada", code = "AE-201", professorId = 2L)

        val oldProfessor = Professor(id = 1L, name = "Dr. Garcia")
        val newProfessor = Professor(id = 2L, name = "Dr. Lopez")

        val existingSubject = Subject(id = 1L, name = "Arquitectura", code = "AE-101", professor = oldProfessor)
        val updatedSubject = Subject(id = 1L, name = "Arquitectura Avanzada", code = "AE-201", professor = newProfessor)

        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject))
        `when`(professorRepository.findById(2L)).thenReturn(Optional.of(newProfessor))
        `when`(subjectRepository.save(any())).thenReturn(updatedSubject)

        val result = subjectService.updateSubject(1L, request)

        assertEquals("Arquitectura Avanzada", result.name)
        assertEquals("AE-201", result.code)
        assertEquals("Dr. Lopez", result.professor.name)
    }

    @Test
    fun `deleteSubject lanza SubjectNotFoundException si no existe`() {
        `when`(subjectRepository.existsById(99L)).thenReturn(false)
        assertThrows<SubjectNotFoundException> { subjectService.deleteSubject(99L) }
    }

    @Test
    fun `deleteSubject elimina exitosamente cuando existe`() {
        `when`(subjectRepository.existsById(1L)).thenReturn(true)

        subjectService.deleteSubject(1L)

        verify(subjectRepository).deleteById(1L)
    }

    private fun <T> any(): T = org.mockito.Mockito.any()
}