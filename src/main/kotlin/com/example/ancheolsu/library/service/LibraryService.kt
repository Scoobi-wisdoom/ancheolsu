package com.example.ancheolsu.library.service

import com.example.ancheolsu.book.entity.Book
import com.example.ancheolsu.book.repository.BookRepository
import com.example.ancheolsu.bookcategoryrelation.entity.BookCategoryRelation
import com.example.ancheolsu.bookcategoryrelation.repository.BookCategoryRelationRepository
import com.example.ancheolsu.category.entity.Category
import com.example.ancheolsu.category.repository.CategoryRepository
import com.example.ancheolsu.common.exception.BusinessException
import com.example.ancheolsu.library.service.model.RegisterBookModel
import com.example.ancheolsu.library.service.model.RegisterBookResultModel
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LibraryService(
    private val categoryRepository: CategoryRepository,
    private val bookRepository: BookRepository,
    private val bookCategoryRelationRepository: BookCategoryRelationRepository,
) {
    @Transactional
    fun registerBook(model: RegisterBookModel): RegisterBookResultModel {
        val category = findByCategoryName(model.category)
        val book = bookRepository.save(model.toEntity())
        saveBookCategoryRelation(book, category)
        return RegisterBookResultModel(book.id)
    }

    private fun findByCategoryName(categoryName: String): Category {
        return categoryRepository.findByCategoryName(categoryName) ?: throw BusinessException(
            message = "category $categoryName does not exist.",
            httpStatus = BAD_REQUEST,
            cause = null,
        )
    }

    private fun saveBookCategoryRelation(
        book: Book,
        category: Category
    ) {
        bookCategoryRelationRepository.save(BookCategoryRelation.from(book, category))
    }
}
