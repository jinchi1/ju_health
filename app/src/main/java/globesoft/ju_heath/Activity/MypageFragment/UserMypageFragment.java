package globesoft.ju_heath.Activity.MypageFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import globesoft.ju_heath.DTO.birthDTO;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by system999 on 2018-01-30.
 */

public class UserMypageFragment extends Fragment implements DialogInterface.OnDismissListener{

    View view;
    String uid;
    FirebaseAuth auth;
    Uri imagePath;
    FirebaseFirestore db;
    Uri path;
    globesoft.ju_heath.DTO.usersDTO usersDTO;
    Context context;

    CircleImageView profileImageView;

    TextView nameTextView;
    TextView phoneTextView;
    TextView ageTextView;
    TextView weightTextView;
    TextView titleNameTextView;
    TextView nowPTcountTextView;
    TextView totalPTcountTextView;
    TextView birthTextView;
    TextView genderTextView;

    LinearLayout setting;
    LinearLayout editLinearLayout;

    AlertDialog.Builder alert_confirm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getContext();
        view = inflater.inflate(R.layout.fragment_user_mypage, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        uid = ((Ju_healthApp)getApplicationContext()).getUserUid();

        setting = (LinearLayout) view.findViewById(R.id.setting);
        editLinearLayout = (LinearLayout) view.findViewById(R.id.editLinearLayout);

        profileImageView = (CircleImageView) view.findViewById(R.id.profileImageView);

        titleNameTextView = (TextView) view.findViewById(R.id.titleNameTextView);
        nowPTcountTextView = (TextView) view.findViewById(R.id.nowPTcountTextView);
        totalPTcountTextView = (TextView) view.findViewById(R.id.totalPTcountTextView);

        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
        ageTextView = (TextView) view.findViewById(R.id.ageTextView);
        weightTextView = (TextView) view.findViewById(R.id.weightTextView);
        birthTextView = (TextView) view.findViewById(R.id.birthTextView);
        genderTextView = (TextView) view.findViewById(R.id.genderTextView);

        alert_confirm = new AlertDialog.Builder(context);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> items = new ArrayList<>();
                items.add("앨범에서 사진 선택");
                items.add("기본 이미지로 변경");

                CharSequence[] item = items.toArray(new String[items.size()]);
                android.support.v7.app.AlertDialog.Builder alert_confirm = new android.support.v7.app.AlertDialog.Builder(getContext());
                alert_confirm
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                switch (position) {
                                    case 0:
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
                                        break;

                                    case 1:
                                        final Map<String,String> hash = new HashMap<>();
                                        hash.put("profile_path", null);
                                        uploadDB(hash);

                                        if(usersDTO.gender == null || usersDTO.gender.length() == 0){
                                            profileImageView.setImageResource(R.drawable.man_icon);
                                        }else {
                                            profileImageView.setImageResource(R.drawable.woman_icon);
                                        }
                                        break;

                                }
                            }
                        });
                android.support.v7.app.AlertDialog alertDialog = alert_confirm.create();
                alertDialog.show();

            }
        });


        // UserMypageFragment실행시 데이터 받아오기
        db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        usersDTO = task.getResult().toObject(globesoft.ju_heath.DTO.usersDTO.class);

                        // 프로필 이미지
                        if(usersDTO.profile_path == null || usersDTO.profile_path.length() == 0) {     // 이미지가 없는 경우
                            if(usersDTO.gender == null || usersDTO.gender.length() == 0 || usersDTO.gender.equals("M")){
                                profileImageView.setImageResource(R.drawable.man_icon);     // 남자 or 성별이 없는 경우 기본 이미지
                            }else {
                                profileImageView.setImageResource(R.drawable.woman_icon);   // 여자 기본 이미지
                            }
                        }else {                                 // 이미지가 있는 경우
                            Glide.with(context).load(usersDTO.profile_path).into(profileImageView);
                        }

                        titleNameTextView.setText(usersDTO.name);
                        nameTextView.setText(usersDTO.name);
                        phoneTextView.setText(usersDTO.phone);

                        nowPTcountTextView.setText(usersDTO.nowCount);
                        totalPTcountTextView.setText(usersDTO.totalCount);


                        // 나이
                        ageTextView.setText(usersDTO.age == null || usersDTO.age.length() == 0 ? "  ㅡ  " : usersDTO.age);

                        // 몸무게
                        weightTextView.setText(usersDTO.weight == null || usersDTO.weight.length() == 0 ? "  ㅡ  " : usersDTO.weight);


                        // 생일
                        if(usersDTO.birth == null || usersDTO.birth.length() == 0) {
                            birthTextView.setText("  ㅡ  ");
                        }else {

                            birthTextView.setText(usersDTO.birth);

                        }

                        // 성별
                        genderTextView.setText(usersDTO.gender == null || usersDTO.gender.length() == 0 ? "  ㅡ  " : usersDTO.gender);

                    }else {
                        System.out.println("실패!!!");
                    }

                    editLinearLayout.setVisibility(View.VISIBLE);

                }

            }
        });

        // 프로필 편집 버튼
        editLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new UserProfileModifyFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name",usersDTO.name);
                bundle.putString("phone",usersDTO.phone);
                bundle.putString("age",usersDTO.age);
                bundle.putString("weight",usersDTO.weight);
                bundle.putString("birth",usersDTO.birth);
                bundle.putString("gender",usersDTO.gender);
                bundle.putString("coachUid",usersDTO.coach);

                fragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_base_lay, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        // 설정버튼
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment mypageFragment = new UserSettingFragment();

                Bundle bundle = new Bundle();
                bundle.putString("uid",uid);
                mypageFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.main_base_lay, mypageFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return view;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            imagePath = data.getData();
            LogUtil.d("pppppppppppppppppppppppppppp"+imagePath.getPath());
            uploadFile(imagePath);

        }

    }


    private void uploadFile(Uri filePath) {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("업로드중...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            int randomNumber = (int)(Math.random()*100000);
            Date now = new Date();
            final String filename = randomNumber + formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            final StorageReference storageRef = storage.getReferenceFromUrl("gs://juperman-b7d2b.appspot.com/").child("profilePhoto/" + filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    path =task.getResult();
                                    LogUtil.d("hellow",path.toString());

                                    final Map<String,String> hash = new HashMap<>();
                                    hash.put("profile_path", path.toString());
                                    uploadDB(hash);

                                    progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                                    System.out.println("업로드 완료!");
//                                    Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                                    Glide.with(context).load(imagePath).into(profileImageView);
                                }
                            });

                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            System.out.println("업로드 실패!");
//                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadDB(final Map<String,String> hash) {

        db.collection("users").document(uid)
                .set(hash, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

                if(usersDTO.connect == true) {
                    db.collection("trainer").document(usersDTO.coach).collection("users").document(uid)
                            .set(hash, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            System.out.println("DB 수정완료");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            System.out.println("DB 수정 실패");

                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    @Override
    public void onDismiss(DialogInterface $dialog) {
        // TODO Auto-generated method stub

        BirthDialog dialog = (BirthDialog) $dialog ;
        birthDTO birthDTO = dialog.getbirth();
        final String birthString = birthDTO.month+"월 "+birthDTO.day+"일";
        birthTextView.setText(birthString);

        db.collection("users").document(uid).update("birthTextView",birthString).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db.collection("trainer").document(usersDTO.coach).collection("users").document(uid).update("birthTextView",birthString).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }





}