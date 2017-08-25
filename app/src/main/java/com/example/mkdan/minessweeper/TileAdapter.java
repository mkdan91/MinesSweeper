package com.example.mkdan.minessweeper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.mkdan.minessweeper.Logic.Board;




public class TileAdapter extends BaseAdapter {

    private Board mBoard;
    private Context mContext;
    private int[] colors = {Color.GREEN, Color.YELLOW, Color.BLUE, Color.MAGENTA, Color.RED};


    public TileAdapter(Context context, Board board) {
        mBoard = board;
        mContext = context;
    }
    @Override
    public int getCount() {
        return mBoard.getRowColSize()*mBoard.getRowColSize();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TileView tileView;
        tileView = (TileView)convertView;
        if(tileView == null) {
            tileView = new TileView(mContext);

            Log.v("Tile Adapter","creating new view for index "+position);
        } else {
            Log.e("Tile Adapter","RECYCLING view for index "+position);
        }

        if(mBoard.getTileByPosition(position).isPressed())
            if (mBoard.getTileByPosition(position).isMine()) {
                tileView.setBackgroundColor(Color.RED);

                }

             else {
                tileView.text.setText(mBoard.getTileByPosition(position).toString());
                for (int i = 1; i < 9; i++) {
                    if (mBoard.getTileByPosition(position).getmNumber() > 5) {
                        tileView.text.setTextColor(colors[colors.length - 1]);
                        continue;
                    }
                    if (mBoard.getTileByPosition(position).getmNumber() == i) {
                        tileView.text.setTextColor(colors[i - 1]);
                    }
                }
                tileView.setBackgroundColor(Color.GRAY);
            }
        else if(mBoard.getTileByPosition(position).isFlagged()) {
            tileView.setBackgroundResource(R.drawable.flag);
        }
        else {
            tileView.setBackgroundResource(R.drawable.button_shape);
        }
        return tileView;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position ) {
        return mBoard.getTileByPosition(0);
    }
}
