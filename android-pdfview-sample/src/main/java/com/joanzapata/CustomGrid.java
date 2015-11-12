package com.joanzapata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.joanzapata.pdfview.sample.R;

import org.vudroid.pdfdroid.codec.PdfContext;

import java.util.ArrayList;

public class CustomGrid extends BaseAdapter {
    private ArrayList<String> imagesId;
    private Context mContext;
    private PdfContext  pdfContext;

    public CustomGrid(Context c, ArrayList<String> imageId) {
        mContext = c;
        imagesId = imageId;
        pdfContext = new PdfContext();
    }

    @Override
    public int getCount() {
        return imagesId.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return imagesId.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.single_grid, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_image);
            // bitmap required
            //imageView.setImageBitmap(renderToBitmap(imagesId.get(position)));
            // Create a bitmap and canvas to draw the page into
        }
        return convertView;
    }


}
