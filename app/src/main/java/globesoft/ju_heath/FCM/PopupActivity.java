package globesoft.ju_heath.FCM;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import globesoft.ju_heath.Common.Constants;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;
import globesoft.ju_heath.http.GFailedHandler;
import globesoft.ju_heath.http.GHttpConstants;
import globesoft.ju_heath.http.GJSONDecoder;
import globesoft.ju_heath.http.GPClient;
import globesoft.ju_heath.http.GResultHandler;
import ra.genius.net.GBean;
import ra.genius.net.GExecutor;
import ra.genius.net.GHttpMethod;

public class PopupActivity extends Activity {

    TextView messageTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");

        messageTextView = (TextView)findViewById(R.id.message_custom);
        messageTextView.setText(message);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                LogUtil.d("fffffffffffffffffffffffffffff");
                finish();
            }
        };
        LogUtil.d("fffffffffffffffffffffffffffffffffffff"+message);
        if(message!=null&&message.equals("트레이너가 회원신청을 수락 했습니다."))
        {
            ((Ju_healthApp)getApplicationContext()).setuserConnect(true);
        }

        timer.schedule(timerTask,2000);


    }


}
