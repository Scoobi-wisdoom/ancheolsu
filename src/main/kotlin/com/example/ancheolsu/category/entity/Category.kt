package com.example.ancheolsu.category.entity

import com.example.ancheolsu.common.BaseEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id

@Entity
class Category(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: CategoryId = 0L,
    val categoryName: String,
): BaseEntity() {
}
