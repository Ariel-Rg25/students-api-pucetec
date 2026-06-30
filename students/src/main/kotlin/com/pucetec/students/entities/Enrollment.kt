package com.pucetec.students.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "enrollments")
class Enrollment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val status: String = "INSCRITO",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    val student: Student,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    val subject: Subject
)