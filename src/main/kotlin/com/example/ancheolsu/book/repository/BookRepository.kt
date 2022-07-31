package com.example.ancheolsu.book.repository

import com.example.ancheolsu.book.entity.Book
import com.example.ancheolsu.book.entity.BookId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface BookRepository : JpaRepository<Book, BookId>, QuerydslPredicateExecutor<Book>
