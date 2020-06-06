package com.project.pocketdoctor.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoctor.R
import com.project.pocketdoctor.model.tables.DoctorType
import com.project.pocketdoctor.ui.adapters.DoctorAdapter.DoctorViewHolder
import com.project.pocketdoctor.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.item_doctor.view.*

class DoctorAdapter(val items: List<DoctorType>) :
    RecyclerView.Adapter<DoctorViewHolder>() {

    var listener: OnItemClickListener? = null

    class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DoctorViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_doctor, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doc = items[position]
        holder.itemView.apply {
            setOnClickListener { listener?.onClick(position) }
            tv_doctor_type.text = doc.type
        }
    }
}