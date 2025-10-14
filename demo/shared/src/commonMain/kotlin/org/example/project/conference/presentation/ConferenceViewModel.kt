package org.example.project.conference.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.conference.data.model.Conference
import org.example.project.conference.data.repository.ConferenceRepository

class ConferenceViewModel(
    private val repository: ConferenceRepository
): ViewModel() {
    private val _conferences = MutableStateFlow<List<Conference>>(emptyList())
    val conferences: StateFlow<List<Conference>> = _conferences.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadConferences() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getConferences()
                .onSuccess { conferences ->
                    _conferences.value = conferences
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }
}
