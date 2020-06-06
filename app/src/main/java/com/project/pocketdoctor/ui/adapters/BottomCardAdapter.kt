package com.project.pocketdoctor.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoctor.R
import com.project.pocketdoctor.model.tables.Card
import com.project.pocketdoctor.ui.adapters.BottomCardAdapter.BottomCardViewHolder
import com.project.pocketdoctor.ui.listeners.OnItemClickListener
import com.project.pocketdoctor.util.reformat
import kotlinx.android.synthetic.main.item_bottom_card.view.*
import java.util.*

class BottomCardAdapter(private val items: ArrayList<Pair<Card, Boolean>>, private var listener: OnItemClickListener) : RecyclerView.Adapter<BottomCardViewHolder>() {

    class BottomCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BottomCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bottom_card, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BottomCardViewHolder, position: Int) {
        val card = items[position].first
        holder.itemView.apply {
            cb_active.isChecked = items[position].second
            tv_name.text = context.getString(R.string.name_format, card.firstName, card.lastName)
            val birthday = reformat(card.birthday, "dd.MM.yyyy")
            tv_birthday.text = birthday?.let { "$it года" } ?: ""
            setOnClickListener { listener.onClick(position) }
            cb_active.setOnCheckedChangeListener { _, checked ->
                items[position] = items[position].copy(second = checked)
            }
        }
    }
}