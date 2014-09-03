package net.gouline.droidxing.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.client.result.ParsedResult;

import net.gouline.droidxing.CaptureActivity;
import net.gouline.droidxing.data.DroidXingResult;

import java.io.Serializable;


public class MainActivity extends Activity {

    private TextView codeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codeTextView = (TextView) findViewById(R.id.txt_code);

        Button scanButton = (Button) findViewById(R.id.btn_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Serializable codeResult = data.getSerializableExtra(CaptureActivity.EXTRA_CODE_RESULT);
            if (codeResult != null && codeResult instanceof DroidXingResult) {
                DroidXingResult codeResultBlock = (DroidXingResult) codeResult;
                ParsedResult parsedResult = codeResultBlock.getParsedResult(); // Parsed data

                codeTextView.setText(parsedResult.toString());
            }
        }
    }
}
