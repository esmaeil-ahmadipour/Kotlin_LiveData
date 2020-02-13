package ir.ea2.kotlin_livedata.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.NetworkUtil
import ir.ea2.kotlin_livedata.R
import ir.ea2.kotlin_livedata.data.remote.model.Note
import ir.ea2.kotlin_livedata.data.repository.AppRepository
import ir.ea2.kotlin_livedata.util.AppStatus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var adapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Because We Not ViewModel Layer , Use AppRepository In Here .
        sendNotesRequest()
    }

    private fun setRecyclerView(notes: List<Note>) {
        mainRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerAdapter(notes)
        mainRecyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addMenuItem -> {
                val intent = Intent(this@MainActivity, SaveNoteActivity::class.java)
                intent.putExtra(AppConstants.STATE_KEY, AppConstants.CREATE_STATE)
                startActivity(intent)
            }
            R.id.refreshMenuItem -> {
                sendNotesRequest()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendNotesRequest() {
        if (NetworkUtil.isInternetAvailable(this)) {
            AppRepository.getInstance().getNotes().observe(

                this@MainActivity,
                Observer {
                    when (it.status) {
                        AppStatus.LOADING -> {
                            progressBar.visibility = View.VISIBLE
                            textView.visibility = View.INVISIBLE
                            mainRecyclerView.visibility = View.INVISIBLE
                        }
                        AppStatus.ERROR -> {
                            textView.text = it.message
                            progressBar.visibility = View.INVISIBLE
                            textView.visibility = View.VISIBLE
                            mainRecyclerView.visibility = View.INVISIBLE
                        }
                        AppStatus.SUCCESS -> {
                            progressBar.visibility = View.INVISIBLE
                            textView.visibility = View.INVISIBLE
                            mainRecyclerView.visibility = View.VISIBLE
                            if (it.data?.notes != null && it.data.notes.isNotEmpty()) {
                                if (adapter == null) {
                                    //Set NoteResponse(Received From MutableLiveData) To RecyclerView
                                    setRecyclerView(it.data.notes)
                                } else {
                                    adapter?.refreshData(it.data.notes)
                                }
                            }
                        }
                    }

                })
        } else {
            textView.text = AppConstants.DISCONNECT_INTERNET_MESSAGE
            progressBar.visibility = View.INVISIBLE
            textView.visibility = View.VISIBLE
            mainRecyclerView.visibility = View.INVISIBLE
        }
    }
}
