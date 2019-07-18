package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.aquagiraffe.rotaryconnect.PdfActivity;
import com.android.aquagiraffe.rotaryconnect.R;
import com.android.aquagiraffe.rotaryconnect.model.BookModel;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {

    Context c;
    ArrayList<BookModel> spacecrafts;
    ArrayList<String> filePaths;


    public BookAdapter(Context c, ArrayList<BookModel> spacecrafts, ArrayList<String> filePaths) {
        this.c = c;
        this.spacecrafts = spacecrafts;
        this.filePaths = filePaths;

    }

    @Override
    public int getCount() {
        return spacecrafts.size();
    }

    @Override
    public Object getItem(int position) {
        return spacecrafts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            convertView = LayoutInflater.from(c).inflate(R.layout.bookmodel, parent,false);
        }
        final BookModel bookModel = (BookModel) this.getItem(position);

        TextView textView = convertView.findViewById(R.id.nameTxt);
        textView.setTypeface(Typeface.createFromAsset(c.getAssets(),"RobotoBold.ttf"));
        ImageView imageView = convertView.findViewById(R.id.ImgBook);
        textView.setText(bookModel.getName());
        ImageView imgCross = convertView.findViewById(R.id.imgCross);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, bookModel.getName(), Toast.LENGTH_SHORT).show();
                OpenPdfView(bookModel.getPath());
            }
        });

        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spacecrafts.remove(position);
                filePaths.remove(position);

                notifyDataSetChanged();
                notifyDataSetInvalidated();
            }
        });

        return convertView;
    }

    private void OpenPdfView(String uri) {

        Intent intent = new Intent(c,PdfActivity.class);
        intent.putExtra("PATH",uri);
        c.startActivity(intent);
    }
}
