package com.example.ancheolsu.category.repository

import com.example.ancheolsu.category.entity.Category
import com.example.ancheolsu.category.entity.CategoryId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface CategoryRepository: JpaRepository<Category, CategoryId>, QuerydslPredicateExecutor<Category> {
    fun findByCategoryName(categoryName: String): Category?
}
