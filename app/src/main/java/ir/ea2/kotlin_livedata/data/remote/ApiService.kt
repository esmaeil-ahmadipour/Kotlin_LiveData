package ir.ea2.kotlin_livedata.data.remote

import ir.ea2.kotlin_livedata.data.remote.model.NoteResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    //`note` is Continue Of BASE_URL.
    //Call<> Need A DataModel From Response.
    //Use `title` AS QueryParameter ..
    //With @Header We Can Set HeaderValue For Response And Sending For Server.

    @GET("note")
    fun getNote(@Query("title") title:String , @Header("deviceApiSdk") deviceApiSdk:String): Call<NoteResponse>
}