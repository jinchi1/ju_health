package globesoft.ju_heath.Activity.ScheduleFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import globesoft.ju_heath.Activity.MemberFragment.PagerAdapter;
import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-01-22.
 */

public class PagerWriteScheduleFragment extends Fragment {


    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_schedulepager, container, false);

            ViewPager newsViewPager = (ViewPager)view.findViewById(R.id.viewPager);

            Bundle bundle = getArguments();
            String startday = bundle.getString("startDay");
            boolean isHalf = bundle.getBoolean("isHalf");
            int week = bundle.getInt("week");
            int people = bundle.getInt("people");

            if(!isHalf) {
                PagerAdapter newPagerAdapter = new PagerAdapter(getChildFragmentManager());
                for (int i = 0; i < week; i++) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("startDay",startday);
                    bundle1.putInt("people",people);
                    bundle1.putInt("week",i);
                    WriteScheduleFragment writeScheduleFragment = new WriteScheduleFragment();
                    writeScheduleFragment.setArguments(bundle1);
                    newPagerAdapter.addFragment(writeScheduleFragment, "Schedule" + i);
                }
                newsViewPager.setAdapter(newPagerAdapter);
                newsViewPager.setCurrentItem(0);
                newsViewPager.setOffscreenPageLimit(1);
                newPagerAdapter.notifyDataSetChanged();
            }
            else if(isHalf)
            {
                PagerAdapter newPagerAdapter = new PagerAdapter(getChildFragmentManager());
                for(int i=0; i<week;i++) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("startDay",startday);
                    bundle1.putInt("people",people);
                    bundle1.putInt("week",i);
                    WriteScheduleFragmenthalf writeScheduleFragment = new WriteScheduleFragmenthalf();
                    writeScheduleFragment.setArguments(bundle1);
                    newPagerAdapter.addFragment(writeScheduleFragment, "Schedule"+i);
                }
                newsViewPager.setAdapter(newPagerAdapter);
                newsViewPager.setCurrentItem(0);
                newsViewPager.setOffscreenPageLimit(2);
                newPagerAdapter.notifyDataSetChanged();
            }
        }
        return view;
    }

}
