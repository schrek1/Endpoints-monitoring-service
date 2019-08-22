package com.example.monitoredendpoints.service

import com.example.monitoredendpoints.response.*
import org.springframework.stereotype.*
import org.springframework.web.client.*


@Service
class SenderService(
        private val restClient: RestTemplate
) {

    suspend fun sendRequest(url: String): SenderResult {
        return try {
            val response = restClient.getForEntity(url, String::class.java)

            if (response.statusCodeValue in 100..399) {
                SenderResult.Success(response.body ?: "null", response.statusCodeValue)
            } else {
                SenderResult.Error(response.body ?: "null", response.statusCodeValue)
            }
        } catch (ex: Exception) {
            SenderResult.Error(ex.localizedMessage, -1)
        }
    }
}