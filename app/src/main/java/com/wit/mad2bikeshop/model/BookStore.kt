package com.wit.mad2bikeshop.model

interface BookStore {
    fun findAll() : List<BookModel>
    fun findById(id: Long) : BookModel?
    fun create(donation: BookModel)
}