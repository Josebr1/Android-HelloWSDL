package com.example.jose.hellowsdl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by jose on 26/08/2016.
 *
 */
public class CelsiusToFahrenheitKSoapActivity extends AppCompatActivity {

    private static final String URL = "http://www.w3schools.com/xml/CelsiusToFahrenheit";
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
                try {
                    String celsius = tCelsius.getText().toString();
                    // Retorno: <string xmlns="http://www.w3schools.com/webservices/">33.8</string>
                    final String fahrenheit = celsiusFahrenheit("http://www.w3schools.com/xml/tempconvert.asmx", celsius);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tFahrenheit.setText(fahrenheit);
                        }
                    });
                } catch (Exception e) {
                    Log.e("livroandroid", "Erro: " + e.getMessage(), e);
                }
            }
        }.start();
    }

    // Faz o POST com KSOAP e retorna o resultado
    public String celsiusFahrenheit(String url, String celsius) throws Exception {
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        SoapObject soapReq = new SoapObject("http://www.w3schools.com/xml/", "CelsiusToFahrenheit");
        soapReq.addProperty("Celsius", celsius);
        soapEnvelope.setOutputSoapObject(soapReq);
        int timeOut = 60000;
        HttpTransportSE httpTransport = new HttpTransportSE(url);
        try {
            // Faz a chamada
            httpTransport.call(URL, soapEnvelope);
            // Lê o retorno
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault) {
                SoapFault fault = (SoapFault) retObj;
                Exception ex = new Exception(fault.faultstring);
                throw ex;
            } else {
                // Retorno OK
                SoapObject result = (SoapObject) retObj;
                if (result.getPropertyCount() > 0) {
                    Object obj = result.getProperty(0);
                    if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                        SoapPrimitive j = (SoapPrimitive) obj;
                        String resultVariable = j.toString();
                        return resultVariable;
                    } else if (obj != null && obj instanceof String) {
                        String resultVariable = (String) obj;
                        return resultVariable;
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return "";
    }
}
