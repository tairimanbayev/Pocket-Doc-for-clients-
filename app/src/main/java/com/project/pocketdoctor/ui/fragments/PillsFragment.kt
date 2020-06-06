package com.project.pocketdoctor.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.project.pocketdoctor.R
import com.project.pocketdoctor.model.tables.Illness
import com.project.pocketdoctor.repositories.PillRepository
import com.project.pocketdoctor.ui.adapters.PillAdapter
import com.project.pocketdoctor.util.Status
import com.project.pocketdoctor.viewmodels.PillViewModel
import com.project.pocketdoctor.viewmodels.factories.PillFactory
import kotlinx.android.synthetic.main.fragment_pills.*
import java.util.*

class PillsFragment : Fragment(R.layout.fragment_pills) {
    private val items = ArrayList<Illness>()
    private val adapter = PillAdapter(items)

    private val viewModel by viewModels<PillViewModel> { PillFactory(PillRepository(requireContext())) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_pills.adapter = adapter
        layout_swipe.setOnRefreshListener { viewModel.reload() }
        viewModel.status.observe(viewLifecycleOwner) {
            if (it is Status.Complete) {
                items.clear()
                items.addAll(it.result)
                adapter.notifyDataSetChanged()
                tv_empty_list.isVisible = items.isEmpty()
            } else if (it is Status.Failure) Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            if (it !is Status.Loading) layout_swipe.isRefreshing = false
        }
    }
}