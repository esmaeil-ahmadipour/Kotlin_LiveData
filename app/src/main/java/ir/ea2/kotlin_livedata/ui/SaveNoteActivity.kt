package ir.ea2.kotlin_livedata.ui

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.NetworkUtil
import ir.ea2.kotlin_livedata.R
import ir.ea2.kotlin_livedata.data.remote.model.Note
import ir.ea2.kotlin_livedata.data.repository.AppRepository
import ir.ea2.kotlin_livedata.util.AppStatus
import kotlinx.android.synthetic.main.activity_main.*
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
            ac_save_titleEditTxt.text.append(note.title)
            ac_save_bodyEditTxt.text.append(note.body)
        }
        sendCategoriesRequest()

    }

    private fun sendCategoriesRequest() {
        if (NetworkUtil.isInternetAvailable(this)) {
            AppRepository.getInstance().getCategories().observe(

                this@SaveNoteActivity,
                Observer {
                    when (it.status) {
                        AppStatus.LOADING -> {
                            updateVisibility(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
                        }
                        AppStatus.ERROR -> {
                            ac_save_textView.text = it.message
                            updateVisibility(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
                        }
                        AppStatus.SUCCESS -> {
                            val category = it.data
                            if (category!!.categories.isNotEmpty()) {
                                category.categories.forEach {
                                    val chkBox = CheckBox(this)
                                    chkBox.text = it.title
                                    ac_save_checkboxContainer.addView(chkBox)
                                }
                                updateVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
                            }
                        }
                    }
                })
        }
        else {
            ac_save_textView.text = AppConstants.DISCONNECT_INTERNET_MESSAGE
            updateVisibility(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
        }
    }
    private fun updateVisibility(pbVisibility: Int, tvVisibility: Int, consVisibility: Int) {
        ac_save_progressBar.visibility = pbVisibility
        ac_save_textView.visibility = tvVisibility
        ac_save_ConstraintLayout.visibility = consVisibility
    }

}
