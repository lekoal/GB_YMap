package com.example.gbymap.ui.marksManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gbymap.R
import com.example.gbymap.domain.marks.MarksEntity
import com.example.gbymap.ui.mainScreen.MarksViewModel

class MarksManagerRecyclerAdapter(
    private val viewModel: MarksViewModel
) :
    RecyclerView.Adapter<MarksManagerRecyclerAdapter.MarksManagerViewHolder>() {

    private val data = mutableListOf<MarksEntity>()

    fun setData(
        marks: List<MarksEntity>
    ) {
        data.clear()
        data.addAll(marks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarksManagerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_marks_manager, parent, false)
        return MarksManagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarksManagerViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MarksManagerViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val editButton: AppCompatImageButton = itemView.findViewById(R.id.item_marks_edit)
        private val deleteButton: AppCompatImageButton =
            itemView.findViewById(R.id.item_marks_delete)
        private val markName: AppCompatTextView = itemView.findViewById(R.id.item_marks_name)
        private val markLat: AppCompatTextView = itemView.findViewById(R.id.item_marks_lat)
        private val markLon: AppCompatTextView = itemView.findViewById(R.id.item_marks_lon)

        fun bind(
            mark: MarksEntity
        ) {
            markName.text = mark.name
            markLat.text = mark.latitude.toString()
            markLon.text = mark.longitude.toString()

            deleteButton.setOnClickListener {
                deleteMark(mark)
            }
            editButton.setOnClickListener {
                editMark(mark)
            }
        }

        private fun deleteMark(mark: MarksEntity) {
            data.remove(mark)
            notifyDataSetChanged()
            viewModel.deleteMark(mark)
        }

        private fun editMark(mark: MarksEntity) {
            viewModel.updateMarkHelper(mark)
        }

    }
}