package com.egraf.refapp.database.local.entities

import org.junit.Assert.*

import org.junit.Test

class TeamTest {

    @Test
    fun `Team setEntityName should return team with name`() {
        val name = "Команда"
        val team = Team().setEntityName(name)
        assertEquals(name, team.name)
    }
}