package com.example.myapplication;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PeersAdapter extends RecyclerView.Adapter<PeersAdapter.ViewHolder> {
    private ArrayList<String> peersList;
    private Context mcon;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView peersTextView;
        private Context mcon;
        private Context mcon2;

        public ViewHolder(Context con, View view) {
            super(view);
            this.mcon = con;
            this.view = view;
            this.mcon2 = view.getContext();
            this.peersTextView = view.findViewById(R.id.peer_item);
            this.peersTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String str = getPeersTextView().getText().toString();
                    str = str.replace(str.substring(str.length()-1), "");
                    Intent intent = new Intent(mcon, SearchPage.class);
                    intent.putExtra("ticker", str);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    mcon.startActivity(intent);
                    System.out.println("clicked: " + str);
                }
            });

        }


        public View getView() {
            return view;
        }

        public TextView getPeersTextView() {
            return peersTextView;
        }
    }

    public PeersAdapter(Context con, ArrayList<String> peersList) {
        this.mcon = con;
        this.peersList = peersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_peer, parent, false);
        PeersAdapter.ViewHolder peersViewHolder = new PeersAdapter.ViewHolder(mcon, view);
        return peersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String peer = peersList.get(position);
        holder.getPeersTextView().setText(peer + ",");
//        holder.getPeersTextView().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Intent intent = new Intent(mcon , SearchPage.class);
//                intent.putExtra("ticker", peer);
//                mcon.startActivity(intent);
//                return false;
//            }
//        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return peersList.size();
    }

}
