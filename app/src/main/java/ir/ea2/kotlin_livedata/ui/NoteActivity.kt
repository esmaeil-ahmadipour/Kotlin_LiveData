package ir.ea2.kotlin_livedata.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.R
import ir.ea2.kotlin_livedata.data.model.Note
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        val note:Note = intent.extras?.get(AppConstants.NOTE_KEY) as Note
        titleTxt.text = note.title
        bodyTxt.text = note.body
        dateTimeTxt.text = note.dateTime
        if (note.categories.isNotEmpty()){
            note.categories.forEach {
                categoryTxt.append(it.title + "  ")
            }
        }

    }
}
