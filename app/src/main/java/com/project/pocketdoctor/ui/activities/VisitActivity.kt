package com.project.pocketdoctor.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.project.pocketdoctor.R
import com.project.pocketdoctor.imageDownloadUrl
import com.project.pocketdoctor.model.tables.DoctorType.Companion.getDoctorRole
import com.project.pocketdoctor.model.tables.Visit
import com.project.pocketdoctor.repositories.VisitRepository
import com.project.pocketdoctor.services.FirebaseMsgService
import com.project.pocketdoctor.util.Status
import com.project.pocketdoctor.util.glideImageInto
import com.project.pocketdoctor.util.jumpToActivity
import com.project.pocketdoctor.viewmodels.VisitViewModel
import com.project.pocketdoctor.viewmodels.factories.VisitFactory
import kotlinx.android.synthetic.main.activity_visit.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class VisitActivity : AppCompatActivity() {

    private var visit: Visit? = null

    private val visitId by lazy { intent.getIntExtra("visitId", -1) }
    private val viewModel by viewModels<VisitViewModel> { VisitFactory(VisitRepository(applicationContext), visitId) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)
        setSupportActionBar(layout_toolbar.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }
        layout_toolbar.toolbar_title.text = getString(R.string.watch_visit)
        tv_call_id.text = getString(R.string.call_id, visitId)
        viewModel.status.observe(this) {
            if (it is Status.Complete) {
                visit = it.result
                initViews()
            }
        }
    }

    private fun initViews() {
        var names = ""
        for (card in visit?.cards ?: emptyList()) {
            if (names.isNotEmpty()) names += ", "
            names += getString(R.string.name_format, card.firstName, card.lastName)
        }
        tv_card.text = names
        tv_create_time.text = visit?.date ?: ""
        tv_visit_time.text = visit?.finishedAt ?: ""
        tv_doctor_role.text = getDoctorRole(visit?.role)
        val doctor = visit?.doctor
        val doctorCard = doctor?.profile?.card
        tv_doctor_name.text = if (doctor == null || doctorCard == null) "" else {
            val clinic = doctor.clinic?.name
            FirebaseMsgService.getInstance {
                glideImageInto(this, imageDownloadUrl(doctorCard.id, it), iv_doctor)
            }
            getString(R.string.name_format, doctorCard.firstName, doctorCard.lastName) +
                    if (clinic != null) "/n$clinic" else ""
        }
    }

    override fun onBackPressed() {
        jumpToActivity(MainActivity::class.java)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}