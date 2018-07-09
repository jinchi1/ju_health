package globesoft.ju_heath.Activity.MemberFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-02-07.
 */

public class SignImageFragment extends Fragment {

    View view;
    ImageView imageView;
    String sign_path;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view==null)
        {
            view = inflater.from(getContext()).inflate(R.layout.fragment_signimage,null);
            imageView = (ImageView)view.findViewById(R.id.signImageView);
        }

        sign_path = getArguments().getString("sign_path");
        Glide.with(getContext()).load(sign_path).into(imageView);

        return view;
    }
}
