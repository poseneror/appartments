package com.poz.appartments;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Or on 30/07/2017.
 */

public class Apartment {
    private int id;
    private String title;
    private double x;
    private double y;
    private int price;
    private String address;
    private String description;
    private Seller seller;

    // for fetching small chunks of data while looking at map
    public Apartment(int id, String title, double x, double y, int price) {
        this.id = id;
        this.title = title;
        this.x = x;
        this.y = y;
        this.price = price;
    }

    public interface OnAparmentFetchedListener {
        public void onApartmentFetched();
    }
    private OnAparmentFetchedListener onAparmentFetchedListener;
    // for fatching all info for details

    public Apartment(Context context, int id, OnAparmentFetchedListener listener) {
        onAparmentFetchedListener = listener;
        final int requestId = id;
        // request apartments from the web
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://appartments.herokuapp.com/GetApartment";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            title = jsonObject.getString("title");
                            x = jsonObject.getDouble("x");;
                            y = jsonObject.getDouble("y");
                            price = jsonObject.getInt("price");
                            address = jsonObject.getString("address");
                            description = jsonObject.getString("description");
                            JSONObject sellerJason = jsonObject.getJSONObject("seller");
                            seller = new Seller(sellerJason.getInt("id"), sellerJason.getString("name"),
                                    sellerJason.getInt("contact"), sellerJason.getString("email"));
                            onAparmentFetchedListener.onApartmentFetched();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // failed to fatch apartments
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id", Integer.toString(requestId));
                return params;
            }
        };
        queue.add(postRequest);
    }

    public int getId(){ return id;}

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public Seller getSeller() {
        return seller;
    }
}