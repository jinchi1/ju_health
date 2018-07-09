package globesoft.ju_heath.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

import globesoft.ju_heath.Activity.MemberFragment.FoldingCellListAdapter;
import globesoft.ju_heath.Common.PushEvent;
import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.BusProvider;
import globesoft.ju_heath.Util.DrawLine;
import globesoft.ju_heath.Util.LogUtil;

import static com.facebook.FacebookSdk.getApplicationContext;
import static globesoft.ju_heath.Activity.ScheduleFragment.ScheduleAdapter.isSign;

/**
 * Created by system777 on 2018-02-01.
 */

public class SignActivity extends Activity {

    public static boolean isdrawing=false;

    FirebaseFirestore db;
    String coachUid;
    String userUid;
    String todayString;
    String startDayString;
    String nowCount;
    int nowCountInt;
    String totalCount;
    int lesson;
    ArrayList<usersDTO> usersDTOArrayList;
    FoldingCellListAdapter adapter;

    LinearLayout container;
   static ImageView saveImageView;
    ImageView EraserImageView;
    TextView coachTodayTextView;
    TextView userTodayTextView;

    LinearLayout coachCanvas;
    LinearLayout userCanvas;

    ProgressDialog progressDialog;
    private DrawLine drawLine = null;
    private DrawLine drawLine2 = null;
    Rect rect;
    Rect rect1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trainingsign);
        container = (LinearLayout)findViewById(R.id.containerLay);
        saveImageView = (ImageView)findViewById(R.id.saveImageView);
        EraserImageView = (ImageView)findViewById(R.id.EraserImageView);

        coachTodayTextView = (TextView)findViewById(R.id.coachTodayTextView);
        userTodayTextView = (TextView)findViewById(R.id.userTodayTextView);
        coachCanvas = (LinearLayout)findViewById(R.id.coachCanvasLay);
        userCanvas = (LinearLayout)findViewById(R.id.userCanvasLay);


        progressDialog = new ProgressDialog(SignActivity.this);
        progressDialog.setTitle("업로드중...");
        progressDialog.setCancelable(false);



        isSign = false;
        Intent intent = getIntent();
        coachUid = intent.getStringExtra("coachUid");
        userUid = intent.getStringExtra("userUid");
        todayString = intent.getStringExtra("lessonDate");
        startDayString = intent.getStringExtra("startDate");
        nowCount = intent.getStringExtra("nowCount");
        if(nowCount!=null)
        {
            nowCountInt = Integer.parseInt(nowCount);

        }
        else
        {
            nowCountInt = 0;
        }
        lesson = intent.getIntExtra("lesson",0);

        coachTodayTextView.setText(todayString);
        userTodayTextView.setText(todayString);

        db = FirebaseFirestore.getInstance();


        LogUtil.d("ccccccccccccccccccccccccccccccccc"+coachUid+userUid);

        EraserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                erase();
            }
        });

        saveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });


    }

    private void resetCurrentMode()
    {

        //만약 그리기 뷰가 초기화 되었으면, 그리기 뷰에 글자색을 알려줌..
        drawLine.setLineColor(Color.parseColor("#000000"));
        drawLine2.setLineColor(Color.parseColor("#000000"));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus &&drawLine == null)
        {
            if(coachCanvas != null) //그리기 뷰가 보여질 레이아웃이 있으면...
            {
                LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
                //그리기 뷰 레이아웃의 넓이와 높이를 찾아서 Rect 변수 생성.
                rect = new Rect(0, 0,
                        coachCanvas.getMeasuredWidth(), coachCanvas.getMeasuredHeight());

                //그리기 뷰 초기화..
                drawLine = new DrawLine(this, rect);

                //그리기 뷰를 그리기 뷰 레이아웃에 넣기 -- 이렇게 하면 그리기 뷰가 화면에 보여지게 됨.
                coachCanvas.addView(drawLine);
            }

        }
        if(drawLine2 == null)
        {

            if(userCanvas != null)
            {
                rect1 = new Rect(0, 0,
                        userCanvas.getMeasuredWidth(), userCanvas.getMeasuredHeight());
                drawLine2 = new DrawLine(this, rect1);
                userCanvas.addView(drawLine2);
            }
        }


        resetCurrentMode();
    }

    public void erase() {


        coachCanvas.removeView(drawLine);
        userCanvas.removeView(drawLine2);
        Bitmap bitmap1 = Bitmap.createBitmap(coachCanvas.getMeasuredWidth(),coachCanvas.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bitmap2 = Bitmap.createBitmap(userCanvas.getMeasuredWidth(),userCanvas.getMeasuredHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bitmap1);
        Canvas canvas2 = new Canvas(bitmap2);
        drawLine = new DrawLine(SignActivity.this,rect);
        drawLine2 = new DrawLine(SignActivity.this,rect1);
        drawLine.draw(canvas1);
        drawLine2.draw(canvas2);
        coachCanvas.addView(drawLine);
        userCanvas.addView(drawLine2);
        saveImageView.setVisibility(View.GONE);
        resetCurrentMode();

    }


    public  void save(){

        String folder = "juperman"; // 폴더 이름

        try {
            progressDialog.show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (SignActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Do the file write\

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

                    // 년월일시분초

                    Date currentTime_1 = new Date();

                    String dateString = formatter.format(currentTime_1);

                    File sdCardPath = Environment.getExternalStorageDirectory();

                    File dirs = new File(Environment.getExternalStorageDirectory(), folder);



                    if (!dirs.exists()) { // 원하는 경로에 폴더가 있는지 확인

                        dirs.mkdirs(); // Test 폴더 생성

                        Log.d("CAMERA_TEST", "Directory Created");

                    }

                    container.buildDrawingCache();

                    Bitmap captureView = container.getDrawingCache();

                    FileOutputStream fos;

                    String save;



                    try {

                        save = sdCardPath.getPath() + "/" + folder + "/" + dateString + ".jpg";

                        // 저장 경로

                        fos = new FileOutputStream(save);

                        captureView.compress(Bitmap.CompressFormat.JPEG, 20, fos); // 캡쳐

                        // 미디어 스캐너를 통해 모든 미디어 리스트를 갱신시킨다.

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            final Uri contentUri = Uri.fromFile(dirs);
                            LogUtil.d("eeeeeeeeeeeeeeee"+contentUri);
                            LogUtil.d("eeeeeeeeeeeeeeee"+save);
                            scanIntent.setData(contentUri);
                            sendBroadcast(scanIntent);
                            /////////////////////////////////////
                            Uri fileUri = Uri.parse(contentUri+"/"+dateString+".jpg" );
                            uploadFile(fileUri);

                        } else {
                            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                            sendBroadcast(intent);
                        }
    /*
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,

                                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));*/

                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                    }

                    // 현재 날짜로 파일을 저장하기

                    Toast.makeText(getApplicationContext(), dateString + ".jpg 저장",

                            Toast.LENGTH_LONG).show();
                    //finish();


                } else {
                    // Request permission from the user
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        SignActivity.this.requestPermissions(
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                        progressDialog.cancel();
                        Toast.makeText(SignActivity.this,"권한 허용후 다시 시도해주세요",Toast.LENGTH_SHORT).show();
                    }

                }
            }

        } catch (Exception e) {

            // TODO: handle exception

            Log.e("Screen", "" + e.toString());

        }


    }

    public static void canSave(){

        saveImageView.setVisibility(View.VISIBLE);
    }



    private void uploadFile(Uri filePath) {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            int randomNumber = (int)(Math.random()*100000);
            Date now = new Date();
            final String filename = randomNumber + formatter.format(now) + ".jpg";
            //storage 주소와 폴더 파일명을 지정해 준다.
            final StorageReference storageRef = storage.getReferenceFromUrl("gs://juperman-b7d2b.appspot.com/").child("signPhoto/" + filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String sign_path =task.getResult().toString();
                                    uploadDB(sign_path,todayString);


                                }
                            });
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            LogUtil.d("eeeeeeeeeeeeeeeeee"+e.getMessage());
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
            Toast.makeText(SignActivity.this,"Fail",Toast.LENGTH_SHORT).show();
            progressDialog.cancel();
        }
    }


    public void uploadDB(String path, final String documentName) {
        final Map<String,String> hash = new HashMap<>();
        hash.put("sign_path", path);
        hash.put("coach_uid", coachUid);
        hash.put("user_uid",userUid);
        hash.put("date",todayString);
        db.collection("users").document(userUid).collection("sign").document(documentName)
                    .set(hash).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    db.collection("trainer").document(coachUid).collection("users").document(userUid).collection("sign").document(documentName)
                            .set(hash).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            String lessonString = String.valueOf(lesson);
                            LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+lessonString);
                            db.collection("trainer").document(coachUid).collection("schedule").document(startDayString).update(lessonString+".sign",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                        nowCountInt = nowCountInt+1;
                                        String pushnowCount = String.valueOf(nowCountInt);
                                        Map<String,String> count = new HashMap<>();
                                        count.put("nowCount",pushnowCount);
                                        db.collection("users").document(userUid).set(count,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SignActivity.this,"Success",Toast.LENGTH_SHORT).show();
                                                BusProvider.getInstance().post(new PushEvent(lesson));
                                                progressDialog.cancel();
                                                finish();

                                            }
                                        });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(SignActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            Toast.makeText(SignActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(SignActivity.this,"Fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
