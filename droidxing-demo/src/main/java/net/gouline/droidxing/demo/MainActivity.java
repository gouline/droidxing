package net.gouline.droidxing.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import net.gouline.droidxing.CaptureFragment;
import net.gouline.droidxing.CaptureListener;
import net.gouline.droidxing.data.CaptureResult;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CaptureFragment captureFragment = CaptureFragment.newInstance();
        captureFragment.setListener(new CaptureListener() {
            @Override
            public void onSuccess(CaptureResult r) {
                String text = null;
                if (r != null) {
                    text = r.getParsedResult().toString();
                }
                Toast.makeText(MainActivity.this, "Captured: " + text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception cause) {
                String text = null;
                if (cause != null) {
                    text = cause.getMessage();
                }
                Toast.makeText(MainActivity.this, "Error: " + text, Toast.LENGTH_SHORT).show();
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_frame, captureFragment)
                .commit();
    }
}
