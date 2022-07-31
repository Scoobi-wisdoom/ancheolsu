package com.example.ancheolsu.library.service

import com.example.ancheolsu.category.repository.CategoryRepository
import com.example.ancheolsu.common.exception.BusinessException
import com.example.ancheolsu.library.service.model.RegisterBookModel
import com.example.ancheolsu.library.service.model.RegisterCategoryModel
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class LibraryServiceTest {
    private val categoryRepository: CategoryRepository = mockk()
    private val libraryService = LibraryService(
        categoryRepository = categoryRepository,
        bookRepository = mockk(),
        bookCategoryRelationRepository = mockk(),
    )

    @Test
    fun `메서드 registerBook 이 받는 인자 중에는 category 가 있는데 이 category 가 Category 테이블에 없으면 BusinessException 이 발생한다`() {
        every { categoryRepository.findByCategoryName(any()) } returns null
        val model = RegisterBookModel(
            title = "",
            author = "",
            category = "",
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
}
