package com.poz.appartments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

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

public class AuthFragment extends Fragment {
    private RadioGroup authMethodRadio;

    private EditText emailText, passwordText, passwordConfirmText, phoneText, nameText;
    private Button authBtn;
    private TextView consoleText;

    String email, password, passwordConfirm, phone, name;

    boolean signin = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authMethodRadio = (RadioGroup) view.findViewById(R.id.auth_method_radio);
        emailText = (EditText) view.findViewById(R.id.auth_email);
        passwordText = (EditText) view.findViewById(R.id.auth_password);
        passwordConfirmText = (EditText) view.findViewById(R.id.auth_password_confirm);
        phoneText = (EditText) view.findViewById(R.id.auth_phone_text);
        nameText = (EditText) view.findViewById(R.id.auth_name_text);
        consoleText = (TextView) view.findViewById(R.id.auth_console);
        authBtn = (Button) view.findViewById(R.id.auth_btn);
        authMethodRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.auth_signin_radio){
                    authBtn.setText("Signin");
                    passwordConfirmText.setVisibility(View.GONE);
                    phoneText.setVisibility(View.GONE);
                    nameText.setVisibility(View.GONE);
                    signin = true;
                } else {
                    authBtn.setText("Signup");
                    passwordConfirmText.setVisibility(View.VISIBLE);
                    phoneText.setVisibility(View.VISIBLE);
                    nameText.setVisibility(View.VISIBLE);
                    signin = false;
                }
            }
        });
        Bundle args = this.getArguments();
        if (args != null) {

        }
        authBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailText.getText().toString();
                password = passwordText.getText().toString();
                passwordConfirm = passwordConfirmText.getText().toString();
                phone = phoneText.getText().toString();
                name = nameText.getText().toString();
                // todo ADD VALIDATORS
                if(!signin && (name.equals("") || email.equals("") ||
                        !password.equals(passwordConfirm) || phone.equals(""))){
                    consoleText.setText("CheckDetails!");
                } else {
                    consoleText.setText("PLEASE WAIT!");
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url;
                    if (signin)
                        url = "https://appartments.herokuapp.com/Signin";
                    else
                        url = "https://appartments.herokuapp.com/NewSeller";
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean auth = jsonObject.getBoolean("auth");
                                        if (auth) {
                                            consoleText.setText("YES");
                                            ((MainActivity)getActivity()).setUserId(jsonObject.getInt("token"));
                                            ((MainActivity)getActivity()).hideFragment();
                                        }
                                        else
                                            consoleText.setText("NO");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    consoleText.setText("ERROR - " + error.toString());
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", name);
                            params.put("email", email);
                            params.put("password", password);
                            params.put("contact", phone);
                            return params;
                        }
                    };
                    queue.add(postRequest);
                }
            }
        });
    }
}
