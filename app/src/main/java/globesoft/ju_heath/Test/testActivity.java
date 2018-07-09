package globesoft.ju_heath.Test;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

public class testActivity extends Activity {

    ImageView testImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        testImageView = (ImageView)findViewById(R.id.testImageView);
        testImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_push();
            }
        });
    }

    public void call_push()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // FMC 메시지 생성 start
                    LogUtil.d("ddddddddddddddddddddddddddddddddddddddddddddddddddddd");
                    JSONObject root = new JSONObject();
                    JSONObject data = new JSONObject();
                    data.put("message", "hellow");
                    data.put("title", "title");
                    root.put("data", data);
                    root.put("priority","high");
                    root.put("to", "dTmC8CVKMpk:APA91bHEYaVxSX4f6ioNPA1Nftu9eXaPsT2DpzNlhtmtim4JfzM8Q41F2vN6W65na9k3P_TSeYgFs1_GSRNyy9GNM88xl20hhHvTAt2yEcbiPMfnWMT_VY5isOh_2ToKdD9rr92hRoq5");
                    // FMC 메시지 생성 end

                    URL Url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.addRequestProperty("Authorization", "key=" + "AAAA993NYAA:APA91bFrHN2RRVTk1VNBTm5ZUB1R9C-nAQv2U-ktWL49ig8-zJA7NueQlFHfA0tuYWEt_l7qa-IQa0TDAoZH4DNkycEvCFDh9af6Nmk0fOkX9e38zGjFkn0lIHoPU0dU5cSzgP9JivvB");
                    conn.setRequestProperty("Content-type", "application/json");
                    OutputStream os = conn.getOutputStream();
                    os.write(root.toString().getBytes("utf-8"));
                    os.flush();
                    conn.getResponseCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
