package de.fh_kiel.iue.mob2021_sp_ms;


import android.app.Activity;
import android.content  .Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// UI.
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public Display display;
    ArrayList<String> lat,lon,wind,temperaturenMin,temperaturenGefühlt,temperaturenMax,luftFeucht;
     ArrayList<String> orte;
     ArrayList<String> temperaturen;
     ArrayList<String> wetter;
     Context context;

        //constructör
    public MyAdapter(Context ct, ArrayList<String> s1, ArrayList<String> s2, ArrayList<String> s3, ArrayList<String> s4, ArrayList<String> s5, ArrayList<String> s6, ArrayList<String> s7, ArrayList<String> s8, ArrayList<String> s9, ArrayList<String> s10)
    {
        context = ct;
        orte = s1;
        temperaturen = s2;
        wetter = s3;
        lon = s4;
        lat = s5;
        wind = s9;
        temperaturenGefühlt = s6;
        temperaturenMin = s7;
        temperaturenMax = s8;
        luftFeucht = s10;



    }

    public interface OnQuantityChangeListener {
        void onQuantityChange( float change );
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //was soll er inflaten/ wozu/ attach.
        //mach so, damit wir eine zu returnende View haben.
        View view = inflater.inflate(R.layout.row_out,parent, false);

        //hier kommt mein Viewholder ins spiel! den return ich mal...
        return new MyViewHolder(view);
    }

    //wir brauchen einen Viewholder auf jedenfall
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position) {
        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getRotation();
        holder.orte.setText(orte.get(position));
        holder.temp.setText(temperaturen.get(position));
        holder.lonLat.setText("Lon: "+lon.get(position) + " Lat: "+ lat.get(position));

        if(display.getRotation() == Surface.ROTATION_0)
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context.getApplicationContext(),onClickActivity.class);
                    intent.putExtra("de.fh_kiel.iue.mob2021_sp_ms.ORT",orte.get(position));
                    intent.putExtra("de.fh_kiel.iue.mob2021_sp_ms.TEMP",temperaturen.get(position));
                    intent.putExtra("de.fh_kiel.iue.mob2021_sp_ms.WET",wetter.get(position));
                    intent.putExtra("de.fh_kiel.iue.mob2021_sp_ms.LON",lon.get(position));
                    intent.putExtra("de.fh_kiel.iue.mob2021_sp_ms.WIN",wind.get(position));
                    intent.putExtra("de.fh_kiel.iue.mob2021_sp_ms.FEU",luftFeucht.get(position));
                    intent.putExtra("de.fh_kiel.iue.mob2021_sp_ms.FEE",temperaturenGefühlt.get(position));
                    intent.putExtra("de.fh_kiel.iue.mob2021_sp_ms.MIN",temperaturenMin.get(position));
                    intent.putExtra("de.fh_kiel.iue.mob2021_sp_ms.MAX",temperaturenMax.get(position));
                    intent.putExtra("de.fh_kiel.iue.mob2021_sp_ms.LAT",lat.get(position));
                    context.startActivity(intent);
                }
            });
        else
                holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override

                public void onClick(View v)
                {
                    TextView     gefühlLand, wetterLand, min, max, windLand;
                    gefühlLand = (TextView)((Activity) context).findViewById(R.id.mainGefühl);
                    min = (TextView)((Activity) context).findViewById(R.id.mainMin);
                    max = (TextView)((Activity) context).findViewById(R.id.mainMax);
                    wetterLand = (TextView)((Activity) context).findViewById(R.id.wetterLand);
                    windLand = (TextView)((Activity) context).findViewById(R.id.windMain);



                    gefühlLand.setText("Gefühlte Temperatur: "+temperaturenGefühlt.get(position));
                    min.setText("Maximale Temperatur: "+temperaturenMin.get(position));
                    max.setText("Minimale Temperatur: "+temperaturenMax.get(position));
                    windLand.setText("Wind in m/s: "+wind.get(position));
                    wetterLand.setText("Wetter: "+wetter.get(position));
                }
            });


    }

    @Override
    public int getItemCount() {
        return orte.size();
    }


    //hier ist der Viewholder.
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
       TextView orte, temp, lonLat;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            orte = itemView.findViewById(R.id.TextOrt);
            temp = itemView.findViewById(R.id.TextTemp);
            lonLat =  itemView.findViewById(R.id.textLonLat);
        }
    }
}