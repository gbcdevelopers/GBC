package gbc.sa.vansales.views;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import gbc.sa.vansales.R;
/**
 * Created by Rakshit on 18-Nov-16.
 */
public class TileView extends RelativeLayout {

    private int background;
    private boolean square;
    private String label;
    private Drawable icon;

    private RelativeLayout tileView;
    private TextView labelView;
    private ImageView iconView;

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TileView, 0, 0);

        try {
            square = a.getBoolean(R.styleable.TileView_square, true);
            background = a.getColor(R.styleable.TileView_mybackground, getResources().getColor(R.color.tile_background));
            icon = a.getDrawable(R.styleable.TileView_myicon);
            label = a.getString(R.styleable.TileView_label);
        } finally {
            a.recycle();
        }

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tileView.setBackgroundColor(getResources().getColor(R.color.tile_highlight));

                    tileView.invalidate();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    tileView.setBackgroundColor(background);

                    tileView.invalidate();
                }

                return false;
            }
        });

        View.inflate(getContext(), R.layout.layout_tile, this);
    }

    public String getLabel() {
        return label;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tileView = (RelativeLayout) findViewById(R.id.tileView);
        labelView = (TextView) findViewById(R.id.labelView);
        iconView = (ImageView) findViewById(R.id.iconView);

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (square) {
            heightMeasureSpec = widthMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        if (background < 0) {
            tileView.setBackgroundColor(background);
        }

        labelView.setText(label);

      //  iconView.setBackground(icon);
    }
}
