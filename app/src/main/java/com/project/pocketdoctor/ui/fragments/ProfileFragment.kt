package com.project.pocketdoctor.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.project.pocketdoctor.R
import com.project.pocketdoctor.imageDownloadUrl
import com.project.pocketdoctor.model.tables.Profile
import com.project.pocketdoctor.repositories.ProfileRepository
import com.project.pocketdoctor.util.Status
import com.project.pocketdoctor.util.getDescription
import com.project.pocketdoctor.util.glideImageInto
import com.project.pocketdoctor.viewmodels.ProfileViewModel
import com.project.pocketdoctor.viewmodels.factories.ProfileFactory
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewModel by viewModels<ProfileViewModel> { ProfileFactory(ProfileRepository(requireContext()), 2) }
    private var profile: Profile? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.status.observe(viewLifecycleOwner) {
            if (it is Status.Complete) {
                profile = it.result
                initViews()
            } else if (it is Status.Failure) Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews() {
        profile?.card?.let {
            tv_name.text = getString(R.string.name_format, it.firstName, it.lastName)
            tv_description.text = getDescription(it.gender, it.birthday)
            tv_phone_number.text = profile?.phoneNumber ?: ""
            glideImageInto(requireActivity(), imageDownloadUrl(it.id, it.fcmId), iv_photo)
        }
    }
}