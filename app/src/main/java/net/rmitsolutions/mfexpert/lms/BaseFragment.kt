package net.rmitsolutions.mfexpert.lms

import android.os.Bundle
import androidx.fragment.app.Fragment

import io.reactivex.disposables.CompositeDisposable


/**
 * Created by Madhu on 03-Jul-2017.
 */
abstract class BaseFragment : Fragment() {

    internal lateinit var compositeDisposable: CompositeDisposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }


}