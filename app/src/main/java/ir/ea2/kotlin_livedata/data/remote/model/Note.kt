package ir.ea2.kotlin_livedata.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Note(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    var title: String,
    @SerializedName("body")
    var body: String,
    @SerializedName("dateTime")
    var dateTime: String,
    @SerializedName("categories")
    var categories: List<Category>
): Serializable