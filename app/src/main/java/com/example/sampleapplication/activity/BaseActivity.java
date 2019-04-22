package com.example.sampleapplication.activity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sampleapplication.R;
import com.example.sampleapplication.enumeration.ApiEnum;
import com.example.sampleapplication.interfaces.IOnApiListener;
import com.google.gson.Gson;

import java.util.Objects;

public class BaseActivity extends AppCompatActivity {


    Gson gson;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (gson == null){
            gson = new Gson();
        }
    }

    public void executeTask(String mUrl, ApiEnum mRequestFor, final IOnApiListener iOnApiListener) {
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        StringRequest request = new StringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgress();
                iOnApiListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                iOnApiListener.onError(error.getMessage());
            }
        });
        queue.add(request);
    }

    private void hideProgress() {
        if (dialog != null) {
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private void showProgress() {
        dialog = getProgressDialog();
        dialog.show();
    }

    public Dialog getProgressDialog() {
        if (dialog == null) {
            dialog = new Dialog(this);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        LayoutInflater factory = LayoutInflater.from(this);
        View customPopupView = factory.inflate(R.layout.dialog_progress, null);
        dialog.setContentView(customPopupView);
        return dialog;
    }
}
