package ir.ea2.kotlin_livedata.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.R
import ir.ea2.kotlin_livedata.data.remote.model.Note
import kotlinx.android.synthetic.main.item_layout.view.*


class RecyclerAdapter(var notes: MutableList<Note>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false))
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.titleTxt.text = notes[position].title
        holder.itemView.dateTimeTxt.text = notes[position].dateTime

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, NoteActivity::class.java)
            intent.putExtra(AppConstants.NOTE_KEY, notes[position])
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.editBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, SaveNoteActivity::class.java)
            intent.putExtra(AppConstants.STATE_KEY, AppConstants.UPDATE_STATE)
            intent.putExtra(AppConstants.NOTE_KEY, notes[position])
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.deleteBtn.setOnClickListener {

        }

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    fun refreshData(notes:List<Note>){
        this.notes.clear()
        this.notes.addAll(notes)
        notifyDataSetChanged()
    }
}