package br.com.brq.personregistry

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.*
import org.hamcrest.CoreMatchers.allOf

class UtilTest {

    companion object {
        fun childAtPosition(
                parentMatcher: Matcher<View>, position: Int): Matcher<View> {

            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description) {
                    description.appendText("Child at position $position in parent ")
                    parentMatcher.describeTo(description)
                }

                public override fun matchesSafely(view: View): Boolean {
                    val parent = view.parent
                    return parent is ViewGroup && parentMatcher.matches(parent)
                            && view == parent.getChildAt(position)
                }
            }
        }

        fun onViewTest(id: Int, text: String): ViewInteraction {
            return onView(allOf(withId(id), withText(text), isDisplayed()))
        }

        fun onViewInputLayout(id: Int, layout: Int): ViewInteraction {
            return onView(
                    Matchers.allOf(withId(id),
                            childAtPosition(
                                    childAtPosition(
                                            withId(layout),
                                            0),
                                    0)))
        }
    }
}