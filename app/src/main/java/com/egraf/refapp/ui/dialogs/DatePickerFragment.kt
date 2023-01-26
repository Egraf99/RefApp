package com.egraf.refapp.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.egraf.refapp.database.local.entities.GameDateTime
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


private const val ARG_DATE = "date"
private const val ARG_REQUEST_CODE_DATE = "requestCodeDate"
private const val RESULT_DATE_KEY = "resultDate"


class DatePickerFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_DATE) as LocalDateTime
        val calendar = Calendar.getInstance()
        calendar.time = Date.from(date.atZone(ZoneId.systemDefault()).toInstant())

        val dateListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                }
                val result = Bundle().apply {
                    putSerializable(
                        RESULT_DATE_KEY,
                        LocalDateTime.ofInstant(calendar.time.toInstant(), ZoneId.systemDefault())
                    )
                }
                val resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE_DATE, "")
                setFragmentResult(resultRequestCode, result)
            }

        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    companion object {
        fun newInstance(date: GameDateTime, requestCode: String): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date.toLocalDateTime())
                putString(ARG_REQUEST_CODE_DATE, requestCode)
            }
            return DatePickerFragment().apply {
                arguments = args
            }
        }

        fun getSelectedDate(result: Bundle) =
            result.getSerializable(RESULT_DATE_KEY) as LocalDateTime
    }
}