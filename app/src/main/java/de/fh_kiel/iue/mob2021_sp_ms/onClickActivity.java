package de.fh_kiel.iue.mob2021_sp_ms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class onClickActivity extends AppCompatActivity {

    String wetter,ort,temperatur, lonS, latS, tempMinS, tempMaxS, tempFeelS, windS, luftFeuchtS;
    TextView wetV, ortV, temperaturV, longLat, tempMin, tempMax, tempFeel, wind, luftFeucht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_click);
        Intent intent = getIntent();

        wetter = intent.getStringExtra("de.fh_kiel.iue.mob2021_sp_ms.WET");
        ort = intent.getStringExtra("de.fh_kiel.iue.mob2021_sp_ms.ORT");
        temperatur = intent.getStringExtra("de.fh_kiel.iue.mob2021_sp_ms.TEMP");
        lonS = intent.getStringExtra("de.fh_kiel.iue.mob2021_sp_ms.LON");
        latS = intent.getStringExtra("de.fh_kiel.iue.mob2021_sp_ms.LAT");
        windS = intent.getStringExtra("de.fh_kiel.iue.mob2021_sp_ms.WIN");
        luftFeuchtS = intent.getStringExtra("de.fh_kiel.iue.mob2021_sp_ms.FEU");
        tempFeelS = intent.getStringExtra("de.fh_kiel.iue.mob2021_sp_ms.FEE");
        tempMaxS = intent.getStringExtra("de.fh_kiel.iue.mob2021_sp_ms.MAX");
        tempMinS = intent.getStringExtra("de.fh_kiel.iue.mob2021_sp_ms.MIN");

        wetV = findViewById(R.id.TextWet);
        ortV = findViewById(R.id.TextOrt);
        temperaturV = findViewById(R.id.TextTemp);
        longLat = findViewById(R.id.contentLonLat);
        tempMin =findViewById(R.id.temperaturMin);
        tempMax = findViewById(R.id.temperaturMax);
        tempFeel = findViewById(R.id.temperaturGef);
        wind = findViewById(R.id.textWind);

        wetV.setText("wetter: "+wetter);
        ortV.setText(ort);
        temperaturV.setText("temp: "+temperatur);
        longLat.setText("Lon: "+lonS+" Lat: "+latS);
        tempMin.setText("min. temp: "+tempMinS);
        tempMax.setText("max. temp: "+tempMaxS);
        tempFeel.setText("gefühlte temp: "+tempFeelS);
        wind.setText("wind in m/s: " +windS);

    }
}