package com.egraf.refapp.database.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egraf.refapp.database.entities.*
import java.util.*

class FakeGameDataSource : GameDataSource {
    private val games = mutableListOf<GameWithAttributes>()
    private val teams = mutableListOf(Team(name = "Команда"))
    private val stadiums = mutableListOf(Stadium(name = "Стадион"))
    private val leagues = mutableListOf(League(name = "Лига"))
    private val referees = mutableListOf(Referee(firstName = "Имя", secondName = "Фамилия", thirdName = "Отчество"))

    // games block
    override fun getGames(): LiveData<List<GameWithAttributes>> = MutableLiveData(games)
    override fun countGames(): LiveData<Int> = MutableLiveData(games.count())
    override fun getGame(id: UUID): LiveData<GameWithAttributes?> {
        games.forEach { game -> if (game.game.id == id) return MutableLiveData(game) }
        return MutableLiveData(null)
    }

    override fun updateGame(game: Game) {
        games.forEachIndexed { index, gameWithAttributes ->
            if (gameWithAttributes.game.id == game.id) {
                games.removeAt(index)
                games.add(index, gameWithAttributes)
            }
        }
    }

    override fun addGame(game: Game) {
        games.add(GameWithAttributes(game = game))
    }

    override fun deleteGame(game: Game) {
        games.forEachIndexed { index, gameWithAttributes ->
            if (gameWithAttributes.game.id == game.id) {
                games.removeAt(index)
            }
        }
    }

    // stadium block
    override fun addStadium(stadium: Stadium) {
        stadiums.add(stadium)
    }
    override fun getStadiums(): LiveData<List<Stadium>> = MutableLiveData(stadiums)
    override fun getStadium(id: UUID): LiveData<Stadium?> {
        stadiums.forEach { stadium -> if (stadium.id == id) return MutableLiveData(stadium) }
        return MutableLiveData(null)
    }
    override fun updateStadium(stadium: Stadium) {
        stadiums.forEachIndexed { index, stadium_ ->
            if (stadium_.id == stadium.id) {
                stadiums.removeAt(index)
                stadiums.add(index, stadium)
            }
        }
    }

    // league block
    override fun getLeagues(): LiveData<List<League>> = MutableLiveData(leagues)
    override fun addLeague(league: League) {
        leagues.add(league)
    }
    override fun updateLeague(league: League) {
        leagues.forEachIndexed { index, league_ ->
            if (league_.id == league.id) {
                leagues.removeAt(index)
                leagues.add(index, league)
            }
        }
    }

    // team block
    override fun getTeams(): LiveData<List<Team>> = MutableLiveData(teams)
    override fun deleteTeam(team: Team) {
        teams.forEachIndexed { index, team_ ->
            if (team_.id == team.id) {
                teams.removeAt(index)
            }
        }
    }

    override fun addTeam(team: Team) {
        teams.add(team)
    }

    // referee block
    override fun getReferees(): LiveData<List<Referee>> = MutableLiveData(referees)
    override fun addReferee(referee: Referee) {
        referees.add(referee)
    }
}