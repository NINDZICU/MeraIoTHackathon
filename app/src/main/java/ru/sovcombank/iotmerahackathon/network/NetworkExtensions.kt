package ru.sovcombank.iotmerahackathon.network

import android.util.Log
import retrofit2.Call
import ru.sovcombank.essn.log.Logger
import java.io.IOException


/**
 *  @throws NotInternetConnectionException throws if not internet connection
 *  @throws ServerException throws if request is fail
 */
fun <T> Call<T>.start(): T? {
    val response =
        Logger.timer(javaClass.name, "Time request url ${request().method()}: ${request().url()}") {
            execute()
//            try {
//                execute()
//            } catch (e: IOException) {
//                Log.d("EXCEPTION", "NO INTERNET CONNECTION")
//            } catch (e: Exception) {
//                Log.d("EXCEPTION", "SERVER EXCEPTION")
//            }
        }

    if (response.isSuccessful) {
        val body = response.body()
        if (body != null) {
            return body
//        } else {
//            Log.d("EXCEPTION", "ResponseBody is null")
//        }
//    } else {
//        Log.d("EXCEPTION", "Response code = ${response.code()}")
//    }

        }
    }
    return null
    //TODO
}