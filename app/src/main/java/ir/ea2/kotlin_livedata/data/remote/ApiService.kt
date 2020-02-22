package ir.ea2.kotlin_livedata.data.remote

import ir.ea2.kotlin_livedata.data.remote.model.CategoriesResponse
import ir.ea2.kotlin_livedata.data.remote.model.Note
import ir.ea2.kotlin_livedata.data.remote.model.NoteResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //`note` is Continue Of BASE_URL.
    //Call<> Need A DataModel From Response.
    //Use `title` AS QueryParameter ..
    //With @Header We Can Set HeaderValue For Response And Sending For Server.

    @GET("note")
    fun getNote(@Query("title") title:String , @Header("deviceApiSdk") deviceApiSdk:String): Call<NoteResponse>

    @GET("note/{id}")
    fun getNoteDetail(@Path("id")id:Long):Call<Note>

    @GET("category")
    fun getCategories() : Call<CategoriesResponse>

    @POST("note")
    fun saveNote(@Body note:Note):Call<Void>

    //Why return Void ? Because Response Body Is Empty.
    @PUT("note/{id}")
    fun updateNote(@Body note:Note,@Path("id")id:Long):Call<Void>

    //Use This fun In AppRepository.
    @DELETE("note/{id}")
    fun deleteNote(@Path("id") id:Long):Call<Void>
}