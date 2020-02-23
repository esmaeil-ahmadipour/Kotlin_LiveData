package ir.ea2.kotlin_livedata.ui

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.Observer
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.NetworkUtil
import ir.ea2.kotlin_livedata.R
import ir.ea2.kotlin_livedata.data.remote.model.Category
import ir.ea2.kotlin_livedata.data.remote.model.Note
import ir.ea2.kotlin_livedata.data.repository.AppRepository
import ir.ea2.kotlin_livedata.util.AppStatus
import kotlinx.android.synthetic.main.activity_save_note.*

class SaveNoteActivity : AppCompatActivity() {
    private lateinit var note: Note
    private lateinit var state: String
    private val category: MutableList<Category> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_note)

        val bundle = intent.extras
         state = bundle?.getString(AppConstants.STATE_KEY).toString()

        if (state == AppConstants.UPDATE_STATE) {
            note = bundle?.getSerializable(AppConstants.NOTE_KEY) as Note
            ac_save_titleEditTxt.text.append(note.title)
            ac_save_bodyEditTxt.text.append(note.body)
        }
        sendCategoriesRequest()
        ac_save_saveBtn.setOnClickListener( View.OnClickListener {
            saveOnClickListener(state)
        })

    }

    private fun saveOnClickListener(state: String?) {
        category.clear()
        ac_save_checkboxContainer.children.forEach {
            if ((it as CheckBox).isChecked) {
                category.add(Category(it.id.toLong(), it.text.toString()))

            }
        }

        when(state){
            AppConstants.UPDATE_STATE->{
                note.title=ac_save_titleEditTxt.text.toString()
                note.body=ac_save_bodyEditTxt.text.toString()
                note.categories=category
                note.dateTime=""
                updateNoteRequest(note)
            }
            AppConstants.CREATE_STATE->{
                this.note = Note(
                    0, ac_save_titleEditTxt.text.toString(), ac_save_bodyEditTxt.text.toString(),
                    "", category
                )
                saveNoteRequest(note)
            }
        }


        this.finish()
    }

    private fun saveNoteRequest(note: Note) {
        if (NetworkUtil.isInternetAvailable(this)) {
            AppRepository.getInstance().saveNote(note).observe(this, Observer {
                when (it.status) {
                    AppStatus.ERROR -> ac_save_resultTxt.text = it.message
                    AppStatus.SUCCESS -> ac_save_resultTxt.text = AppConstants.SUCCESSFUL_MESSAGE
                }
            })
        }
    }
    private fun updateNoteRequest(note: Note) {
        if (NetworkUtil.isInternetAvailable(this)) {
            AppRepository.getInstance().updateNote(note).observe(this, Observer {
                when (it.status) {
                    AppStatus.ERROR -> ac_save_resultTxt.text = it.message
                    AppStatus.SUCCESS -> ac_save_resultTxt.text = AppConstants.SUCCESSFUL_MESSAGE
                }
            })
        }
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
                            if (category!!.data.isNotEmpty()) {
                                category.data.forEach {
                                    val chkBox = CheckBox(this)
                                    chkBox.id = it.id.toInt()
                                    chkBox.text = it.title
                                    if(state==AppConstants.UPDATE_STATE){
                                        if(note.categories.contains(it)){
                                            chkBox.isChecked=true
                                        }
                                    }
                                    ac_save_checkboxContainer.addView(chkBox)
                                }
                                updateVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
                            }
                        }
                    }
                })
        } else {
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
