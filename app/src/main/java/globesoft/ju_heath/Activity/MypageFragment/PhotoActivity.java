package globesoft.ju_heath.Activity.MypageFragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import globesoft.ju_heath.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by system999 on 2018-02-08.
 */

public class PhotoActivity extends AppCompatActivity {

    Context context;
    PhotoView photoImageView;
    TextView titleNameTextView;
    LinearLayout backBtnLinearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_photo);
        context = this;

        photoImageView = (PhotoView) findViewById(R.id.photoImageView);
        titleNameTextView = (TextView) findViewById(R.id.titleNameTextView);
        backBtnLinearLayout = (LinearLayout) findViewById(R.id.backBtnLinearLayout);

        Intent intent = getIntent();
        String name = (String)intent.getSerializableExtra("name");
        String img = (String)intent.getSerializableExtra("img");

        Glide.with(context).setDefaultRequestOptions(RequestOptions.centerInsideTransform()).load(img).into(photoImageView);
        titleNameTextView.setText(name);

        backBtnLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
