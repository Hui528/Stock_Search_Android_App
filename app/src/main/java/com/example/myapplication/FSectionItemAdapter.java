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

public class FSectionItemAdapter extends RecyclerView.Adapter<FSectionItemAdapter.ViewHolder> {
    private ArrayList<String> fItemList;
    public RequestQueue requestQueue;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView ftlTextView;
        private TextView ftrTextView;
        private TextView fblTextView;
        private TextView fbrTextView;
        private ImageView imgView;
        private ImageView f_change_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ftlTextView = itemView.findViewById(R.id.f_item_tl);
            this.ftrTextView = itemView.findViewById(R.id.f_item_tr);
            this.fblTextView = itemView.findViewById(R.id.f_item_bl);
            this.fbrTextView = itemView.findViewById(R.id.f_item_br);
            this.imgView = itemView.findViewById(R.id.f_item_img);
            this.f_change_icon = itemView.findViewById(R.id.f_change_icon);
            this.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), SearchPage.class);
                    intent.putExtra("ticker", ftlTextView.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }

        public View getItemView() {
            return itemView;
        }

        public TextView getFtlTextView() {
            return ftlTextView;
        }

        public TextView getFtrTextView() {
            return ftrTextView;
        }

        public TextView getFblTextView() {
            return fblTextView;
        }

        public TextView getFbrTextView() {
            return fbrTextView;
        }

        public ImageView getImgView() {
            return imgView;
        }

        public ImageView getF_change_icon() {
            return f_change_icon;
        }
    }

    public FSectionItemAdapter(ArrayList<String> fItemList, Context con) {
        this.fItemList = fItemList;
        requestQueue = Volley.newRequestQueue(con);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.f_section_item, parent, false);
        FSectionItemAdapter.ViewHolder fSectionItemAdapter = new FSectionItemAdapter.ViewHolder(view);
        return fSectionItemAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ticker = fItemList.get(position);
//        final String ticker = str.substring(0, str.length() - 1);
//        holder.getFtlTextView().setText(fItem.getTicker());
//        holder.getFblTextView().setText(fItem.getName());
//        holder.getFtrTextView().setText("$" + fItem.getPrice());
//        holder.getFbrTextView().setText("$" + fItem.getChange() + " (" + fItem.getChange_per() + ") ");

        holder.getFtlTextView().setText(ticker);

        fetchDescription(ticker, holder);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchLatestPrice(ticker, holder);
            }
        }, 0, TimeUnit.SECONDS.toMillis(15));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return fItemList.size();
    }

    private void fetchLatestPrice(String ticker, ViewHolder holder) {
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
                            TextView f_tr_tv = holder.getFtrTextView();
                            TextView f_br_tv = holder.getFbrTextView();
                            f_tr_tv.setText("$" + String.format("%.2f", last_price));
                            f_br_tv.setText("$" + String.format("%.2f", change) + " (" + String.format("%.2f", change_percentage) + ") ");
                            ImageView f_change_icon = holder.getF_change_icon();
                            if(change > 0) {
                                f_br_tv.setTextColor(Color.parseColor("#39D800"));
                                f_change_icon.setImageResource(R.drawable.trending_up);
                            }
                            if(change < 0) {
                                f_br_tv.setTextColor(Color.parseColor("#E60505"));
                                f_change_icon.setImageResource(R.drawable.trending_down);
                            }
                            if(change == 0) {
                                f_change_icon.setVisibility(View.GONE);
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

    private void fetchDescription(String ticker, ViewHolder holder) {
        JsonObjectRequest DescriptionData = new JsonObjectRequest(
                Request.Method.GET,
                "https://hw8-backend-server.wl.r.appspot.com/description/" + ticker,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String company_name = response.getString("company_name");
                            TextView f_bl_tv = holder.getFblTextView();
                            f_bl_tv.setText(company_name);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getDescription() statesError: " + statesError.toString());
                    }
                }
        );
        this.requestQueue.add(DescriptionData);
    }

    public ArrayList<String> getfItemList() {
        return fItemList;
    }

    public void removeItem(int position) {
        fItemList.remove(position);
        notifyItemRemoved(position);
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(fItemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(fItemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;

    }
}
