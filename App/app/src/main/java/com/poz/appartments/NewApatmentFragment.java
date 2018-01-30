package com.poz.appartments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.MapView;

import java.util.HashMap;
import java.util.Map;

public class NewApatmentFragment extends Fragment {
    private EditText titleText, addressText, descriptionText, priceText;
    private int seller;
    private TextView consoleText;
    private double lan;
    private double lat;
    private int price;
    private String title, address, description;
    private Button postBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_apatment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleText = (EditText) view.findViewById(R.id.new_ap_title_text);
        addressText = (EditText) view.findViewById(R.id.new_ap_address_text);
        descriptionText = (EditText) view.findViewById(R.id.new_ap_description_text);
        priceText = (EditText) view.findViewById(R.id.new_ap_price_text);
        consoleText = (TextView) view.findViewById(R.id.new_ap_console);
        postBtn = (Button) view.findViewById(R.id.new_ap_post_btn);
        Bundle args = this.getArguments();
        if (args != null) {
            lan = args.getDouble("lan");
            lat = args.getDouble("lat");
            address = args.getString("address");
            addressText.setText(address);
            consoleText.setText("lan - " + lan + ", lat - " + lat);
        }
        seller = ((MainActivity)getActivity()).getUserId();

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consoleText.setText("PLEASE WAIT!");
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = "https://appartments.herokuapp.com/PostApartment";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                ((MainActivity)getActivity()).hideFragment();
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
