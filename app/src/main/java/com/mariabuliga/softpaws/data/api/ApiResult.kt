package com.mariabuliga.softpaws.data.api

import com.mariabuliga.softpaws.utils.Constants
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response

sealed class ApiResult<T : Any> {

    class Success<T : Any>(val data: T) : ApiResult<T>()
    class Error<T : Any>(val code: Int?, val message: String?, val parsedError: String?) :
        ApiResult<T>()

    class Unauthorized<T : Any> : ApiResult<T>()
    class Exception<T : Any>(val message: String?) : ApiResult<T>()
}

suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiResult.Success(body)
        } else {
            if (response.code() == Constants.SESSION_EXPIRE_CODE ||
                response.code() == Constants.SESSION_EXPIRE_CODE_ALT
            ) {
                ApiResult.Unauthorized()
            } else {
                val error = response.errorBody()?.string()
                val errorJson = error?.let { JSONObject(it) }
                var message: String? = null
                if (errorJson != null && errorJson.has("message")) {
                    message = errorJson.getString("message")
                }
                ApiResult.Error(
                    code = response.code(), message = message, parsedError = error
                )
            }
        }
    } catch (e: HttpException) {
        ApiResult.Error(code = e.code(), message = e.message, parsedError = null)
    } catch (e: Throwable) {
        ApiResult.Exception(message = e.message)
    }
}