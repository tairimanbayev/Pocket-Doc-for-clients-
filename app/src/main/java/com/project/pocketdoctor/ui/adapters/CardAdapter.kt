package com.project.pocketdoctor.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoctor.R
import com.project.pocketdoctor.imageDownloadUrl
import com.project.pocketdoctor.model.tables.Card
import com.project.pocketdoctor.services.FirebaseMsgService
import com.project.pocketdoctor.ui.adapters.CardAdapter.CardViewHolder
import com.project.pocketdoctor.ui.listeners.OnCardClickListener
import com.project.pocketdoctor.util.getDescription
import com.project.pocketdoctor.util.glideImageInto
import kotlinx.android.synthetic.main.item_card.view.*

class CardAdapter(private val items: List<Card>, var boundCard: Int = -1, private val listener: OnCardClickListener) :
    RecyclerView.Adapter<CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CardViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = items[position]
        val bound = card.id == boundCard
        holder.itemView.apply {
            tv_name.text = context.getString(R.string.name_format, card.firstName, card.lastName)
            tv_description.text = getDescription(card.gender, card.birthday)
            tv_height.text = if ((card.height ?: 0) > 0) card.height.toString() else "Не указан"
            tv_weight.text = if ((card.weight ?: 0) > 0) card.weight.toString() else "Не указан"
            sw_bind.isVisible = !bound
            lbl_bind.isVisible = !bound
            lbl_bound.isVisible = bound
            btn_delete.isVisible = !bound
            btn_delete.setOnClickListener { listener.onDeleteClick(position) }
            btn_edit.setOnClickListener { listener.onEditClick(position) }
            cl.setOnClickListener { listener.onClick(position) }
            sw_bind.setOnCheckedChangeListener { _, isChecked -> if (isChecked) listener.onActivate(position) }
            FirebaseMsgService.getInstance {
                glideImageInto(context, imageDownloadUrl(card.id, it), iv_card_photo)
            }
        }
    }
}