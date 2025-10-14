package org.example.project.conference.data.model

import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

@Serializable
data class ConferenceDetail(
    val id: Int,
    val name: String,
    val format: String? = null,
    val status: String? = null,
    @SerialName("status_title")
    val statusTitle: String? = null,
    val url: String? = null,
    val image: ConferenceImage? = null,
    val rating: Float? = null,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    val oneday: Int? = null,
    @SerialName("custom_date")
    val customDate: String? = null,
    @SerialName("country_id")
    val countryId: Int? = null,
    val country: String? = null,
    @SerialName("city_id")
    val cityId: Int? = null,
    val city: String? = null,
    val categories: List<Category> = emptyList(),
    @SerialName("type_id")
    val typeId: Int? = null,
    val type: ConferenceType? = null,
    @SerialName("register_url")
    val registerUrl: String? = null,
    val about: String? = null
)

@Serializable
data class ConferenceImage(
    val id: String? = null,
    val url: String? = null,
    val preview: String? = null,
    @SerialName("placeholder_color")
    val placeholderColor: String? = null,
    val width: Int? = null,
    val height: Int? = null
)


@Serializable
data class ConferenceType(
    val id: Int,
    val name: String
)

@Serializable
data class ConferenceDetailResponse(
    val error: String? = null,
    val data: ConferenceDetail
)