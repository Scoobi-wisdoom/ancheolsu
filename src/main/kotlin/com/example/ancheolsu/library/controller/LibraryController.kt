package com.example.ancheolsu.library.controller

import com.example.ancheolsu.library.controller.request.RegisterBookRequest
import com.example.ancheolsu.library.controller.request.RegisterCategoryRequest
import com.example.ancheolsu.library.controller.response.RegisterBookResponse
import com.example.ancheolsu.library.controller.response.RegisterCategoryResponse
import com.example.ancheolsu.library.controller.response.SearchBookResponse
import com.example.ancheolsu.library.service.LibraryService
import com.example.ancheolsu.library.service.model.SearchBookByCategoryModel
import com.example.ancheolsu.library.service.model.SearchBookModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction.ASC
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LibraryController(
    private val libraryService: LibraryService,
) {
    @PostMapping("/book")
    fun registerBook(@RequestBody registerBookRequest: RegisterBookRequest): RegisterBookResponse {
        return RegisterBookResponse.from(
            libraryService.registerBook(
                registerBookRequest.toModel()
            )
        )
    }

    @PostMapping("/category")
    fun registerCategory(@RequestBody registerCategoryRequest: RegisterCategoryRequest): RegisterCategoryResponse {
        return RegisterCategoryResponse.from(
            libraryService.registerCategory(
                registerCategoryRequest.toModel()
            )
        )
    }

    @GetMapping("/book")
    fun searchBook(
        @RequestParam title: String?,
        @RequestParam author: String?,
        @PageableDefault(page = 0, size = 10, sort = ["createdAt"], direction = ASC) pageable: Pageable,
    ): Page<SearchBookResponse> {
        val model = SearchBookModel(
            title = title,
            author = author,
            pageable = pageable,
        )

        return libraryService.searchBook(model).map { SearchBookResponse.from(it) }
    }

    @GetMapping("/book/category")
    fun searchBookByCategory(
        @RequestParam category: String,
        @PageableDefault(page = 0, size = 10, sort = ["createdAt"], direction = ASC) pageable: Pageable,
    ): Page<SearchBookResponse> {
        val model = SearchBookByCategoryModel(
            categoryName = category,
            pageable = pageable
        )

        return libraryService.searchBookByCategory(model).map { SearchBookResponse.from(it) }
    }
}
