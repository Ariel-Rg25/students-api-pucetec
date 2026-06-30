package com.pucetec.students.services

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.entities.Professor
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.ProfessorNotFoundException
import com.pucetec.students.repositories.ProfessorRepository
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
class ProfessorServiceTest {

    @Mock lateinit var professorRepository: ProfessorRepository
    @InjectMocks lateinit var professorService: ProfessorService

    @Test
    fun `createProfessor lanza BlankNameException si nombre vacio`() {
        val request = ProfessorRequest(name = "   ", email = "test@test.com")
        assertThrows<BlankNameException> { professorService.createProfessor(request) }
    }

    @Test
    fun `createProfessor guarda correctamente`() {
        val request = ProfessorRequest(name = "Dr. Silva", email = "silva@test.com")
        val professor = Professor(id = 1L, name = "Dr. Silva", email = "silva@test.com")

        `when`(professorRepository.save(any())).thenReturn(professor)

        val result = professorService.createProfessor(request)
        assertEquals("Dr. Silva", result.name)
    }

    @Test
    fun `getAllProfessors retorna lista`() {
        val professor = Professor(id = 1L, name = "Dr. Silva")
        `when`(professorRepository.findAll()).thenReturn(listOf(professor))

        val result = professorService.getAllProfessors()
        assertEquals(1, result.size)
    }

    @Test
    fun `getProfessorById lanza excepcion si no existe`() {
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<ProfessorNotFoundException> { professorService.getProfessorById(99L) }
    }

    @Test
    fun `getProfessorById retorna profesor si existe`() {
        val professor = Professor(id = 1L, name = "Dr. Silva")
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))

        val result = professorService.getProfessorById(1L)
        assertEquals("Dr. Silva", result.name)
    }

    @Test
    fun `updateProfessor lanza BlankNameException si nombre vacio`() {
        val request = ProfessorRequest(name = "", email = "test@test.com")
        assertThrows<BlankNameException> { professorService.updateProfessor(1L, request) }
    }

    @Test
    fun `updateProfessor lanza excepcion si no existe`() {
        val request = ProfessorRequest(name = "Dr. Silva", email = "test@test.com")
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows<ProfessorNotFoundException> { professorService.updateProfessor(99L, request) }
    }

    @Test
    fun `updateProfessor actualiza exitosamente`() {
        val request = ProfessorRequest(name = "Dr. Nuevo", email = "nuevo@test.com")
        val existing = Professor(id = 1L, name = "Dr. Viejo")
        val updated = Professor(id = 1L, name = "Dr. Nuevo", email = "nuevo@test.com")

        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(professorRepository.save(any())).thenReturn(updated)

        val result = professorService.updateProfessor(1L, request)
        assertEquals("Dr. Nuevo", result.name)
    }

    @Test
    fun `deleteProfessor lanza excepcion si no existe`() {
        `when`(professorRepository.existsById(99L)).thenReturn(false)
        assertThrows<ProfessorNotFoundException> { professorService.deleteProfessor(99L) }
    }

    @Test
    fun `deleteProfessor borra exitosamente`() {
        `when`(professorRepository.existsById(1L)).thenReturn(true)
        professorService.deleteProfessor(1L)
        verify(professorRepository).deleteById(1L)
    }

    private fun <T> any(): T = org.mockito.Mockito.any()
}