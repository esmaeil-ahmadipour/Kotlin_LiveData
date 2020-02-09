package ir.ea2.kotlin_livedata.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Note(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("dateTime")
    val dateTime: String,
    @SerializedName("categories")
    val categories: List<Category>
): Serializable