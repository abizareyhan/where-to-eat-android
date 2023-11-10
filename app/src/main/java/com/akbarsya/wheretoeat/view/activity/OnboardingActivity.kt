package com.akbarsya.wheretoeat.view.activity

import android.content.Intent
import android.content.IntentSender
import android.content.res.ColorStateList
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.akbarsya.wheretoeat.R
import com.akbarsya.wheretoeat.common.abstracts.BaseActivity
import com.akbarsya.wheretoeat.common.abstracts.ResourceState
import com.akbarsya.wheretoeat.common.constant.WhereToEatActivity
import com.akbarsya.wheretoeat.common.exception.NullMappingException
import com.akbarsya.wheretoeat.databinding.ActivityOnboardingBinding
import com.akbarsya.wheretoeat.enum.LoginFailureType
import com.akbarsya.wheretoeat.extension.observe
import com.akbarsya.wheretoeat.extension.pxFromDp
import com.akbarsya.wheretoeat.extension.showShortToast
import com.akbarsya.wheretoeat.model.LoginFailureDialogModel
import com.akbarsya.wheretoeat.model.OnboardingState
import com.akbarsya.wheretoeat.view.dialogfragment.LoginFailureDialogFragment
import com.akbarsya.wheretoeat.viewmodel.OnboardingViewModel
import com.akbarsya.wheretoeat.viewmodel.ProfileViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity: BaseActivity<ActivityOnboardingBinding>(
    ActivityOnboardingBinding::inflate
) {
    private val onboardingViewModel: OnboardingViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    private val oneTapSignInGoogleIntentIntentResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        handleOneTapSignInGoogleCallback(result)
    }

    override fun init() {
        observeOnboardingState()
        observeBeginSignInResult()
        observeSignInWithCredential()
        observeSignInAnonymously()
        observeUserByFirebaseUid()
        observeCreateNewUser()

        onboardingViewModel.initializeGoogleOneTapSignIn(this@OnboardingActivity)
        onboardingViewModel.nextOnBoardingState(1)

        with(binding) {
            tvNext.setOnClickListener {
                onboardingViewModel.nextOnBoardingState()
            }

            tvSkip.setOnClickListener {
                onboardingViewModel.nextOnBoardingState(3)
            }

            tvFinish.setOnClickListener {
                showLoading()
                oneTapSignIn()
            }
        }
    }

    private fun observeOnboardingState() {
        observe(onboardingViewModel.onboardingStateLiveData) {
            setOnboardingUI(it)
        }
    }

    private fun setOnboardingUI(onboardingState: OnboardingState) {
        with(binding) {
            ivThumbnail.setImageDrawable(ContextCompat.getDrawable(this@OnboardingActivity, onboardingState.imageDrawableResID))
            tvTitle.setText(onboardingState.titleResID)
            tvSubtitle.setText(onboardingState.subtitleResID)

            when(onboardingState.step) {
                1 -> {
                    tvSkip.visibility = View.VISIBLE
                    tvNext.visibility = View.VISIBLE
                    tvFinish.visibility = View.GONE

                    firstIndicator.layoutParams.width = 64.pxFromDp
                    secondIndicator.layoutParams.width = 8.pxFromDp
                    thirdIndicator.layoutParams.width = 8.pxFromDp

                    firstIndicator.requestLayout()
                    secondIndicator.requestLayout()
                    thirdIndicator.requestLayout()

                    firstIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@OnboardingActivity, R.color.colorPrimary))
                    secondIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@OnboardingActivity, R.color.colorGray))
                    thirdIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@OnboardingActivity, R.color.colorGray))
                }
                2 -> {
                    tvSkip.visibility = View.VISIBLE
                    tvNext.visibility = View.VISIBLE
                    tvFinish.visibility = View.GONE

                    firstIndicator.layoutParams.width = 8.pxFromDp
                    secondIndicator.layoutParams.width = 64.pxFromDp
                    thirdIndicator.layoutParams.width = 8.pxFromDp

                    firstIndicator.requestLayout()
                    secondIndicator.requestLayout()
                    thirdIndicator.requestLayout()

                    firstIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@OnboardingActivity, R.color.colorGray))
                    secondIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@OnboardingActivity, R.color.colorPrimary))
                    thirdIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@OnboardingActivity, R.color.colorGray))
                }
                3 -> {
                    tvSkip.visibility = View.GONE
                    tvNext.visibility = View.GONE
                    tvFinish.visibility = View.VISIBLE

                    firstIndicator.layoutParams.width = 8.pxFromDp
                    secondIndicator.layoutParams.width = 8.pxFromDp
                    thirdIndicator.layoutParams.width = 64.pxFromDp

                    firstIndicator.requestLayout()
                    secondIndicator.requestLayout()
                    thirdIndicator.requestLayout()

                    firstIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@OnboardingActivity, R.color.colorGray))
                    secondIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@OnboardingActivity, R.color.colorGray))
                    thirdIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@OnboardingActivity, R.color.colorPrimary))
                }
            }
        }
    }

    private fun observeBeginSignInResult() {
        observe(onboardingViewModel.beginSignInResultLiveData) {
            when(it.status) {
                ResourceState.SUCCESS -> {
                    hideLoading()
                    try {
                        val intentSenderRequest = IntentSenderRequest.Builder(
                            it.data.pendingIntent.intentSender
                        ).build()
                        oneTapSignInGoogleIntentIntentResultLauncher.launch(intentSenderRequest)
                    } catch (e: IntentSender.SendIntentException) {
                        showLoginFailureBottomSheet(LoginFailureType.ONE_TAP_UI_NOT_SUPPORTED)
                    }
                }
                ResourceState.FAILED -> {
                    hideLoading()
                    val loginFailureType = when(it.exception) {
                        is ApiException -> when(it.exception.statusCode) {
                            else -> LoginFailureType.GOOGLE_ACCOUNT_NOT_FOUND
                        }
                        else -> LoginFailureType.GOOGLE_LOGIN_FAILED
                    }

                    showLoginFailureBottomSheet(loginFailureType)
                }
                ResourceState.LOADING -> showLoading()
            }
        }
    }

    private fun observeSignInWithCredential() {
        observe(onboardingViewModel.signInWithCredentialLiveData) {
            when(it.status) {
                ResourceState.SUCCESS -> profileViewModel.fetchUserByFirebaseUid()
                ResourceState.FAILED -> showLoginFailureBottomSheet(LoginFailureType.GOOGLE_LOGIN_FAILED)
                ResourceState.LOADING -> showLoading()
            }
        }
    }

    private fun observeSignInAnonymously() {
        observe(onboardingViewModel.signInAnonymouslyLiveData) {
            when(it.status) {
                ResourceState.SUCCESS -> profileViewModel.fetchUserByFirebaseUid()
                ResourceState.FAILED -> showShortToast(R.string.something_went_wrong_try_again)
                ResourceState.LOADING -> showLoading()
            }
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
                    onboardingViewModel.createNewUser()
                }
                ResourceState.LOADING -> showLoading()
            }
        }
    }

    private fun observeCreateNewUser() {
        observe(onboardingViewModel.createNewUserLiveData) {
            when(it.status) {
                ResourceState.SUCCESS -> {
                    hideLoading()
                    if(it.data.preferenceTags.isEmpty()) {
                        openPreferencesChooserActivity()
                    } else {
                        openMainActivity()
                    }
                }
                ResourceState.FAILED -> {
                    hideLoading()
                    showShortToast(R.string.something_went_wrong_try_again)
                }
                ResourceState.LOADING -> showLoading()
            }
        }
    }

    private fun oneTapSignIn() {
        onboardingViewModel.oneTapSignInGoogle()
    }

    private fun signInAnonymously() {
        onboardingViewModel.signInAnonymously()
    }

    private fun handleOneTapSignInGoogleCallback(activityResult: ActivityResult) {
        try {
            val credential = onboardingViewModel.oneTapClient.getSignInCredentialFromIntent(activityResult.data)
            val authCredential = GoogleAuthProvider.getCredential(credential.googleIdToken, null)
            when {
                credential.googleIdToken != null -> onboardingViewModel.signInWithCredential(authCredential)
                else -> showLoginFailureBottomSheet(LoginFailureType.GOOGLE_ID_TOKEN_MISSING)
            }
        } catch (e: ApiException) {
            if(e.statusCode != CommonStatusCodes.CANCELED) {
                showLoginFailureBottomSheet(LoginFailureType.GOOGLE_LOGIN_FAILED)
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

    private fun showLoginFailureBottomSheet(loginFailureType: LoginFailureType?) {
        if(loginFailureType != null) {
            LoginFailureDialogFragment.newInstance(
                LoginFailureDialogModel(
                    loginFailureType,
                    ::oneTapSignIn,
                    ::signInAnonymously
                )
            ).showBottomSheet(supportFragmentManager)
        }
    }
}