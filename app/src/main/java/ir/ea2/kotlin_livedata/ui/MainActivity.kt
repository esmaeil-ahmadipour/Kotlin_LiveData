package ir.ea2.kotlin_livedata.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.DataUtil
import ir.ea2.kotlin_livedata.R
import ir.ea2.kotlin_livedata.data.remote.model.Note
import ir.ea2.kotlin_livedata.data.repository.AppRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Because We Not ViewModel Layer , Use AppRepository In Here .
        AppRepository.getInstance().getNotes().observe(
            this@MainActivity,
            Observer {
                //Set NoteResponse(Received From MutableLiveData) To RecyclerView
                setRecyclerView(it.notes)
            })
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
        }
        return super.onOptionsItemSelected(item)
    }
}
