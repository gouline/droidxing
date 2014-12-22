package net.gouline.droidxing;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
public class CaptureActivityTest extends ActivityInstrumentationTestCase2<CaptureActivity> {

    public CaptureActivityTest() {
        super(CaptureActivity
                .class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testDefaultStatus() {
        onView(withText("Place a barcode inside the viewfinder rectangle to scan it.")).check(matches(isDisplayed()));
    }

}
