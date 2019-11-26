package com.worldline.nicolaldi;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * @author Nicola Verbeeck
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testLoading() {
        onView(withId(R.id.items_container)).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                final RecyclerView.Adapter adapter = ((RecyclerView) view).getAdapter();
                assertNotNull(adapter);
                assertNotEquals(0, adapter.getItemCount());
            }
        });
    }

    @Test
    public void checkOnItem200() {
        onView(withId(R.id.items_container)).perform(RecyclerViewActions.actionOnItemAtPosition(200, click()));
    }

}