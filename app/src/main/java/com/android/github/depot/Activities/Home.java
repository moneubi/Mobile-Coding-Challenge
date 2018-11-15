package com.android.github.depot.Activities;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.github.depot.Adapters.Depot_Adapter;
import com.android.github.depot.Model.Depot;
import com.android.github.depot.R;
import com.android.github.depot.Utilities.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /*==============================================================*/
    /* Mes valeurs statics */
    private static final String TAG = Home.class.getSimpleName();
    private JSONObject json = null;
    /* Fin */
    /* Les composants de mon activité */
    private Toolbar toolbar;
    private LinearLayout l_sect_loader, l_sect_liste;
    private ListView lst_alls;
    private RelativeLayout r_view_other;
    /* Fin */
    /* Composants externes */
    private Properties properties;
    /* Fin */
    private List<Depot> depots = new ArrayList<>();
    private Depot depot;
    private Depot_Adapter adapter;
    private int init_pag = 0;
    /*==============================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*==========================================================*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Android Challenge");
        setSupportActionBar(toolbar);
        /*==========================================================*/
        /* Instatiation de ma classe externe */
        properties = new Properties();
        /* Fin */
        /*==========================================================*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*==========================================================*/
        lst_alls = (ListView) findViewById(R.id.lst_alls);
        r_view_other = (RelativeLayout)findViewById(R.id.r_view_other);
        l_sect_loader = (LinearLayout)findViewById(R.id.l_sect_loader);
        l_sect_liste = (LinearLayout)findViewById(R.id.l_sect_liste);
        /*==========================================================*/
        /* Récupérer la liste des riposfindViewById(R.id.r_view_other)itory */
        try {
            OkHttpClient client = new OkHttpClient();
            Response response = null;

            Request request = new Request.Builder()
                    .url(properties.getHostAPI())
                    .header("User-Agent", "OkHttp Headers.java")
                    .addHeader("Content-Type", "text/json; Charset=UTF-8")
                    .addHeader("Accept", "application/json; q=0.5")
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .build();
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                response = client.newCall(request).execute();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "Erreur :" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            json = new JSONObject(response.body().string());
                            Log.d(TAG, "Liste des dépôts: " + json);
                            JSONArray jdepots = json.optJSONArray("items");
                            Log.d(TAG, "Dépôts: " + json.optJSONArray("items"));
                            if (jdepots != null) {
                                Log.d(TAG, "nombre dépôts: " + jdepots.length());

                                for (int i = 0; i < jdepots.length(); i++){
                                    JSONObject jdepot = jdepots.getJSONObject(i);

                                    if (jdepot != null){
                                        JSONObject juser = jdepot.getJSONObject("owner");
                                        depot = new Depot();
                                        depot.setName(jdepot.optString("name"));
                                        depot.setDescription(jdepot.optString("description"));
                                        depot.setScore(jdepot.optString("name"));
                                        depot.setScore(jdepot.optString("stargazers_count"));
                                        depot.setUser(juser.optString("login"));
                                        depot.setAvatar(juser.optString("avatar_url"));

                                        depots.add(depot);

                                    }
                                }
                                /* Reload View */
                                Home.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter = new Depot_Adapter(Home.this,depots);
                                        lst_alls.setAdapter(adapter);
                                        findViewById(R.id.l_sect_loader).setVisibility(View.GONE);
                                        findViewById(R.id.l_sect_liste).setVisibility(View.VISIBLE);
                                    }
                                });
                                /* End */
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (response.body() != null) {
                        response.body().close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Fin */
        /*==========================================================*/
        /* écouter le scroll de la listview */
        lst_alls.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (lst_alls.getLastVisiblePosition() == 29){
                    if (init_pag == 0){
                        init_pag = 1;
                        r_view_other.setVisibility(View.VISIBLE);

                        Start_Pagination("2");
                    }
                }else if (lst_alls.getLastVisiblePosition() == 59){
                    if (init_pag == 1){
                        init_pag = 2;
                        r_view_other.setVisibility(View.VISIBLE);

                        Start_Pagination("3");
                    }
                }else if (lst_alls.getLastVisiblePosition() == 89){
                    if (init_pag == 2){
                        init_pag = 3;
                        r_view_other.setVisibility(View.VISIBLE);

                        Start_Pagination("4");
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        /* Fin */
    }

    /*==============================================================*/
    /* My overrides méthodes */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /* Fin */
    /*==============================================================*/
    /* call api Pagination */
    private void Start_Pagination(String page){
        try {
            OkHttpClient client = new OkHttpClient();
            Response response = null;

            Request request = new Request.Builder()
                    .url(properties.getHostAPI()+"&page="+page)
                    .header("User-Agent", "OkHttp Headers.java")
                    .addHeader("Content-Type", "text/json; Charset=UTF-8")
                    .addHeader("Accept", "application/json; q=0.5")
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .build();
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                response = client.newCall(request).execute();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "Erreur :" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jso = new JSONObject(response.body().string());
                            JSONArray jdepots = json.optJSONArray("items");
                            if (jdepots != null) {

                                for (int i = 0; i < jdepots.length(); i++){
                                    JSONObject jdepot = jdepots.getJSONObject(i);

                                    if (jdepot != null){
                                        JSONObject juser = jdepot.getJSONObject("owner");
                                        depot = new Depot();
                                        depot.setName(jdepot.optString("name"));
                                        depot.setDescription(jdepot.optString("description"));
                                        depot.setScore(jdepot.optString("name"));
                                        depot.setScore(jdepot.optString("stargazers_count"));
                                        depot.setUser(juser.optString("login"));
                                        depot.setAvatar(juser.optString("avatar_url"));

                                        depots.add(depot);
                                    }
                                }
                                /* Reload View */
                                Home.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG,"Nbr Ligne liste :"+depots.size());
                                        adapter.notifyDataSetChanged();
                                        findViewById(R.id.r_view_other).setVisibility(View.GONE);
                                    }
                                });
                                /* End */
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (response.body() != null) {
                        response.body().close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /* Fin */
    /*==============================================================*/
}
