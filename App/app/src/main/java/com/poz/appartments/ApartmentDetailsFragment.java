package com.poz.appartments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ApartmentDetailsFragment extends Fragment implements Apartment.OnAparmentFetchedListener {
    private int id;
    private Apartment ap;

    private TextView titleText, addressText, descriptionText, priceText,
            sellerNameText, sellerContactText, sellerEmailText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if (args != null) {
            id = args.getInt("id");
            ap  = new Apartment(getContext(), id, this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleText = (TextView) view.findViewById(R.id.apartmet_details_title_text);
        addressText = (TextView) view.findViewById(R.id.apartment_details_address);
        descriptionText = (TextView) view.findViewById(R.id.apartment_details_description);
        priceText = (TextView) view.findViewById(R.id.apartment_details_price_text);
        sellerNameText = (TextView) view.findViewById(R.id.apartment_details_seller_name_text);
        sellerContactText = (TextView) view.findViewById(R.id.apartmet_details_phone_text);
        sellerEmailText = (TextView) view.findViewById(R.id.apartment_details_seller_email_text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apartment_details, container, false);
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
