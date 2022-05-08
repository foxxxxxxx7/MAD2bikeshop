package com.wit.mad2bikeshop.ui.theme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.databinding.ActivityThemeBinding
import com.wit.mad2bikeshop.databinding.FragmentBookBinding
import com.wit.mad2bikeshop.ui.about.AboutViewModel
import com.wit.mad2bikeshop.ui.book.BookViewModel
import java.util.*

private var _fragBinding: ActivityThemeBinding? = null
private val fragBinding get() = _fragBinding!!

class Theme : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)
        setButtonListener(fragBinding)
    }

    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = ActivityThemeBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setButtonListener(fragBinding)
        return root
    }



    fun setButtonListener(layout: ActivityThemeBinding) {
    layout.themeToggle.setOnCheckedChangeListener { _, checkedId ->
        when (checkedId) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
}



