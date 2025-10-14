package org.example.project.conference.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Conference(
    val id: Int,
    val name: String,
    val format: String,
    val status: String,
    val status_title: String,
    val url: String,
    val image: Image? = null,
    val rating: Float? = null,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    val oneday: Int? = null,
    val custom_date: String? = null,
    @SerialName("country")
    val country: String,
    @SerialName("city")
    val city: String,
    val categories: List<Category> = emptyList(),
    val type_id: Int,
    val type: Type? = null
)

@Serializable
data class Image(
    val id: String,
    val url: String,
    val preview: String,
    val placeholder_color: String? = null,
    val width: Int,
    val height: Int
)

@Serializable
data class Category(
    val id: Int,
    val name: String,
    val url: String
)

@Serializable
data class Type(
    val id: Int,
    val name: String
)

@Serializable
data class ConferenceListResponse(
    val error: String? = null,
    val data: ConferenceListData
)

@Serializable
data class ConferenceListData(
    val counts: Int,
    val pagination: Pagination,
    val result: List<ConferenceResult>
)

@Serializable
data class Pagination(
    val page_size: Int,
    val current_page: Int
)

@Serializable
data class ConferenceResult(
    val view_type: Int,
    val conference: Conference
)
