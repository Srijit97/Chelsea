package com.srijit.chelsea

import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class Common {
    private val thorApiClient = apiClient {
        expectSuccess = false
        defaultRequest {
            header(HttpHeaders.ContentType, "application/json")
            host = "thor-app-97.herokuapp.com"
            url {
                protocol = URLProtocol.HTTPS
            }
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v(tag = "HTTP Client", message = message)
                }
            }
        }
        install(HttpRequestRetry) {
            maxRetries = 3
            modifyRequest { Napier.v(tag = "HTTP Client", message = "Retry") }
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            socketTimeoutMillis = 15000
            connectTimeoutMillis = 15000
        }
        HttpResponseValidator {
            validateResponse { response: HttpResponse ->
                Napier.d(tag = "msg1") { response.status.value.toString() }
                when (response.status.value) {
                    in 300..399 -> Unit
                    in 400..499 -> Unit
                    in 500..599 -> Unit
                    else -> Unit
                }
            }
        }
            .also { initLogger() }
    }

    suspend fun getPlayers(): List<Players>{
        val response = thorApiClient.get {
            url("/players")
        }
        val playerList = Json.decodeFromString<List<Players>>(string = response.body())
        Napier.d(message = playerList.toString(), tag = "msg1")
        return playerList
    }
}

@Serializable
data class Players(val playerName: String)