package com.mariabuliga.softpaws.utils

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorHandlingInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            throw NoInternetException("No internet connection.")
        }

        try {
            val response = chain.proceed(chain.request())

            when (response.code()) {
                in 400..499 -> {
                    throw ClientErrorException("Client error: ${response.code()} ${response.message()}")
                }

                in 500..599 -> {
                    throw ServerErrorException("Server error: ${response.code()} ${response.message()}")
                }
            }
            return response
        } catch (e: UnknownHostException) {
            throw NoInternetException("Unable to resolve host. Please check your connection.", e)
        } catch (e: SocketTimeoutException) {
            throw TimeoutException("Request timed out. Please try again.", e)
        } catch (e: IOException) {
            throw NetworkIOException("Network I/O error occurred.", e)
        }
    }

}

class NoInternetException(message: String, cause: Throwable? = null) : IOException(message, cause)
class TimeoutException(message: String, cause: Throwable? = null) : IOException(message, cause)
class NetworkIOException(message: String, cause: Throwable? = null) : IOException(message, cause)
class ClientErrorException(message: String, cause: Throwable? = null) : IOException(message, cause)
class ServerErrorException(message: String, cause: Throwable? = null) : IOException(message, cause)
