package com.akbarsya.wheretoeat.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.akbarsya.wheretoeat.common.abstracts.BaseActivity
import com.akbarsya.wheretoeat.common.abstracts.ResourceState
import com.akbarsya.wheretoeat.common.constant.BundleKey
import com.akbarsya.wheretoeat.common.constant.WhereToEatActivity
import com.akbarsya.wheretoeat.databinding.ActivityDeeplinkBinding
import com.akbarsya.wheretoeat.extension.observe
import com.akbarsya.wheretoeat.extension.parcelable
import com.akbarsya.wheretoeat.util.DeepLinkManager
import com.akbarsya.wheretoeat.viewmodel.ProfileViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeepLinkActivity : BaseActivity<ActivityDeeplinkBinding>(
    ActivityDeeplinkBinding::inflate
) {
    private val profileViewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var deepLinkManager: DeepLinkManager

    private val deepLinkData: Uri? by lazy {
        intent?.data
    }

    private val deepLinkExtras: Uri? by lazy {
        intent?.extras?.parcelable(BundleKey.DEEPLINK_URI)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }

    override fun init() {
        handleDeepLink()
        observeUserByFirebaseUid()
    }

    private fun handleDeepLink() {
        val currentUser = Firebase.auth.currentUser

        if(currentUser != null) {
            val deepLink = deepLinkExtras ?: deepLinkData

            deepLink?.let { deepLinkUri ->
                deepLinkManager.handleDeepLink(this@DeepLinkActivity, deepLinkUri)
                finish()
            } ?: kotlin.run {
                profileViewModel.fetchUserByFirebaseUid()
            }
        } else {
            openOnboardingActivity()
        }
    }

    private fun observeUserByFirebaseUid() {
        observe(profileViewModel.fetchUserByFirebaseUidLiveData) {
            when(it.status) {
                ResourceState.SUCCESS -> {
                    if(it.data.preferenceTags.isEmpty()) {
                        openPreferencesChooserActivity()
                    } else {
                        openMainActivity()
                    }
                }
                ResourceState.FAILED -> {
                    openOnboardingActivity()
                }
                ResourceState.LOADING -> showLoading()
            }
        }
    }

    private fun openPreferencesChooserActivity() {
        startActivity(Intent(this, WhereToEatActivity.PREFERENCES_CHOOSER))
        finish()
    }

    private fun openMainActivity() {
        startActivity(Intent(this, WhereToEatActivity.HOME))
        finish()
    }

    private fun openOnboardingActivity() {
        startActivity(Intent(this, WhereToEatActivity.ONBOARDING))
        finish()
    }
}