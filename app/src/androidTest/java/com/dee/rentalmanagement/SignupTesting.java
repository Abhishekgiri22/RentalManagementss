package com.dee.rentalmanagement;

import androidx.test.rule.ActivityTestRule;

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

public class SignupTesting {

    @Rule
    public ActivityTestRule<Signup_Form> testRule =new ActivityTestRule<>(Signup_Form.class);
    @Test
    public void testSignup() {
        onView(withId(R.id.etEmail)).perform(typeText("deelol474@gmail.com"));
        onView(withId(R.id.etName)).perform(typeText("Deepen"));
        onView(withId(R.id.etPassword)).perform(typeText("#Helloworld545"));
        onView(withId(R.id.etConpassword)).perform(typeText("#Helloworld545"));
        onView(withId(R.id.etPhone)).perform(typeText("4567567"));
        onView(withId(R.id.etAddress)).perform(typeText("Nepal"));

        onView(withId(R.id.btnRegister)).perform(click());
        onView(withText("Registered Successfully"))
                .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }
    }
