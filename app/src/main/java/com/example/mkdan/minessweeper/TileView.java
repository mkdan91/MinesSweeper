package com.example.mkdan.minessweeper;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TileView extends LinearLayout {
    public TextView text;

    public TileView(Context context) {
        super(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        text = new TextView(context);
        text.setLayoutParams(layoutParams);
        text.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        text.setGravity(Gravity.CENTER_VERTICAL);
        text.setTextSize(20);
        text.setTextColor(Color.BLACK);
        setBackgroundResource(R.drawable.button_shape);
        this.addView(text);

    }
}
