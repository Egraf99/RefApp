package com.egraf.refapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.egraf.refapp.R

private const val TAG = "DeleteDialog"
private const val RESULT_DELETE_KEY = "DeleteRequest"
private const val ARG_REQUEST_CODE_DELETE = "requestCodeDate"

class DeleteDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val positiveButtonListener =
            DialogInterface.OnClickListener { _: DialogInterface, answer: Int ->
                val result = Bundle().apply { putInt(RESULT_DELETE_KEY, answer) }
                val resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE_DELETE, "")
                Log.d(TAG, AlertDialog.BUTTON_POSITIVE.toString())
                Log.d(TAG, answer.toString())
                setFragmentResult(resultRequestCode, result)
            }

        val neutralButtonListener =
            DialogInterface.OnClickListener { _: DialogInterface, answer: Int ->
                val result = Bundle().apply { putInt(RESULT_DELETE_KEY, answer) }
                val resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE_DELETE, "")
                Log.d(TAG, AlertDialog.BUTTON_NEUTRAL.toString())
                Log.d(TAG, answer.toString())
                setFragmentResult(resultRequestCode, result)
            }

        val alertDialog = AlertDialog.Builder(activity)
            .setTitle(R.string.warning)
            .setMessage(R.string.warning_delete_text)
            .setPositiveButton(R.string.delete, positiveButtonListener)
            .setNeutralButton(R.string.cancel, neutralButtonListener)

        return alertDialog.create()
    }

    companion object {
        fun newInstance(requestCode: String): DeleteDialog {
            val args = Bundle().apply {
                putString(ARG_REQUEST_CODE_DELETE, requestCode)
            }
            return DeleteDialog().apply { arguments = args }
        }

        fun getDeleteAnswer(result: Bundle): Int {
            return result.getInt(RESULT_DELETE_KEY)
        }
    }
}