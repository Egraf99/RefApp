package com.egraf.refapp.database.entities

import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class RefereeTest {
    private data class Name(
        val firstName: String = "FirstName",
        val secondName: String = "SecondName",
        val thirdName: String = "ThirdName"
    )

    private var name = Name()
    private lateinit var referee: Referee

    @Before
    fun setUp() {
        referee = Referee().setEntityName("${name.secondName} ${name.firstName} ${name.thirdName}")
    }

    @Test
    fun `setEntityName должна вернуть судью с правильным именем`() {
        assertEquals(
            "${referee.firstName} not equals to ${name.firstName}",
            referee.firstName, name.firstName
        )
    }

    @Test
    fun `setEntityName должна вернуть судью с правильной фамилией`() {
        assertEquals(
            "${referee.secondName} not equals to ${name.secondName}",
            referee.secondName, name.secondName
        )
    }

    @Test
    fun `setEntityName должна вернуть судью с правильным отчеством`() {
        assertEquals(
            "${referee.thirdName} not equals to ${name.thirdName}",
            referee.thirdName, name.thirdName
        )
    }

    @Test
    fun `shortName возвращает фамилию и имя`() {
        assertEquals(referee.shortName, "${name.secondName} ${name.firstName}")
    }

    @Test
    fun `fullName возвращает фамилию имя и отчество`() {
        assertEquals(referee.fullName, "${name.secondName} ${name.firstName} ${name.thirdName}")
    }
}