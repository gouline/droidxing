package net.gouline.droidxing.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.client.result.ParsedResult;

import net.gouline.droidxing.CaptureActivity;
import net.gouline.droidxing.data.CaptureResult;

import java.io.Serializable;

public class DemoActivity extends Activity {

    private static final String ARG_STATUS = "status";

    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = (TextView) findViewById(R.id.txt_status);

        findViewById(R.id.btn_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(DemoActivity.this, CaptureActivity.class), 0);
            }
        });

        if (savedInstanceState != null) {
            statusTextView.setText(savedInstanceState.getString(ARG_STATUS));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_STATUS, statusTextView.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Serializable codeResult = data.getSerializableExtra(CaptureActivity.EXTRA_CODE_RESULT);
            if (codeResult != null && codeResult instanceof CaptureResult) {
                CaptureResult codeResultBlock = (CaptureResult) codeResult;
                ParsedResult parsedResult = codeResultBlock.getParsedResult();
                displayResult(parsedResult);
            }
        }
    }

    private void displayResult(ParsedResult parsedResult) {
        statusTextView.setText(parsedResult.toString());
    }

}
