package com.example.fifth_home_task.network

import com.example.fifth_home_task.model.BankATM
import com.example.fifth_home_task.model.BankFilial
import com.example.fifth_home_task.model.BankInfobox
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import io.reactivex.Observable

private const val BASE_URL =
    "https://belarusbank.by/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

interface BankApiService {
    @GET("api/atm?city=Гомель")
    fun getATM(): Observable<List<BankATM>>

    @GET("api/infobox?city=Гомель")
    fun getInfobox(): Observable<List<BankInfobox>>

    @GET("api/filials_info?city=Гомель")
    fun getFilial(): Observable<List<BankFilial>>
}

object BankApi {
    val retrofitService: BankApiService by lazy {
        retrofit.create(BankApiService::class.java)
    }
}