package com.poz.appartments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NewAppartmentActivity extends AppCompatActivity {
    private EditText titleText, addressText, descriptionText, priceText;
    private int seller = 1;
    private TextView consoleText;
    private double lan;
    private double lat;
    private int price;
    private String title, address, description;
    private Button postBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appartment);

        titleText = (EditText) findViewById(R.id.new_ap_title_text);
        addressText = (EditText) findViewById(R.id.new_ap_address_text);
        descriptionText = (EditText) findViewById(R.id.new_ap_description_text);
        priceText = (EditText) findViewById(R.id.new_ap_price_text);
        consoleText = (TextView) findViewById(R.id.new_ap_console);
        consoleText.setText("lan - " + lan + ", lat - " + lat);
        postBtn = (Button) findViewById(R.id.new_ap_post_btn);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            lan = b.getDouble("lan");
            lat = b.getDouble("lat");
            address = b.getString("address");
            addressText.setText(address);
        }

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consoleText.setText("PLEASE WAIT!");
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://appartments.herokuapp.com/PostApartment";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                consoleText.setText("ERROR - " + error.toString());
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        title = titleText.getText().toString();
                        address = addressText.getText().toString();
                        description = descriptionText.getText().toString();
                        price = Integer.parseInt(priceText.getText().toString());
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("title", title);
                        params.put("x", Double.toString(lat));
                        params.put("y", Double.toString(lan));
                        params.put("address", address);
                        params.put("description", description);
                        params.put("price", Integer.toString(price));
                        params.put("seller", Integer.toString(seller));
                        return params;
                    }
                };
                queue.add(postRequest);
            }
        });
    }

}
