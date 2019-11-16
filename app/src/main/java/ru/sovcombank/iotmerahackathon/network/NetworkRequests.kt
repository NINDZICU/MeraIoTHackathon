package ru.sovcombank.iotmerahackathon.network

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import ru.sovcombank.iotmerahackathon.network.model.Coordinate
import java.util.*

interface NetworkRequests {

    @GET("coordinates")
    fun getCoordinate(): Observable<Response<Coordinate>>
}