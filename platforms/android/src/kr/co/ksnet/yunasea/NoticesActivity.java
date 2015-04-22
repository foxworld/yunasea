package kr.co.ksnet.yunasea;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by PeterLEE on 2015-04-22.
 */
public class NoticesActivity extends Activity {
    WebView wvNotices = null;
    String sErrorURL = null;
    public static ProgressBar pBar = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        wvNotices = (WebView)findViewById(R.id.wv_notices);
        pBar = (ProgressBar) this.findViewById(R.id.progress_bar_notices);
        //wvNotices.clearHistory();
        //wvNotices.clearCache(true);
        wvNotices.setWebChromeClient(new AndroidWebChromeClient(this, pBar));
        wvNotices.getSettings().setJavaScriptEnabled(true);
        //wvNotices.addJavascriptInterface(new AndroidJavascriptInterface(this), "android"); // Web에서 App 실행하기
        wvNotices.getSettings().setBuiltInZoomControls(true);
        wvNotices.setWebViewClient(new NoticesWebViewClient());
        wvNotices.loadUrl(getString(R.string.notices_url));

    }

    @Override
    public void onBackPressed() {
        if(wvNotices.canGoBack()) {
            wvNotices.goBack();
        }
        else {
            finish();
        }
    }

    class NoticesWebViewClient extends WebViewClient {                          // 자기 자신 브라우져에 띄우기 위해  추가
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            Toast.makeText(NoticesActivity.this, getString(R.string.page_error_message), Toast.LENGTH_LONG).show();
    		/* 네트워크 및 기타 페이지를 열수가 없을때 최종 페이지를 담기 위해 처리*/
            wvNotices.loadUrl(getString(R.string.network_error_url));
            sErrorURL = failingUrl;
        }
    }

    public class AndroidWebChromeClient  extends WebChromeClient {
        private Activity activity;
        private ProgressBar progressBar;

        public AndroidWebChromeClient(Activity activity, ProgressBar progressBar) { // 다른 객체에서 사용을 할수 있도록 추가
            this.activity = activity;
            this.progressBar = progressBar;
        }
        public AndroidWebChromeClient(Activity activity) { // 다른 객체에서 사용을 할수 있도록 추가
            this.activity = activity;
            this.progressBar = null;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if( this.progressBar != null) {
                //Log.d("foxworld", "newProgress="+newProgress);
                this.progressBar.setProgress(newProgress);
            }
        }

        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            // TODO Auto-generated method stub

            new AlertDialog.Builder(this.activity)
                    .setTitle("알림")
                    .setMessage(message)  // 기존 alert 내용을 넣어주기 위해 받은거 그래로 넣기
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() { // 두개이상  같은 메소드가 있으면 하나는 풀메소드 이름을 부여 하여함

                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    result.confirm();
                                }
                            })
                    .setCancelable(true)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {  // 백버튼 처리하기위해 넣어줌
                        public void onCancel(DialogInterface dialog) {
                            // TODO Auto-generated method stub
                            result.cancel();
                        }
                    })
                    .show();
            return true;
        }
    }
}
