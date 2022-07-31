package com.example.ancheolsu.bookcategoryrelation.repository

import com.example.ancheolsu.bookcategoryrelation.entity.BookCategoryRelation
import com.example.ancheolsu.bookcategoryrelation.entity.BookCategoryRelationId
import com.example.ancheolsu.category.entity.CategoryId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface BookCategoryRelationRepository : JpaRepository<BookCategoryRelation, BookCategoryRelationId>,
    QuerydslPredicateExecutor<BookCategoryRelation> {
        fun findAllByCategoryId(categoryId: CategoryId): List<BookCategoryRelation>
}
