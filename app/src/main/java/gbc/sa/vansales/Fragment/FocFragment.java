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
import gbc.sa.vansales.models.Sales;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class FocFragment extends Fragment {


    View viewmain;
    ListView listSales;
    public static  SalesAdapter adapter;
    public static ArrayList<Sales> salesarrayList;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewmain =inflater.inflate(R.layout.fragment_salesinvoice,container,false);
        listSales=(ListView) viewmain.findViewById(R.id.list_sales);
        fab=(FloatingActionButton) viewmain.findViewById(R.id.fab);

        String strProductname[]={"A","B","c","D"};
        salesarrayList = new ArrayList<>();


        for(int i=0;i<4;i++)
        {
            Sales product=new Sales();
            product.setName(strProductname[i]);
            product.setCases("0");
            product.setPic("0");

            salesarrayList.add(product);
        }



        adapter=new SalesAdapter(getActivity(),salesarrayList);
        listSales.setAdapter(adapter);

        listSales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,final View view,final int position, long id) {



                    final Dialog dialog=new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_with_crossbutton);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    ImageView iv_cancle=(ImageView)dialog.findViewById(R.id.imageView_close);

                    Button btn_save=(Button)dialog.findViewById(R.id.btn_save);
                    final EditText ed_cases=(EditText)dialog.findViewById(R.id.ed_cases);
                    final EditText ed_pcs=(EditText)dialog.findViewById(R.id.ed_pcs);

                    final EditText ed_cases_inv=(EditText)dialog.findViewById(R.id.ed_cases_inv);
                    final EditText ed_pcs_inv=(EditText)dialog.findViewById(R.id.ed_pcs_inv);


                        ed_cases_inv.setText("10");
                        ed_pcs_inv.setText("3");
                        ed_cases_inv.setEnabled(false);
                        ed_pcs_inv.setEnabled(false);




                    LinearLayout ll_1=(LinearLayout)dialog.findViewById(R.id.ll_1);


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
                        String strcaseinv=ed_cases_inv.getText().toString();
                        String strpcsinv=ed_pcs_inv.getText().toString();


                        TextView tv_cases=(TextView)view.findViewById(R.id.tv_cases_value);
                        TextView tv_pcs=(TextView)view.findViewById(R.id.tv_pcs_value);


                        tv_cases.setText(strCase);
                        tv_pcs.setText(strpcs);


                        Sales sales=salesarrayList.get(position);
                        sales.setPic(strpcs);
                        sales.setCases(strCase);
                        double total=0;

                        for(int i=0;i<salesarrayList.size();i++)
                        {
                            Sales sales1=salesarrayList.get(i);
                            total =total+(Double.parseDouble(sales1.getCases())*54+ Double.parseDouble(sales1.getPic())*2.25) ;


                        }
                        TextView tv=(TextView)viewmain.findViewById(R.id.tv_amt);
                        tv.setText(String.valueOf(total));

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
