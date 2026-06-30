package com.pucetec.students.controllers

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.services.ProfessorService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/professors")
class ProfessorController(
    val professorService: ProfessorService
) {
    val logger = LoggerFactory.getLogger(ProfessorController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProfessor(@RequestBody request: ProfessorRequest): ProfessorResponse {
        return professorService.createProfessor(request)
    }

    @GetMapping
    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("Request to get all professors")
        return professorService.getAllProfessors()
    }

    @GetMapping("/{id}")
    fun getProfessorById(@PathVariable id: Long): ProfessorResponse {
        return professorService.getProfessorById(id)
    }

    @PutMapping("/{id}")
    fun updateProfessor(
        @PathVariable id: Long,
        @RequestBody request: ProfessorRequest
    ): ProfessorResponse {
        return professorService.updateProfessor(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProfessor(@PathVariable id: Long) {
        professorService.deleteProfessor(id)
    }
}