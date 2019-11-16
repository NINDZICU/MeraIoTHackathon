package ru.sovcombank.iotmerahackathon.network

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.sovcombank.iotmerahackathon.network.model.Coordinate
import java.util.concurrent.TimeUnit

class NetworkApi {

    companion object {
        private const val BASE_URL = "http://192.168.0.15:8080/"

    }

    private var networkApi: NetworkApi? = null
    private var networkRequests: NetworkRequests

    fun getInstance(): NetworkApi {
        if (networkApi == null) networkApi = NetworkApi()
        return networkApi as NetworkApi
    }

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addNetworkInterceptor(HttpLoggingInterceptor{
                Log.d("NETWORK", it)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        networkRequests = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(NetworkRequests::class.java)
    }

    fun getCoordinates(): Observable<Response<Coordinate>> {
        return networkRequests.getCoordinate()
    }

}