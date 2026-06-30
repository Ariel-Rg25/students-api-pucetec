package com.pucetec.students.services

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.ProfessorNotFoundException
import com.pucetec.students.exceptions.SubjectNotFoundException
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.ProfessorRepository
import com.pucetec.students.repositories.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SubjectService(
    private val subjectRepository: SubjectRepository,
    private val professorRepository: ProfessorRepository 
) {
    private val logger = LoggerFactory.getLogger(SubjectService::class.java)

    fun createSubject(request: SubjectRequest): SubjectResponse {
        validateRequest(request)

        logger.info("Creating subject ${request.name}")
        val professor = professorRepository.findById(request.professorId).orElseThrow {
            ProfessorNotFoundException("No se encontró el profesor con ID: ${request.professorId}")
        }

        val savedSubject = subjectRepository.save(request.toEntity(professor))
        return savedSubject.toResponse()
    }

    fun getAllSubjects(): List<SubjectResponse> {
        logger.info("Getting all subjects")
        return subjectRepository.findAll().map { it.toResponse() }
    }

    fun getSubjectById(id: Long): SubjectResponse {
        logger.info("Getting subject by id: $id")
        val subject = subjectRepository.findById(id).orElseThrow {
            SubjectNotFoundException("No se encontró la materia con ID: $id")
        }
        return subject.toResponse()
    }

    fun updateSubject(id: Long, request: SubjectRequest): SubjectResponse {
        validateRequest(request)

        logger.info("Updating subject with id: $id")
        val existingSubject = subjectRepository.findById(id).orElseThrow {
            SubjectNotFoundException("No se encontró la materia con ID: $id")
        }

        val professor = professorRepository.findById(request.professorId).orElseThrow {
            ProfessorNotFoundException("No se encontró el profesor con ID: ${request.professorId}")
        }

        val updatedSubject = Subject(
            id = existingSubject.id,
            name = request.name,
            code = request.code,
            professor = professor
        )
        return subjectRepository.save(updatedSubject).toResponse()
    }

    fun deleteSubject(id: Long) {
        logger.info("Deleting subject with id: $id")
        if (!subjectRepository.existsById(id)) {
            throw SubjectNotFoundException("No se encontró la materia con ID: $id")
        }
        subjectRepository.deleteById(id)
    }

    private fun validateRequest(request: SubjectRequest) {
        if (request.name.isBlank()) throw BlankNameException("El nombre de la materia no puede estar en blanco")
        if (request.code.isBlank()) throw BlankNameException("El código de la materia no puede estar en blanco")
    }
}