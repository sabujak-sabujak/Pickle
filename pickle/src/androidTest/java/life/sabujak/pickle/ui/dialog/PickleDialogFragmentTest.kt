package life.sabujak.pickle.ui.dialog

import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.util.Checks
import life.sabujak.pickle.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PickleDialogFragmentTest {
    // must be proceeded when screen on
    @Test
    fun configTest() {

        // GIVEN
        val givenTitle = "Charlezz"
        val themeColorRes = R.color.GR200
        val config = Config.Builder()
            .setTitle(givenTitle)
            .setThemeColorRes(themeColorRes)
            .build()

        // WHEN
        with(launchFragment(instantiate = { PickleDialogFragment(config) })) {
            // THEN
            onView(withId(R.id.topbar_title)).check(matches(withText(givenTitle)))
            onView(withId(R.id.topbar_title)).check(matches(withTextColor(themeColorRes)))
            onView(withId(R.id.count)).check(matches(withTextColor(themeColorRes)))
        }


    }

    private fun withTextColor(expectedId: Int): Matcher<View> {
        return object : BoundedMatcher<View, TextView>(TextView::class.java) {
            override fun matchesSafely(textView: TextView): Boolean {
                val color = ContextCompat.getColor(textView.context, expectedId)
                return textView.currentTextColor == color
            }

            override fun describeTo(description: Description) {
                description.appendText("with text color: ")
            }
        }
    }

}