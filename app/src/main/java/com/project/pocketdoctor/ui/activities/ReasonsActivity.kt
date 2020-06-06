package com.project.pocketdoctor.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import com.project.pocketdoctor.R
import com.project.pocketdoctor.repositories.ReasonRepository
import com.project.pocketdoctor.ui.adapters.ReasonAdapter
import com.project.pocketdoctor.util.Status
import com.project.pocketdoctor.viewmodels.ReasonsViewModel
import com.project.pocketdoctor.viewmodels.factories.ReasonsFactory
import kotlinx.android.synthetic.main.activity_reasons.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class ReasonsActivity : AppCompatActivity() {

    private val tag by lazy { intent.getStringExtra("doctorTag") }
    private val viewModel by viewModels<ReasonsViewModel> {
        ReasonsFactory(ReasonRepository(applicationContext), tag)
    }

    private val adapter = ReasonAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reasons)
        setSupportActionBar(layout_toolbar.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        rv_reasons.adapter = adapter

        btn_next.setOnClickListener {
            Intent(this, CreateVisitActivity::class.java).apply {
                putExtra("doctorTag", tag)
                putExtra("reasons", adapter.checkedItems)
                startActivity(this)
            }
        }

        viewModel.status.observe(this) {
            progress_bar.isVisible = it is Status.Loading
            if (it is Status.Complete) {
                adapter.items = it.result
            } else if (it is Status.Failure) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}