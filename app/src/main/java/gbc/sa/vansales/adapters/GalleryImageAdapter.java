package gbc.sa.vansales.adapters;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import gbc.sa.vansales.R;
/**
 * Created by Rakshit on 16-Apr-17.
 */
public class GalleryImageAdapter extends BaseAdapter {
    private Context mContext;

    public GalleryImageAdapter(Context context)
    {
        mContext = context;
    }
    public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }

    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup)
    {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);
        i.setImageResource(mImageIds[index]);
        i.setLayoutParams(new Gallery.LayoutParams(200, 200));
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        return i;
    }
    public Integer[] mImageIds = {
            R.drawable.advertise1,
            R.drawable.advertise2,
            R.drawable.advertise3,
            R.drawable.advertise4,
            R.drawable.advertise5,
            R.drawable.advertise6,
            R.drawable.advertise1,
            R.drawable.advertise2,
            R.drawable.advertise3
    };
}
