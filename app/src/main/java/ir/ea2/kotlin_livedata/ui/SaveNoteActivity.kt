package ir.ea2.kotlin_livedata.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.R
import ir.ea2.kotlin_livedata.data.remote.model.Note
import kotlinx.android.synthetic.main.activity_save_note.*

class SaveNoteActivity : AppCompatActivity() {
    private lateinit var note:Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_note)

        val bundle = intent.extras
        val state = bundle?.getString(AppConstants.STATE_KEY)

        if (state == AppConstants.UPDATE_STATE){
            note = bundle.getSerializable(AppConstants.NOTE_KEY) as Note
            titleEditTxt.text.append(note.title)
            bodyEditTxt.text.append(note.body)
        }

    }
}
