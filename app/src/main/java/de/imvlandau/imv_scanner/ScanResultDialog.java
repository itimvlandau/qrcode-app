package de.imvlandau.imv_scanner;

import android.content.Context;
import android.os.Handler;
import android.util.TypedValue;
import android.view.WindowManager;
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Handler handler = new Handler();
        handler.postDelayed(this::dismiss, 3000);
    }

    @Override
    public void dismiss() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.dismiss();
    }

    private static int resolveDialogTheme(@NonNull Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(androidx.appcompat.R.attr.alertDialogTheme, outValue, true);
        return outValue.resourceId;
    }

}
