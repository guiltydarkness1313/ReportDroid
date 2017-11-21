package com.reportdroid.shibuyaxpress.reportdroid.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.reportdroid.shibuyaxpress.reportdroid.R;

/**
 * Created by paulf on 16-Nov-17.
 */

public class ReportHolder extends RecyclerView.ViewHolder {
   public ImageView imgReport;
    public TextView title;
    public TextView autor;
    public TextView location;
    public ReportHolder(View v) {
        super(v);
        imgReport=v.findViewById(R.id.img_report);
        title=v.findViewById(R.id.txt_title);
        autor=v.findViewById(R.id.txt_autor);
        location=v.findViewById(R.id.txt_location);
    }
}
