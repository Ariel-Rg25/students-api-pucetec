package com.pucetec.students.controllers

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.services.SubjectService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subjects")
class SubjectController(
    val subjectService: SubjectService
) {
    val logger = LoggerFactory.getLogger(SubjectController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSubject(@RequestBody request: SubjectRequest): SubjectResponse {
        return subjectService.createSubject(request)
    }

    @GetMapping
    fun getAllSubjects(): List<SubjectResponse> {
        logger.info("Request to get all subjects")
        return subjectService.getAllSubjects()
    }

    @GetMapping("/{id}")
    fun getSubjectById(@PathVariable id: Long): SubjectResponse {
        return subjectService.getSubjectById(id)
    }

    @PutMapping("/{id}")
    fun updateSubject(
        @PathVariable id: Long,
        @RequestBody request: SubjectRequest
    ): SubjectResponse {
        return subjectService.updateSubject(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSubject(@PathVariable id: Long) {
        subjectService.deleteSubject(id)
    }
}