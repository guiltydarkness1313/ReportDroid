package com.reportdroid.shibuyaxpress.reportdroid.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.reportdroid.shibuyaxpress.reportdroid.Models.Report;
import com.reportdroid.shibuyaxpress.reportdroid.Models.ResponseUser;
import com.reportdroid.shibuyaxpress.reportdroid.Models.User;
import com.reportdroid.shibuyaxpress.reportdroid.R;
import com.reportdroid.shibuyaxpress.reportdroid.Holders.ReportHolder;
import com.reportdroid.shibuyaxpress.reportdroid.Services.ApiService;
import com.reportdroid.shibuyaxpress.reportdroid.Services.ApiServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by paulf on 16-Nov-17.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportHolder> {

    private List<Report> reportes;
    private Activity activity;
    private String autorText;

    public void setAutorText(String autorText) {
        this.autorText = autorText;
    }

    private static final String TAG = ReportAdapter.class.getSimpleName();

    public ReportAdapter(Activity activity){
        this.reportes = new ArrayList<>();
        this.activity=activity;
    }

    public void setReportes(List<Report> reportes) {
        this.reportes = reportes;
    }

    @Override
    public ReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_report, parent, false);
        return new ReportHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReportHolder holder, int position) {

        final Report reporte=this.reportes.get(position);
        holder.location.setText(reporte.getAddress());
        //buscar por id y enganchar aca
        //holder.autor.setText(reporte.getUser_id());
        //holder.autor.setText(autorText);
        //
        ApiService service=ApiServiceGenerator.createService(ApiService.class);
        retrofit2.Call<ResponseUser> call=service.showUser(reporte.getUser_id());
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseUser> call, Response<ResponseUser> response) {
                ResponseUser u=response.body();
                Log.d(TAG,"el autor es " +u.getData().getUsername());
                String autor=u.getData().getUsername();
                holder.autor.setText(autor);
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseUser> call, Throwable t) {
                Log.d(TAG,"nulos errores");
            }
        });
        holder.title.setText(reporte.getTitle());
        String url = ApiService.API_BASE_URL + "/images/" + reporte.getImage();
        //Glide.with(viewHolder.itemView.getContext()).load(url).into(viewHolder.fotoImage);
            Glide.with(holder.itemView.getContext()).load(url).into(holder.imgReport);


    }

    @Override
    public int getItemCount() {
        return this.reportes.size();
    }
}
