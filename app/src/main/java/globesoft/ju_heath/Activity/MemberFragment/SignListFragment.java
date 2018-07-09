package globesoft.ju_heath.Activity.MemberFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import globesoft.ju_heath.DTO.SignDTO;
import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-02-07.
 */

public class SignListFragment extends Fragment {

    View view;
    ListView listView;
    SignListAdapter adapter;
    ArrayList<SignDTO> signDTOArrayList;
    String userUid;
    String coachUid;
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view==null)
        {

            view = inflater.from(getContext()).inflate(R.layout.fragment_signlist,null);
            db = FirebaseFirestore.getInstance();
            Bundle bundle = getArguments();
            userUid = bundle.getString("userUid");
            coachUid = bundle.getString("coachUid");
            listView = (ListView)view.findViewById(R.id.signListView);
            listView.setDivider(null);
            signDTOArrayList = new ArrayList<>();
            adapter = new SignListAdapter(getContext(),signDTOArrayList);
            listView.setAdapter(adapter);

        }
        callSign(userUid,coachUid);
        return view;
    }


    public void callSign(String userUid,String coachUid){

        db.collection("trainer").document(coachUid).collection("users").document(userUid).collection("sign").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                signDTOArrayList.clear();

                for (DocumentSnapshot document : task.getResult()) {
                    SignDTO signDTO = document.toObject(SignDTO.class);
                    signDTOArrayList.add(signDTO);
                }
                adapter.addItems(signDTOArrayList);
                adapter.notifyDataSetChanged();
            }
        });



    }

}
