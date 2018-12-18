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
import br.com.brq.personregistry.UtilTest.Companion.onViewInputLayout
import br.com.brq.personregistry.data.netWork.RetrofitService.Companion.baseUrl
import br.com.concretesolutions.requestmatcher.InstrumentedTestRequestMatcherRule
import br.com.concretesolutions.requestmatcher.RequestMatcherRule
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

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
        baseUrl = serverRule.url("/").toString()

        mockWebServer.url(baseUrl)
    }

    @Test
    fun testFillCep() {

        serverRule.mockWebServer.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                return MockResponse().setBody(serverRule.readFixture("address.json"))
            }
        })

        activityRule.launchActivity(Intent())

        onView(withId(R.id.fab))
                .perform(click())

        onView(withId(R.id.editCep))
                .perform(scrollTo())
                .perform(replaceText("12071580"), pressImeActionButton(), closeSoftKeyboard())

        onView(withId(R.id.editStreet))
                .perform(scrollTo())
                .perform(click())

        onViewInputLayout(R.id.editCity, R.id.textInputLayout7)
                .check(matches(withText("Taubaté")))
    }

    @Test
    fun testInsertRegistry() {
        activityRule.launchActivity(Intent())
        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.editName))
                .perform(replaceText("Lieberson Xavier dos Santos"), closeSoftKeyboard())

        onView(withId(R.id.editCpf))
                .perform(scrollTo()).perform(replaceText("33111284867"), closeSoftKeyboard())

        onView(withId(R.id.editCep))
                .perform(scrollTo()).perform(replaceText("12071580"), closeSoftKeyboard())

        onView(withId(R.id.editStreet))
                .perform(scrollTo()).perform(replaceText("Rua Dona Benta"), closeSoftKeyboard())

        onView(withId(R.id.editNeighborhood))
                .perform(scrollTo()).perform(replaceText("Jardim Gurilândia"), closeSoftKeyboard())


        onView(withId(R.id.editNumber))
                .perform(scrollTo()).perform(replaceText("1231"), closeSoftKeyboard())


        onView(withId(R.id.editCity))
                .perform(scrollTo()).perform(replaceText("Taubaté"), closeSoftKeyboard())


        onView(withId(R.id.editState))
                .perform(scrollTo()).perform(replaceText("São Paulo"), closeSoftKeyboard())

        onView(withId(R.id.editBirthday))
                .perform(scrollTo()).perform(replaceText("31/10/1984"), closeSoftKeyboard())

        onView(withId(R.id.btnRegistry))
                .perform(scrollTo()).perform(click())

        SystemClock.sleep(200)
        onView(withId(R.id.recycleview_person))
                .check(matches(hasDescendant(withText("Lieberson Xavier dos Santos"))))
    }
}