package com.egraf.refapp.actions

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.egraf.refapp.views.textInput.ETIWithEndButton
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


class DrawableAssertions {
    companion object {
        fun withDrawable(resourceId: Int): org.hamcrest.Matcher<View> {
            return DrawableMatcher(resourceId)
        }

        fun withEndIconType(type: ETIWithEndButton.TextEditIconType): org.hamcrest.Matcher<View> {
            return EndIconTypeMatcher(type)
        }

        fun noDrawable(): org.hamcrest.Matcher<View> {
            return DrawableMatcher(-1)
        }
    }
}

private class DrawableMatcher(val expectedId: Int) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("with drawable from resource id: ")
        description.appendValue(expectedId)
    }

    override fun matchesSafely(target: View?): Boolean {
        if (target !is ImageView) {
            return false
        }
        val imageView: ImageView = target
        if (expectedId < 0) {
            return imageView.drawable == null
        }
        val resources: Resources = target.getContext().resources
        val expectedDrawable: Drawable = resources.getDrawable(expectedId) ?: return false
        val bitmap = getBitmap(imageView.drawable)
        val otherBitmap = getBitmap(expectedDrawable)
        return bitmap?.sameAs(otherBitmap) ?: false
    }
}

class EndIconTypeMatcher(private val type: ETIWithEndButton.TextEditIconType) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("end icon with type: ")
        description.appendValue(type)
    }

    override fun matchesSafely(target: View?): Boolean {
        if (target !is ETIWithEndButton)
            return false

        return target.typeEndIcon == type
    }
}

fun getBitmap(drawable: Drawable): Bitmap? {
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}