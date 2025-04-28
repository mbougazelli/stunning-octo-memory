package com.example.animecharacters.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class EmailViewModel @Inject constructor() : ViewModel(){
    private val _subject = MutableStateFlow("")
    val subject: StateFlow<String> = _subject

    private val _recipient = MutableStateFlow("")
    val recipient: StateFlow<String> = _recipient

    private val _body = MutableStateFlow("")
    val body: StateFlow<String> = _body

    val isFormValid = combine(_subject, _body, _recipient) { subject, recipient, body ->
        subject.isNotBlank() && recipient.isNotBlank() && body.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    fun updateSubject(newSubject: String) {
        _subject.value = newSubject
    }

    fun updateRecipient(newRecipient: String) {
        _recipient.value = newRecipient
    }

    fun updateBody(newBody: String) {
        _body.value = newBody
    }

    fun clearFields() {
        _subject.value = ""
        _recipient.value = ""
        _body.value = ""
    }
}