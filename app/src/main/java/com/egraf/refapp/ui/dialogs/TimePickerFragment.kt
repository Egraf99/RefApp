package com.egraf.refapp.ui.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.egraf.refapp.database.entities.GameDateTime
import com.egraf.refapp.database.entities.GameTime
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

private const val TAG = "TimePicker"
private const val ARG_TIME = "time"
private const val RESULT_DATE_KEY = "resultDate"
private const val ARG_REQUEST_CODE_TIME = "requestCodeTime"

class TimePickerFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_TIME) as LocalDateTime
        val calendar = Calendar.getInstance()
        calendar.time = Date.from(date.atZone(ZoneId.systemDefault()).toInstant())

        val timeListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->
                calendar.apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                val bundle = Bundle().apply {
                    putSerializable(
                        RESULT_DATE_KEY,
                        LocalDateTime.ofInstant(calendar.time.toInstant(), ZoneId.systemDefault())
                    )
                }
                val resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE_TIME, "")
                setFragmentResult(resultRequestCode, bundle)
            }

        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(
            requireContext(),
            timeListener,
            initialHour,
            initialMinute,
            true
        )
    }

    companion object {
        fun newInstance(date: GameDateTime, requestCode: String): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, date.toLocalDateTime())
                putString(ARG_REQUEST_CODE_TIME, requestCode)
            }
            return TimePickerFragment().apply {
                arguments = args
            }
        }

        fun getSelectedTime(result: Bundle) = result.getSerializable(RESULT_DATE_KEY) as LocalDateTime
    }
}