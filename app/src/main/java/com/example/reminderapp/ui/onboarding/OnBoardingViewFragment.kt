package com.example.reminderapp.ui.onboarding

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.reminderapp.R
import com.example.reminderapp.common.base.BaseFragment
import com.example.reminderapp.common.extensions.listenValue
import com.example.reminderapp.databinding.FragmentViewOnBoardingBinding
import com.example.reminderapp.domain.model.OnBoardingItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class OnBoardingViewFragment: BaseFragment(R.layout.fragment_view_on_boarding) {
    private val binding by viewBinding(FragmentViewOnBoardingBinding::bind)
    private val viewModel: OnBoardingViewViewModel by viewModels<OnBoardingViewViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        Log.d("CreateFragment", "create")
    }

    private fun observeViewModel() = with(viewModel) {
        screenData.listenValue(::bindUi)
    }

    private fun bindUi(data: OnBoardingItem?) {
        binding.description.text = data?.text
        data?.imageId?.let {
            binding.image.setImageResource(data.imageId)
        }
    }

    @Parcelize
    data class Param(
        val id: Int,
        val imageId: Int,
        val text: String
    ): Parcelable
}