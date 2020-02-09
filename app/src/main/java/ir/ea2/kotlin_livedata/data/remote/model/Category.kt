package ir.ea2.kotlin_livedata.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Category(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String
): Serializable