package br.com.brq.personregistry.view

import android.content.Intent
import android.os.SystemClock
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.brq.personregistry.R
import br.com.concretesolutions.requestmatcher.InstrumentedTestRequestMatcherRule
import br.com.concretesolutions.requestmatcher.RequestMatcherRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java, false, false)

    @get:Rule
    val serverRule: RequestMatcherRule = InstrumentedTestRequestMatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.url(serverRule.url("/").toString())
    }

    @Test
    fun testClickFloatActionButtonShowDialog() {
        activityRule.launchActivity(Intent())
        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.edit_name)).check(matches(isDisplayed()))
    }

    @Test
    fun testFillCep() {

        serverRule.addFixture("address.json")
        activityRule.launchActivity(Intent())
        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.edit_cep))
            .perform(scrollTo())
            .perform(replaceText("12071580"), pressImeActionButton(), closeSoftKeyboard())
SystemClock.sleep(5000)
        onView(withText("Taubaté")).perform( scrollTo()).check(matches(isDisplayed()))
    }

    @Test
    fun testInsertRegistry() {
        activityRule.launchActivity(Intent())
        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.edit_name))
            .perform(replaceText("Lieberson Xavier dos Santos"), closeSoftKeyboard())

        onView(withId(R.id.edit_cpf))
            .perform(scrollTo()).perform(replaceText("33111284867"), closeSoftKeyboard())

        onView(withId(R.id.edit_cep))
            .perform(scrollTo()).perform(replaceText("12071580"), closeSoftKeyboard())

        onView(withId(R.id.edit_street))
            .perform(scrollTo()).perform(replaceText("Rua Dona Benta"), closeSoftKeyboard())

        onView(withId(R.id.edit_neighborhood))
            .perform(scrollTo()).perform(replaceText("Jardim Gurilândia"), closeSoftKeyboard())


        onView(withId(R.id.edit_number))
            .perform(scrollTo()).perform(replaceText("1231"), closeSoftKeyboard())


        onView(withId(R.id.edit_city))
            .perform(scrollTo()).perform(replaceText("Taubaté"), closeSoftKeyboard())


        onView(withId(R.id.edit_state))
            .perform(scrollTo()).perform(replaceText("São Paulo"), closeSoftKeyboard())

        onView(withId(R.id.edit_birthday))
            .perform(scrollTo()).perform(replaceText("31/10/1984"), closeSoftKeyboard())

        onView(withId(R.id.btn_registry))
            .perform(scrollTo()).perform(click())

        onView(withId(R.id.recycleview_person))
            .check(matches(hasDescendant(withText("Lieberson Xavier dos Santos"))))
    }

}