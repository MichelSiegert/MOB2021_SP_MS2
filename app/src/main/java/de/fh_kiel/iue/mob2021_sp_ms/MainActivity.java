package de.fh_kiel.iue.mob2021_sp_ms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    
    //ich brauch nen global recycler...
    public RecyclerView recyclerView;
    public Button refButton;

    public Parcelable savedRecyclerLayoutState;
    public static final String BUNDLE_RECYCLER_STATE = "State";
    public MyAdapter myAdapter;

    //visual parts
    public ProgressBar prog;
    public CheckBox check;

    //für erstellung von daten!
    static DataContainer data = new DataContainer();

    //relevant vars
    boolean hasFinished = false;
    int cPos;


    //oncreate
    protected void onCreate( Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check = findViewById(R.id.TestBox);



        refButton = findViewById(R.id.butRefresh);
        refButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(myAdapter.orte.size() < 50) {
                    for (int i = 0; i < 100; i++) {
                        myAdapter.orte.add("");
                        myAdapter.temperaturen.add("");
                        myAdapter.temperaturenGefühlt.add("");
                        myAdapter.temperaturenMax.add("");
                        myAdapter.temperaturenMin.add("");
                        myAdapter.wetter.add("");
                        myAdapter.wind.add("");
                        myAdapter.luftFeucht.add("");
                        myAdapter.lon.add("");
                        myAdapter.lat.add("");
                    }
                }

                if(check.isChecked())
                {
                    new  creator().execute();
                }
                else
                    {    //start async after pressing this button
                        new DownloadFiles().execute();
                    }
            }
        });

        //everything recycler:
        if(savedInstanceState != null)
        {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_STATE);
        }
        prog = findViewById(R.id.loadProgress);

        //init der view
        recyclerView = findViewById(R.id.RecView);
        myAdapter = new MyAdapter(this,data.städte, data.temperatur, data.wetter, data.lon, data.lat, data.temperaturenGefühlt, data.temperaturenMin, data.temperaturenMax, data.wind, data.luftfeucht);


        recyclerView.setAdapter(myAdapter);
        if (savedRecyclerLayoutState == null) recyclerView.setLayoutManager(new LinearLayoutManager(this));
        else recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState)
    {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(BUNDLE_RECYCLER_STATE,recyclerView.getLayoutManager().onSaveInstanceState());
    }



    //asyncTask zum faken von daten!
    private class creator extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            for(int i=0; i<100;i++) {
                myAdapter.orte.set(i ,"Hamburg");
                //generate a random number between 0 and 50!
                //typischer Michelcode...
                myAdapter.temperaturen.set(i ,(Float.toString((float) (Math.random()*50))).substring(0,5));
                myAdapter.wetter.set(i ,"bewölkt");
                myAdapter.lon.set(i ,(Float.toString((float) (Math.random()*10))).substring(0,4));
                myAdapter.lat.set(i ,(Float.toString((float) (Math.random()*10))).substring(0,4));
                //ich habe gegoogled und das soll angeblich funktionieren!
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                    }
                });
                try
                {
                    Thread.sleep(200);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                 prog.setProgress(i+1);
                if (isCancelled())
                {
                    Toast.makeText(MainActivity.this, "das laden der Daten ist fehlgeschlagen!s", Toast.LENGTH_LONG).show();
                    break;
                }
            }
            
            //und erneut rettet StackOverflow den tag.
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(MainActivity.this, "Die Daten wurden erfolgreich geladen!", Toast.LENGTH_LONG).show();
                }
            }
            );
            return null;
        }
    }



    private class DownloadFiles extends AsyncTask<String,String,String>
    {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String ... strings)
        {
            String line = "",
                    stringData = "";
            //nicht trivialer teil des Codes.
            try {
                EditText ortsEingabe = findViewById(R.id.textInput);
                String ortsInput = ortsEingabe.getText().toString();
                Log.d("TAG",ortsInput);
                URL url;
                if (ortsInput.length() <2) ortsInput ="";
                if (ortsInput != "") {
                    URL myInput = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + ortsInput + "&appid=1c1a1b5bc5706b35790855762fe5b8c3&units=metric&lang=de");
                    HttpURLConnection htmlInput = (HttpURLConnection) myInput.openConnection();
                    InputStream inputStream = htmlInput.getInputStream();
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
                    String InputData = inputReader.readLine();
                    JSONObject jInput = new JSONObject(InputData);

                    //gibt es nicht, wenn es ein fehlerhafter wert ist soll das programm terminieren!
                    String lonInput;
                    lonInput = jInput.getJSONObject("coord").getString("lon");
                    Log.d("Tag",lonInput);

                    String latInput = jInput.getJSONObject("coord").getString("lat");

                    url = new URL("https://api.openweathermap.org/data/2.5/find?lat=" + latInput + "&lon=" + lonInput + "&cnt=50&appid=1c1a1b5bc5706b35790855762fe5b8c3&units=metric&lang=de");
                }
                else {
                    url = new URL("https://api.openweathermap.org/data/2.5/find?lat=54.33244&lon=10.18842&cnt=1&appid=1c1a1b5bc5706b35790855762fe5b8c3&units=metric&lang=de");
                }
                HttpURLConnection html = (HttpURLConnection) url.openConnection();
                InputStream input  = html.getInputStream();
                BufferedReader reader=  new BufferedReader(new InputStreamReader(input));

                stringData = "";
                while(line != null)
                {
                    line = reader.readLine();
                    if (line != null)stringData += line;
                    Log.d("tag",stringData);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable()
                              {
                                  public void run()
                                  {
                                      Toast.makeText(MainActivity.this, "Die Daten wurden nicht gefunden!", Toast.LENGTH_LONG).show();
                                  }
                              });

                return null;
                }


            Log.d("TAG",stringData);

            //JASON!
            JSONArray JAEintrag, JAWetter;

            JSONObject JOListe,
            JOOrt,
            JOTemperaturen, JOCoordinates,
            JOWind;
            try {

                    JOListe = new JSONObject(stringData);
                    JAEintrag = JOListe.getJSONArray("list");
                    for(int i=0; i<JAEintrag.length();i++)
                    {
                        //der tatsächlich relevante teil!
                        JOOrt = JAEintrag.getJSONObject(i);
                        JOCoordinates = JOOrt.getJSONObject("coord");
                        JOTemperaturen= JOOrt.getJSONObject("main");
                        JOWind = JOOrt.getJSONObject("wind");
                        JAWetter = JOOrt.getJSONArray("weather");

                        myAdapter.lat.set(i, JOCoordinates.getString("lat"));
                        myAdapter.lon.set(i, JOCoordinates.getString("lon"));
                        myAdapter.orte.set(i, JOOrt.getString("name"));
                        myAdapter.temperaturen.set(i, JOTemperaturen.getString("temp"));
                        myAdapter.wind.set(i,JOWind.getString("speed"));
                        myAdapter.temperaturenGefühlt.set(i, JOTemperaturen.getString("feels_like"));
                        myAdapter.temperaturenMin.set(i, JOTemperaturen.getString("temp_max"));
                        myAdapter.temperaturenMax.set(i, JOTemperaturen.getString("temp_min"));
                        myAdapter.wetter.set(i, JAWetter.getJSONObject(0).getString("description"));
                        prog.setProgress(i*2+2);

                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                myAdapter.notifyDataSetChanged();

                            }
                        });



                    }
              //  }
            }catch (JSONException e) {
                e.printStackTrace();
            }
                // Escape early if cancel() is called
            runOnUiThread(new Runnable()
                          {
                              public void run()
                              {
                                  Toast.makeText(MainActivity.this, "Die Daten wurden erfolgreich geladen!", Toast.LENGTH_LONG).show();
                              }
                          }
            );
                return null;
        }
    }
}