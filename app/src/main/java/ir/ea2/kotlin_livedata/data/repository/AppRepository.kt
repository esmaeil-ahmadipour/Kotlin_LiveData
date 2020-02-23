package ir.ea2.kotlin_livedata.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.data.remote.Resource
import ir.ea2.kotlin_livedata.data.remote.RetrofitService
import ir.ea2.kotlin_livedata.data.remote.model.Category
import ir.ea2.kotlin_livedata.data.remote.model.DataResponse
import ir.ea2.kotlin_livedata.data.remote.model.Note
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

    fun getNotes(title: String): MutableLiveData<Resource<DataResponse<Note>>> {
        //For Pass Result Of This Thread , Using ObserverPattern In LiveData.
        //Because Need Change Data , We Using MutableLiveData.

        var responseResult: MutableLiveData<Resource<DataResponse<Note>>> = MutableLiveData()
        //Set Default Value For : When Response Not Switched Between Failure Or Success.
        responseResult.value = Resource.loading()

        //Set Variable deviceSDKVersion For Send To Header Of Response.
        val deviceSDKVersion: String = android.os.Build.VERSION.SDK_INT.toString()

        //Method enqueue() : Use For Create Enqueue Of Request.After Any Callback Get Answer, GoTo Next Request.
        //For Create Request Using Callback interface and Implemented Method's.
        RetrofitService.apiService.getNote(title, deviceSDKVersion)
            .enqueue(object : Callback<DataResponse<Note>> {

                //Any Request Have Two State: RequestHasError Or RequestIsOk .
                //onFailure : RequestHasError And Detail's Available In Throwable Variable.

                override fun onFailure(call: Call<DataResponse<Note>>, t: Throwable) {

                    responseResult.value = Resource.error(AppConstants.FAILED_MESSAGE, null)
                }

                //onResponse : RequestIsOk And Detail's Available In Response Variable.
                override fun onResponse(
                    call: Call<DataResponse<Note>>,
                    response: Response<DataResponse<Note>>
                ) {
                    if (response.isSuccessful) {
                        //Set Response Data To MutableLiveData Variable.
                        if (response.body()?.data == null) {
                            responseResult.value =
                                Resource.error(AppConstants.DATA_ERROR_MESSAGE, null)
                        } else {
                            responseResult.value = Resource.success(response.body())
                            Log.i(AppConstants.NETWORK_TEST, response.code().toString())
                        }

                    } else {
                        //When Response Code isn't `200` .
                        responseResult.value = Resource.error(
                            AppConstants.ERROR_MESSAGE + response.code().toString(),
                            null
                        )

                    }
                }
            })
        return responseResult
    }

    fun getNoteDetails(id: Long): MutableLiveData<Resource<Note>> {
        var responseResult: MutableLiveData<Resource<Note>> = MutableLiveData()
        responseResult.value = Resource.loading()

        RetrofitService.apiService.getNoteDetail(id).enqueue(object : Callback<Note> {
            override fun onFailure(call: Call<Note>, t: Throwable) {
                responseResult.value = Resource.error(AppConstants.FAILED_MESSAGE, null)
            }

            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful) {
                    //Set Response Data To MutableLiveData Variable.
                    if (response.body() == null) {
                        responseResult.value = Resource.error(AppConstants.DATA_ERROR_MESSAGE, null)
                    } else {
                        responseResult.value = Resource.success(response.body())
                        Log.i(AppConstants.NETWORK_TEST, response.code().toString())
                    }

                } else {
                    //When Response Code isn't `200` .
                    responseResult.value = Resource.error(
                        AppConstants.ERROR_MESSAGE + response.code().toString(),
                        null
                    )

                }
            }
        })
        return responseResult
    }

    fun getCategories(): MutableLiveData<Resource<DataResponse<Category>>> {
        var responseResult: MutableLiveData<Resource<DataResponse<Category>>> = MutableLiveData()
        responseResult.value = Resource.loading()
        RetrofitService.apiService.getCategories().enqueue(object : Callback<DataResponse<Category>> {
            override fun onFailure(call: Call<DataResponse<Category>>, t: Throwable) {
                responseResult.value = Resource.error(AppConstants.FAILED_MESSAGE, null)
            }

            override fun onResponse(
                call: Call<DataResponse<Category>>,
                response: Response<DataResponse<Category>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.data == null) {
                        responseResult.value = Resource.error(AppConstants.DATA_ERROR_MESSAGE, null)
                    } else {
                        responseResult.value = Resource.success(response.body())
                        Log.i(AppConstants.NETWORK_TEST, response.code().toString())
                    }
                } else {
                    responseResult.value = Resource.error(
                        AppConstants.ERROR_MESSAGE + response.code().toString(),
                        null
                    )
                }
            }
        })
        return responseResult
    }

    fun saveNote(note: Note): MutableLiveData<Resource<Void>> {
        val responseResult: MutableLiveData<Resource<Void>> = MutableLiveData()
        responseResult.value = Resource.loading()
        RetrofitService.apiService.saveNote(note).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                responseResult.value = Resource.error(AppConstants.FAILED_MESSAGE, null)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                responseResult.value= Resource.success(null)
                } else {
                responseResult.value= Resource.error(AppConstants.ERROR_MESSAGE,null)
                }
            }
        })
        return responseResult
    }

    fun updateNote(note: Note): MutableLiveData<Resource<Void>> {
        val responseResult: MutableLiveData<Resource<Void>> = MutableLiveData()
        responseResult.value = Resource.loading()
        RetrofitService.apiService.updateNote(note,note.id).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                responseResult.value = Resource.error(AppConstants.FAILED_MESSAGE, null)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    responseResult.value= Resource.success(null)
                } else {
                    responseResult.value= Resource.error(AppConstants.ERROR_MESSAGE,null)
                }
            }
        })
        return responseResult
    }

    //Use this fun In The Adapter !
    fun deleteNote(note: Note): MutableLiveData<Resource<Void>> {
        val responseResult: MutableLiveData<Resource<Void>> = MutableLiveData()
        responseResult.value = Resource.loading()
        RetrofitService.apiService.deleteNote(note.id).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                responseResult.value = Resource.error(AppConstants.FAILED_MESSAGE, null)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    responseResult.value= Resource.success(null)
                } else {
                    responseResult.value= Resource.error(AppConstants.ERROR_MESSAGE,null)
                }
            }
        })
        return responseResult
    }

    fun registerUser(email: String, password: String): MutableLiveData<Resource<Void>> {
        var result: MutableLiveData<Resource<Void>> = MutableLiveData();
        result.value = Resource.loading()
        if (email.equals("") || password.equals("")) {
            result.value = Resource.error(AppConstants.REGISTER_ERROR_MESSAGE, null)
        }else{
            RetrofitService.apiService.signup(email, password).enqueue(object : Callback<Void>{
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    result.value= Resource.error(AppConstants.ERROR_MESSAGE,null)
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful){
                        result.value= Resource.success(null)
                    }else{
                        result.value= Resource.error(AppConstants.ERROR_MESSAGE,null)
                    }
                }
            })
        }
        return result
    }
}