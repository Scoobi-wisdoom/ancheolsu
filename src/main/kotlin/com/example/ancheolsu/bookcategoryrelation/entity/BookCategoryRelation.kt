package com.example.ancheolsu.bookcategoryrelation.entity

import com.example.ancheolsu.book.entity.Book
import com.example.ancheolsu.book.entity.BookId
import com.example.ancheolsu.category.entity.Category
import com.example.ancheolsu.category.entity.CategoryId
import com.example.ancheolsu.common.BaseEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id

@Entity
class BookCategoryRelation(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: BookCategoryRelationId = 0L,
    val bookId: BookId,
    val categoryId: CategoryId,
) : BaseEntity() {
    companion object {
        fun from(book: Book, categories: List<Category>): List<BookCategoryRelation> {
            return categories.map {
                BookCategoryRelation(
                    bookId = book.id,
                    categoryId = it.id,
                )
            }
        }
    }
}
