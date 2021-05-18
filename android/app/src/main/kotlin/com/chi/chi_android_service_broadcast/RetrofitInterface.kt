package com.chi.chi_android_service_broadcast

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming


interface RetrofitInterface {
    @GET("video/6868420/download/?search_query=&tracking_id=bl9jjt6dnko")
    @Streaming
    fun downloadFile(): Call<ResponseBody>
}