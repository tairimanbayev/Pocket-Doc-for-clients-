package com.project.pocketdoctor.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.pocketdoctor.R
import com.project.pocketdoctor.imageDownloadUrl
import com.project.pocketdoctor.model.tables.DoctorType
import com.project.pocketdoctor.model.tables.Visit
import com.project.pocketdoctor.ui.adapters.VisitAdapter.VisitViewHolder
import com.project.pocketdoctor.ui.listeners.OnItemClickListener
import com.project.pocketdoctor.ui.listeners.OnScrolledListener
import com.project.pocketdoctor.util.glideImageInto
import com.project.pocketdoctor.util.reformat
import kotlinx.android.synthetic.main.item_visit.view.*

class VisitAdapter(private val items: List<Visit>) : RecyclerView.Adapter<VisitViewHolder>() {
    private var isLoading = false
    private var finished = false
    var listener: OnItemClickListener? = null
    var scrollListener: OnScrolledListener? = null

    companion object {
        private const val VIEW_ITEM = 0
        private const val VIEW_LOADING = 1
    }

    class VisitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager != null) {
            val lm = layoutManager as LinearLayoutManager
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!finished && !isLoading && lm.findLastVisibleItemPosition() >= lm.itemCount - 5) {
                        scrollListener?.onScrolled()
                        isLoading = true
                    }
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VisitViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(if (viewType == VIEW_ITEM) R.layout.item_visit else R.layout.item_loading, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VisitViewHolder, position: Int) {
        if (getItemViewType(position) != 1) {
            val visit = items[position]
            holder.itemView.apply {
                setOnClickListener { listener?.onClick(position) }
                tv_visit_id.text = context.getString(R.string.call_id, visit.id)
                val doctorCard = visit.doctor?.profile?.card
                tv_doctor_name.text = when {
                    doctorCard != null -> {
                        glideImageInto(context, imageDownloadUrl(doctorCard.id, visit.fcmId), iv_doctor)
                        context.getString(R.string.name_format, doctorCard.firstName, doctorCard.lastName)
                    }
                    visit.doctorId != null -> context.getString(R.string.doctor_deleted)
                    else -> context.getString(R.string.not_assigned)
                }
                tv_doctor_role.text = DoctorType.getDoctorRole(visit.role)
                var cards = ""
                for (card in visit.cards) {
                    if (cards.isNotEmpty()) cards += ", "
                    cards += context.getString(R.string.name_format, card.firstName, card.lastName)
                }
                tv_patients.text = cards
                var reasons = ""
                for (reason in visit.reasons) {
                    if (reasons.isNotEmpty()) reasons += ", "
                    reasons += reason.reasonTitle
                }
                tv_reasons.text = if (reasons.isEmpty()) "Не указана" else reasons
                tv_create_time.text = reformat(visit.createdAt ?: "")
                tv_visit_time.text = reformat(visit.date ?: "")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size - 1 && items[position].id == -1) VIEW_LOADING else VIEW_ITEM
    }

    fun setLoaded() {
        isLoading = false
    }

    fun finish() {
        finished = true
    }
}