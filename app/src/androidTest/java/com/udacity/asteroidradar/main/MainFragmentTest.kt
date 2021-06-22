package com.udacity.asteroidradar.main

import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.asteroidradar.AsteroidsAdapter
import com.udacity.asteroidradar.R
import org.hamcrest.Matcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.*


@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    private lateinit var scenario: FragmentScenario<MainFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer()
    }

    @Test
    fun fragment_RecyclerViewIsDisplayed() {
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(isRoot()).perform(delay())
        onView(withId(R.id.asteroid_recycler)).check(matches(isDisplayed()))
    }

    @Test
    fun fragment_ScrollToClickDetailMatches() {
        onView(isRoot()).perform(delay())
        onView(withId(R.id.asteroid_recycler))
                .perform(
                        RecyclerViewActions.scrollTo<AsteroidsAdapter.AsteroidsViewHolder>(
                                hasDescendant(withText("(2021 MB)"))
                        )
                )

        onView(isRoot()).perform(delay())

        onView(withId(R.id.asteroid_recycler))
                .perform(
                        RecyclerViewActions.actionOnItem<AsteroidsAdapter.AsteroidsViewHolder>(
                                hasDescendant(withText("(2021 LV6)")),
                                click()
                        )
                )
    }


    private fun delay(): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $2 seconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(10000)
            }
        }
    }

}