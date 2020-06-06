package com.project.pocketdoctor.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoctor.R
import com.project.pocketdoctor.imageDownloadUrl
import com.project.pocketdoctor.model.tables.DoctorType.Companion.getDoctorRole
import com.project.pocketdoctor.model.tables.Illness
import com.project.pocketdoctor.services.FirebaseMsgService
import com.project.pocketdoctor.ui.adapters.IllnessAdapter.IllnessViewHolder
import com.project.pocketdoctor.util.glideImageInto
import kotlinx.android.synthetic.main.item_illness.view.*

class IllnessAdapter(private val items: List<Illness>) : RecyclerView.Adapter<IllnessViewHolder>() {
    var fcmId = ""

    class IllnessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        FirebaseMsgService.getInstance { fcmId = it }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        IllnessViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_illness, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: IllnessViewHolder, position: Int) {
        val illness = items[position]
        val card = illness.card
        holder.itemView.apply {
            tv_doctor_role.text = getDoctorRole(illness.visit?.role)
            tv_doctor_name.text = card?.let {
                glideImageInto(context, imageDownloadUrl(card.id, fcmId), iv_doctor)
                context.getString(R.string.name_format, it.firstName, it.lastName)
            } ?: ""
            tv_diagnosis.text = illness.diagnosis ?: ""
            tv_complaint.text = illness.complaint ?: ""
            tv_inspection.text = illness.inspection ?: ""
            tv_appointment.text = illness.appointment ?: ""
            tv_result.text = illness.result ?: ""
        }
    }
}