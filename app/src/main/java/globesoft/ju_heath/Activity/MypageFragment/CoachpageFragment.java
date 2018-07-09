package globesoft.ju_heath.Activity.MypageFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-01-12.
 */
public class CoachpageFragment extends Fragment{



    View view;
    TextView nameTextView;
    ImageView profileImageView;
    LinearLayout setting;
    String uid;
    String name;
    FirebaseAuth auth;
    Uri imagePath;
    FirebaseFirestore db;
    Uri path;
    globesoft.ju_heath.DTO.usersDTO usersDTO;
    globesoft.ju_heath.DTO.coachDTO coachDTO;
    Context context;
    ViewPager viewPager;
    LinearLayout update_btn;
    CoachpageViewPagerAdapter viewPagerAdapter;
    TextView title;
    RelativeLayout callingRelativeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.fragment_coach_mypage, container, false);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            profileImageView = (ImageView) view.findViewById(R.id.profileImageView);
            update_btn = (LinearLayout) view.findViewById(R.id.editLinearLayout);

            setting = (LinearLayout) view.findViewById(R.id.setting);
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            title = (TextView) view.findViewById(R.id.title);
            callingRelativeLayout = (RelativeLayout) view.findViewById(R.id.calling);

        }

        context = getContext();
        update_btn.setVisibility(View.GONE);
        setting.setVisibility(View.GONE);
        uid = getArguments().getString("uid");
        auth = FirebaseAuth.getInstance();
        db =FirebaseFirestore.getInstance();
        title.setText("코치정보");
        callingRelativeLayout.setVisibility(View.VISIBLE);

        call_api_coachProfile();

        callingRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + coachDTO.phone));
                context.startActivity(intent);

            }
        });

        return view;
    }

    public void call_api_coachProfile()
    {

        DocumentReference documentReference = db.collection("trainer").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    coachDTO = document.toObject(globesoft.ju_heath.DTO.coachDTO.class);
                    nameTextView.setText(coachDTO.name);
                    if(coachDTO.profile_path!=null&&!coachDTO.profile_path.equals("")) Glide.with(context).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.profile_man).error(R.drawable.profile_man)).load(coachDTO.profile_path).into(profileImageView);
                    viewPager.setClipToPadding(false);
                    viewPagerAdapter = new CoachpageViewPagerAdapter(context, coachDTO);
                    viewPager.setAdapter(viewPagerAdapter);
                }
            }
        });


    }
}
