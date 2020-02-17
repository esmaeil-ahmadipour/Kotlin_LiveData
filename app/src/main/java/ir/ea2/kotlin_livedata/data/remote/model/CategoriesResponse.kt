package ir.ea2.kotlin_livedata.data.remote.model

import com.google.gson.annotations.SerializedName

data class CategoriesResponse (
    @SerializedName("categories")
    val categories: List<Category>)

