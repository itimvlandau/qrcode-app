package de.imvlandau.imv_scanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.google.zxing.Result;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final int RC_PERMISSION = 10;
    private CodeScanner mCodeScanner;
    private boolean mPermissionGranted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCodeScanner = new CodeScanner(this, findViewById(R.id.scanner));
        mCodeScanner.setDecodeCallback(result -> {
            Status status = processResult(result);
            runOnUiThread(() -> {
                de.imvlandau.imv_scanner.ScanResultDialog dialog = new de.imvlandau.imv_scanner.ScanResultDialog(this, status);
                dialog.setOnDismissListener(d -> mCodeScanner.startPreview());
                dialog.show();
            });
        });
        mCodeScanner.setErrorCallback(error -> runOnUiThread(
                () -> Toast.makeText(this, getString(R.string.scanner_error, error), Toast.LENGTH_LONG).show()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }
    }

    private Status processResult(Result result) {
        int code = -1;
        try {
            URL url = new URL("https://imv-landau.de/api/attendees/validate/" + result.getText());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            code = connection.getResponseCode(); // show code result :)
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Status.fromInteger(code);

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