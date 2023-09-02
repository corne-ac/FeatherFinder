package com.ryanblignaut.featherfinder.utils

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


object ApiRequestUtility {
    enum class RequestMethod {
        GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE,
    }

    val gson: Gson = GsonBuilder().create()

    // TODO: what happens if the JSON is invalid? eg we get a completely different object from the server back.
    // This function deserializes JSON data into an object of type T using Gson.
    inline fun <reified T> fromJson(json: String): Result<T> {
        return try {
            Result.success(gson.fromJson(json, T::class.java))
        } catch (e: JsonSyntaxException) {
            Result.failure(e)
        }
    }

    suspend inline fun <reified T> makeRequestJson(
        apiUrl: String,
        method: RequestMethod = RequestMethod.GET,
        body: String? = null,
        headers: Map<String, String>? = null,
    ): Result<T> {
        val stringResult = makeRequestString(apiUrl, method, body, headers)
        // If the stringResult is a failure, return the failure else deserialize the JSON string into an object of type Result<T>.
        return stringResult.fold(::fromJson, Result.Companion::failure)

//        The above code is equivalent to the following:
//        return stringResult.getOrElse { return Result.failure(it) }.run { fromJson<T>(this) }
    }

    suspend fun makeRequestString(
        apiUrl: String,
        method: RequestMethod = RequestMethod.GET,
        body: String? = null,
        headers: Map<String, String>? = null,
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            makeRequestStringInternal(

                apiUrl,
                method,
                body,
                headers
            )
        }
    }

    // This function makes a request to the given API url and returns the response as a string.
    // This function is private because it is only used internally from a different thread.
    private fun makeRequestStringInternal(
        apiUrl: String,
        method: RequestMethod = RequestMethod.GET,
        body: String? = null,
        headers: Map<String, String>? = null,
    ): Result<String> {
        var connection: HttpURLConnection? = null
        try {
            val url = URL(apiUrl.toString())
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = method.name
            connection.connectTimeout = 5000

            // Add headers to the request
            headers?.forEach { (key, value) ->
                connection.setRequestProperty(key, value)
            }

            // Add the request body if it exists
            if (method != RequestMethod.GET && body != null) {
                // For non-GET requests, add the request body if we passed one in.
                connection.doOutput = true
                connection.outputStream.bufferedWriter().use { it.write(body) }
            }


            // This concept was touched on in the following website:
            // https://curlconverter.com/java-httpurlconnection/
            // But it did connection.getResponseCode() / 100 == 2, this is technically correct because it is doing integer division. We round down so 201 / 100 = 2.
            // But I think it is more readable to do connection.getResponseCode() % 200 == 0 because it is more explicit.

            val stream =
                if (connection.responseCode % 200 != 0) connection.errorStream else connection.inputStream


            val content = stream.bufferedReader().use { it.readText() }

            return if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                Result.success(content)
            } else {
                Result.failure(Exception("Error: $apiUrl\n server responded with ${connection.responseCode} - $content"))
            }
        } catch (ex: Exception) {
            return Result.failure(ex)
        } finally {
            connection?.disconnect()
        }
    }

}
