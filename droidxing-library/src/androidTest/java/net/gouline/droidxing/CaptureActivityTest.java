package net.gouline.droidxing;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CaptureActivityTest {

    @Rule
    public ActivityTestRule<CaptureActivity> mActivityRule = new ActivityTestRule<>(CaptureActivity.class);

    @Test
    public void defaultStatus() {
        onView(withText("Place a barcode inside the viewfinder rectangle to scan it."))
                .check(matches(isDisplayed()));
    }

}
