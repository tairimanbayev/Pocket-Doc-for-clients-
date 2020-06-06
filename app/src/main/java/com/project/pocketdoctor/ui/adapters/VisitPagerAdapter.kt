package com.project.pocketdoctor.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.pocketdoctor.ui.fragments.VisitListFragment

class VisitPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int) = VisitListFragment.newInstance(position == 1)
}