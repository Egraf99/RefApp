package com.egraf.refapp.actions

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.actionWithAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Matchers.allOf
import java.util.*

class TextInputLayoutActions {
    class ReplaceTextAction(private val text: String): ViewAction {
        override fun getDescription(): String {
            return String.format(Locale.ROOT, "replace text(%s)", text)
        }

        override fun getConstraints(): org.hamcrest.Matcher<View> {
            return allOf(isDisplayed(), ViewMatchers.isAssignableFrom(TextInputLayout::class.java))
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as? TextInputLayout?)?.let {
                it.editText?.setText(text)
            }
        }
    }

    companion object {
        @JvmStatic
        fun replaceText(text: String): ViewAction {
            return actionWithAssertions(ReplaceTextAction(text))
        }
    }
}