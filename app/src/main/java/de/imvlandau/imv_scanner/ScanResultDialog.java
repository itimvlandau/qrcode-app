package de.imvlandau.imv_scanner;

import android.content.Context;
import android.os.Handler;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.zxing.Result;

public class ScanResultDialog extends AppCompatDialog {

    ImageView imageView ;
    public ScanResultDialog(@NonNull Context context, @NonNull Status result) {
        super(context, resolveDialogTheme(context));
        setTitle("Scan Result");
        setContentView(R.layout.dialog_scan_result);
        ((TextView) findViewById(R.id.text_view)).setText(result.toString());
        imageView = findViewById(R.id.image_view);
        switch (result){
            case ACCEPTED:
                imageView.setImageDrawable(AppCompatResources.getDrawable(getContext(),R.drawable.ic_baseline_check_circle_24));
                break;
            case ALREADY_HERE:
            case NOT_REGISTERED:
                imageView.setImageDrawable(AppCompatResources.getDrawable(getContext(),R.drawable.ic_baseline_pan_tool_24));
            case BAD_URL:
                imageView.setImageDrawable(AppCompatResources.getDrawable(getContext(),R.drawable.ic_round_cancel_24));
        }
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
