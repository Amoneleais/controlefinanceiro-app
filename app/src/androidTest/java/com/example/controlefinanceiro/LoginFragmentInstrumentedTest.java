package com.example.controlefinanceiro;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;

public class LoginFragmentInstrumentedTest {

    @Test
    public void testCredenciaisInvalidas() {
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.usuario)).perform(typeText("tes12"));
        onView(withId(R.id.senha)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());

        activityScenario.close();
    }

    @Test
    public void testCamposVazios() {
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.btnLogin)).perform(click());

        activityScenario.close();
    }
}
