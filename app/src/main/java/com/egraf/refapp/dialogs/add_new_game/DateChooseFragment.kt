package com.egraf.refapp.dialogs.add_new_game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.databinding.DateChooseBinding

private const val TAG = "DateChooseFragment"
class DateChooseFragment: Fragment() {
    private val binding get() = _binding!!
    private val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this).get(AddNewGameViewModel::class.java)
    }
    private var _binding: DateChooseBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: create")
        _binding = DateChooseBinding.inflate(inflater).apply {
            dateChoose.text = "24.04"
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}