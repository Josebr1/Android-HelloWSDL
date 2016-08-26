package com.example.jose.hellowsdl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.w3c.dom.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import livroandroid.lib.utils.HttpHelper;
import livroandroid.lib.utils.XMLUtils;


public class CelsiusToFahrenheitPostActivity extends AppCompatActivity {

    String URL = "http://www.w3schools.com/xml/tempconvert.asmx/CelsiusToFahrenheit";
    private EditText tCelsius;
    private EditText tFahrenheit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celsius_to_fahrenheit_post);
        tCelsius = (EditText) findViewById(R.id.tCelsius);
        tFahrenheit = (EditText) findViewById(R.id.tFahrenheit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onClickConverter(View view) {
        new Thread() {
            @Override
            public void run() {
                String celsius = tCelsius.getText().toString();
                //Parâmetro para enviar por post
                Map<String, String> params = new HashMap<>();
                params.put("celsius", celsius);
                try {
                    // Retorno: <string xmlns="http://www.w3schools.com/webservices/">33.8</string>
                    HttpHelper http = new HttpHelper();
                    String s = http.doPost(URL, params, "UTF-8");

                    Element root = XMLUtils.getRoot(s, "UTF-8");
                    // Lê o texto do XML
                    final String fahrenheit = XMLUtils.getText(root);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tFahrenheit.setText(fahrenheit);
                        }
                    });
                } catch (IOException e) {
                    Log.e("livroandroid", "Erro: " + e.getMessage(), e);
                }
            }
        }.start();
    }
}
