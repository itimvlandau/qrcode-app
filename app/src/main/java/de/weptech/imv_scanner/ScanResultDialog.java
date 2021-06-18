package de.weptech.imv_scanner;

import android.content.Context;
import android.os.Handler;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.google.zxing.Result;

public class ScanResultDialog extends AppCompatDialog {
    public ScanResultDialog(@NonNull Context context, @NonNull Result result) {
        super(context, resolveDialogTheme(context));
        setTitle("Scan Result");
        setContentView(R.layout.dialog_scan_result);
        ((TextView) findViewById(R.id.text_view)).setText(result.getText());
        ((Button) findViewById(R.id.ok_button)).setOnClickListener(v -> dismiss());

    }

    @Override
    public void show() {
        super.show();
        Handler handler = new Handler();
        handler.postDelayed(this::dismiss, 1000);
    }

    private static int resolveDialogTheme(@NonNull Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(androidx.appcompat.R.attr.alertDialogTheme, outValue, true);
        return outValue.resourceId;
    }

}
