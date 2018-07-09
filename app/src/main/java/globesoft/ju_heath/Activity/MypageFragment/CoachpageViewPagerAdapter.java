package globesoft.ju_heath.Activity.MypageFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by system777 on 2018-01-31.
 */

public class CoachpageViewPagerAdapter extends PagerAdapter {

    private Context context;
    private globesoft.ju_heath.DTO.coachDTO coachDTO;
    View view;

    TextView nameTextView;
    TextView phoneTextView;
    TextView gymTextView;
    TextView licenseTextView;
    TextView awardTextView;
    TextView careerTextView;

    LinearLayout licenseBtnLinearLayout;
    LinearLayout awardBtnLinearLayout;
    LinearLayout careerBtnLinearLayout;
    LinearLayout licenseLinearLayout;
    LinearLayout awardLinearLayout;
    LinearLayout careerLinearLayout;

    ImageView licenseDownImageView;
    ImageView licenseUpImageView;
    ImageView awardDownImageView;
    ImageView awardUpImageView;
    ImageView careerDownImageView;
    ImageView careerUpImageView;

    FirebaseFirestore db;
    FirebaseStorage storage;
    ImageView image;
    String image_name;
    Uri imagePath;
    Uri path;
    String uid;

    AlertDialog.Builder alert_confirm;
    Fragment fragment;

    public CoachpageViewPagerAdapter(Context context, globesoft.ju_heath.DTO.coachDTO coachDTO) {
        this.context = context;
        this.coachDTO = coachDTO;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        alert_confirm = new AlertDialog.Builder(context);
        uid = ((Ju_healthApp)getApplicationContext()).getUserUid();


        if (position == 0) {
            view = View.inflate(context, R.layout.fragment_mypage_first, null);

            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
            gymTextView = (TextView) view.findViewById(R.id.gymTextView);
            licenseTextView = (TextView) view.findViewById(R.id.licenseTextView);
            awardTextView = (TextView) view.findViewById(R.id.awardTextView);
            careerTextView = (TextView) view.findViewById(R.id.careerTextView);

            licenseLinearLayout = (LinearLayout) view.findViewById(R.id.licenseLinearLayout);
            awardLinearLayout = (LinearLayout) view.findViewById(R.id.awardLinearLayout);
            careerLinearLayout = (LinearLayout) view.findViewById(R.id.careerLinearLayout);
            licenseBtnLinearLayout = (LinearLayout) view.findViewById(R.id.licenseBtnLinearLayout);
            awardBtnLinearLayout = (LinearLayout) view.findViewById(R.id.awardBtnLinearLayout);
            careerBtnLinearLayout = (LinearLayout) view.findViewById(R.id.careerBtnLinearLayout);

            licenseDownImageView = (ImageView) view.findViewById(R.id.licenseDownImageView);
            licenseUpImageView = (ImageView) view.findViewById(R.id.licenseUpImageView);
            awardDownImageView = (ImageView) view.findViewById(R.id.awardDownImageView);
            awardUpImageView = (ImageView) view.findViewById(R.id.awardUpImageView);
            careerDownImageView = (ImageView) view.findViewById(R.id.careerDownImageView);
            careerUpImageView = (ImageView) view.findViewById(R.id.careerUpImageView);

            nameTextView.setText(coachDTO.name);
            phoneTextView.setText(coachDTO.phone);
            gymTextView.setText(coachDTO.gym);
            if (coachDTO.license != null && !coachDTO.license.equals(""))
                licenseTextView.setText(coachDTO.license);
            if (coachDTO.award != null && !coachDTO.award.equals(""))
                awardTextView.setText(coachDTO.award);
            if (coachDTO.career != null && !coachDTO.career.equals(""))
                careerTextView.setText(coachDTO.career);


            // 자격증 보이기 버튼 클릭시
            licenseBtnLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(TextUtils.isEmpty(coachDTO.license))) {
                        int result = licenseLinearLayout.getVisibility();
                        if (result == View.VISIBLE) {
                            licenseDownImageView.setVisibility(View.VISIBLE);
                            licenseUpImageView.setVisibility(View.GONE);
                            licenseLinearLayout.setVisibility(View.GONE);
                        } else {
                            licenseDownImageView.setVisibility(View.GONE);
                            licenseUpImageView.setVisibility(View.VISIBLE);
                            licenseLinearLayout.setVisibility(View.VISIBLE);
                        }
                    }else {
                        int result = licenseDownImageView.getVisibility();
                        if (result == View.VISIBLE) {
                            licenseDownImageView.setVisibility(View.GONE);
                            licenseUpImageView.setVisibility(View.VISIBLE);
                        }else {
                            licenseDownImageView.setVisibility(View.VISIBLE);
                            licenseUpImageView.setVisibility(View.GONE);
                        }
                    }

                }
            });

            // 수상경력 보이기 버튼 클릭시
            awardBtnLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!(TextUtils.isEmpty(coachDTO.award))) {
                        int result = awardLinearLayout.getVisibility();
                        if (result == View.VISIBLE) {
                            awardDownImageView.setVisibility(View.VISIBLE);
                            awardUpImageView.setVisibility(View.GONE);
                            awardLinearLayout.setVisibility(View.GONE);
                        } else {
                            awardDownImageView.setVisibility(View.GONE);
                            awardUpImageView.setVisibility(View.VISIBLE);
                            awardLinearLayout.setVisibility(View.VISIBLE);
                        }
                    }else {
                        int result = awardDownImageView.getVisibility();
                        if (result == View.VISIBLE) {
                            awardDownImageView.setVisibility(View.GONE);
                            awardUpImageView.setVisibility(View.VISIBLE);
                        }else {
                            awardDownImageView.setVisibility(View.VISIBLE);
                            awardUpImageView.setVisibility(View.GONE);
                        }
                    }

                }
            });
            // 경력 보이기 버튼 클릭시
            careerBtnLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!(TextUtils.isEmpty(coachDTO.career))) {
                        int result = careerLinearLayout.getVisibility();
                        if (result == View.VISIBLE) {
                            careerDownImageView.setVisibility(View.VISIBLE);
                            careerUpImageView.setVisibility(View.GONE);
                            careerLinearLayout.setVisibility(View.GONE);
                        } else {
                            careerDownImageView.setVisibility(View.GONE);
                            careerUpImageView.setVisibility(View.VISIBLE);
                            careerLinearLayout.setVisibility(View.VISIBLE);
                        }
                    }else {
                        int result = careerDownImageView.getVisibility();
                        if (result == View.VISIBLE) {
                            careerDownImageView.setVisibility(View.GONE);
                            careerUpImageView.setVisibility(View.VISIBLE);
                        }else {
                            careerDownImageView.setVisibility(View.VISIBLE);
                            careerUpImageView.setVisibility(View.GONE);
                        }
                    }

                }
            });


            // ===============================================================================
            container.addView(view, 0);
        } else {
            view = View.inflate(context, R.layout.fragment_mypage_second, null);

            final ImageView img1;
            final ImageView img2;
            final ImageView img3;


            img1 = (ImageView) view.findViewById(R.id.img1);
            img2 = (ImageView) view.findViewById(R.id.img2);
            img3 = (ImageView) view.findViewById(R.id.img3);


            // Photo 1
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(coachDTO.img1 != null) {

                        Intent intent = new Intent(context, PhotoActivity.class);
                        intent.putExtra("name", coachDTO.name);
                        intent.putExtra("img", coachDTO.img1);
                        context.startActivity(intent);
//                        finish();

                    }

                }
            });

            // Photo 2
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(coachDTO.img2 != null) {

                        Intent intent = new Intent(context, PhotoActivity.class);
                        intent.putExtra("name", coachDTO.name);
                        intent.putExtra("img", coachDTO.img2);
                        context.startActivity(intent);
//                        finish();

                    }

                }
            });

            // Photo 3
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(coachDTO.img3 != null) {

                        Intent intent = new Intent(context, PhotoActivity.class);
                        intent.putExtra("name", coachDTO.name);
                        intent.putExtra("img", coachDTO.img3);
                        context.startActivity(intent);
//                        finish();

                    }

                }
            });


            if (coachDTO.img1 == null || coachDTO.img1.equals("")) {
                img1.setImageResource(R.drawable.member_big_album_frame);
            } else {
                Glide.with(context).load(coachDTO.img1).into(img1);
            }
            if (coachDTO.img2 == null || coachDTO.img2.equals("")) {
                img2.setImageResource(R.drawable.member_small_album_frame);
            } else {
                Glide.with(context).load(coachDTO.img2).into(img2);
            }
            if (coachDTO.img3 == null || coachDTO.img3.equals("")) {
                img3.setImageResource(R.drawable.member_small_album_frame);
            } else {
                Glide.with(context).load(coachDTO.img3).into(img3);
            }


            container.addView(view, 1);

        }


        return view;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }




}
