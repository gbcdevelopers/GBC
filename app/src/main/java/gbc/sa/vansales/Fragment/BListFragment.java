package gbc.sa.vansales.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gbc.sa.vansales.R;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class BListFragment extends Fragment {


    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.activity_gnbreturn,container,false);


        return view;
    }
}
