package com.egraf.refapp.ui.dialogs.add_new_game.referee_choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.database.local.entities.Referee
import com.egraf.refapp.databinding.RefereeChooseBinding
import com.egraf.refapp.ui.dialogs.add_new_game.ChooserFragment
import com.egraf.refapp.ui.dialogs.add_new_game.Position
import com.egraf.refapp.views.custom_views.GameComponent

class RefereeChooseFragment : ChooserFragment() {
    private val binding get() = _binding!!
    private var _binding: RefereeChooseBinding? = null

//    private val viewModel: RefereeChooseViewModel by viewModels()

    override fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle {
        return super.putGameComponentsInSavedBundle(bundle).apply {
            putParcelable(
                CHIEF_REFEREE_VALUE,
                binding.chiefRefereeView.item.getOrElse { Referee() }
            )
            putParcelable(
                FIRST_ASSISTANT_VALUE,
                binding.firstAssistantView.item.getOrElse { Referee() }
            )
            putParcelable(
                SECOND_ASSISTANT_VALUE,
                binding.secondAssistantView.item.getOrElse { Referee() }
            )
            putParcelable(
                RESERVE_REFEREE_VALUE,
                binding.reserveRefereeView.item.getOrElse { Referee() }
            )
            putParcelable(
                INSPECTOR_VALUE,
                binding.inspectorView.item.getOrElse { Referee() }
            )
        }
    }

    override fun getGameComponentsFromSavedBundle(bundle: Bundle) {
        binding.chiefRefereeView.item =
            GameComponent(bundle.getParcelable<Referee>(CHIEF_REFEREE_VALUE)).filter { !it.isEmpty }

        binding.firstAssistantView.item =
            GameComponent(bundle.getParcelable<Referee>(FIRST_ASSISTANT_VALUE)).filter { !it.isEmpty }

        binding.secondAssistantView.item =
            GameComponent(bundle.getParcelable<Referee>(SECOND_ASSISTANT_VALUE)).filter { !it.isEmpty }

        binding.reserveRefereeView.item =
            GameComponent(bundle.getParcelable<Referee>(RESERVE_REFEREE_VALUE)).filter { !it.isEmpty }

        binding.inspectorView.item =
            GameComponent(bundle.getParcelable<Referee>(INSPECTOR_VALUE)).filter { !it.isEmpty }
    }

    override fun showNextFragment() {
        putComponentsInArguments()
        val game = getGameFromBundle(bundle)
        addGameToDB(game)
    }

    override fun showPreviousFragment() {
        putComponentsInArguments()
        findNavController().popBackStack()
    }

    override val nextPosition: Position = Position.DISMISS
    override val previousPosition: Position = Position.MIDDLE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RefereeChooseBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        binding.chiefRefereeView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.firstAssistantView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.secondAssistantView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.reserveRefereeView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
        binding.inspectorView.bind(
            this.parentFragmentManager, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope
        )
    }
}