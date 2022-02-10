package com.wit.mad2bikeshop.model

interface BookStore {
    fun findAll() : List<BookModel>
    fun findById(id: Long) : BookModel?
    fun create(booking: BookModel)
    fun update(booking: BookModel)
    fun delete(booking: BookModel)
}