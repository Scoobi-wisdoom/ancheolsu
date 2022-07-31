package com.example.ancheolsu.book.entity

import com.example.ancheolsu.common.BaseEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id

@Entity
class Book(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: BookId = 0L,
    val author: String,
    val title: String,
    var borrowable: Boolean,
) : BaseEntity() {
}
