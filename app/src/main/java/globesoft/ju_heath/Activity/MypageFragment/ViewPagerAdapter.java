package globesoft.ju_heath.Activity.MypageFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by system999 on 2018-01-19.
 */

public class ViewPagerAdapter extends PagerAdapter {

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

    ArrayList<String> update_list;

    int edit_count = 0;

    public ViewPagerAdapter(Context context, globesoft.ju_heath.DTO.coachDTO coachDTO, CoachMypageFragment fragment) {
        this.context = context;
        this.coachDTO = coachDTO;
        this.fragment = fragment;
    }

    public ViewPagerAdapter(Context context, globesoft.ju_heath.DTO.coachDTO coachDTO) {
        this.context = context;
        this.coachDTO = coachDTO;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

        Fragment fragmenta = ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.main_base_lay);

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
                    image = img1;
                    image_name = "img1";
                    selectPhoto();
                }
            });

            // Photo 2
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image = img2;
                    image_name = "img2";
                    selectPhoto();
                }
            });

            // Photo 3
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image = img3;
                    image_name = "img3";
                    selectPhoto();
                }
            });


            if (coachDTO.img1 != null &&!coachDTO.img1.equals(""))
                Glide.with(context).load(coachDTO.img1).into(img1);
            if (coachDTO.img2 != null &&!coachDTO.img2.equals(""))
                Glide.with(context).load(coachDTO.img2).into(img2);
            if (coachDTO.img3 != null &&!coachDTO.img3.equals(""))
                Glide.with(context).load(coachDTO.img3).into(img3);


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

    public void selectPhoto() {

        if (image.getDrawable() == null) {        // ImageView 에 이미지가 없는 경우

            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ab.setView(R.layout.dialog_add_photo);
            }
            ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    fragment.startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 2);

                }
            });
            ab.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = ab.show();
            TextView message_custom = (TextView) dialog.findViewById(R.id.message_custom);
            TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
            Button posButton = (Button)dialog.findViewById(android.R.id.button1);
            posButton.setTextColor(Color.parseColor("#ee2b47"));
            Button negButton = (Button)dialog.findViewById(android.R.id.button2);
            negButton.setTextColor(Color.parseColor("#333333"));

        } else {                                // ImageView 에 이미지가 있는 경우

            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ab.setView(R.layout.dialog_delete_photo);
            }
            ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    deleteDB(uid);

                    if (image_name.equals("img1")) {
                        deleteFile(coachDTO.img1);
                    } else if (image_name.equals("img2")) {
                        deleteFile(coachDTO.img2);
                    } else if (image_name.equals("img3")) {
                        deleteFile(coachDTO.img3);
                    }

                }
            });
            ab.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = ab.show();
            TextView message_custom = (TextView) dialog.findViewById(R.id.message_custom);
            TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
            Button posButton = (Button)dialog.findViewById(android.R.id.button1);
            posButton.setTextColor(Color.parseColor("#ee2b47"));
            Button negButton = (Button)dialog.findViewById(android.R.id.button2);
            negButton.setTextColor(Color.parseColor("#333333"));

        }
    }

    private void uploadFile(Uri filePath) {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("업로드중...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            int randomNumber = (int) (Math.random() * 100000);
            Date now = new Date();
            final String filename = randomNumber + formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            final StorageReference storageRef = storage.getReferenceFromUrl("gs://juperman-b7d2b.appspot.com/").child("Photos/" + filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    path = task.getResult();
                                    LogUtil.d("hellow", path.toString());
                                    uploadDB(uid, path.toString());
                                    progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                                    Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                                    Glide.with(context).load(imagePath).into(image);
                                }
                            });

                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }


    public void uploadDB(final String uid, String path) {


        final Map<String, String> hash = new HashMap<>();
        hash.put(image_name, path);

        if (image_name.equals("img1")) {
            coachDTO.img1 = path;
        } else if (image_name.equals("img2")) {
            coachDTO.img2 = path;
        } else if (image_name.equals("img3")) {
            coachDTO.img3 = path;
        }

        db.collection("trainer").document(uid)
                .set(hash, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        });



    }

    public void deleteDB(final String uid) {


        final Map<String, String> hash = new HashMap<>();
        hash.put(image_name, null);


        db.collection("trainer").document(uid)
                .set(hash, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Glide.with(context).load(null).into(image);
            }
        });

    }

    public void deleteFile(String img) {

        int num = img.indexOf(".png");
        String filename = img.substring(num - 18, num + 4);

        //storage 주소와 폴더 파일명을 지정해 준다.
        final StorageReference desertRef = storage.getReferenceFromUrl("gs://juperman-b7d2b.appspot.com/").child("Photos/" + filename);

        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Toast.makeText(getApplicationContext(), "파일 삭제 성공", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Toast.makeText(getApplicationContext(), "파일 삭제 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            imagePath = data.getData();
            LogUtil.d("pppppppppppppppppppppppppppp" + imagePath.getPath());
            uploadFile(imagePath);

        }
    }





}
