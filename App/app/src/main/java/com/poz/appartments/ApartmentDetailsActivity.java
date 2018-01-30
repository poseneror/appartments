package com.poz.appartments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Or on 01/08/2017.
 */

public class ApartmentDetailsActivity extends AppCompatActivity implements Apartment.OnAparmentFetchedListener{
    private int id;
    private Apartment ap;

    private TextView titleText, addressText, descriptionText, priceText,
            sellerNameText, sellerContactText, sellerEmailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_apartment_details);
        Bundle b = getIntent().getExtras();
        titleText = (TextView) findViewById(R.id.apartmet_details_title_text);
        addressText = (TextView) findViewById(R.id.apartment_details_address);
        descriptionText = (TextView) findViewById(R.id.apartment_details_description);
        priceText = (TextView) findViewById(R.id.apartment_details_price_text);
        sellerNameText = (TextView) findViewById(R.id.apartment_details_seller_name_text);
        sellerContactText = (TextView) findViewById(R.id.apartmet_details_phone_text);
        sellerEmailText = (TextView) findViewById(R.id.apartment_details_seller_email_text);

        if (b != null) {
            id = b.getInt("id");
            ap  = new Apartment(this, id, this);
        }
    }

    @Override
    public void onApartmentFetched() {
        titleText.setText(ap.getTitle());
        addressText.setText(ap.getAddress());
        descriptionText.setText(ap.getDescription());
        priceText.setText(Integer.toString(ap.getPrice()));
        sellerNameText.setText(ap.getSeller().getName());
        sellerContactText.setText(Integer.toString(ap.getSeller().getContact()));
        sellerEmailText.setText(ap.getSeller().getEmail());
    }
}
