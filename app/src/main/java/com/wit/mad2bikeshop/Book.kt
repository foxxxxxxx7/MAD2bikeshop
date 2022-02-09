package com.wit.mad2bikeshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wit.mad2bikeshop.databinding.ActivityBookBinding

class Book : AppCompatActivity() {

    private lateinit var bookLayout : ActivityBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookLayout = ActivityBookBinding.inflate(layoutInflater)
        setContentView(bookLayout.root)
    }
}