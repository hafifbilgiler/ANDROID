package com.example.nodemcu_webserver;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class MainActivity extends AppCompatActivity {
    TextView textView;
    TextView mavi_durum, yesil_durum,version;
    WebView web;
    EditText ipadresi;
    String green="",blue;
    ImageView yesil_image,mavi_image;
    Button button_yesil_ac,buton_yesil_kapat,buton_mavi_ac,buton_mavi_kapat;
    int on=0,off=0,check=0;
    private String DEBUG_TAG;
    String satir;
    String dosya ="";
    String ip="";
    String status_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_yesil_ac = (Button) findViewById(R.id.button);
        buton_yesil_kapat = (Button) findViewById(R.id.button2);
        buton_mavi_ac = (Button) findViewById(R.id.button3);
        buton_mavi_kapat=(Button)findViewById(R.id.button4);
        mavi_durum = (TextView) findViewById(R.id.textView2);
        yesil_durum = (TextView) findViewById(R.id.textView3);
        version = (TextView) findViewById(R.id.version);
        yesil_image = (ImageView) findViewById(R.id.imageView);
        mavi_image = (ImageView) findViewById(R.id.imageView2) ;
        button_yesil_ac.setBackgroundColor(Color.GREEN);
        buton_yesil_kapat.setBackgroundColor(Color.DKGRAY);
        buton_mavi_ac.setBackgroundColor(Color.GREEN);
        buton_mavi_kapat.setBackgroundColor(Color.DKGRAY);
        ipadresi=(EditText)findViewById(R.id.editText);
        ipadresi.setText("192.168.0.0");
        version.setText(" ver 1.0.0");
        button_yesil_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip=ipadresi.getText().toString()+"/green/on";
                new backdround().execute();
            }//
        });
        buton_yesil_kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip=ipadresi.getText().toString()+"/green/off";
                new backdround().execute();
            }
        });
        buton_mavi_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip=ipadresi.getText().toString()+"/red/on";
                new backdround().execute();
            }
        });
        buton_mavi_kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip=ipadresi.getText().toString()+"/red/off";
                new backdround().execute();
            }
        });
    }
    class backdround extends AsyncTask<String,String,String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection=null;
            URL url=null;
            InputStream is=null;
            try {
                url = new URL("http://"+ip);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                is = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                satir=null;
                dosya ="";
                if(check == 0) {
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        status_code = "CONNECT";
                    }else{
                        status_code = "CAN NOT CONNECTION";
                    }
                }
                while ((satir=br.readLine())!=null){
                    Log.d("satir:",satir);
                    dosya += satir;
                }
                return dosya;
            }catch (IOException e){
                e.printStackTrace();
                status_code = "CAN NOT CONNECTION";
                check=0;
            }finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            if(check == 0) {
                if (status_code == "CONNECT" ) {
                    Toast.makeText( getApplicationContext(),status_code, Toast.LENGTH_LONG).show();
                    check=1;
                }else if(status_code == "CAN NOT CONNECTION"){
                    Toast.makeText( getApplicationContext(),status_code, Toast.LENGTH_LONG).show();
                    check=0;
                }
            }
            try {
                if (s.matches("(.*)<p>GREEN - STATE: off</p>(.*)")) {
                    button_yesil_ac.setBackgroundColor(Color.GREEN);
                    buton_yesil_kapat.setBackgroundColor(Color.DKGRAY);
                    yesil_image.setImageResource(R.drawable.green_off);
                    yesil_durum.setText("KAPALI");
                }
                if (s.matches("(.*)<p>GREEN - STATE: on</p>(.*)")) {
                    buton_yesil_kapat.setBackgroundColor(Color.RED);
                    button_yesil_ac.setBackgroundColor(Color.DKGRAY);
                    yesil_image.setImageResource(R.drawable.green_on);
                    yesil_durum.setText("ACIK");
                }
                if (s.matches("(.*)<p>RED - STATE: off</p>(.*)")) {
                    buton_mavi_ac.setBackgroundColor(Color.GREEN);
                    buton_mavi_kapat.setBackgroundColor(Color.DKGRAY);
                    mavi_image.setImageResource(R.drawable.blue_off);
                    mavi_durum.setText("KAPALI");
                }
                if (s.matches("(.*)<p>RED - STATE: on</p>(.*)")) {
                    buton_mavi_ac.setBackgroundColor(Color.DKGRAY);
                    buton_mavi_kapat.setBackgroundColor(Color.RED);
                    mavi_image.setImageResource(R.drawable.blue_on);
                    mavi_durum.setText("ACIK");
                }
            } catch (Exception ex) {
            }
        }
    }

}