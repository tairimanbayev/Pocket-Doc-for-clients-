package com.project.pocketdoctor.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import com.project.pocketdoctor.R
import com.project.pocketdoctor.model.tables.Illness
import com.project.pocketdoctor.repositories.IllnessRepository
import com.project.pocketdoctor.ui.adapters.IllnessAdapter
import com.project.pocketdoctor.util.Status
import com.project.pocketdoctor.viewmodels.IllnessesViewModel
import com.project.pocketdoctor.viewmodels.factories.IllnessesFactory
import kotlinx.android.synthetic.main.activity_illnesses_history.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class IllnessesHistoryActivity : AppCompatActivity() {
    private val items = ArrayList<Illness>()
    private val adapter = IllnessAdapter(items)
    private val cardId by lazy { intent.getIntExtra("cardId", -1) }

    private val viewModel by viewModels<IllnessesViewModel> {
        IllnessesFactory(IllnessRepository(applicationContext), cardId, true)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_illnesses_history)
        setSupportActionBar(layout_toolbar.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }
        toolbar_title.text = getString(R.string.visits_history)
        rv_illnesses.adapter = adapter
        layout_swipe.setOnRefreshListener { viewModel.reload() }
        viewModel.status.observe(this) {
            if (it is Status.Complete) {
                items.clear()
                items.addAll(it.result)
                adapter.notifyDataSetChanged()
                tv_empty_list.isVisible = items.isEmpty()
            } else if (it is Status.Failure) Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            if (it !is Status.Loading) layout_swipe.isRefreshing = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}