package com.tasbih.digitaltasbih.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.GZIPInputStream

/**
 * Independent HTTP client dedicated strictly to Near Me features (OSM Overpass, Nominatim, OSRM).
 * Completely isolated from core app/Tasbih networking and Cloudflare endpoints.
 * Enforces proper User-Agent headers, timeouts, and automatic GZIP decompression.
 */
object NearMeHttpClient {
    private const val TAG = "NearMeHttpClient"
    private const val CONNECT_TIMEOUT_MS = 10_000
    private const val READ_TIMEOUT_MS = 15_000
    const val USER_AGENT = "DigitalTasbihApp/1.0 (Android; contact: support@imaanix.in)"

    data class HttpResponse(
        val statusCode: Int,
        val body: String,
        val isSuccessful: Boolean = statusCode in 200..299
    )

    /**
     * Executes an HTTP GET request on the IO dispatcher.
     */
    suspend fun get(urlString: String, customHeaders: Map<String, String> = emptyMap()): HttpResponse =
        withContext(Dispatchers.IO) {
            executeRequest(urlString, method = "GET", body = null, headers = customHeaders)
        }

    /**
     * Executes an HTTP POST request with form-encoded or raw body on the IO dispatcher.
     */
    suspend fun post(
        urlString: String,
        body: String,
        contentType: String = "application/x-www-form-urlencoded",
        customHeaders: Map<String, String> = emptyMap()
    ): HttpResponse = withContext(Dispatchers.IO) {
        val headers = customHeaders.toMutableMap()
        headers["Content-Type"] = contentType
        executeRequest(urlString, method = "POST", body = body, headers = headers)
    }

    private fun executeRequest(
        urlString: String,
        method: String,
        body: String?,
        headers: Map<String, String>
    ): HttpResponse {
        var conn: HttpURLConnection? = null
        return try {
            val url = URL(urlString)
            conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = method
                connectTimeout = CONNECT_TIMEOUT_MS
                readTimeout = READ_TIMEOUT_MS
                setRequestProperty("User-Agent", USER_AGENT)
                setRequestProperty("Accept", "application/json, text/plain, */*")
                setRequestProperty("Accept-Encoding", "gzip")

                for ((key, value) in headers) {
                    setRequestProperty(key, value)
                }

                if (body != null) {
                    doOutput = true
                    OutputStreamWriter(outputStream, Charsets.UTF_8).use { writer ->
                        writer.write(body)
                        writer.flush()
                    }
                }
            }

            val statusCode = conn.responseCode
            val isSuccess = statusCode in 200..299
            val inputStream: InputStream? = if (isSuccess) conn.inputStream else conn.errorStream

            val responseBody = if (inputStream != null) {
                val isGzip = "gzip".equals(conn.contentEncoding, ignoreCase = true)
                val effectiveStream = if (isGzip) GZIPInputStream(inputStream) else inputStream
                BufferedReader(InputStreamReader(effectiveStream, Charsets.UTF_8)).use { it.readText() }
            } else {
                ""
            }

            HttpResponse(statusCode = statusCode, body = responseBody)
        } catch (e: Exception) {
            Log.w(TAG, "Request failed for $urlString: ${e.message}")
            HttpResponse(statusCode = -1, body = e.message ?: "Network error")
        } finally {
            conn?.disconnect()
        }
    }
}
