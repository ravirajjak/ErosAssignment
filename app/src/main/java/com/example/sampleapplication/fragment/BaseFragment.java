package com.example.sampleapplication.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.sampleapplication.interfaces.IOnApiListener;
import com.example.sampleapplication.enumeration.ApiEnum;
import com.google.gson.Gson;

import java.util.Objects;

public class BaseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Gson gson;
    private Dialog dialog;

    public BaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BaseFragment newInstance(String param1, String param2) {
        BaseFragment fragment = new BaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        init();
    }

    private void init() {
        if (gson == null)
            gson = new Gson();
    }

    public void executeTask(String mUrl, ApiEnum mRequestFor, final IOnApiListener iOnApiListener) {
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
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
            dialog = new Dialog(Objects.requireNonNull(getActivity()));
            Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        }
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        LayoutInflater factory = LayoutInflater.from(getActivity());
        View customPopupView = factory.inflate(R.layout.dialog_progress, null);
        dialog.setContentView(customPopupView);
        return dialog;
    }

}
