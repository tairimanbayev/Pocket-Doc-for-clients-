package com.project.pocketdoctor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.project.pocketdoctor.R
import com.project.pocketdoctor.model.tables.Visit
import com.project.pocketdoctor.repositories.VisitRepository
import com.project.pocketdoctor.ui.activities.IllnessesActivity
import com.project.pocketdoctor.ui.activities.VisitActivity
import com.project.pocketdoctor.ui.adapters.VisitAdapter
import com.project.pocketdoctor.ui.listeners.OnItemClickListener
import com.project.pocketdoctor.ui.listeners.OnScrolledListener
import com.project.pocketdoctor.util.Status
import com.project.pocketdoctor.viewmodels.VisitListViewModel
import com.project.pocketdoctor.viewmodels.factories.VisitListFactory
import kotlinx.android.synthetic.main.fragment_visit_list.*
import java.util.*

class VisitListFragment private constructor() : Fragment(R.layout.fragment_visit_list), OnItemClickListener,
    OnScrolledListener {
    private var isHistory = false
    private val items = ArrayList<Visit>()
    private val adapter = VisitAdapter(items)

    private lateinit var viewModel: VisitListViewModel

    companion object {
        fun newInstance(isHistory: Boolean): VisitListFragment {
            val visitListFragment = VisitListFragment()
            val bundle = Bundle()
            bundle.putBoolean("isHistory", isHistory)
            visitListFragment.arguments = bundle
            return visitListFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        isHistory = arguments?.getBoolean("isHistory") ?: false
        val viewModelStore = viewModelStore
        val requireContext = requireContext()
        viewModel = ViewModelProvider(
            viewModelStore, VisitListFactory(VisitRepository(requireContext.applicationContext), isHistory)
        ).get(VisitListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.listener = this
        adapter.scrollListener = this
        rv_visits.adapter = adapter
        layout_swipe.setOnRefreshListener { viewModel.load(true) }
        viewModel.status.observe(viewLifecycleOwner) {
            if (it is Status.Complete) {
                items.removeAll { item -> item.id == -1 }
                for (v in it.result.visits) {
                    val index = items.indexOfFirst { item -> item.id == v.id }
                    if (index != -1 && v != items[index]) items[index] = v
                    else if (v.createdAt?.contains("2020") == true) items.add(v)
                }
                tv_empty_list.isVisible = items.isEmpty() && it.result.lastPage ?: true
                if (it.result.lastPage == false)
                    items.add(Visit(id = -1))
                else if (it.result.lastPage == true) adapter.finish()
                adapter.setLoaded()
                adapter.notifyDataSetChanged()
            } else if (it is Status.Failure) Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            if (it !is Status.Loading) layout_swipe.isRefreshing = false
        }
    }

    override fun onClick(position: Int) {
        val intent = Intent(activity, if (isHistory) IllnessesActivity::class.java else VisitActivity::class.java)
        intent.putExtra("visitId", items[position].id)
        startActivity(intent)
    }

    override fun onScrolled() {
        viewModel.load()
    }
}