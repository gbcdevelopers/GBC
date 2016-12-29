package gbc.sa.vansales.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.CategoryListActivity;
import gbc.sa.vansales.activities.SalesInvoiceActivity;
import gbc.sa.vansales.adapters.ExpandReturnAdapter;
import gbc.sa.vansales.utils.AnimatedExpandableListView;
import gbc.sa.vansales.utils.OnSwipeTouchListener;
import gbc.sa.vansales.utils.Settings;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class GListFragment extends Fragment {


    public static ExpandReturnAdapter adapter;
    public static ArrayList<String> arrProductList;
    View view;
    FloatingActionButton btn_float;

    AnimatedExpandableListView exp_list;
   public static RelativeLayout rl_middle;

    private SwipeLayout swipeLayout;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.activity_gnbreturn,container,false);
        btn_float=(FloatingActionButton)view.findViewById(R.id.fab);
        exp_list=(AnimatedExpandableListView)view.findViewById(R.id.exp_product);
        rl_middle=(RelativeLayout)view.findViewById(R.id.rl_middle);

        arrProductList=new ArrayList<>();


        if(arrProductList.size()==0)
        {
            rl_middle.setVisibility(View.VISIBLE);
        }else {
            rl_middle.setVisibility(View.GONE);
        }

        adapter=new ExpandReturnAdapter(getActivity(),arrProductList,exp_list,"glist");
        exp_list.setAdapter(adapter);
        setListView();





        btn_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SalesInvoiceActivity.tab_position=2;
                Settings.setString("from","glist");
                Intent intent=new Intent(getActivity(), CategoryListActivity.class);
                getActivity().startActivity(intent);
            }
        });

       exp_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
           @Override
           public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
               return true;
           }
       });







        return view;
    }

    private void setListView() {
        try{
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View header = inflater.inflate(R.layout.expand_return_groupview, exp_list, false);
            swipeLayout = (SwipeLayout)header.findViewById(R.id.swipe_layout);



            setSwipeViewFeatures();


        }
        catch (Exception e){
            e.printStackTrace();
        }

        // listView.addHeaderView(header);
    }

    private void setSwipeViewFeatures() {
        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, getView().findViewById(R.id.bottom_wrapper));
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                Log.i("adapter", "onClose");
            }
            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                Log.i("adapter", "on swiping");
            }
            @Override
            public void onStartOpen(SwipeLayout layout) {
                Log.i("adapter", "on start open");
            }
            @Override
            public void onOpen(SwipeLayout layout) {
                Log.i("adapter", "the BottomView totally show");
            }
            @Override
            public void onStartClose(SwipeLayout layout) {
                Log.i("adapter", "the BottomView totally close");
            }
            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
    }
}
