package com.egraf.refapp.ui.dialogs.add_new_game.referee_choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Referee
import com.egraf.refapp.databinding.RefereeChooseBinding
import com.egraf.refapp.ui.dialogs.add_new_game.ChooserFragment
import com.egraf.refapp.ui.dialogs.add_new_game.Position
import com.egraf.refapp.ui.dialogs.entity_add_dialog.referee.AddRefereeDialogFragment
import com.egraf.refapp.ui.dialogs.entity_add_dialog.referee.InfoRefereeDialogFragment
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.SearchDialogFragment
import com.egraf.refapp.ui.dialogs.search_entity.SearchItem.Companion.randomId
import com.egraf.refapp.utils.close
import com.egraf.refapp.views.custom_views.GameComponent

class RefereeChooseFragment: ChooserFragment(), FragmentResultListener {
    private val binding get() = _binding!!
    private var _binding: RefereeChooseBinding? = null

    private val viewModel: RefereeChooseViewModel by viewModels()

    override fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle {
        return bundle.apply {
            putParcelable(
                CHIEF_REFEREE_VALUE,
                binding.chiefRefereeView.item
                    .getOrElse { Referee() } as Referee
            )
            putParcelable(
                FIRST_ASSISTANT_VALUE,
                binding.firstAssistantView.item
                    .getOrElse { Referee() } as Referee
            )
            putParcelable(
                SECOND_ASSISTANT_VALUE,
                binding.secondAssistantView.item
                    .getOrElse { Referee() } as Referee
            )
            putParcelable(
                RESERVE_REFEREE_VALUE,
                binding.reserveRefereeView.item
                    .getOrElse { Referee() } as Referee
            )
            putParcelable(
                INSPECTOR_VALUE,
                binding.inspectorView.item
                    .getOrElse { Referee() } as Referee
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

    override fun showNextFragment() {}

    override fun showPreviousFragment() {
        putComponentsInArguments()
        findNavController().popBackStack()
    }

    override val nextPosition: Position = Position.DISMISS
    override val previousPosition: Position = Position.MIDDLE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (request in listOf(
            REQUEST_SEARCH_CHIEF_REFEREE,
            REQUEST_ADD_CHIEF_REFEREE,
            REQUEST_SEARCH_FIRST_ASSISTANT,
            REQUEST_ADD_FIRST_ASSISTANT,
            REQUEST_SEARCH_SECOND_ASSISTANT,
            REQUEST_ADD_SECOND_ASSISTANT,
            REQUEST_SEARCH_RESERVE_REFEREE,
            REQUEST_ADD_RESERVE_REFEREE,
            REQUEST_SEARCH_INSPECTOR,
            REQUEST_ADD_INSPECTOR
        ))
            parentFragmentManager.setFragmentResultListener(request, viewLifecycleOwner, this)
    }

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
        binding.chiefRefereeView.apply {
            setOnClickListener {
                SearchDialogFragment(
                    this.title, this.icon,
                    receiveSearchItems = { GameRepository.get().getReferees() },
                    request = REQUEST_SEARCH_CHIEF_REFEREE
                ).show(parentFragmentManager, FRAGMENT_SEARCH_CHIEF_REFEREE)
            }
            setOnInfoClickListener {
                InfoRefereeDialogFragment(
                    this.title,
                    componentId = (this.item.getOrThrow(IllegalStateException("Info button shouldn't be able when GameComponentView don't have item")) as Referee).savedValue
                ).show(parentFragmentManager, FRAGMENT_INFO_CHIEF_REFEREE)
            }
        }
        binding.firstAssistantView.apply {
            setOnClickListener {
                SearchDialogFragment(
                    this.title, this.icon,
                    receiveSearchItems = { GameRepository.get().getReferees() },
                    request = REQUEST_SEARCH_FIRST_ASSISTANT
                ).show(parentFragmentManager, FRAGMENT_SEARCH_FIRST_ASSISTANT)
            }
            setOnInfoClickListener {
                InfoRefereeDialogFragment(
                    this.title,
                    componentId = (this.item.getOrThrow(IllegalStateException("Info button shouldn't be able when GameComponentView don't have item")) as Referee).savedValue
                ).show(parentFragmentManager, FRAGMENT_INFO_FIRST_ASSISTANT)
            }
        }
        binding.secondAssistantView.apply {
            setOnClickListener {
                SearchDialogFragment(
                    this.title, this.icon,
                    receiveSearchItems = { GameRepository.get().getReferees() },
                    request = REQUEST_SEARCH_SECOND_ASSISTANT
                ).show(parentFragmentManager, FRAGMENT_SEARCH_SECOND_ASSISTANT)
            }
            setOnInfoClickListener {
                InfoRefereeDialogFragment(
                    this.title,
                    componentId = (this.item.getOrThrow(IllegalStateException("Info button shouldn't be able when GameComponentView don't have item")) as Referee).savedValue
                ).show(parentFragmentManager, FRAGMENT_INFO_SECOND_ASSISTANT)
            }
        }
        binding.reserveRefereeView.apply {
            setOnClickListener {
                SearchDialogFragment(
                    this.title, this.icon,
                    receiveSearchItems = { GameRepository.get().getReferees() },
                    request = REQUEST_SEARCH_RESERVE_REFEREE
                ).show(parentFragmentManager, FRAGMENT_SEARCH_RESERVE_REFEREE)
            }
            setOnInfoClickListener {
                InfoRefereeDialogFragment(
                    this.title,
                    componentId = (this.item.getOrThrow(IllegalStateException("Info button shouldn't be able when GameComponentView don't have item")) as Referee).savedValue
                ).show(parentFragmentManager, FRAGMENT_INFO_RESERVE_REFEREE)
            }
        }
        binding.inspectorView.apply {
            setOnClickListener {
                SearchDialogFragment(
                    this.title, this.icon,
                    receiveSearchItems = { GameRepository.get().getReferees() },
                    request = REQUEST_SEARCH_INSPECTOR
                ).show(parentFragmentManager, FRAGMENT_SEARCH_INSPECTOR)
            }
            setOnInfoClickListener {
                InfoRefereeDialogFragment(
                    this.title,
                    componentId = (this.item.getOrThrow(IllegalStateException("Info button shouldn't be able when GameComponentView don't have item")) as Referee).savedValue
                ).show(parentFragmentManager, FRAGMENT_INFO_INSPECTOR)
            }
        }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_SEARCH_CHIEF_REFEREE -> {
                val item = GameComponent(
                    Referee(
                        SearchDialogFragment.getTitle(result),
                        SearchDialogFragment.getId(result),
                        bySpace = true
                    )
                ).filter { it.id != EmptyItem.id }
                when (SearchDialogFragment.getTypeOfResult(result)) {
                    SearchDialogFragment.Companion.ResultRequest.SEARCH_ITEM_RESULT_REQUEST -> {
                        binding.chiefRefereeView.item = item
                        parentFragmentManager.close(FRAGMENT_SEARCH_CHIEF_REFEREE)
                    }
                    SearchDialogFragment.Companion.ResultRequest.INFO_RESULT_REQUEST -> {
                        InfoRefereeDialogFragment(
                            title = getString(R.string.referee),
                            componentId = SearchDialogFragment.getId(result),
                        ).show(parentFragmentManager, FRAGMENT_INFO_CHIEF_REFEREE)
                    }
                    SearchDialogFragment.Companion.ResultRequest.ADD_RESULT_REQUEST -> {
                        AddRefereeDialogFragment(
                            title = getString(R.string.add_referee),
                            referee= Referee(SearchDialogFragment.getTitle(result), randomId(), bySpace = true),
                            request = REQUEST_ADD_CHIEF_REFEREE,
                            functionSaveEntityInDB = viewModel.addRefereeToDB
                        ).show(parentFragmentManager, FRAGMENT_ADD_CHIEF_REFEREE)
                    }
                }
            }
            REQUEST_ADD_CHIEF_REFEREE -> {
                parentFragmentManager.close(
                    FRAGMENT_SEARCH_CHIEF_REFEREE,
                    FRAGMENT_ADD_CHIEF_REFEREE
                )
                binding.chiefRefereeView.item =
                    GameComponent(
                        Referee(
                            AddRefereeDialogFragment.getTitle(result),
                            AddRefereeDialogFragment.getId(result),
                            bySpace = true
                        )
                    )
            }
            REQUEST_SEARCH_FIRST_ASSISTANT -> {
                val item = GameComponent(
                    Referee(
                        SearchDialogFragment.getTitle(result),
                        SearchDialogFragment.getId(result),
                        bySpace = true
                    )
                ).filter { it.id != EmptyItem.id }
                when (SearchDialogFragment.getTypeOfResult(result)) {
                    SearchDialogFragment.Companion.ResultRequest.SEARCH_ITEM_RESULT_REQUEST -> {
                        binding.firstAssistantView.item = item
                        parentFragmentManager.close(FRAGMENT_SEARCH_FIRST_ASSISTANT)
                    }
                    SearchDialogFragment.Companion.ResultRequest.INFO_RESULT_REQUEST -> {
                        InfoRefereeDialogFragment(
                            title = getString(R.string.referee),
                            componentId = SearchDialogFragment.getId(result),
                        ).show(parentFragmentManager, FRAGMENT_INFO_FIRST_ASSISTANT)
                    }
                    SearchDialogFragment.Companion.ResultRequest.ADD_RESULT_REQUEST -> {
                        AddRefereeDialogFragment(
                            title = getString(R.string.add_referee),
                            referee= Referee(SearchDialogFragment.getTitle(result), randomId(), bySpace = true),
                            request = REQUEST_ADD_FIRST_ASSISTANT,
                            functionSaveEntityInDB = viewModel.addRefereeToDB
                        ).show(parentFragmentManager, FRAGMENT_ADD_FIRST_ASSISTANT)
                    }
                }
            }
            REQUEST_ADD_FIRST_ASSISTANT -> {
                parentFragmentManager.close(
                    FRAGMENT_SEARCH_FIRST_ASSISTANT,
                    FRAGMENT_ADD_FIRST_ASSISTANT
                )
                binding.firstAssistantView.item =
                    GameComponent(
                        Referee(
                            AddRefereeDialogFragment.getTitle(result),
                            AddRefereeDialogFragment.getId(result),
                            bySpace = true
                        )
                    )
            }
            REQUEST_SEARCH_SECOND_ASSISTANT -> {
                val item = GameComponent(
                    Referee(
                        SearchDialogFragment.getTitle(result),
                        SearchDialogFragment.getId(result),
                        bySpace = true
                    )
                ).filter { it.id != EmptyItem.id }
                when (SearchDialogFragment.getTypeOfResult(result)) {
                    SearchDialogFragment.Companion.ResultRequest.SEARCH_ITEM_RESULT_REQUEST -> {
                        binding.secondAssistantView.item = item
                        parentFragmentManager.close(FRAGMENT_SEARCH_SECOND_ASSISTANT)
                    }
                    SearchDialogFragment.Companion.ResultRequest.INFO_RESULT_REQUEST -> {
                        InfoRefereeDialogFragment(
                            title = getString(R.string.referee),
                            componentId = SearchDialogFragment.getId(result),
                        ).show(parentFragmentManager, FRAGMENT_INFO_SECOND_ASSISTANT)
                    }
                    SearchDialogFragment.Companion.ResultRequest.ADD_RESULT_REQUEST -> {
                        AddRefereeDialogFragment(
                            title = getString(R.string.add_referee),
                            referee= Referee(SearchDialogFragment.getTitle(result), randomId(), bySpace = true),
                            request = REQUEST_ADD_SECOND_ASSISTANT,
                            functionSaveEntityInDB = viewModel.addRefereeToDB
                        ).show(parentFragmentManager, FRAGMENT_ADD_SECOND_ASSISTANT)
                    }
                }
            }
            REQUEST_ADD_SECOND_ASSISTANT -> {
                parentFragmentManager.close(
                    FRAGMENT_SEARCH_SECOND_ASSISTANT,
                    FRAGMENT_ADD_SECOND_ASSISTANT
                )
                binding.secondAssistantView.item =
                    GameComponent(
                        Referee(
                            AddRefereeDialogFragment.getTitle(result),
                            AddRefereeDialogFragment.getId(result),
                            bySpace = true
                        )
                    )
            }
            REQUEST_SEARCH_RESERVE_REFEREE -> {
                val item = GameComponent(
                    Referee(
                        SearchDialogFragment.getTitle(result),
                        SearchDialogFragment.getId(result),
                        bySpace = true
                    )
                ).filter { it.id != EmptyItem.id }
                when (SearchDialogFragment.getTypeOfResult(result)) {
                    SearchDialogFragment.Companion.ResultRequest.SEARCH_ITEM_RESULT_REQUEST -> {
                        binding.reserveRefereeView.item = item
                        parentFragmentManager.close(FRAGMENT_SEARCH_RESERVE_REFEREE)
                    }
                    SearchDialogFragment.Companion.ResultRequest.INFO_RESULT_REQUEST -> {
                        InfoRefereeDialogFragment(
                            title = getString(R.string.referee),
                            componentId = SearchDialogFragment.getId(result),
                        ).show(parentFragmentManager, FRAGMENT_INFO_RESERVE_REFEREE)
                    }
                    SearchDialogFragment.Companion.ResultRequest.ADD_RESULT_REQUEST -> {
                        AddRefereeDialogFragment(
                            title = getString(R.string.add_referee),
                            referee= Referee(SearchDialogFragment.getTitle(result), randomId(), bySpace = true),
                            request = REQUEST_ADD_RESERVE_REFEREE,
                            functionSaveEntityInDB = viewModel.addRefereeToDB
                        ).show(parentFragmentManager, FRAGMENT_ADD_RESERVE_REFEREE)
                    }
                }
            }
            REQUEST_ADD_RESERVE_REFEREE -> {
                parentFragmentManager.close(
                    FRAGMENT_SEARCH_RESERVE_REFEREE,
                    FRAGMENT_ADD_RESERVE_REFEREE
                )
                binding.reserveRefereeView.item =
                    GameComponent(
                        Referee(
                            AddRefereeDialogFragment.getTitle(result),
                            AddRefereeDialogFragment.getId(result),
                            bySpace = true
                        )
                    )
            }
            REQUEST_SEARCH_INSPECTOR -> {
                val item = GameComponent(
                    Referee(
                        SearchDialogFragment.getTitle(result),
                        SearchDialogFragment.getId(result),
                        bySpace = true
                    )
                ).filter { it.id != EmptyItem.id }
                when (SearchDialogFragment.getTypeOfResult(result)) {
                    SearchDialogFragment.Companion.ResultRequest.SEARCH_ITEM_RESULT_REQUEST -> {
                        binding.inspectorView.item = item
                        parentFragmentManager.close(FRAGMENT_SEARCH_INSPECTOR)
                    }
                    SearchDialogFragment.Companion.ResultRequest.INFO_RESULT_REQUEST -> {
                        InfoRefereeDialogFragment(
                            title = getString(R.string.referee),
                            componentId = SearchDialogFragment.getId(result),
                        ).show(parentFragmentManager, FRAGMENT_INFO_INSPECTOR)
                    }
                    SearchDialogFragment.Companion.ResultRequest.ADD_RESULT_REQUEST -> {
                        AddRefereeDialogFragment(
                            title = getString(R.string.add_referee),
                            referee= Referee(SearchDialogFragment.getTitle(result), randomId(), bySpace = true),
                            request = REQUEST_ADD_INSPECTOR,
                            functionSaveEntityInDB = viewModel.addRefereeToDB
                        ).show(parentFragmentManager, FRAGMENT_ADD_INSPECTOR)
                    }
                }
            }
            REQUEST_ADD_INSPECTOR -> {
                parentFragmentManager.close(
                    FRAGMENT_SEARCH_INSPECTOR,
                    FRAGMENT_ADD_INSPECTOR
                )
                binding.inspectorView.item =
                    GameComponent(
                        Referee(
                            AddRefereeDialogFragment.getTitle(result),
                            AddRefereeDialogFragment.getId(result),
                            bySpace = true
                        )
                    )
            }
        }
    }

    companion object {
        private const val REQUEST_SEARCH_CHIEF_REFEREE = "RequestChiefReferee"
        private const val REQUEST_ADD_CHIEF_REFEREE = "RequestAddChiefReferee"
        private const val REQUEST_SEARCH_FIRST_ASSISTANT = "RequestFirstReferee"
        private const val REQUEST_ADD_FIRST_ASSISTANT = "RequestAddFirstReferee"
        private const val REQUEST_SEARCH_SECOND_ASSISTANT = "RequestSecondReferee"
        private const val REQUEST_ADD_SECOND_ASSISTANT = "RequestAddSecondReferee"
        private const val REQUEST_SEARCH_RESERVE_REFEREE = "RequestReserveReferee"
        private const val REQUEST_ADD_RESERVE_REFEREE = "RequestAddReserveReferee"
        private const val REQUEST_SEARCH_INSPECTOR = "RequestInspector"
        private const val REQUEST_ADD_INSPECTOR = "RequestAddInspector"

        private const val FRAGMENT_SEARCH_CHIEF_REFEREE = "FragmentSearchChiefReferee"
        private const val FRAGMENT_ADD_CHIEF_REFEREE = "FragmentAddChiefReferee"
        private const val FRAGMENT_INFO_CHIEF_REFEREE = "FragmentAddChiefReferee"
        private const val FRAGMENT_SEARCH_FIRST_ASSISTANT = "FragmentSearchFirstReferee"
        private const val FRAGMENT_ADD_FIRST_ASSISTANT = "FragmentAddFirstReferee"
        private const val FRAGMENT_INFO_FIRST_ASSISTANT = "FragmentAddFirstReferee"
        private const val FRAGMENT_SEARCH_SECOND_ASSISTANT = "FragmentSearchSecondReferee"
        private const val FRAGMENT_ADD_SECOND_ASSISTANT = "FragmentAddSecondReferee"
        private const val FRAGMENT_INFO_SECOND_ASSISTANT = "FragmentAddSecondReferee"
        private const val FRAGMENT_SEARCH_RESERVE_REFEREE = "FragmentSearchReserveReferee"
        private const val FRAGMENT_ADD_RESERVE_REFEREE = "FragmentAddReserveReferee"
        private const val FRAGMENT_INFO_RESERVE_REFEREE = "FragmentAddReserveReferee"
        private const val FRAGMENT_SEARCH_INSPECTOR = "FragmentSearchInspector"
        private const val FRAGMENT_ADD_INSPECTOR = "FragmentAddInspector"
        private const val FRAGMENT_INFO_INSPECTOR = "FragmentAddInspector"
    }
}