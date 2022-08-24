package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CongDialog extends DialogFragment {
    private String action;
    private int shares;
    private String ticker;

    public CongDialog(String action, int shares, String ticker) {
        this.action = action;
        this.shares = shares;
        this.ticker = ticker;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cong, container, false);

        TextView msg_1_tv = view.findViewById(R.id.cong_msg_1);
        TextView msg_2_tv = view.findViewById(R.id.cong_msg_2);
        msg_1_tv.setText("You have successfully " + action + " " + shares);
        msg_2_tv.setText("shares of " + ticker);
        Button closeBtn = view.findViewById(R.id.cong_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }
}
