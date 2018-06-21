package net.rmitsolutions.mfexpert.lms.helpers

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Madhu on 24-Apr-2018.
 */

fun ViewGroup.inflateView(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}
