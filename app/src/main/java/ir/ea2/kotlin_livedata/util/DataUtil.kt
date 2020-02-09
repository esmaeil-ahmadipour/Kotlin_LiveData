package ir.ea2.kotlin_livedata

import ir.ea2.kotlin_livedata.data.remote.model.Category
import ir.ea2.kotlin_livedata.data.remote.model.Note

object DataUtil {
    fun getNotes(): List<Note> {
        val notes: MutableList<Note> = ArrayList()
        for (i in 1..5) {
            notes.add(
                Note(
                    i.toLong(),
                    "note $i",
                    "salam man note $i hastam",
                    "2019-08-07 23:58",
                    listOf(
                        Category(
                            1,
                            "Programming"
                        ), Category(2, "Work")
                    )
                )
            )
        }
        return notes.toList()
    }

    fun getCategories(): List<Category> {
        return listOf(
            Category(
                1,
                "Programming"
            ), Category(2, "Work")
        )
    }
}