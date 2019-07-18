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
import com.android.aquagiraffe.rotaryconnect.model.PdfModel;

import java.util.ArrayList;

public class AnnounsmentAdapter extends BaseAdapter
{
    Context c;
    ArrayList<PdfModel> spacecrafts = new ArrayList<PdfModel>();
    ArrayList<String> filePaths;


    ImageView imageView, img_cross;
    TextView textView;


    public AnnounsmentAdapter(Context c, ArrayList<PdfModel> spacecrafts, ArrayList<String> filePaths)
    {
        this.c = c;
        this.spacecrafts = spacecrafts;
        this.filePaths= filePaths;
    }

    @Override
    public int getCount()
    {
        return spacecrafts.size();
    }

    @Override
    public Object getItem(int position)
    {
        return spacecrafts.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {

        final PdfModel pdfModel = (PdfModel) this.getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(c).inflate(com.android.aquagiraffe.rotaryconnect.R.layout.pdfmodel, parent,false);
        }

        textView = convertView.findViewById(com.android.aquagiraffe.rotaryconnect.R.id.nameTxt);
        imageView = convertView.findViewById(com.android.aquagiraffe.rotaryconnect.R.id.spacecraftImg);
        textView.setText(pdfModel.getName());
        textView.setTypeface(Typeface.createFromAsset(c.getAssets(),"RobotoBold.ttf"));
        img_cross = convertView.findViewById(com.android.aquagiraffe.rotaryconnect.R.id.img_cross);

        img_cross.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                spacecrafts.remove(pdfModel);
                AnnounsmentAdapter.this.notifyDataSetChanged();
                filePaths.remove(position);
                notifyDataSetInvalidated();
            }
        });

        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(c, pdfModel.getName(), Toast.LENGTH_SHORT).show();
                OpenPdfView(pdfModel.getPath());
            }
        });
        return convertView;
    }

    private void OpenPdfView(String uri)
    {
        Intent intent = new Intent(c, PdfActivity.class);
        intent.putExtra("PATH", uri);
        c.startActivity(intent);
    }

}
