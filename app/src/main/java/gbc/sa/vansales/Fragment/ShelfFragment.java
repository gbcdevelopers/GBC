package gbc.sa.vansales.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.CategoryListActivity;
import gbc.sa.vansales.adapters.SalesAdapter;
import gbc.sa.vansales.data.Const;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class ShelfFragment extends Fragment {

    View viewmain;
    ListView listShelf;
  public static SalesAdapter adapter;

    FloatingActionButton fab;


//    ArrayList<CustomerData> dataArrayList;


   public static ArrayList<String> arrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewmain =inflater.inflate(R.layout.fragment_shelf,container,false);
        listShelf=(ListView) viewmain.findViewById(R.id.list_shelf);

        fab=(FloatingActionButton)viewmain.findViewById(R.id.fab);

        Const.addlist.clear();
        arrayList = new ArrayList<>();
        arrayList.add("AM5508 LARGE SWISS ROLL VANILL (24x110G)");
        arrayList.add("BM5508 SMALL SWISS ROLL VANILL (24x110G)");
        arrayList.add("CM5508 BIG SWISS ROLL VANILL (24x110G)");
        arrayList.add("DM5508 SWISS ROLL VANILL (24x110G)");
        arrayList.add("EM5508 ROLL VANILL (24x110G)");



        adapter=new SalesAdapter(getActivity().getBaseContext(),arrayList, R.layout.sales_list);
        listShelf.setAdapter(adapter);

        listShelf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {


                final Dialog dialog=new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_with_crossbutton);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ImageView iv_cancle=(ImageView)dialog.findViewById(R.id.imageView_close);
                Button btn_save=(Button)dialog.findViewById(R.id.btn_save);
                final EditText ed_cases=(EditText)dialog.findViewById(R.id.ed_cases);
                final EditText ed_pcs=(EditText)dialog.findViewById(R.id.ed_pcs);


                LinearLayout ll_1=(LinearLayout)dialog.findViewById(R.id.ll_1);
                ll_1.setVisibility(View.GONE);
                iv_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();


                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String strCase=ed_cases.getText().toString();
                        String strpcs=ed_pcs.getText().toString();


                        TextView tv_cases=(TextView)view.findViewById(R.id.tv_cases_value);
                        TextView tv_pcs=(TextView)view.findViewById(R.id.tv_pcs_value);

                        tv_cases.setText(strCase);
                        tv_pcs.setText(strpcs);

                        dialog.dismiss();

                    }
                });

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), CategoryListActivity.class);
                getActivity().startActivity(intent);


            }
        });


        return viewmain;
    }


}
