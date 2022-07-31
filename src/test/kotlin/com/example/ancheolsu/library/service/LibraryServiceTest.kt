package com.example.ancheolsu.library.service

import com.example.ancheolsu.category.repository.CategoryRepository
import com.example.ancheolsu.common.exception.BusinessException
import com.example.ancheolsu.library.service.model.RegisterBookModel
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
}
