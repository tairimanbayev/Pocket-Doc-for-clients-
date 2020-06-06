package com.project.pocketdoctor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.project.pocketdoctor.R
import com.project.pocketdoctor.model.tables.DoctorType
import com.project.pocketdoctor.ui.activities.ReasonsActivity
import com.project.pocketdoctor.ui.adapters.DoctorAdapter
import com.project.pocketdoctor.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_services.*

class ServicesFragment : Fragment(R.layout.fragment_services) {
    private val doctorAdapter = DoctorAdapter(DoctorType.getDoctorList())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_doctors.adapter = doctorAdapter
        doctorAdapter.listener = object : OnItemClickListener {
            override fun onClick(position: Int) {
                Intent(context, ReasonsActivity::class.java).apply {
                    putExtra("doctorTag", DoctorType.getDoctorTag(doctorAdapter.items[position].type))
                    startActivity(this)
                }
            }
        }
    }
}