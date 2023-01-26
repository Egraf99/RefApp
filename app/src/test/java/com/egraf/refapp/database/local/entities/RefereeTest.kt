package com.egraf.refapp.database.local.entities

import com.egraf.refapp.ui.dialogs.search_entity.SearchItem.Companion.randomId
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
        assertThat(referee.isEmpty).isFalse()
        assertThat(referee.firstName).isEqualTo("Ivan")
        assertThat(referee.middleName).isEqualTo("Ivanov")
        assertThat(referee.lastName).isEqualTo("")
    }

    @Test
    fun `Referee("f_Ivan_ m_Ivanov_ l_Ivanovich_") should return Referee(fistName = "Ivan", middleName = "Ivanov", lastName = "Ivanovich")`() {
        val referee = Referee("f_Ivan_ m_Ivanov_ l_Ivanovich_")
        assertThat(referee.isEmpty).isFalse()
        assertThat(referee.firstName).isEqualTo("Ivan")
        assertThat(referee.middleName).isEqualTo("Ivanov")
        assertThat(referee.lastName).isEqualTo("Ivanovich")
    }

    @Test
    fun `Referee(" m_Ivanov_ l_Ivanovch_ f_Ivan_ ") should return Referee(fistName = "Ivan", middleName = "Ivanov", lastName = "Ivanovich")`() {
        val referee = Referee(" m_Ivanov_ l_Ivanovich_ f_Ivan_ ")
        assertThat(referee.isEmpty).isFalse()
        assertThat(referee.firstName).isEqualTo("Ivan")
        assertThat(referee.middleName).isEqualTo("Ivanov")
        assertThat(referee.lastName).isEqualTo("Ivanovich")
    }

    @Test
    fun `Referee("f_Ivan_ Ivanov l_Ivanovich_") should return Referee(fistName = "Ivan", lastName = "Ivanovich")`() {
        val referee = Referee("f_Ivan_ Ivanov l_Ivanovich_")
        assertThat(referee.isEmpty).isFalse()
        assertThat(referee.firstName).isEqualTo("Ivan")
        assertThat(referee.middleName).isEqualTo("")
        assertThat(referee.lastName).isEqualTo("Ivanovich")
    }

    @Test
    fun `Referee("m_Ivanov_") should return Referee(middleName = "Ivanov")`() {
        val referee = Referee("m_Ivanov_")
        assertThat(referee.isEmpty).isFalse()
        assertThat(referee.firstName).isEqualTo("")
        assertThat(referee.middleName).isEqualTo("Ivanov")
        assertThat(referee.lastName).isEqualTo("")
    }

    @Test
    fun `Referee("f_Ivan_ m_Ivanov_") should return Referee(fistName = "Ivan", middleName = "Ivanov")`() {
        val referee = Referee("f_Ivan_ m_Ivanov_")
        assertThat(referee.isEmpty).isFalse()
        assertThat(referee.firstName).isEqualTo("Ivan")
        assertThat(referee.middleName).isEqualTo("Ivanov")
        assertThat(referee.lastName).isEqualTo("")
    }

    @Test
    fun `Referee with randomId but empty fields should return EmptyReferee`() {
        val referee = Referee(id = randomId())
        assertThat(referee.isEmpty).isTrue()
    }

    @Test
    fun `Referee with randomId and at least one filled field should return not empty Referee`() {
        val referee = Referee(id = randomId(), middleName = "Ivanov")
        assertThat(referee.isEmpty).isFalse()
        assertThat(referee.middleName).isEqualTo("Ivanov")
    }

    @Test
    fun `Referee with empty string with bySpace=true should return EmptyReferee`() {
        val referee = Referee("", bySpace = true)
        assertThat(referee.isEmpty).isTrue()
    }

    @Test
    fun `Referee with name "Ivanov Ivan Ivanovch" and bySpace=true should return Referee(fistName = "Ivan", middleName = "Ivanov", lastName = "Ivanovich")`() {
        val referee = Referee("Ivanov Ivan Ivanovich", bySpace = true)
        assertThat(referee.isEmpty).isFalse()
        assertThat(referee.firstName).isEqualTo("Ivan")
        assertThat(referee.middleName).isEqualTo("Ivanov")
        assertThat(referee.lastName).isEqualTo("Ivanovich")
    }

    @Test
    fun `Referee with name "Ivanov Ivan" and bySpace=true should return Referee(fistName = "Ivan", middleName = "Ivanov", lastName = "")`() {
        val referee = Referee("Ivanov Ivan", bySpace = true)
        assertThat(referee.isEmpty).isFalse()
        assertThat(referee.firstName).isEqualTo("Ivan")
        assertThat(referee.middleName).isEqualTo("Ivanov")
        assertThat(referee.lastName).isEmpty()
    }
}