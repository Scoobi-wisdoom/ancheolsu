package com.example.ancheolsu.library.service

import com.example.ancheolsu.book.entity.Book
import com.example.ancheolsu.book.entity.BookId
import com.example.ancheolsu.book.entity.QBook
import com.example.ancheolsu.book.repository.BookRepository
import com.example.ancheolsu.bookcategoryrelation.entity.BookCategoryRelation
import com.example.ancheolsu.bookcategoryrelation.entity.QBookCategoryRelation
import com.example.ancheolsu.bookcategoryrelation.repository.BookCategoryRelationRepository
import com.example.ancheolsu.category.entity.Category
import com.example.ancheolsu.category.entity.CategoryId
import com.example.ancheolsu.category.entity.QCategory
import com.example.ancheolsu.category.repository.CategoryRepository
import com.example.ancheolsu.common.exception.BusinessException
import com.example.ancheolsu.library.service.model.RegisterBookModel
import com.example.ancheolsu.library.service.model.RegisterBookResultModel
import com.example.ancheolsu.library.service.model.RegisterCategoryModel
import com.example.ancheolsu.library.service.model.RegisterCategoryResultModel
import com.example.ancheolsu.library.service.model.SearchBookByCategoryModel
import com.example.ancheolsu.library.service.model.SearchBookModel
import com.example.ancheolsu.library.service.model.SearchBookResultModel
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
        val categories = findValidCategoryNames(model.categoryNames)
        val book = bookRepository.save(model.toEntity())
        saveBookCategoryRelation(book, categories)
        return RegisterBookResultModel(book.id)
    }

    private fun findValidCategoryNames(categoryNames: Set<String>): List<Category> {
        val categories = findAllByCategoryNames(categoryNames)
        val notExistingCategoryNames = categoryNames - categories.map { it.categoryName }.toSet()
        return if (notExistingCategoryNames.isEmpty()) categories else throw BusinessException(
            message = "non existent categories: $notExistingCategoryNames",
            cause = null,
            httpStatus = BAD_REQUEST,
        )
    }

    private fun findAllByCategoryNames(categoryNames: Set<String>): List<Category> {
        return categoryRepository.findAll(QCategory.category.categoryName.`in`(categoryNames)).toList()
    }

    private fun saveBookCategoryRelation(
        book: Book,
        categories: List<Category>,
    ) {
        bookCategoryRelationRepository.saveAll(BookCategoryRelation.from(book, categories))
    }

    @Transactional
    fun registerCategory(model: RegisterCategoryModel): RegisterCategoryResultModel {
        validateCategoryRegister(model.categoryName)

        val category = categoryRepository.save(model.toEntity())

        return RegisterCategoryResultModel(category.id)
    }

    private fun validateCategoryRegister(categoryName: String) {
        if (findByCategoryName(categoryName) != null) throw BusinessException(
            message = "category $categoryName already exists.",
            cause = null,
            httpStatus = BAD_REQUEST,
        )
    }

    private fun findByCategoryName(categoryName: String): Category? {
        return categoryRepository.findByCategoryName(categoryName)
    }

    @Transactional(readOnly = true)
    fun searchBook(model: SearchBookModel): Page<SearchBookResultModel> {
        val predicate = getSearchBookPredicateFrom(model)
        val books = findAllBooksFrom(predicate, model.pageable)

        return getSearchBookResultModels(books)
    }

    private fun getSearchBookPredicateFrom(model: SearchBookModel): BooleanBuilder {
        val booleanBuilder = BooleanBuilder()
        model.title?.let {
            booleanBuilder.and(
                QBook.book.title.eq(it)
            )
        }
        model.author?.let {
            booleanBuilder.and(
                QBook.book.author.eq(it)
            )
        }
        return booleanBuilder
    }

    private fun findAllBooksFrom(
        predicate: BooleanBuilder,
        pageable: Pageable
    ): Page<Book> {
        return bookRepository.findAll(predicate, pageable)
    }

    private fun getSearchBookResultModels(
        books: Page<Book>
    ): Page<SearchBookResultModel> {
        val bookIds = books.map { it.id }.toList()
        val bookCategoryRelations = findAllBookCategoryRelationByBookIds(bookIds)
        val bookIdToCategoryRelations = bookCategoryRelations.groupBy { it.bookId }

        val categoryIds = bookCategoryRelations.map { it.categoryId }
        val categoryIdToCategory = findAllCategoriesFrom(categoryIds).associateBy { it.id }

        return books.map { book ->
            val correspondingCategoryRelations = bookIdToCategoryRelations[book.id]!!
            val correspondingCategoryNames =
                correspondingCategoryRelations.map { categoryIdToCategory[it.categoryId]!!.categoryName }

            SearchBookResultModel(
                categories = correspondingCategoryNames,
                author = book.author,
                title = book.title,
                borrowable = book.borrowable
            )
        }
    }

    private fun findAllBookCategoryRelationByBookIds(bookIds: List<BookId>): MutableIterable<BookCategoryRelation> =
        bookCategoryRelationRepository.findAll(QBookCategoryRelation.bookCategoryRelation.bookId.`in`(bookIds))

    private fun findAllCategoriesFrom(categoryIds: List<CategoryId>): MutableIterable<Category> =
        categoryRepository.findAll(QCategory.category.id.`in`(categoryIds))

    @Transactional(readOnly = true)
    fun searchBookByCategory(model: SearchBookByCategoryModel): Page<SearchBookResultModel> {
        val category = findValidCategoryName(model.categoryName)
        val bookCategoryRelations = findAllBookCategoryRelationByCategoryId(category.id)
        val bookIds = bookCategoryRelations.map { it.bookId }.distinct()
        val books = findAllBooksByIds(bookIds = bookIds, pageable = model.pageable)

        return getSearchBookResultModels(books)
    }

    private fun findValidCategoryName(categoryName: String): Category {
        return findByCategoryName(categoryName) ?: throw BusinessException(
            message = "category $categoryName does not exist.",
            cause = null,
            httpStatus = BAD_REQUEST,
        )
    }

    private fun findAllBooksByIds(
        bookIds: List<BookId>,
        pageable: Pageable
    ) = bookRepository.findAll(QBook.book.id.`in`(bookIds), pageable)

    private fun findAllBookCategoryRelationByCategoryId(categoryId: CategoryId): List<BookCategoryRelation> {
        return bookCategoryRelationRepository.findAllByCategoryId(categoryId)
    }
}
