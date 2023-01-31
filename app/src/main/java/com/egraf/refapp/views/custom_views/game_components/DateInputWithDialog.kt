package com.egraf.refapp.views.custom_views.game_components

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.egraf.refapp.database.local.entities.GameDate
import com.egraf.refapp.database.local.entities.GameDateTime
import com.egraf.refapp.ui.dialogs.DatePickerFragment
import com.egraf.refapp.ui.dialogs.add_new_game.stadium_choose.StadiumChooseFragment
import com.egraf.refapp.views.custom_views.GameComponent
import com.egraf.refapp.views.custom_views.GameComponentView
import com.egraf.refapp.views.custom_views.GameComponentViewWithIcon
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate
import java.util.*

class DateInputWithDialog(context: Context, attrs: AttributeSet) :
    GameComponentView<LocalDate, GameDate>(context, attrs),
    FragmentResultListener {
    private lateinit var fragmentManager: FragmentManager
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var onUpdateDate: (GameDate) -> Unit
    fun bind(
        fragmentManager: FragmentManager,
        lifecycleOwner: LifecycleOwner,
        coroutineScope: CoroutineScope,
        onUpdateDate: (GameDate) -> Unit = {},
    ) {
        this.fragmentManager = fragmentManager
        this.coroutineScope = coroutineScope
        this.onUpdateDate = onUpdateDate
        this.fragmentManager.setFragmentResultListener(
            REQUEST_INPUT_DATE, lifecycleOwner, this
        )
        this.setOnClickListener {
            DatePickerFragment
                .newInstance(GameDateTime(), REQUEST_INPUT_DATE)
                .show(fragmentManager, FRAGMENT_DATE)
        }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_INPUT_DATE -> {
                val date = GameDate(DatePickerFragment.getSelectedDate(result).toLocalDate())
                this.item =
                    GameComponent(date)
                onUpdateDate(date)
            }
        }
    }

    companion object {
        private const val REQUEST_INPUT_DATE = "DialogInputDate"
        private const val FRAGMENT_DATE = "FragmentDialogDate"
    }
}