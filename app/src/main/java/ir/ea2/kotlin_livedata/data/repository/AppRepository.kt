package ir.ea2.kotlin_livedata.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.data.remote.RetrofitService
import ir.ea2.kotlin_livedata.data.remote.model.NoteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//In AppRepository Class , We Access To All Method's Of ApiService.
class AppRepository private constructor() {
    companion object {
        //Create Private Object From Own Class.
        private val singeltonInstance: AppRepository = AppRepository()

        fun getInstance(): AppRepository {
            //When Writing : AppRepository.getInstance() , We Access All Method's Of This Class.
            return singeltonInstance
        }
    }

    fun getNotes(): MutableLiveData<NoteResponse> {
        //For Pass Result Of This Thread , Using ObserverPattern In LiveData.
        //Because Need Change Data , We Using MutableLiveData.
        var responseResult: MutableLiveData<NoteResponse> = MutableLiveData()

        //Method enqueue() : Use For Create Enqueue Of Request.After Any Callback Get Answer, GoTo Next Request.
        //For Create Request Using Callback interface and Implemented Method's.
        RetrofitService.apiService.getNote().enqueue(object : Callback<NoteResponse> {

            //Any Request Have Two State: RequestHasError Or RequestIsOk .
            //onFailure : RequestHasError And Detail's Available In Throwable Variable.
            override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                Log.i(AppConstants.NETWORK_TEST, AppConstants.FAILED_MESSAGE)

            }

            //onResponse : RequestIsOk And Detail's Available In Response Variable.
            override fun onResponse(call: Call<NoteResponse>, response: Response<NoteResponse>) {
                if (response.isSuccessful) {
                    //Set Response Data To MutableLiveData Variable.
                    responseResult.value = response.body()
                }
            }
        })
        return responseResult
    }
}