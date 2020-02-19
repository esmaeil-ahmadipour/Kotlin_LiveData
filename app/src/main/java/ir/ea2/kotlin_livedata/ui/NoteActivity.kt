package ir.ea2.kotlin_livedata.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.NetworkUtil
import ir.ea2.kotlin_livedata.R
import ir.ea2.kotlin_livedata.data.repository.AppRepository
import ir.ea2.kotlin_livedata.util.AppStatus
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        val id: Long = intent.extras?.get(AppConstants.NOTE_ID_KEY) as Long
        sendNoteDetailsRequest(id)
    }

    private fun sendNoteDetailsRequest(id: Long) {
        if (NetworkUtil.isInternetAvailable(this)) {
            AppRepository.getInstance().getNoteDetails(id).observe(

                this@NoteActivity,
                Observer {
                    when (it.status) {
                        AppStatus.LOADING -> {
                            updateVisibility(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
                        }
                        AppStatus.ERROR -> {
                            textView.text = it.message
                            updateVisibility(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
                        }
                        AppStatus.SUCCESS -> {
                            val note = it.data
                            titleTxt.text = note!!.title
                            bodyTxt.text = note.body
                            dateTimeTxt.text = note.dateTime
                            if (note.categories.isNotEmpty()) {
                                note.categories.forEach {
                                    categoryTxt.append(it.title + "  ")
                                }
                            }
                            updateVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
                        }
                    }
                })
        }
        else {
            textView.text = AppConstants.DISCONNECT_INTERNET_MESSAGE
            updateVisibility(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
        }
    }

    private fun updateVisibility(pbVisibility: Int, tvVisibility: Int, consVisibility: Int) {
        progressBar.visibility = pbVisibility
        textView.visibility = tvVisibility
        ConstraintLayout.visibility = consVisibility
    }
}
