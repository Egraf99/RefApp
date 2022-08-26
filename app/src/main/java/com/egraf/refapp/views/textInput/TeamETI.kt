package com.egraf.refapp.views.textInput

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import com.egraf.refapp.GameRepository

class TeamETI(context: Context, attrs: AttributeSet? = null) :
    ETIWithEndButton(context, attrs) {
    private val gameRepository = GameRepository.get()
    private val teamsListLiveData = gameRepository.getTeams()

    fun init(lifecycleOwner: LifecycleOwner) {
        super.init()
        teamsListLiveData.observe(lifecycleOwner) {teams ->
           setEntities(teams)
        }
    }
}