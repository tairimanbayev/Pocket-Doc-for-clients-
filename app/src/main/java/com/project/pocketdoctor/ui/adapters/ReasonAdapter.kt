package com.project.pocketdoctor.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoctor.R
import com.project.pocketdoctor.model.tables.Reason
import com.project.pocketdoctor.ui.adapters.ReasonAdapter.ReasonViewHolder
import kotlinx.android.synthetic.main.item_reason.view.*
import java.util.*

class ReasonAdapter : RecyclerView.Adapter<ReasonViewHolder>() {
    var items: List<Reason> = emptyList()
        set(value) {
            field = value
            checkedItems.removeAll { value.map { item -> item.id }.contains(it) }
            notifyDataSetChanged()
        }
    val checkedItems = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReasonViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_reason, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ReasonViewHolder, position: Int) {
        val reason = items[position]
        holder.itemView.cb_reason.apply {
            cb_reason.text = reason.reasonTitle
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) checkedItems.add(reason.id)
                else checkedItems.remove(reason.id)
            }
        }
    }

    class ReasonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}