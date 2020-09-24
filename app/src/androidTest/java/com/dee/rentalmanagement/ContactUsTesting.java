package com.dee.rentalmanagement;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class ContactUsTesting {
@Rule
public ActivityTestRule<ContactUsActivity> testRule = new ActivityTestRule<>(ContactUsActivity.class);

@Test
public void testContactTrip(){

    onView(withId(R.id.etMessage)).perform(typeText("Message"));

    onView(withId(R.id.btnCSubmit)).perform(click());
    onView(withText("Message cannot be added"))
            .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

}
}
