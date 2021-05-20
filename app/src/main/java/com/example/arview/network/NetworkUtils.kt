package com.example.arview.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.arview.exceptions.NoConnectivityException
import com.example.arview.network.pojo.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.Socket


//suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Response<T>? {
//    return try {
//        call()
//    } catch (e: Exception) {
//        null
//    }
//}

suspend fun <T : Any> safeApiCall(
    errorConverter: Converter<ResponseBody, ErrorResponse>,
    apiCall: suspend () -> Response<T>
): Resource<T> {
    try {
        val response = apiCall()
        return if (response.isSuccessful && response.body() != null)
            Resource.Success<T>(response.body() as T)
        else {
            errorConverter.convert(response.errorBody())?.let { Resource.GenericError(it) }!!
        }
    } catch (throwable: Throwable) {
        Log.d("ErrorTag", throwable.message.toString())
        when (throwable) {
            is ConnectException,
            is NoConnectivityException -> {
                return Resource.Error(NoConnectivityException())
            }
            is HttpException -> {
//                    val code = throwable.code()
//                    if(code == 503){
////                        val errorResponse = ErrorResponse("Server is unavailable")
////                        ResultWrapper.GenericError(code, errorResponse)
//                    }else{
                val errorResponse: ErrorResponse = throwable.response()?.body() as ErrorResponse
                return Resource.GenericError(errorResponse)
//                    }
            }
            else -> {
                return Resource.Error(Exception(throwable.message))
            }
        }
    }
//    }
}

suspend fun <T : Any> safeApiCallTwo(
    apiCall: suspend () -> Response<T>
): Resource<T> {
    try {
        val response = apiCall()
        if (response.isSuccessful && response.body() != null) {
//            var json = Gson()
//            if (response.body() != null) {
//                var customObject = response.body() as T
//                if (customObject != null) {
//                    var castedObject = json.toJson(customObject)
//                    Log.d("ErrorTag:", "CastedObject: $castedObject")
//                } else
//                    Log.d("ErrorTag:", "Null")
//            } else
//                Log.d("ErrorTag:", "If responsbody null raw: " + response.raw().toString())
            return Resource.Success<T>(response.body() as T)
        } else {
            if (response.errorBody() != null) {
                val errorResponse = ErrorResponse(
                    message = response.raw().code.toString() + ":" + response.message()
                        ?: ""
                )
                return Resource.GenericError(errorResponse)
            } else
                return Resource.GenericError(ErrorResponse("Unknown error"))
        }
    } catch (throwable: Throwable) {
        Log.d("ErrorTag", throwable.message.toString())
        when (throwable) {
            is ConnectException,
            is NoConnectivityException -> {
                return Resource.Error(NoConnectivityException())
            }
            is HttpException -> {
                val errorResponse: ErrorResponse = throwable.response()?.body() as ErrorResponse
                return Resource.GenericError(errorResponse)
            }
            is IOException -> {
                return Resource.Error(Exception("IOException: " + throwable.message))
            }
            else -> {
                return Resource.Error(Exception(throwable.message))
            }
        }
    }
}

/**
 * Checks the availability of network connection not the actual internet connection
 * After Marshmellow new Connectivity Api is used  called networkCapabilities.
 *
 */
fun Context.isNetworkAvailable(): Boolean {
    var result = false
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                )
            }
        }
    } else {
        cm?.run {
            cm.activeNetworkInfo?.run {
                result =
                    type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_MOBILE
            }
        }
    }
    return result
}


suspend fun Context.isNetworkConnectedSuspend(): Boolean {
    // Dispatchers.Main
    return withContext(Dispatchers.IO) {
        try {
            val timeoutMs = 1500
            val socket = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)

            socket.connect(socketAddress, timeoutMs)
            socket.close()

            true
        } catch (e: IOException) {
            false
        }
    }
}

