package com.egraf.refapp.dialogs.add_new_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.databinding.TeamChooseBinding
import com.egraf.refapp.listeners.TeamListener

class TeamChooseFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: TeamChooseBinding? = null
    private val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this)[AddNewGameViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TeamChooseBinding.inflate(inflater).apply {
            teamHomeLayout.init(
                this@TeamChooseFragment,
                addNewGameViewModel
            )
            teamGuestLayout.init(
                this@TeamChooseFragment,
                addNewGameViewModel
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listener = TeamListener(addNewGameViewModel)
        parentFragmentManager.setFragmentResultListener(
            TeamListener.REQUEST_ADD_TEAM_IN_DB, viewLifecycleOwner, listener
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}