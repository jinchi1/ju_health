package globesoft.ju_heath.Activity.MemberFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2018-01-29.
 */

public class FoldingCellListAdapter_NonMember extends BaseAdapter {
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    Context context;
    ArrayList<usersDTO> objects;


    public FoldingCellListAdapter_NonMember(Context context, ArrayList<usersDTO> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        final usersDTO usersDTO = objects.get(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        final FoldingCellListAdapter_NonMember.ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new FoldingCellListAdapter_NonMember.ViewHolder();
            LayoutInflater vi = LayoutInflater.from(context);
            cell = (FoldingCell) vi.inflate(R.layout.cell2, parent, false);
            // binding view parts to view holder
            viewHolder.member_photo_ImageView = (ImageView)cell.findViewById(R.id.member_photoImageView);
            viewHolder.nameTextView = (TextView) cell.findViewById(R.id.nameTextView);
            viewHolder.connectImageView = (ImageView)cell.findViewById(R.id.connectImageView);
            viewHolder.phoneImageView = (ImageView)cell.findViewById(R.id.phoneImageView);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (FoldingCellListAdapter_NonMember.ViewHolder) cell.getTag();


        }
        viewHolder.nameTextView.setText(usersDTO.name);
        if(usersDTO.profile_path!=null&&!usersDTO.profile_path.equals(""))Glide.with(context).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.profile_man).error(R.drawable.profile_man)).load(usersDTO.profile_path).into(viewHolder.member_photo_ImageView);
        viewHolder.phoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + usersDTO.phone));
                context.startActivity(intent);
            }
        });
        viewHolder.connectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!usersDTO.connect) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(context);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ab.setView(R.layout.dialog_custom_connect);
                    }
                    ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            connect(usersDTO.uid, usersDTO.coach,usersDTO.token);
                            viewHolder.connectImageView.setImageResource(R.drawable.icon_connect_blue);
                            usersDTO.connect = true;
                            notifyDataSetChanged();

                        }
                    });
                    ab.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog dialog = ab.show();

                    TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                    Button posButton = (Button) dialog.findViewById(android.R.id.button1);
                    posButton.setTextColor(Color.parseColor("#ee2b47"));
                    Button negButton = (Button) dialog.findViewById(android.R.id.button2);
                    negButton.setTextColor(Color.parseColor("#333333"));

                }
            }
        });







        return cell;
    }

    public void connect(final String uid, final String coachUid,final String userToken)
    {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(uid);
        documentReference.update("connect",true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DocumentReference documentReference1 = db.collection("trainer").document(coachUid).collection("users").document(uid);
                documentReference1.update("connect",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        notifyDataSetChanged();
                        call_push(userToken,"S");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView member_photo_ImageView;
        TextView nameTextView;
        ImageView connectImageView;
        ImageView phoneImageView;
        //////////////////////////////////////////

    }


    public void call_push(final String user_token, final String type)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // FMC 메시지 생성 start
                    LogUtil.d("ddddddddddddddddddddddddddddddddddddddddddddddddddddd");
                    JSONObject root = new JSONObject();
                    JSONObject data = new JSONObject();


                    if(type.equals("S"))
                    {
                        data.put("type","S");
                        data.put("message","트레이너가 회원신청을 수락 했습니다.");
                    }
                    data.put("title", "title");
                    root.put("data", data);
                    root.put("priority","high");
                    root.put("to", user_token);
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
