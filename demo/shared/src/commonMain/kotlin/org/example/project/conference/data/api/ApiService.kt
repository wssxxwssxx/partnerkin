package org.example.project.conference.data.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.example.project.conference.data.model.Conference
import org.example.project.conference.data.model.ConferenceDetail
import org.example.project.conference.data.model.ConferenceDetailResponse
import org.example.project.conference.data.model.ConferenceListResponse

class ApiService(private val client: HttpClient) {

    private val baseUrl = "https://partnerkin.com/api_ios_test"
    private val apiKey = "DMwdj29q@S29shslok2"

    suspend fun getConferenceList(): List<Conference> {
        val response = client.get("$baseUrl/list") {
            parameter("api_key", apiKey)
        }.body<ConferenceListResponse>()
        return response.data.result.map { it.conference }
    }

    suspend fun getConferenceDetail(): ConferenceDetail {
        val response = client.get("$baseUrl/view") {
            parameter("api_key", apiKey)
        }.body<ConferenceDetailResponse>()
        return response.data
    }
}
