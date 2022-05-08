package com.wit.mad2bikeshop.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/* Creating a class called AboutViewModel that inherits from ViewModel. */
class AboutViewModel : ViewModel() {

    /* Creating a private variable called _text that is a MutableLiveData of type String. */
    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    /* Creating a public LiveData variable called text that is a LiveData of type String. */
    val text: LiveData<String> = _text
}