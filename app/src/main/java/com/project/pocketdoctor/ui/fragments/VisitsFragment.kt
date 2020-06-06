package com.project.pocketdoctor.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.project.pocketdoctor.R
import com.project.pocketdoctor.ui.adapters.VisitPagerAdapter
import kotlinx.android.synthetic.main.fragment_visits.*

class VisitsFragment : Fragment(R.layout.fragment_visits) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_pager!!.adapter = VisitPagerAdapter(this)
        TabLayoutMediator(tab_layout, view_pager) { tab, i ->
            tab.text = if (i == 0) "Текущие" else "История"
        }.attach()
    }
}