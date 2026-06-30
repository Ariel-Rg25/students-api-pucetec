package com.pucetec.students.services

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.entities.Professor
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.ProfessorNotFoundException
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.ProfessorRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProfessorService(
    private val repository: ProfessorRepository
) {
    private val logger = LoggerFactory.getLogger(ProfessorService::class.java)

    fun createProfessor(request: ProfessorRequest): ProfessorResponse {
        if (request.name.isBlank()) throw BlankNameException("El nombre del profesor no puede estar en blanco")

        logger.info("Creating professor ${request.name}")
        val savedProfessor = repository.save(request.toEntity())
        return savedProfessor.toResponse()
    }

    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("Getting all professors")
        return repository.findAll().map { it.toResponse() }
    }

    fun getProfessorById(id: Long): ProfessorResponse {
        logger.info("Getting professor by id: $id")
        val professor = repository.findById(id).orElseThrow {
            ProfessorNotFoundException("No se encontró el profesor con ID: $id")
        }
        return professor.toResponse()
    }

    fun updateProfessor(id: Long, request: ProfessorRequest): ProfessorResponse {
        if (request.name.isBlank()) throw BlankNameException("El nombre del profesor no puede estar en blanco")

        logger.info("Updating professor with id: $id")
        val existingProfessor = repository.findById(id).orElseThrow {
            ProfessorNotFoundException("No se encontró el profesor con ID: $id")
        }

        val updatedProfessor = Professor(
            id = existingProfessor.id,
            name = request.name,
            email = request.email
        )
        return repository.save(updatedProfessor).toResponse()
    }

    fun deleteProfessor(id: Long) {
        logger.info("Deleting professor with id: $id")
        if (!repository.existsById(id)) {
            throw ProfessorNotFoundException("No se encontró el profesor con ID: $id")
        }
        repository.deleteById(id)
    }
}