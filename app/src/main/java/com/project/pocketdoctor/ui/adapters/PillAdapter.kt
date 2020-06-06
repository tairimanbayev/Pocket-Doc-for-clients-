package com.project.pocketdoctor.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoctor.R
import com.project.pocketdoctor.model.tables.DoctorType.Companion.getDoctorRole
import com.project.pocketdoctor.model.tables.Illness
import com.project.pocketdoctor.ui.adapters.PillAdapter.PillViewHolder
import kotlinx.android.synthetic.main.item_pill.view.*
import kotlinx.android.synthetic.main.item_pills_list.view.*

class PillAdapter(private val items: List<Illness>) : RecyclerView.Adapter<PillViewHolder>() {

    class PillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PillViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pills_list, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PillViewHolder, position: Int) {
        val illness = items[position]
        holder.itemView.apply {
            tv_doctor_role.text = getDoctorRole(illness.visit?.role)
            tv_diagnosis.text = illness.diagnosis ?: ""
            layout_pills.removeAllViews()
            Log.d("PillAdapterLogcat", "onBindViewHolder: $illness")
            for (p in illness.pills ?: emptyList()) {
                Log.d("PillAdapterLogcat", "onBindViewHolder: $p")
                val pillView: View = LayoutInflater.from(context).inflate(R.layout.item_pill, null, false)
                pillView.tv_pill_name.text = p.name
                pillView.tv_pill_description.text = p.description
                layout_pills.addView(pillView)
            }
        }
    }
}