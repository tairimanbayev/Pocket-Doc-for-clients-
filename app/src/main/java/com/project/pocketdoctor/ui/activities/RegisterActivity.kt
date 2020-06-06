package com.project.pocketdoctor.ui.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.project.pocketdoctor.R
import com.project.pocketdoctor.model.tables.Card
import com.project.pocketdoctor.model.tables.Profile
import com.project.pocketdoctor.repositories.CardRepository
import com.project.pocketdoctor.util.Status
import com.project.pocketdoctor.util.getText
import com.project.pocketdoctor.util.jumpToActivity
import com.project.pocketdoctor.viewmodels.RegisterViewModel
import com.project.pocketdoctor.viewmodels.factories.RegisterFactory
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private var card = Card()
    private var profile = Profile()

    private var updateProfile = false
    private val viewModel by viewModels<RegisterViewModel> { RegisterFactory(CardRepository(applicationContext)) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        et_birthday.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                et_birthday.setText(getString(R.string.date, day, month + 1, year))
                et_birthday.error = null
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
        btn_save.setOnClickListener { onSaveClick() }
        viewModel.status.observe(this) {
            when (it) {
                is Status.Complete -> if (it.result == updateProfile) jumpToActivity(MainActivity::class.java)
                is Status.Failure -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                is Status.Idle -> jumpToActivity(MainActivity::class.java)
            }
        }
    }

    private fun onSaveClick() {
        var error = false
        val setError = { error = true }
        val firstName = getText(ti_first_name, setError)
        val lastName = getText(ti_last_name, setError)
        val birthday = getText(ti_birthday, setError)
        if (!error) {
            card.firstName = firstName
            card.lastName = lastName
            card.birthday = birthday
            card.gender = if (rb_male.isChecked) 1 else 0
            viewModel.saveCard(card)
            val email = ti_email.getText()
            if (email.isNotEmpty()) {
                updateProfile = true
                profile.email = email
                viewModel.updateProfile(profile)
            }
        }
    }
}