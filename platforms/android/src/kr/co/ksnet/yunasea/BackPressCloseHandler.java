package kr.co.ksnet.yunasea;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by PeterLEE on 2015-02-12.
 */
public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity,
                activity.getString(R.string.back_button_finish_message), Toast.LENGTH_SHORT);
        toast.show();
    }
}
