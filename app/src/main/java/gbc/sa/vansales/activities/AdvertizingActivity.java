package gbc.sa.vansales.activities;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.GalleryImageAdapter;
/**
 * Created by Rakshit on 16-Apr-17.
 */
public class AdvertizingActivity extends AppCompatActivity {
    ImageView selectedImage;
    ImageView iv_back;
    TextView tv_top_header;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        Gallery gallery = (Gallery) findViewById(R.id.gallery);
        selectedImage = (ImageView) findViewById(R.id.imageView);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Advertizement");

        gallery.setSpacing(1);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(this);
        gallery.setAdapter(galleryImageAdapter);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // show the selected Image
                selectedImage.setImageResource(galleryImageAdapter.mImageIds[position]);
            }
        });
    }

}
