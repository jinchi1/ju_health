package globesoft.ju_heath.Activity.ScheduleFragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import globesoft.ju_heath.DTO.LessonDTO;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2018-01-16.
 */

public class WriteScheduleAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<LessonDTO> dataArr;
    private WriteScheduleFragment fragment;
    private WriteScheduleFragmenthalf fragmenthalf;
    private ModifyScheduleFragment modifyFragment;
    private ModifyScheduleFragmenthalf modifyFragmenthalf;

    public WriteScheduleAdapter(Context context, ArrayList<LessonDTO> arrayList)
    {
        this.context = context;
        this.dataArr = arrayList;
    }

    public WriteScheduleAdapter(Context context, ArrayList<LessonDTO> arrayList, WriteScheduleFragment fragment)
    {
        this.context = context;
        this.dataArr = arrayList;
        this.fragment = fragment;
    }


    public WriteScheduleAdapter(Context context, ArrayList<LessonDTO> arrayList, WriteScheduleFragmenthalf fragmenthalf)
    {
        this.context = context;
        this.dataArr = arrayList;
        this.fragmenthalf = fragmenthalf;
    }

    public WriteScheduleAdapter(Context context, ArrayList<LessonDTO> arrayList, ModifyScheduleFragment modifyFragment)
    {
        this.context = context;
        this.dataArr = arrayList;
        this.modifyFragment = modifyFragment;
    }

    public WriteScheduleAdapter(Context context, ArrayList<LessonDTO> arrayList, ModifyScheduleFragmenthalf modifyFragmenthalf)
    {
        this.context = context;
        this.dataArr = arrayList;
        this.modifyFragmenthalf = modifyFragmenthalf;
    }

    @Override
    public int getCount() {
        return dataArr.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addItems(ArrayList<LessonDTO> addItem) {
        dataArr = addItem;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View v = view;
        ViewHolder viewHolder;
        final LessonDTO data = dataArr.get(position);

        if(v==null)
        {
            v= LayoutInflater.from(context).inflate(R.layout.row_writeschedule, null);
            viewHolder = new ViewHolder();
            viewHolder.checkbox = (CheckBox)v.findViewById(R.id.checkbox);
            viewHolder.rowLayout = (LinearLayout)v.findViewById(R.id.row_layout);
            viewHolder.rowImageView = (ImageView)v.findViewById(R.id.row_image);
            viewHolder.rowTextView = (TextView)v.findViewById(R.id.row_text);
            v.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder)view.getTag();
        }

        if(data.check)
        {
            viewHolder.checkbox.setChecked(true);
            //LogUtil.d("hellow"+position);
        }
        else
        {
            viewHolder.checkbox.setChecked(false);

        }

        if(data.state)
        {

            viewHolder.rowLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            viewHolder.rowImageView.setVisibility(View.GONE);
            viewHolder.rowTextView.setVisibility(View.GONE);
            if(data.name!=null&&data.name.size()>0)
            {
                viewHolder.rowImageView.setVisibility(View.VISIBLE);
                viewHolder.rowTextView.setVisibility(View.VISIBLE);
                //LogUtil.d("pppppppppppppppppppppppppppppppppp"+data.name);
                String a = data.name.get(0).substring(0,1);
                LogUtil.d("aaaaaaaaaaaaaaaaaaaaaaaaa"+a);
                viewHolder.rowTextView.setText(a);
            }
        }
        else
        {
            viewHolder.rowLayout.setBackgroundColor(Color.parseColor("#dfdfdf"));
            viewHolder.rowImageView.setVisibility(View.GONE);
            viewHolder.rowTextView.setVisibility(View.GONE);
        }




        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(fragment!=null)
                {
                    if(isChecked)
                    {
                        data.check = true;
                        fragment.modifyButton.setImageResource(R.drawable.modify_button);
                    }
                    else
                    {
                        data.check = false;
                        for(int i=0; i<126; i++)
                        {
                            if(dataArr.get(i).check)
                            {
                               fragment.modifyButton.setImageResource(R.drawable.modify_button);
                                break;
                            }
                            else
                            {
                               fragment.modifyButton.setImageResource(R.drawable.modify_button_grey);
                            }
                        }
                    }
                }
                else if(fragmenthalf!=null)
                {
                    if(isChecked)
                    {
                        data.check = true;
                        fragmenthalf.modifyButton.setImageResource(R.drawable.modify_button);
                    }
                    else
                    {
                        data.check = false;
                        for(int i=0; i<252; i++)
                        {
                            if(dataArr.get(i).check)
                            {
                                fragmenthalf.modifyButton.setImageResource(R.drawable.modify_button);
                                break;
                            }
                            else
                            {
                                fragmenthalf.modifyButton.setImageResource(R.drawable.modify_button_grey);
                            }
                        }
                    }

                }
                else if(modifyFragment!=null)
                {
                    if(isChecked)
                    {
                        data.check = true;
                        modifyFragment.modifyButton.setImageResource(R.drawable.modify_button);
                    }
                    else
                    {
                        data.check = false;
                        for(int i=0; i<126; i++)
                        {
                            if(dataArr.get(i).check)
                            {
                                modifyFragment.modifyButton.setImageResource(R.drawable.modify_button);
                                break;
                            }
                            else
                            {
                                modifyFragment.modifyButton.setImageResource(R.drawable.modify_button_grey);
                            }
                        }
                    }
                }
                else if(modifyFragmenthalf!=null)
                {
                    if(isChecked)
                    {
                        data.check = true;
                        modifyFragmenthalf.modifyButton.setImageResource(R.drawable.modify_button);
                    }
                    else
                    {
                        data.check = false;
                        for(int i=0; i<252; i++)
                        {
                            if(dataArr.get(i).check)
                            {
                                modifyFragmenthalf.modifyButton.setImageResource(R.drawable.modify_button);
                                break;
                            }
                            else
                            {
                                modifyFragmenthalf.modifyButton.setImageResource(R.drawable.modify_button_grey);
                            }
                        }
                    }

                }


            }
        });


        return v;
    }


    private static class ViewHolder {

        public CheckBox checkbox;
        public LinearLayout rowLayout;
        public ImageView rowImageView;
        public TextView rowTextView;

    }

}
