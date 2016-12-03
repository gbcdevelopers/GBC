package gbc.sa.vansales.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DataAdapter;

/**
 * Created by eheuristic on 12/2/2016.
 */

public class AllCustomerFragment extends Fragment {

    DataAdapter dataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_begin_day, container, false);

    }
}
