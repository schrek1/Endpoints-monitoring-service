package com.example.monitoredendpoints.response

sealed class SenderResult(val statusCode: Int) {
    class Success(val response: String, statusCode: Int) : SenderResult(statusCode)
    class Error(val errorMessage: String, statusCode: Int) : SenderResult(statusCode)
}