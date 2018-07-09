package globesoft.ju_heath.Activity.MypageFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import globesoft.ju_heath.Activity.LoginActivity;
import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by system777 on 2018-01-12.
 */

public class MypageFragment extends Fragment{


    View view;
    TextView nameTextView;
    ImageView logoutImageView;
    ImageView profileImageView;
    String uid;
    String name;
    FirebaseAuth auth;
    Uri imagePath;
    FirebaseFirestore db;
    Uri path;
    usersDTO usersDTO;
    globesoft.ju_heath.DTO.coachDTO coachDTO;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
        {

            auth = FirebaseAuth.getInstance();
            db =FirebaseFirestore.getInstance();

            view = inflater.inflate(R.layout.fragment_mypage,container,false);
            nameTextView = (TextView)view.findViewById(R.id.nameTextView);
            logoutImageView = (ImageView)view.findViewById(R.id.logoutImageView);
            profileImageView = (ImageView)view.findViewById(R.id.profileImageView);

            uid = getArguments().getString("uid");
            context = getContext();


            logoutImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),"로그아웃",Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    LoginManager.getInstance().logOut();
                    ((Ju_healthApp)getApplicationContext()).setuserValue("guest");
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            profileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);


                }
            });
        }

        if(((Ju_healthApp)getApplicationContext()).getuserValue().equals("user"))
        {

            db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    usersDTO = task.getResult().toObject(usersDTO.class);
                    nameTextView.setText(usersDTO.name);
                    if(usersDTO.profile_path!=null)
                    {
                        Glide.with(context).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.profile_man).error(R.drawable.profile_man)).load(usersDTO.profile_path).into(profileImageView);
                    }
                }
            });

        }else
        {
            db.collection("trainer").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    coachDTO = task.getResult().toObject(globesoft.ju_heath.DTO.coachDTO.class);

                    nameTextView.setText(coachDTO.name);
                    if(coachDTO.profile_path!=null)
                    {
                        Glide.with(context).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.profile_man).error(R.drawable.profile_man)).load(coachDTO.profile_path).into(profileImageView);
                    }
                }
            });
        }
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
            Date now = new Date();
            final String filename = formatter.format(now) + ".png";
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
                                    uploadDB(uid,path.toString());
                                    progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                                    Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                                    Glide.with(context).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.profile_man).error(R.drawable.profile_man)).load(imagePath).into(profileImageView);
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
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }


    public void uploadDB(final String uid, String path) {


        final Map<String,String> hash = new HashMap<>();
        hash.put("profile_path", path);

        if(((Ju_healthApp)getApplicationContext()).getuserValue().equals("user"))
        {
            db.collection("users").document(uid)
                    .set(hash, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    db.collection("trainer").document(usersDTO.coach).collection("users").document(uid)
                            .set(hash, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                        }
                    });


                }
            });
        }
        else
        {
            db.collection("trainer").document(uid)
                    .set(hash, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {

                }
            });

        }




    }

}
