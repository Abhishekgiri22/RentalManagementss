package com.dee.rentalmanagement;

import android.app.Activity;
import android.view.View;

import androidx.test.espresso.ViewAction;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

public class logintestingInstrumented {

    @Rule
public ActivityTestRule<Login_Form> testRule =new ActivityTestRule<>(Login_Form.class);

    @Test
    public void testLogin(){
    onView(withId(R.id.etEmail)).perform(typeText("deem4sters@gmail.com"));
    onView(withId(R.id.etPassword)).perform(typeText("#Helloworld545"));

    onView(withId(R.id.btnLogin)).perform(click());
    onView(withText("Login Sucessfully"))
             .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));


}


}
