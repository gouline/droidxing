package net.gouline.droidxing.demo;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
public class DemoActivityTest extends ActivityInstrumentationTestCase2<DemoActivity> {

    public DemoActivityTest() {
        super(DemoActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testClickScanButton() {
        onView(withText("Scan")).perform(click());
    }

}
