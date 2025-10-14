package org.example.project.conference.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.conference.data.model.ConferenceDetail
import org.example.project.conference.data.repository.ConferenceRepository

class ConferenceDetailViewModel(
    private val repository: ConferenceRepository
) : ViewModel() {

    private val _conferenceDetail = MutableStateFlow<ConferenceDetail?>(null)
    val conferenceDetail: StateFlow<ConferenceDetail?> = _conferenceDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadConferenceDetail(conferenceId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getConferenceDetail()
                .onSuccess { detail ->
                    _conferenceDetail.value = detail
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }
}