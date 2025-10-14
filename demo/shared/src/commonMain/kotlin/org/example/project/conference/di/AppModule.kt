package org.example.project.conference.di

import io.ktor.client.*

import org.example.project.conference.data.api.ApiService
import org.example.project.conference.data.repository.ConferenceRepository
import org.example.project.conference.presentation.ConferenceDetailViewModel
import org.example.project.conference.presentation.ConferenceViewModel
import org.koin.dsl.module

expect fun createHttpClient(): HttpClient

val appModule = module {
    single { createHttpClient() }
    single { ApiService(get()) }
    single { ConferenceRepository(get()) }
    single { ConferenceViewModel(get()) }
    single { ConferenceDetailViewModel(get()) }
}