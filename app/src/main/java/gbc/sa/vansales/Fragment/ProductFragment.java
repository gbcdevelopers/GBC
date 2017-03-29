package gbc.sa.vansales.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.ItemComplaints;
import gbc.sa.vansales.activities.SalesInvoiceActivity;
import gbc.sa.vansales.adapters.ProductListAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by eheuristic on 12/21/2016.
 */
public class ProductFragment extends Fragment {
    ListView list_product;
    View view;
    View common_header;
    ArrayList<String> arrayList;
    ProductListAdapter adapter;
    FloatingActionButton button;
    ArrayList<ArticleHeader> articles = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_product_list, container, false);
        list_product = (ListView) view.findViewById(R.id.list_product);
        articles = ArticleHeaders.get();
        common_header = (View) view.findViewById(R.id.inc_common_header);
        common_header.setVisibility(View.GONE);
        button = (FloatingActionButton) view.findViewById(R.id.btn_float);
        arrayList = new ArrayList<>();
        for (ArticleHeader article : articles) {
            arrayList.add(UrlBuilder.decodeString(article.getMaterialDesc1()));
        }
        adapter = new ProductListAdapter(getActivity(), arrayList, R.layout.checkable_productlist, "product");
        list_product.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("IN PRODUCT CLicked", "CLICK");
                if (SalesInvoiceActivity.tab_position == 2) {
                    GListFragment.setProductList();
                } else if (SalesInvoiceActivity.tab_position == 3) {
                    BListFragment.setProductList();
                } else if (SalesInvoiceActivity.tab_position == 99) {
                    ItemComplaints.setProductList();
                }
                getActivity().finish();
            }
        });
        return view;
    }
}
