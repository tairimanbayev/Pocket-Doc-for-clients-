package com.project.pocketdoctor.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.pocketdoctor.R
import com.project.pocketdoctor.ui.listeners.BottomDialogListener
import kotlinx.android.synthetic.main.fragment_dialog_create_visit.*

class CreateVisitDialogFragment(private val adapter: RecyclerView.Adapter<*>, private val listener: BottomDialogListener) :
    BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_create_visit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler_view.adapter = adapter
        btn_add.setOnClickListener { listener.onClick() }
    }

    override fun onCancel(dialog: DialogInterface) {
        listener.onCancel()
        super.onCancel(dialog)
    }

    fun showLoading(show: Boolean) {
        progress_bar?.isVisible = show
    }
}