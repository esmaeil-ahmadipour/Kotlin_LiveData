package ir.ea2.kotlin_livedata.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.NetworkUtil
import ir.ea2.kotlin_livedata.R
import ir.ea2.kotlin_livedata.data.remote.model.Note
import ir.ea2.kotlin_livedata.data.repository.AppRepository
import ir.ea2.kotlin_livedata.util.AppStatus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private var adapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Because We Not ViewModel Layer , Use AppRepository In Here .
        sendNotesRequest("")
    }

    private fun setRecyclerView(notes: List<Note>) {
        if (adapter == null) {
            mainRecyclerView.layoutManager = LinearLayoutManager(this)
            adapter = RecyclerAdapter(notes.toMutableList(),this)
            mainRecyclerView.adapter = adapter
        } else {
            adapter?.refreshData(notes)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchView = menu?.findItem(R.id.searchMenuItem)?.actionView as SearchView

        searchView.apply {
            isSubmitButtonEnabled = true
            queryHint = resources.getString(R.string.searchBar_hint_txt)
            setOnQueryTextListener(this@MainActivity)
        }

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
                sendNotesRequest("")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendNotesRequest(title:String) {
        if (NetworkUtil.isInternetAvailable(this)) {
            AppRepository.getInstance().getNotes(title).observe(

                this@MainActivity,
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
                            updateVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
                            setRecyclerView(it.data!!.data)
                        }
                    }
                })
        } else {
            textView.text = AppConstants.DISCONNECT_INTERNET_MESSAGE
            updateVisibility(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
        }
    }

    private fun updateVisibility(pbVisibility: Int, tvVisibility: Int, rvVisibility: Int) {
        progressBar.visibility = pbVisibility
        textView.visibility = tvVisibility
        mainRecyclerView.visibility = rvVisibility
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.i(AppConstants.NETWORK_TEST, query)
        sendNotesRequest(query!!)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.i(AppConstants.NETWORK_TEST, newText)
        sendNotesRequest(newText!!)

        return true
    }
}
