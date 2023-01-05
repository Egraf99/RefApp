package com.egraf.refapp.database.entities

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RefereeTest {
    @Test
    fun `Referee("") should return EmptyReferee`() {
        assertThat(Referee("").isEmpty).isTrue()
    }

    @Test
    fun `Referee() should return EmptyReferee`() {
        assertThat(Referee().isEmpty).isTrue()
    }

    @Test
    fun `Referee("Ivanov Ivan") should return EmptyReferee`() {
        val referee = Referee("f_Ivan_ m_Ivanov_")
        assertThat(referee.firstName).isEqualTo("Ivan")
        assertThat(referee.middleName).isEqualTo("Ivanov")
        assertThat(referee.lastName).isEqualTo("")
    }

    @Test
    fun `Referee("f_Ivan_ m_Ivanov_ l_Ivanovich_") should return Referee(fistName = "Ivan")`() {
        val referee = Referee("f_Ivan_ m_Ivanov_ l_Ivanovich_")
        assertThat(referee.firstName).isEqualTo("Ivan")
        assertThat(referee.middleName).isEqualTo("Ivanov")
        assertThat(referee.lastName).isEqualTo("Ivanovich")
    }

    @Test
    fun `Referee(" m_Ivanov_ l_Ivanovich_ f_Ivan_ ") should return Referee(fistName = "Ivan")`() {
        val referee = Referee(" m_Ivanov_ l_Ivanovich_ f_Ivan_ ")
        assertThat(referee.firstName).isEqualTo("Ivan")
        assertThat(referee.middleName).isEqualTo("Ivanov")
        assertThat(referee.lastName).isEqualTo("Ivanovich")
    }

    @Test
    fun `Referee("f_Ivan_ Ivanov l_Ivanovich_") should return Referee(fistName = "Ivan")`() {
        val referee = Referee("f_Ivan_ Ivanov l_Ivanovich_")
        assertThat(referee.firstName).isEqualTo("Ivan")
        assertThat(referee.middleName).isEqualTo("")
        assertThat(referee.lastName).isEqualTo("Ivanovich")
    }

    @Test
    fun `Referee("m_Ivanov_") should return Referee(middleName = "Ivanov")`() {
        val referee = Referee("m_Ivanov_")
        assertThat(referee.firstName).isEqualTo("")
        assertThat(referee.middleName).isEqualTo("Ivanov")
        assertThat(referee.lastName).isEqualTo("")
    }

    @Test
    fun `Referee("f_Ivan_ m_Ivanov_") should return Referee(fistName = "Ivan")`() {
        val referee = Referee("f_Ivan_ m_Ivanov_")
        assertThat(referee.firstName).isEqualTo("Ivan")
        assertThat(referee.middleName).isEqualTo("Ivanov")
        assertThat(referee.lastName).isEqualTo("")
    }
}