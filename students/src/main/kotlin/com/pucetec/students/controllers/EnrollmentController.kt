package com.pucetec.students.controllers

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.dto.EnrollmentUpdateRequest
import com.pucetec.students.services.EnrollmentService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/enrollments")
class EnrollmentController(
    val enrollmentService: EnrollmentService
) {
    val logger = LoggerFactory.getLogger(EnrollmentController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createEnrollment(@RequestBody request: EnrollmentRequest): EnrollmentResponse {
        return enrollmentService.createEnrollment(request)
    }

    @GetMapping
    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Request to get all enrollments")
        return enrollmentService.getAllEnrollments()
    }

    @GetMapping("/{id}")
    fun getEnrollmentById(@PathVariable id: Long): EnrollmentResponse {
        return enrollmentService.getEnrollmentById(id)
    }

    @PutMapping("/{id}")
    fun updateEnrollmentStatus(
        @PathVariable id: Long,
        @RequestBody request: EnrollmentUpdateRequest
    ): EnrollmentResponse {
        return enrollmentService.updateEnrollmentStatus(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEnrollment(@PathVariable id: Long) {
        enrollmentService.deleteEnrollment(id)
    }
}