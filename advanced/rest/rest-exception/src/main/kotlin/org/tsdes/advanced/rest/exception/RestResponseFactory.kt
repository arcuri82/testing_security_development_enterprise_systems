package org.tsdes.advanced.rest.exception

import org.springframework.http.ResponseEntity
import org.tsdes.advanced.rest.dto.WrappedResponse


object RestResponseFactory {

    fun <T> notFound(message: String): ResponseEntity<WrappedResponse<T>> {

        return ResponseEntity.status(404).body(
                WrappedResponse<T>(code = 404, message = message)
                        .validated())
    }

    fun <T> userFailure(message: String): ResponseEntity<WrappedResponse<T>> {

        return ResponseEntity.status(400).body(
                WrappedResponse<T>(code = 400, message = message)
                        .validated())
    }

    fun <T> payload(httpCode: Int, data: T): ResponseEntity<WrappedResponse<T>> {

        return ResponseEntity.status(httpCode).body(
                WrappedResponse(code = httpCode, data = data)
                        .validated())
    }

    fun  noPayload(httpCode: Int): ResponseEntity<WrappedResponse<Void>> {

        return ResponseEntity.status(httpCode).body(
                WrappedResponse<Void>(code = httpCode).validated())
    }
}