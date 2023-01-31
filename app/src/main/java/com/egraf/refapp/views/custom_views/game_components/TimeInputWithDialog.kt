package com.egraf.refapp.views.custom_views.game_components

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.egraf.refapp.database.local.entities.GameDateTime
import com.egraf.refapp.database.local.entities.GameTime
import com.egraf.refapp.ui.dialogs.TimePickerFragment
import com.egraf.refapp.views.custom_views.GameComponent
import com.egraf.refapp.views.custom_views.GameComponentView
import kotlinx.coroutines.CoroutineScope
import java.time.LocalTime

class TimeInputWithDialog(context: Context, attrs: AttributeSet) :
    GameComponentView<LocalTime, GameTime>(context, attrs),
    FragmentResultListener {
    private lateinit var fragmentManager: FragmentManager
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var onUpdateTime: (GameTime) -> Unit
    fun bind(
        fragmentManager: FragmentManager,
        lifecycleOwner: LifecycleOwner,
        coroutineScope: CoroutineScope,
        onUpdateTime: (GameTime) -> Unit = {},
    ) {
        this.fragmentManager = fragmentManager
        this.coroutineScope = coroutineScope
        this.onUpdateTime = onUpdateTime
        this.fragmentManager.setFragmentResultListener(
            REQUEST_INPUT_TIME, lifecycleOwner, this
        )
        this.setOnClickListener {
            TimePickerFragment
                .newInstance(GameDateTime(), REQUEST_INPUT_TIME)
                .show(fragmentManager, FRAGMENT_TIME)
        }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_INPUT_TIME -> {
                val date = GameTime(TimePickerFragment.getSelectedTime(result).toLocalTime())
                this.item =
                    GameComponent(date)
                onUpdateTime(date)
            }
        }
    }

    companion object {
        private const val REQUEST_INPUT_TIME = "DialogInputTime"
        private const val FRAGMENT_TIME = "FragmentDialogTime"
    }
}
