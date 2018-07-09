package globesoft.ju_heath.Activity.MemberFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2017-12-19.
 */

public class MemberFragment extends Fragment {

    private View view;
    private ListView theListView;
    FirebaseFirestore db;
    String uid;
    ArrayList<usersDTO> usersDTOArrayList;
    FoldingCellListAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (view == null) {
            view = inflater.inflate(R.layout.fragment_member, container, false);
            theListView = (ListView) view.findViewById(R.id.mainListView);
            usersDTOArrayList = new ArrayList<>();

            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            LogUtil.d("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"+uid);
            db = FirebaseFirestore.getInstance();

            theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    // toggle clicked cell state
                    ((FoldingCell) view).toggle(false);
                    // register in adapter that state for selected cell is toggled
                    adapter.registerToggle(pos);
                }
            });
        }


        call_member_api();
        return view;
    }

    public void call_member_api() {

        db.collection("trainer").document(uid).collection("users").whereEqualTo("connect",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                usersDTOArrayList.clear();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        usersDTO usersDTO = document.toObject(usersDTO.class);
                        usersDTOArrayList.add(usersDTO);
                        Log.i("mmmmmmmmmmmmmmmmmmmmmm",document.getId() + " => " + document.getData());
                    }
                    adapter = new FoldingCellListAdapter(getContext(), usersDTOArrayList);
                    theListView.setAdapter(adapter);

                }else {
                    Log.i("mmmmmmmmmmmmmmm","Error getting documents: "+ task.getException().toString());
                }


            }
        });

    }


}
