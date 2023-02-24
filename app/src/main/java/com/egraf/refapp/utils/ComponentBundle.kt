package com.egraf.refapp.utils

import android.os.Bundle
import com.egraf.refapp.database.local.entities.*
import java.time.LocalDateTime
import java.util.*


private const val CREATED_TIME = "BundleCreatedTime"
private const val HOME_TEAM = "HomeTeam"
private const val GUEST_TEAM = "GuestTeam"
private const val STADIUM = "Stadium"
private const val LEAGUE = "League"
private const val DATE = "Date"
private const val TIME = "Time"
private const val PAY = "Pay"
private const val PASS = "Pass"
private const val CHIEF_REFEREE = "ChiefReferee"
private const val FIRST_ASSISTANT = "FirstAssistant"
private const val SECOND_ASSISTANT = "SecondAssistant"
private const val RESERVE_REFEREE = "ReserveReferee"
private const val INSPECTOR = "Inspector"

fun Bundle.putCurrentTime(): Bundle =
    this.apply { putSerializable(CREATED_TIME, LocalDateTime.now()) }
fun Bundle.getCreatedTime(): LocalDateTime =
    this.getSerializable(CREATED_TIME) as LocalDateTime? ?: LocalDateTime.MIN

fun Bundle.putDate(date: GameDate?): Bundle = this.apply { putParcelable(DATE, date) }
fun Bundle.getDate(): GameDate? = this.getParcelable(DATE)

fun Bundle.putTime(time: GameTime?): Bundle = this.apply { putParcelable(TIME, time) }
fun Bundle.getTime(): GameTime? = this.getParcelable(TIME)

fun Bundle.putHomeTeam(team: Team?): Bundle = this.apply { putParcelable(HOME_TEAM, team) }
fun Bundle.getHomeTeam(): Team? = this.getParcelable(HOME_TEAM)

fun Bundle.putGuestTeam(team: Team?): Bundle = this.apply { putParcelable(GUEST_TEAM, team) }
fun Bundle.getGuestTeam(): Team? = this.getParcelable(GUEST_TEAM)

fun Bundle.putLeague(league: League?): Bundle = this.apply { putParcelable(LEAGUE, league) }
fun Bundle.getLeague(): League? = this.getParcelable(LEAGUE)

fun Bundle.putStadium(stadium: Stadium?): Bundle = this.apply { putParcelable(STADIUM, stadium) }
fun Bundle.getStadium(): Stadium? = this.getParcelable(STADIUM)

fun Bundle.putChiefReferee(referee: Referee?): Bundle = this.apply { putParcelable(CHIEF_REFEREE, referee) }
fun Bundle.getChiefReferee(): Referee? = this.getParcelable(CHIEF_REFEREE)

fun Bundle.putFirstAssistant(referee: Referee?): Bundle = this.apply { putParcelable(FIRST_ASSISTANT, referee) }
fun Bundle.getFirstAssistant(): Referee? = this.getParcelable(FIRST_ASSISTANT)

fun Bundle.putSecondAssistant(referee: Referee?): Bundle = this.apply { putParcelable(SECOND_ASSISTANT, referee) }
fun Bundle.getSecondAssistant(): Referee? = this.getParcelable(SECOND_ASSISTANT)

fun Bundle.putReserveReferee(referee: Referee?): Bundle = this.apply { putParcelable(RESERVE_REFEREE, referee) }
fun Bundle.getReserveReferee(): Referee? = this.getParcelable(RESERVE_REFEREE)

fun Bundle.putInspector(referee: Referee?): Bundle = this.apply { putParcelable(INSPECTOR, referee) }
fun Bundle.getInspector(): Referee? = this.getParcelable(INSPECTOR)

fun Bundle.containPaidKey(): Boolean = this.containsKey(PAY)
fun Bundle.containPassKey(): Boolean = this.containsKey(PASS)
fun Bundle.putPaid(isPaid: Boolean): Bundle = this.apply { putBoolean(PAY, isPaid) }
fun Bundle.getPaid(): Boolean = this.getBoolean(PAY)
fun Bundle.putPassed(isPassed: Boolean): Bundle = this.apply { putBoolean(PASS, isPassed) }
fun Bundle.getPassed(): Boolean = this.getBoolean(PASS)
