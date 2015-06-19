package net.gouline.droidxing.test;

import android.widget.TextView;

import net.gouline.droidxing.CaptureActivity;
import net.gouline.droidxing.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
public class CaptureActivityTest {

    @Test
    public void startActivity() {
        CaptureActivity activity = Robolectric.setupActivity(CaptureActivity.class);
        TextView statusView = (TextView) activity.findViewById(R.id.status_view);
        assertThat(statusView.getText()).isEqualTo("Place a barcode inside the viewfinder rectangle to scan it.");
    }

}
