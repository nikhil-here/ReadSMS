package com.nikhil.mandlik.readsms.utility

sealed class Response<T>(
    val data : T? = null,
    val msg : String? = null,
    val error : Throwable? = null
){
    class Success<T>(data : T) : Response<T>(data)
    class Loading<T>() : Response<T>()
    class Error<T>(msg: String? = null, throwable: Throwable? = null) :
        Response<T>(msg = msg, error = throwable)
}