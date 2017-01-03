package gbc.sa.vansales.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.MessageListAdapter;
import gbc.sa.vansales.utils.RoundedImageView;

/**
 * Created by eheuristic on 12/2/2016.
 */

public class MessageFragment extends Fragment {

    ListView lv_message;
    RoundedImageView iv_round;
  public static   MessageListAdapter adapter;
    View view;

    String arr[]={"silent meeting","silent meeting"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.message_fragment, container, false);
        lv_message=(ListView)view.findViewById(R.id.lv_messages);
        iv_round=(RoundedImageView)view.findViewById(R.id.roundedImageView);

        adapter=new MessageListAdapter(getActivity().getBaseContext(),arr);
        lv_message.setAdapter(adapter);


        return view;

    }


}
