package ir.ea2.kotlin_livedata.data.remote.model

import com.google.gson.annotations.SerializedName

data class DataResponse<T> (
    @SerializedName("data")
    val data: List<T>)

