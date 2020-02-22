package ir.ea2.kotlin_livedata.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.NetworkUtil
import ir.ea2.kotlin_livedata.R
import ir.ea2.kotlin_livedata.data.remote.model.Note
import ir.ea2.kotlin_livedata.data.repository.AppRepository
import ir.ea2.kotlin_livedata.util.AppStatus
import kotlinx.android.synthetic.main.activity_save_note.*
import kotlinx.android.synthetic.main.item_layout.view.*


class RecyclerAdapter(var notes: MutableList<Note> , var lifeCycleOwner:LifecycleOwner ) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false))
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.titleTxt.text = notes[position].title
        holder.itemView.dateTimeTxt.text = notes[position].dateTime

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, NoteActivity::class.java)
            intent.putExtra(AppConstants.NOTE_ID_KEY, notes[position].id)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.editBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, SaveNoteActivity::class.java)
            intent.putExtra(AppConstants.STATE_KEY, AppConstants.UPDATE_STATE)
            intent.putExtra(AppConstants.NOTE_KEY, notes[position])
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.deleteBtn.setOnClickListener {
            deleteNoteRequest(notes[position],position,holder.itemView.context)
        }

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    fun refreshData(notes:List<Note>){
        this.notes.clear()
        this.notes.addAll(notes)
        notifyDataSetChanged()
    }
    fun deleteData(position: Int){
        this.notes.removeAt(position)
        notifyDataSetChanged()
    }

    private fun deleteNoteRequest(note: Note , position: Int , context: Context) {
        if (NetworkUtil.isInternetAvailable(context)) {
            AppRepository.getInstance().deleteNote(note).observe(lifeCycleOwner, Observer {
                when (it.status) {
                    AppStatus.ERROR -> Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                    AppStatus.SUCCESS -> {
                        deleteData(position)
                    }
                }
            })
        }
    }
}