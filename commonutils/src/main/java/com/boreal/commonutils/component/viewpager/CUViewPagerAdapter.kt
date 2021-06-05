package com.boreal.commonutils.component.viewpager

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CUViewPagerAdapter(
    private val fragmentModel: ArrayList<CUFragmentModel>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = fragmentModel.size

    override fun createFragment(position: Int) = fragmentModel[position].fragment
}