package com.mariabuliga.softpaws.data.api

sealed class ApiResult<T : Any> {

    class Success<T : Any>(val data: T) : ApiResult<T>()
    class Error<T : Any>(val code: Int?, val message: String?, val parsedError: String?) :
        ApiResult<T>()
    class Exception<T: Any>(val message: String?): ApiResult<T>()
}