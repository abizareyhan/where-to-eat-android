package com.akbarsya.wheretoeat.common.abstracts

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding>(
    val bindingFactory: (LayoutInflater) -> VB,
): Fragment() {
    protected val binding: VB by lazy { bindingFactory(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutTransitionTypeToChanging()
        init()
    }

    abstract fun init()

    private fun setLayoutTransitionTypeToChanging() {
        with(binding) {
            (root as? ViewGroup)?.layoutTransition?.enableTransitionType(
                LayoutTransition.CHANGING
            )
        }
    }
}