package com.akbarsya.wheretoeat.common.abstracts

import android.animation.LayoutTransition
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding>(
    val bindingFactory: (LayoutInflater) -> VB,
): AppCompatActivity() {
    protected val binding: VB by lazy { bindingFactory(layoutInflater) }

    val progressDialog by lazy {
        ProgressDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = binding.root
        setContentView(view)
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

    fun changeStatusBarColor(@ColorRes color: Int) {
        window.apply {
            statusBarColor = ContextCompat.getColor(this@BaseActivity, color)
        }
    }

    fun changeNavigationBarColor(@ColorRes color: Int) {
        window.apply {
            setNavigationBarColor(ContextCompat.getColor(this@BaseActivity, color))
        }
    }

    fun showLoading() {
        if(progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please Wait")
        progressDialog.show()
    }

    fun hideLoading() {
        progressDialog.dismiss()
    }
}