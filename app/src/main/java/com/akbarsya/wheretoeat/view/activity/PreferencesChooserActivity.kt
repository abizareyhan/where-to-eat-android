package com.akbarsya.wheretoeat.view.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.akbarsya.wheretoeat.R
import com.akbarsya.wheretoeat.adapter.PreferenceTagAdapter
import com.akbarsya.wheretoeat.common.abstracts.BaseActivity
import com.akbarsya.wheretoeat.common.abstracts.ResourceState
import com.akbarsya.wheretoeat.common.constant.WhereToEatActivity
import com.akbarsya.wheretoeat.databinding.ActivityPreferencesChooserBinding
import com.akbarsya.wheretoeat.extension.observe
import com.akbarsya.wheretoeat.extension.setItemDecorations
import com.akbarsya.wheretoeat.extension.showShortToast
import com.akbarsya.wheretoeat.model.PreferenceTag
import com.akbarsya.wheretoeat.viewmodel.PreferencesChooserViewModel
import com.akbarsya.wheretoeat.viewmodel.ProfileViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PreferencesChooserActivity: BaseActivity<ActivityPreferencesChooserBinding>(
    ActivityPreferencesChooserBinding::inflate
) {
    private val viewModel: PreferencesChooserViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    private val adapter: PreferenceTagAdapter by lazy {
        PreferenceTagAdapter(::onTagSelected)
    }

    override fun init() {
        initializeRecyclerView()
        updateButtonState()
        observePreferenceTag()
        observeUpdateUser()

        viewModel.fetchTags()

        with(binding) {
            tvFinish.setOnClickListener {
                profileViewModel.updateUser(
                    adapter.items.filter {
                        it.isSelected
                    }
                )
            }
        }
    }

    private fun initializeRecyclerView() {
        with(binding) {
            val layoutManager = FlexboxLayoutManager(this@PreferencesChooserActivity)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START

            val itemDecorationHorizontal = FlexboxItemDecoration(this@PreferencesChooserActivity).apply {
                setDrawable(ContextCompat.getDrawable(this@PreferencesChooserActivity, R.drawable.bg_divider_6dp))
                setOrientation(FlexboxItemDecoration.HORIZONTAL)
            }

            val itemDecorationVertical = FlexboxItemDecoration(this@PreferencesChooserActivity).apply {
                setDrawable(ContextCompat.getDrawable(this@PreferencesChooserActivity, R.drawable.bg_divider_4dp))
                setOrientation(FlexboxItemDecoration.VERTICAL)
            }

            rvTags.adapter = adapter
            rvTags.layoutManager = layoutManager
            rvTags.setItemDecorations(itemDecorationHorizontal, itemDecorationVertical)
        }
    }

    private fun observePreferenceTag() {
        observe(viewModel.preferenceTagLiveData) {
            when(it.status) {
                ResourceState.SUCCESS -> {
                    adapter.updateItems(it.data ?: listOf())
                } else -> {
                    showShortToast("Failed / Loading")
                }
            }
        }
    }

    private fun observeUpdateUser() {
        observe(profileViewModel.updateUserLiveData) {
            when(it.status) {
                ResourceState.SUCCESS -> openMainActivity()
                else -> showShortToast("Failed / Loading")
            }
        }
    }

    private fun onTagSelected(selectedPreferenceTag: PreferenceTag) {
        val preferenceTags = adapter.items.map { preferenceTag ->
            if(selectedPreferenceTag == preferenceTag) {
                val updatedPreferenceTag = preferenceTag.copy()
                updatedPreferenceTag.isSelected = !selectedPreferenceTag.isSelected
                updatedPreferenceTag
            } else {
                preferenceTag
            }
        }

        adapter.updateItems(preferenceTags)
        updateButtonState()
    }

    private fun updateButtonState() {
        val selectedCount = adapter.items.count {
            it.isSelected
        }

        if(selectedCount >= 3) {
            binding.tvFinish.text = getString(R.string.finish_label)
            binding.tvFinish.isEnabled = true
        } else {
            binding.tvFinish.text = getString(R.string.personal_preferences_button_disabled, selectedCount, MIN_SELECTED_COUNT)
            binding.tvFinish.isEnabled = false
        }
    }

    private fun openMainActivity() {
        startActivity(Intent(this, WhereToEatActivity.HOME))
        finish()
    }

    companion object {
        private const val MIN_SELECTED_COUNT = 3
    }
}