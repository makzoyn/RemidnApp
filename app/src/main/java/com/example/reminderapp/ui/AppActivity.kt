package com.example.reminderapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.reminderapp.R
import com.example.reminderapp.common.ToolbarController
import com.example.reminderapp.common.ToolbarControllerProvider
import com.example.reminderapp.common.base.BaseActivity
import com.example.reminderapp.common.extensions.listenValue
import com.example.reminderapp.common.util.DialogUtils
import com.example.reminderapp.databinding.ActivityAppBinding
import com.example.reminderapp.repository.PreferencesDataStoreRepository
import com.example.reminderapp.singleresult.NetworkErrorEvents
import com.example.reminderapp.singleresult.NetworkErrorResult
import com.example.reminderapp.singleresult.ReceiveMessageFromPushEvent
import com.example.reminderapp.singleresult.ReceiveMessageFromPushResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity: BaseActivity(R.layout.activity_app), ToolbarControllerProvider {
    private var navController : NavController? = null

    val viewModel: AppViewModel by viewModels<AppViewModelImpl>()
    private val binding by viewBinding(ActivityAppBinding::bind)

    @Inject
    lateinit var preferencesDataStoreRepository: PreferencesDataStoreRepository

    @Inject
    lateinit var receiveMessageFromPushResult: ReceiveMessageFromPushResult

    @Inject
    lateinit var networkErrorResult: NetworkErrorResult

    private var toolbarController: ToolbarController? = null
    override fun provideToolbarController(): ToolbarController? = toolbarController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        lifecycleScope.launch {
            subscribeOnDeleteMessageResult()
            subscribeOnNetworkErrorResult()
        }
        prepareToolbarController()
        observeViewModel()
    }

    private suspend fun subscribeOnNetworkErrorResult() {
        networkErrorResult.events.collect { event ->
            if(event is NetworkErrorEvents.ShowErrorDialog) {
                showMessage(message = event.message, title = event.title)
            }
        }
    }

    private fun observeViewModel() = with(viewModel) {
        navigationFlow.listenValue(::onNavigation)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    private fun onNavigation(navId: Int?) {
        navId?.let {
            navController?.navigate(navId)
        }
    }

    private fun showMessage(message: String, title: String) {
        runOnUiThread {
            val dialog = DialogUtils.createSimpleOkErrorDialog(this, title, message)
            dialog.setCancelable(false)
            dialog.show()
        }
    }
    private fun prepareToolbarController() {
        toolbarController = ToolbarController(binding.toolbar)
    }

    private suspend fun subscribeOnDeleteMessageResult() {
        receiveMessageFromPushResult.events.collect { event ->
            if(event is ReceiveMessageFromPushEvent.ReceivePushEvent) {
                viewModel.deleteRemindAfterPush(event.id)
            }
        }
    }

}