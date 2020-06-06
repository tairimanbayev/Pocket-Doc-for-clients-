package com.project.pocketdoctor.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.project.pocketdoctor.R
import com.project.pocketdoctor.model.tables.Card
import com.project.pocketdoctor.repositories.CardRepository
import com.project.pocketdoctor.ui.activities.EditProfileActivity
import com.project.pocketdoctor.ui.activities.IllnessesHistoryActivity
import com.project.pocketdoctor.ui.adapters.CardAdapter
import com.project.pocketdoctor.ui.listeners.OnCardClickListener
import com.project.pocketdoctor.util.Status
import com.project.pocketdoctor.viewmodels.CardViewModel
import com.project.pocketdoctor.viewmodels.factories.CardFactory
import kotlinx.android.synthetic.main.fragment_card.*

class CardFragment : Fragment(R.layout.fragment_card), OnCardClickListener {
    private val REQUEST_CODE = 134
    private val items = ArrayList<Card>()
    private val adapter = CardAdapter(items, 0, this)
    private val viewModel by viewModels<CardViewModel> { CardFactory(CardRepository(requireContext())) }

    private var activatedLoading = false
        set(value) {
            field = value
            checkLoading()
        }
    private var deletedLoading = false
        set(value) {
            field = value
            checkLoading()
        }
    private var statusLoading = false
        set(value) {
            field = value
            checkLoading()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_cards.adapter = adapter

        viewModel.status.observe(viewLifecycleOwner) {
            if (it is Status.Complete) {
                items.clear()
                items.addAll(it.result)
                adapter.notifyDataSetChanged()
            } else if (it is Status.Failure) Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            statusLoading = it is Status.Loading
        }
        viewModel.activated.observe(viewLifecycleOwner) {
            if (it is Status.Complete) {
                adapter.boundCard = it.result
                adapter.notifyDataSetChanged()
            } else if (it is Status.Failure) Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            activatedLoading = it is Status.Loading
        }
        viewModel.deleted.observe(viewLifecycleOwner) {
            if (it is Status.Complete) {
                items.removeAt(it.result)
                adapter.notifyItemRemoved(it.result)
            } else if (it is Status.Failure) Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            deletedLoading = it is Status.Loading
        }

        fab_create_card.setOnClickListener { Intent(context, EditProfileActivity::class.java).apply { startActivityForResult(this, REQUEST_CODE) } }
    }

    override fun onActivate(position: Int) {
        viewModel.activate(items[position].id)
    }

    override fun onClick(position: Int) {
        Intent(context, IllnessesHistoryActivity::class.java).apply {
            putExtra("cardId", items[position].id)
            startActivity(this)
        }
    }

    override fun onDeleteClick(position: Int) {
        AlertDialog.Builder(requireContext()).setTitle(R.string.confirm_card_delete).setNegativeButton(R.string.no, null)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.delete(items[position], position)
            }
            .show()
    }

    override fun onEditClick(position: Int) {
        Intent(context, EditProfileActivity::class.java).apply {
            putExtra("cardId", items[position].id)
            startActivityForResult(this, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) viewModel.reload()
        else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkLoading() {
        progress_bar.isVisible = activatedLoading && deletedLoading && statusLoading
    }
}