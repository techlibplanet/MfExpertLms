package net.rmitsolutions.mfexpert.lms.repayment.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class RepaymentPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private var mFragmentCollection: MutableList<Fragment> = ArrayList()
    private var mTitleCollection: MutableList<String> = ArrayList()
    fun addFragment(title: String, fragment: Fragment) {
        mTitleCollection.add(title)
        mFragmentCollection.add(fragment)
    }

    //Needed for
    override fun getPageTitle(position: Int): CharSequence? {
        return mTitleCollection[position]
    }

    override fun getItem(position: Int): Fragment? {
        return mFragmentCollection[position]
    }

    override fun getCount(): Int {
        return mFragmentCollection.size
    }


}