package com.example.myapplication;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PSectionItemAdapter extends RecyclerView.Adapter<PSectionItemAdapter.ViewHolder> {
    private ArrayList<PItem> pItemList;
    public RequestQueue requestQueue;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView ptlTextView;
        private TextView ptrTextView;
        private TextView pblTextView;
        private TextView pbrTextView;
        private ImageView imgView;
        private ImageView p_change_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ptlTextView = itemView.findViewById(R.id.p_item_tl);
            this.ptrTextView = itemView.findViewById(R.id.p_item_tr);
            this.pblTextView = itemView.findViewById(R.id.p_item_bl);
            this.pbrTextView = itemView.findViewById(R.id.p_item_br);
            this.imgView = itemView.findViewById(R.id.p_item_img);
            this.p_change_icon = itemView.findViewById(R.id.p_change_icon);
            this.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), SearchPage.class);
                    intent.putExtra("ticker", ptlTextView.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }

        public View getItemView() {
            return itemView;
        }

        public TextView getPtlTextView() {
            return ptlTextView;
        }

        public TextView getPtrTextView() {
            return ptrTextView;
        }

        public TextView getPblTextView() {
            return pblTextView;
        }

        public TextView getPbrTextView() {
            return pbrTextView;
        }

        public ImageView getImgView() {
            return imgView;
        }

        public ImageView getP_change_icon() {
            return p_change_icon;
        }
    }

    public PSectionItemAdapter(ArrayList<PItem> pItemList, Context con) {
        this.pItemList = pItemList;
        requestQueue = Volley.newRequestQueue(con);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.p_section_item, parent, false);
        PSectionItemAdapter.ViewHolder pSectionItemAdapter = new PSectionItemAdapter.ViewHolder(view);
        return pSectionItemAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PItem pItem = pItemList.get(position);
        holder.getPtlTextView().setText(pItem.getTicker());
        holder.getPblTextView().setText(pItem.getShares() + " shares");

//        holder.getPtrTextView().setText("$" + pItem.getPrice());
//        holder.getPbrTextView().setText("$" + pItem.getChange() + " (" + pItem.getChange_per() + ") ");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchLatestPrice(pItem.getTicker(), holder);
            }
        }, 0, TimeUnit.SECONDS.toMillis(15));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return pItemList.size();
    }

    private void fetchLatestPrice(String ticker, PSectionItemAdapter.ViewHolder holder) {
        JsonObjectRequest LatestPriceData = new JsonObjectRequest(
                Request.Method.GET,
                "https://hw8-backend-server.wl.r.appspot.com/latestPrice/" + ticker,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            double last_price = response.getDouble("last_price");
                            double change = response.getDouble("change");
                            double change_percentage = response.getDouble("change_percentage");
                            TextView f_tr_tv = holder.getPtrTextView();
                            TextView f_br_tv = holder.getPbrTextView();
                            f_tr_tv.setText("$" + String.format("%.2f", last_price));
                            f_br_tv.setText("$" + String.format("%.2f", change) + " (" + String.format("%.2f", change_percentage) + ") ");
                            ImageView p_change_icon = holder.getP_change_icon();
                            if(change > 0) {
                                f_br_tv.setTextColor(Color.parseColor("#39D800"));
                                p_change_icon.setImageResource(R.drawable.trending_up);
                            }
                            if(change < 0) {
                                f_br_tv.setTextColor(Color.parseColor("#E60505"));
                                p_change_icon.setImageResource(R.drawable.trending_down);
                            }
                            if(change == 0) {
                                p_change_icon.setVisibility(View.GONE);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getLatestPrice() statesError: " + statesError.toString());
                    }
                }
        );
        requestQueue.add(LatestPriceData);
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(pItemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(pItemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;

    }

    public ArrayList<PItem> getpItemList() {
        return pItemList;
    }
}
