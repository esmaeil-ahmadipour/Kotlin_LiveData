package ir.ea2.kotlin_livedata.data.remote

import ir.ea2.kotlin_livedata.data.remote.model.NoteResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    //`note` is Continue Of BASE_URL.
    //Call<> Need A DataModel From Response.
    @GET("note")
    fun getNote(): Call<NoteResponse>
}