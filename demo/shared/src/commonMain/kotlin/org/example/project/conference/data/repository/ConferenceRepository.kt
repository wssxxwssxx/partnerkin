package org.example.project.conference.data.repository

import org.example.project.conference.data.api.ApiService
import org.example.project.conference.data.model.Conference
import org.example.project.conference.data.model.ConferenceDetail

class ConferenceRepository(private val apiService: ApiService) {

    suspend fun getConferences(): Result<List<Conference>> {
        return try {
            val conferences = apiService.getConferenceList()
            Result.success(conferences)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getConferenceDetail(): Result<ConferenceDetail> {
        return try {
            val detail = apiService.getConferenceDetail()
            Result.success(detail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
