package de.imvlandau.imv_scanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private static final int RC_PERMISSION = 10;
    private CodeScanner mCodeScanner;
    private boolean mPermissionGranted;
    private Button okButton;
    private EditText codeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okButton = findViewById(R.id.ok_button);
        codeEditText = findViewById(R.id.code_edit_text);
        okButton.setOnClickListener(v -> {
            if (codeEditText.getText().length() > 5 || codeEditText.getText().length() < 4) {
                Toast.makeText(MainActivity.this, "The Code is False", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(() -> processResult(codeEditText.getText().toString())).start();

            codeEditText.getText().clear();

        });
        mCodeScanner = new CodeScanner(this, findViewById(R.id.scanner));
        mCodeScanner.setDecodeCallback(result -> processResult(result.getText()));
        mCodeScanner.setErrorCallback(error -> runOnUiThread(
                () -> Toast.makeText(this, getString(R.string.scanner_error, error), Toast.LENGTH_LONG).show()));
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            mPermissionGranted = false;
            requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_PERMISSION);
        } else {
            mPermissionGranted = true;
        }
    }

    private void processResult(String result) {
        int code = -1;
        try {
            URL url = new URL("https://imv-landau.de:8080/api/attendees/validate/" + result);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            code = connection.getResponseCode(); // show code result :)

            // This snippet can read the response page ( accepted, im Used , etc..)
            /*
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String string = null;
            while ((string = bufferedReader.readLine()) != null) {
                System.out.println("Received " + string);
            }

            */
        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(() ->
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        }
        final Status status = Status.fromInteger(code);

        runOnUiThread(() -> {
            de.imvlandau.imv_scanner.ScanResultDialog dialog = new de.imvlandau.imv_scanner.ScanResultDialog(this, status);
            dialog.setOnDismissListener(d -> mCodeScanner.startPreview());
            dialog.show();
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionGranted) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}