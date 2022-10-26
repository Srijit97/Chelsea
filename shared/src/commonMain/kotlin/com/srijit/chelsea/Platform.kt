package com.srijit.chelsea

import io.ktor.client.*

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun initLogger()

expect fun apiClient(config: HttpClientConfig<*>.() -> Unit): HttpClient