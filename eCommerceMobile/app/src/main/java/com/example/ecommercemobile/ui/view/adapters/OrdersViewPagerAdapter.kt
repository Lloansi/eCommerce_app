package com.example.ecommercemobile.ui.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OrdersViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val orderCategories: List<Fragment>
): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return orderCategories.size
    }

    override fun createFragment(position: Int): Fragment {
        return orderCategories[position]
    }
}