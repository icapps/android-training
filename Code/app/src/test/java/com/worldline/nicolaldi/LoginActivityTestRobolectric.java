package com.worldline.nicolaldi;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
import androidx.test.rule.ActivityTestRule;

/**
 * @author Nicola Verbeeck
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTestRobolectric {

    @Rule
    public ActivityTestRule<LoginActivity> activityRule
            = new ActivityTestRule<LoginActivity>(LoginActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            context.getSharedPreferences(LoginActivity.PREFERENCE_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .commit();
        }
    };

    SharedPreferences sharedPreferences;

    @Before
    public void setup() {
        sharedPreferences = activityRule.getActivity()
                .getSharedPreferences(LoginActivity.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Test
    public void testFirstTimePinSet() {
        assertTrue(sharedPreferences.getAll().isEmpty());

        onView(withText("1")).perform(click());
        onView(withText("2")).perform(click());
        onView(withText("3")).perform(click());
        onView(withText("4")).perform(click());

        onView(withId(R.id.login_button_ok)).perform(click());

        assertFalse(sharedPreferences.getAll().isEmpty());
        assertEquals("1234", sharedPreferences.getString(LoginActivity.PREFERENCE_KEY_PIN, null));
    }

    @Test
    public void testBackspace() {
        onView(withText("1")).perform(click());
        onView(withText("1")).perform(click());
        onView(withId(R.id.login_button_backspace)).perform(click());

        onView(withText("2")).perform(click());
        onView(withText("3")).perform(click());
        onView(withText("4")).perform(click());

        onView(withId(R.id.login_button_ok)).perform(click());

        assertEquals("1234", sharedPreferences.getString(LoginActivity.PREFERENCE_KEY_PIN, null));
    }

    @Test
    public void testClickBeforeEnoughItems() {
        onView(withId(R.id.login_button_ok)).perform(click());
        assertTrue(sharedPreferences.getAll().isEmpty());

        onView(withText("1")).perform(click());
        onView(withId(R.id.login_button_ok)).perform(click());
        assertTrue(sharedPreferences.getAll().isEmpty());

        onView(withText("1")).perform(click());
        onView(withId(R.id.login_button_ok)).perform(click());
        assertTrue(sharedPreferences.getAll().isEmpty());

        onView(withText("1")).perform(click());
        onView(withId(R.id.login_button_ok)).perform(click());
        assertTrue(sharedPreferences.getAll().isEmpty());

        onView(withText("1")).perform(click());
        onView(withId(R.id.login_button_ok)).perform(click());
        assertFalse(sharedPreferences.getAll().isEmpty());
    }

    @Test
    public void testShowsChoosePin() {
        onView(withId(R.id.login_label_hint)).check(matches(withText("Please configure pincode")));
    }
}