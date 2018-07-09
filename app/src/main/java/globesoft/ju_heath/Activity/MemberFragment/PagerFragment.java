package globesoft.ju_heath.Activity.MemberFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-01-12.
 */

public class PagerFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_memberpager, container, false);

            ViewPager newsViewPager = (ViewPager) view.findViewById(R.id.viewPager);

            PagerAdapter newPagerAdapter = new PagerAdapter(getChildFragmentManager());
            newPagerAdapter.addFragment(new MemberFragment(), getString(R.string.member));
            newPagerAdapter.addFragment(new NonMemberFragment(), getString(R.string.Nonmember));
            newsViewPager.setAdapter(newPagerAdapter);
            newsViewPager.setCurrentItem(0);
            newPagerAdapter.notifyDataSetChanged();
            TabLayout tab_lay = (TabLayout) view.findViewById(R.id.tabLay);
            tab_lay.setupWithViewPager(newsViewPager);

        }
        return view;
    }

}
