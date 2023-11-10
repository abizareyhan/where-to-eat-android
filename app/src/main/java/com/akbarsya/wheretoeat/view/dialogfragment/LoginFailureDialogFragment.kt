package com.akbarsya.wheretoeat.view.dialogfragment

import android.content.Intent
import android.provider.Settings
import android.view.View
import androidx.core.os.bundleOf
import com.akbarsya.wheretoeat.R
import com.akbarsya.wheretoeat.common.abstracts.BaseBottomSheetDialogFragment
import com.akbarsya.wheretoeat.common.constant.BundleKey
import com.akbarsya.wheretoeat.databinding.DialogFragmentLoginFailureBinding
import com.akbarsya.wheretoeat.enum.LoginFailureType
import com.akbarsya.wheretoeat.model.LoginFailureDialogModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFailureDialogFragment: BaseBottomSheetDialogFragment<DialogFragmentLoginFailureBinding>(
    DialogFragmentLoginFailureBinding::inflate
) {
    lateinit var dataModel: LoginFailureDialogModel

    companion object {
        fun newInstance(dataModel: LoginFailureDialogModel): LoginFailureDialogFragment {
            return LoginFailureDialogFragment().also {
                it.arguments = bundleOf(
                    BundleKey.INPUT_BUNDLE_DATA to dataModel
                )
            }
        }
    }

    override fun init() {
        with(binding) {
            (arguments?.get(BundleKey.INPUT_BUNDLE_DATA) as? LoginFailureDialogModel)?.let { loginFailureDialogModel ->
                dataModel = loginFailureDialogModel

                when(dataModel.loginFailureType) {
                    LoginFailureType.ONE_TAP_UI_NOT_SUPPORTED -> {
                        tvFailureTitle.text = getString(R.string.one_tap_ui_not_supported)
                        btPrimaryAction.text = getString(R.string.continue_as_anonym)
                        btSecondaryAction.visibility = View.GONE

                        btPrimaryAction.setOnClickListener {
                            openAddAccountGoogle()
                        }
                    }
                    LoginFailureType.GOOGLE_ACCOUNT_NOT_FOUND -> {
                        tvFailureTitle.text = getString(R.string.google_account_not_found)
                        btPrimaryAction.text = getString(R.string.add_google_account)
                        btSecondaryAction.text = getString(R.string.continue_as_anonym)

                        btPrimaryAction.setOnClickListener {
                            openAddAccountGoogle()
                        }

                        btSecondaryAction.setOnClickListener {
                            loginAnonymously()
                        }
                    }
                    LoginFailureType.GOOGLE_ID_TOKEN_MISSING, LoginFailureType.GOOGLE_LOGIN_FAILED -> {
                        tvFailureTitle.text = getString(R.string.google_login_failure)
                        btPrimaryAction.text = getString(R.string.login_with_another_account)
                        btSecondaryAction.text = getString(R.string.continue_as_anonym)

                        btPrimaryAction.setOnClickListener {
                            loginWithAnotherAccount()
                        }

                        btSecondaryAction.setOnClickListener {
                            loginAnonymously()
                        }
                    }
                }
            } ?: kotlin.run {
                dismiss()
            }
        }
    }

    private fun openAddAccountGoogle() {
        val addAccountIntent = Intent(Settings.ACTION_ADD_ACCOUNT).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addAccountIntent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
        startActivity(addAccountIntent)
        dismiss()
    }

    private fun loginWithAnotherAccount() {
        dataModel.loginWithAnotherAccount()
        dismiss()
    }

    private fun loginAnonymously() {
        dataModel.loginAnonymously()
        dismiss()
    }
}