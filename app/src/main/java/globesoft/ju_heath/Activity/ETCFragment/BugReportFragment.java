package globesoft.ju_heath.Activity.ETCFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-01-12.
 */

public class BugReportFragment extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view==null)
        {
            view = inflater.inflate(R.layout.fragment_bugreport,container,false);

        }

        return view;
    }
}

