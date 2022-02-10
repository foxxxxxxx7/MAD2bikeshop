package com.wit.mad2bikeshop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wit.mad2bikeshop.databinding.ActivityBookBinding
import com.wit.mad2bikeshop.main.BikeshopApp

class Book : AppCompatActivity() {

    private lateinit var bookLayout : ActivityBookBinding
    lateinit var app: BikeshopApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = this.application as BikeshopApp
        bookLayout = ActivityBookBinding.inflate(layoutInflater)
        setContentView(bookLayout.root)
    }
}