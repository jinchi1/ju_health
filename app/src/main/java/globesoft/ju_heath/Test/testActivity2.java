package globesoft.ju_heath.Test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;


import com.bumptech.glide.Glide;

import globesoft.ju_heath.R;

public class testActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        ImageView imageView = (ImageView)findViewById(R.id.testImageView);

        Glide.with(this).asGif().load(R.raw.gigigi).into(imageView);

    }
}
