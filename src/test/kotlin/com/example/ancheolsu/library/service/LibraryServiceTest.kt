package com.example.ancheolsu.library.service

import com.example.ancheolsu.book.entity.Book
import com.example.ancheolsu.book.repository.BookRepository
import com.example.ancheolsu.bookcategoryrelation.entity.BookCategoryRelation
import com.example.ancheolsu.bookcategoryrelation.repository.BookCategoryRelationRepository
import com.example.ancheolsu.category.entity.Category
import com.example.ancheolsu.category.repository.CategoryRepository
import com.example.ancheolsu.common.exception.BusinessException
import com.example.ancheolsu.library.service.model.RegisterBookModel
import com.example.ancheolsu.library.service.model.RegisterCategoryModel
import com.example.ancheolsu.library.service.model.SearchBookByCategoryModel
import com.example.ancheolsu.library.service.model.SearchBookModel
import com.querydsl.core.types.Predicate
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

internal class LibraryServiceTest {
    private val categoryRepository: CategoryRepository = mockk()
    private val bookRepository: BookRepository = mockk()
    private val bookCategoryRelationRepository: BookCategoryRelationRepository = mockk()

    private val libraryService = LibraryService(
        categoryRepository = categoryRepository,
        bookRepository = bookRepository,
        bookCategoryRelationRepository = bookCategoryRelationRepository,
    )

    @Test
    fun `메서드 registerBook 이 받는 인자 중에는 category 가 있는데 이 category 가 Category 테이블에 없으면 BusinessException 이 발생한다`() {
        every { categoryRepository.findAll(any<Predicate>()) } returns listOf()
        val model = RegisterBookModel(
            title = "",
            author = "",
            categoryNames = setOf(""),
        )

        assertThrows<BusinessException> {
            libraryService.registerBook(model)
        }
    }

    @Test
    fun `메서드 registerCategory 이 받는 인자 중에는 category 가 있는데 이 categoryName 이 Category 테이블에 이미 있으면 BusinessException 이 발생한다`() {
        every { categoryRepository.findByCategoryName(any()) } returns mockk()
        val model = RegisterCategoryModel(categoryName = "")

        assertThrows<BusinessException> {
            libraryService.registerCategory(model)
        }
    }

    @Test
    fun `메서드 searchBook 은 title, author 로 book 을 검색하고 book 의 categories 정보도 반환한다`() {
        val one = "one"
        val bookOne = Book(id = 0, author = one, title = one, borrowable = true)
        val two = "two"
        val bookTwo = Book(id = 1, author = two, title = two, borrowable = true)
        val pageable = PageRequest.of(0, 10)

        every { bookRepository.findAll(any<Predicate>(), any<Pageable>()) } returns PageImpl(
            listOf(bookOne, bookTwo),
            pageable,
            2L,
        )

        every { bookCategoryRelationRepository.findAll(any<Predicate>()) } returns mutableListOf(
            BookCategoryRelation(id = 0, bookId = 0, categoryId = 0),
            BookCategoryRelation(id = 1, bookId = 1, categoryId = 0),
            BookCategoryRelation(id = 2, bookId = 1, categoryId = 1),
        )

        val categoryOne = Category(id = 0, categoryName = one)
        val categoryTwo = Category(id = 1, categoryName = two)
        every { categoryRepository.findAll(any<Predicate>()) } returns mutableListOf(
            categoryOne,
            categoryTwo,
        )

        val actual =
            libraryService.searchBook(
                SearchBookModel(
                    title = null,
                    author = null,
                    pageable = pageable
                )
            ).toList()

        assertAll(
            { assertTrue(actual[0].title == bookOne.title) },
            { assertTrue(actual[0].author == bookOne.author) },
            { assertTrue(actual[0].borrowable == bookOne.borrowable) },
            { assertTrue(actual[0].categories.size == 1) },
            { assertTrue(actual[0].categories == listOf(categoryOne.categoryName)) },

            { assertTrue(actual[1].title == bookTwo.title) },
            { assertTrue(actual[1].author == bookTwo.author) },
            { assertTrue(actual[1].borrowable == bookTwo.borrowable) },
            { assertTrue(actual[1].categories.size == 2) },
            { assertTrue(actual[1].categories == listOf(categoryOne.categoryName, categoryTwo.categoryName)) },
        )
    }

    @Test
    fun `메서드 searchBookByCategory 은 categoryName 으로 book 을 검색해 해당 category 를 가진 모든 book 을 반환한다`() {
        val sharedCategory = Category(id = 0, categoryName = "shared")
        every { categoryRepository.findByCategoryName(any()) } returns sharedCategory

        val bookCategoryRelation = BookCategoryRelation(id = 0, bookId = 0, categoryId = sharedCategory.id)
        every { bookCategoryRelationRepository.findAllByCategoryId(sharedCategory.id) } returns listOf(
            bookCategoryRelation
        )

        val pageable = PageRequest.of(0, 10)
        every { bookRepository.findAll(any<Predicate>(), any<Pageable>()) } returns PageImpl(
            listOf(
                Book(id = 0, author = "", title = "", borrowable = true),
                Book(id = 1, author = "", title = "", borrowable = true),
            ),
            pageable,
            2L,
        )

        every { bookCategoryRelationRepository.findAll(any<Predicate>()) } returns mutableListOf(
            BookCategoryRelation(id = 0, bookId = 0, categoryId = sharedCategory.id),
            BookCategoryRelation(id = 1, bookId = 1, categoryId = sharedCategory.id),
            BookCategoryRelation(id = 2, bookId = 1, categoryId = 1),
        )

        every { categoryRepository.findAll(any<Predicate>()) } returns mutableListOf(
            sharedCategory,
            Category(id = 1, categoryName = ""),
        )

        val actual = libraryService.searchBookByCategory(
            SearchBookByCategoryModel(
                categoryName = sharedCategory.categoryName,
                pageable = pageable,
            )
        ).toList()

        assertAll(
            { assertTrue(actual.size == 2) },
            { assertTrue(actual[0].categories.contains(sharedCategory.categoryName)) },
            { assertTrue(actual[1].categories.contains(sharedCategory.categoryName)) },
        )
    }
}
