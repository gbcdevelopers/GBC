package gbc.sa.vansales.Fragment;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.CategoryListActivity;
import gbc.sa.vansales.activities.ShelfStockActivity;
import gbc.sa.vansales.adapters.SalesAdapter;
import gbc.sa.vansales.adapters.ShelfAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.models.ShelfProduct;
import gbc.sa.vansales.utils.Settings;

/**
 * Created by eheuristic on 12/5/2016.
 */
public class ShelfFragment extends Fragment {
    View viewmain;
    ListView listShelf;
    public static ShelfAdapter adapter;
    FloatingActionButton fab;
//    ArrayList<CustomerData> dataArrayList;
    public static ArrayList<ShelfProduct> arrayList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewmain = inflater.inflate(R.layout.fragment_shelf, container, false);
        listShelf = (ListView) viewmain.findViewById(R.id.list_shelf);
        fab = (FloatingActionButton) viewmain.findViewById(R.id.fab);
        String strProductname[] = {"AM5508 LARGE SWISS ROLL VANILL (24x110G)", "BM5508 SMALL SWISS ROLL VANILL (24x110G)", "CM5508 BIG SWISS ROLL VANILL (24x110G)", "DM5508 SWISS ROLL VANILL (24x110G)", "EM5508 ROLL VANILL (24x110G)"};
        Const.addlist.clear();
        arrayList = new ArrayList<>();
        adapter = new ShelfAdapter(getActivity().getBaseContext(), arrayList, R.layout.sales_list);
        listShelf.setAdapter(adapter);
        listShelf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final ShelfProduct product = arrayList.get(position);
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_with_crossbutton);
                TextView tv = (TextView)dialog.findViewById(R.id.dv_title);
                tv.setText(product.getProductname());
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ImageView iv_cancle = (ImageView) dialog.findViewById(R.id.imageView_close);
                Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
                final EditText ed_cases = (EditText) dialog.findViewById(R.id.ed_cases);
                final EditText ed_pcs = (EditText) dialog.findViewById(R.id.ed_pcs);
                LinearLayout ll_1 = (LinearLayout) dialog.findViewById(R.id.ll_1);
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
                        String strCase = ed_cases.getText().toString();
                        String strpcs = ed_pcs.getText().toString();
                        TextView tv_cases = (TextView) view.findViewById(R.id.tv_cases_value);
                        TextView tv_pcs = (TextView) view.findViewById(R.id.tv_pcs_value);
                        tv_cases.setText(strCase);
                        tv_pcs.setText(strpcs);

                        if(ed_cases.getText().toString().isEmpty()||ed_cases.getText().toString()==null){
                            strCase = "0";
                        }
                        if(ed_pcs.getText().toString().isEmpty()||ed_pcs.getText().toString()==null){
                            strpcs = "0";
                        }
                        product.setPro_pcs(Integer.parseInt(strpcs));
                        product.setPro_case(Integer.parseInt(strCase));
                        double total = 0;
                        for (int i = 0; i < arrayList.size(); i++) {
                            ShelfProduct product1 = arrayList.get(i);
                            total = total + (product1.getPro_case() * 54 + product1.getPro_pcs() * 2.25);
                            Log.v("pos", product1.getPro_case() + " - " + product1.getPro_pcs() + " - " + total);
                        }
                        TextView tv = (TextView) viewmain.findViewById(R.id.tv_total);
                        tv.setText(String.valueOf(total));
                        dialog.dismiss();
                    }
                });
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Settings.setString("from","shelf");
//                ShelfStockActivity.tab_position=0;
                Intent intent = new Intent(getActivity(), CategoryListActivity.class);

                getActivity().startActivity(intent);
            }
        });
        return viewmain;
    }
}
