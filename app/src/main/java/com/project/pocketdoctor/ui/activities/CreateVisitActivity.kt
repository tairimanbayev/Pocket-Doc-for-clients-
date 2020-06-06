package com.project.pocketdoctor.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.project.pocketdoctor.R
import com.project.pocketdoctor.model.tables.Card
import com.project.pocketdoctor.model.tables.DoctorType
import com.project.pocketdoctor.model.tables.Visit
import com.project.pocketdoctor.repositories.CardRepository
import com.project.pocketdoctor.ui.adapters.BottomCardAdapter
import com.project.pocketdoctor.ui.fragments.CreateVisitDialogFragment
import com.project.pocketdoctor.ui.listeners.BottomDialogListener
import com.project.pocketdoctor.ui.listeners.OnItemClickListener
import com.project.pocketdoctor.util.Status
import com.project.pocketdoctor.util.dateFormatSymbols
import com.project.pocketdoctor.util.jumpToActivity
import com.project.pocketdoctor.viewmodels.CreateVisitViewModel
import com.project.pocketdoctor.viewmodels.factories.CreateVisitFactory
import kotlinx.android.synthetic.main.activity_create_visit.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateVisitActivity : AppCompatActivity(), BottomDialogListener, OnItemClickListener {

    private val doctorTag by lazy { intent.getStringExtra("doctorTag") }
    private val reasons by lazy { intent.getIntegerArrayListExtra("reasons") }
    private val viewModel by viewModels<CreateVisitViewModel> { CreateVisitFactory(CardRepository(applicationContext)) }

    private val activeCards = ArrayList<Pair<Card, Boolean>>()
    private val bottomCardAdapter = BottomCardAdapter(activeCards, this)
    private var bottomDialog: CreateVisitDialogFragment? = null

    private val cardObserver by lazy {
        Observer<Status<List<Card>>> {
            if (it is Status.Complete) {
                for (card in it.result) {
                    val index = activeCards.indexOfFirst { item -> item.first.id == card.id }
                    if (index == -1) activeCards.add(card to false)
                    else activeCards[index] = activeCards[index].copy(first = card)
                }
                bottomCardAdapter.notifyDataSetChanged()
            } else if (it is Status.Failure) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
            bottomDialog?.showLoading(it is Status.Loading)
        }
    }

    private var cardAdded = false
    private var dateAdded = false
    private var tag = ""
    private var year = 0
    private var month = 0
    private var day = 0
    private var hour = 0
    private var minute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_visit)

        setSupportActionBar(layout_toolbar.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }
        toolbar_title.text = DoctorType.getDoctorRole(doctorTag)
        checkFields()
        viewModel.visit.observe(this) {
            btn_next.isEnabled = true
            if (it is Status.Complete) {
                jumpToActivity(VisitActivity::class.java, bundleOf("visitId" to it.result.id))
            } else if (it is Status.Failure) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCancel() {
        checkCards()
    }

    override fun onClick() {
        viewModel.cardList.removeObserver(cardObserver)
        if (tag == "card") Intent(this, EditProfileActivity::class.java).apply { startActivity(this) }
    }

    override fun onClick(position: Int) {
        val card = activeCards[position]
        activeCards[position] = card.copy(second = !card.second)
        bottomDialog?.dismiss()
        cardAdded = activeCards.any { it.second }
        checkCards()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun checkFields() {
        btn_next.isEnabled = cardAdded && dateAdded
    }

    private fun checkCards() {
        var result = ""
        activeCards.forEach {
            if (it.second) result += getString(R.string.name_format, it.first.firstName, it.first.lastName)
        }
        tv_card.text = result
        checkFields()
        tag = ""
        viewModel.cardList.removeObserver(cardObserver)
    }

    fun onCardClick(view: View) {
        tag = "card"
        bottomDialog = CreateVisitDialogFragment(bottomCardAdapter, this)
        bottomDialog?.show(supportFragmentManager, "SelectCardDialog")
        viewModel.cardList.observe(this, cardObserver)
    }

    fun onTimeClick(view: View) {
        val c = Calendar.getInstance()
        val timePicker = TimePickerDialog(this, { _, hourSet, minuteSet ->
            hour = hourSet
            minute = minuteSet
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, day)
            c.set(Calendar.HOUR_OF_DAY, hourSet)
            c.set(Calendar.MINUTE, minuteSet)
            val dateFormat = SimpleDateFormat("d MMMM HH:mm", dateFormatSymbols)
            tv_time.text = dateFormat.format(c.time)
            dateAdded = true
            checkFields()
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true)
        DatePickerDialog(this, { _, yearSet, monthSet, daySet ->
            year = yearSet
            month = monthSet
            day = daySet
            timePicker.show()
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
    }

    fun onNextClick(view: View) {
        val selectedCards = ArrayList<Card>()
        activeCards.forEach { if (it.second) selectedCards.add(it.first) }
        val date = String.format("%04d-%02d-%02d %02d:%02d:00", year, month, day, hour, minute)
        val visit = Visit(role = doctorTag, comment = et_reason.text.toString(), date = date, cards = selectedCards)
        viewModel.createVisit(visit, reasons)
        btn_next.isEnabled = false
    }
}