package com.reportdroid.shibuyaxpress.reportdroid.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ResultCallback;
import com.reportdroid.shibuyaxpress.reportdroid.Models.Report;
import com.reportdroid.shibuyaxpress.reportdroid.Models.ResponseUser;
import com.reportdroid.shibuyaxpress.reportdroid.Models.User;
import com.reportdroid.shibuyaxpress.reportdroid.R;
import com.reportdroid.shibuyaxpress.reportdroid.Adapters.ReportAdapter;
import com.reportdroid.shibuyaxpress.reportdroid.Services.ApiService;
import com.reportdroid.shibuyaxpress.reportdroid.Services.ApiServiceGenerator;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView reportesList;
    private static final String TAG = MenuActivity.class.getSimpleName();
    private SwipeRefreshLayout refreshLayout;
    private CircleImageView profileNav;
    private TextView nameNav;
    private TextView emailNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        refreshLayout=findViewById(R.id.refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        refreshLayout.setRefreshing(false);
                        initialize();

                    }
                }, 2000);

            }
        });

        initialize();

        reportesList=findViewById(R.id.recicler_report);
        reportesList.setLayoutManager(new LinearLayoutManager(this));
        reportesList.setAdapter(new ReportAdapter(this));

        //initialize
        initialize();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launcher=new Intent(MenuActivity.this,RegisterReportActivity.class);
                startActivityForResult(launcher,REGISTER_FORM_REQUEST);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        profileNav=navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        nameNav=navigationView.getHeaderView(0).findViewById(R.id.txt_nav_name);
        emailNav=navigationView.getHeaderView(0).findViewById(R.id.txt_nav_email);
        String url = ApiService.API_BASE_URL + "/images/" + User.getInstance().getProfile();
        Glide.with(MenuActivity.this).load(url).into(profileNav);
        nameNav.setText(User.getInstance().getUsername());
        emailNav.setText(User.getInstance().getEmail());
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initialize() {

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<Report>> call = service.getReports();

        call.enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Report> reportes = response.body();
                        Log.d(TAG, "reportes: " + reportes.get(0).getUser_id());

                        ReportAdapter adapter = (ReportAdapter) reportesList.getAdapter();
                        adapter.setReportes(reportes);
                        adapter.notifyDataSetChanged();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }
    protected void LogOut(){
        final AlertDialog.Builder windows=new AlertDialog.Builder(this);
        windows.setTitle("Cerrar Sesión");
        windows.setMessage("¿Seguro desea salir de la aplicación?");
        windows.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"Log out",Toast.LENGTH_LONG).show();
                        Intent rocket=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(rocket);
                        finish();
                        overridePendingTransition(R.anim.animex_triad,R.anim.animex_four);
                    }
        });
        windows.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //hacer nada

            }
        });

        windows.show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            SalirApp();
        }
    }
    private static final int REGISTER_FORM_REQUEST = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTER_FORM_REQUEST) {
            // refresh data
            initialize();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_person) {
            // Handle the camera action
        } else if (id == R.id.nav_exit) {
            LogOut();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void SalirApp(){
        AlertDialog.Builder bob=new AlertDialog.Builder(this);
        bob.setTitle("ReportDroid");
        bob.setMessage("¿Desea salir de la aplicación?");
        bob.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        bob.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        bob.show();
    }
}
