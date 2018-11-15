package com.android.github.depot.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.github.depot.Model.Depot;
import com.android.github.depot.R;
import com.android.github.depot.Utilities.GlideApp;

import java.util.List;

public class Depot_Adapter extends BaseAdapter {
    /*==========================================================*/
    private Context context;
    private LayoutInflater inflater;
    private List<Depot> depots;
    /*==========================================================*/
    /*==========================================================*/
    /* Mon constructeur */

    public Depot_Adapter(Context context, List<Depot> depots) {
        this.context = context;
        this.depots = depots;
        notifyDataSetChanged();
    }
    /*==========================================================*/

    @Override
    public int getCount() {
        return depots.size();
    }

    @Override
    public Object getItem(int i) {
        return depots.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView txt_name_depot,txt_description_depot,txt_name_user,txt_nbr_etoile;
        ImageView img_admin;
        /*==========================================================*/
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.depot_item, null);
        /*==========================================================*/
        txt_name_depot = (TextView)view.findViewById(R.id.txt_name_depot);
        txt_description_depot = (TextView)view.findViewById(R.id.txt_description_depot);
        txt_name_user = (TextView)view.findViewById(R.id.txt_name_admin);
        txt_nbr_etoile = (TextView)view.findViewById(R.id.txt_nbr_etoile);
        img_admin = (ImageView)view.findViewById(R.id.img_admin);
        /*==========================================================*/
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTStd_Roman.otf");
        Typeface typeFacegold = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeuecondenced.otf");
        txt_name_depot.setTypeface(typeFacegold);
        txt_description_depot.setTypeface(typeFace);
        txt_name_user.setTypeface(typeFace);
        txt_nbr_etoile.setTypeface(typeFacegold);
        /*==========================================================*/
        Depot depot = depots.get(i);
        txt_name_depot.setText(depot.getName());
        txt_description_depot.setText(depot.getDescription());
        txt_name_user.setText(depot.getUser());
        int nbretoile = Integer.parseInt(depot.getScore());
        float nbrk = nbretoile / 1000;
        txt_nbr_etoile.setText(String.valueOf(nbrk)+" K");
        GlideApp.with(context).load(depot.getAvatar()).circleCrop().placeholder(R.drawable.placeholder).into(img_admin);
        /*==========================================================*/
        return view;
    }
}
