package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AutocompleteAdapter extends ArrayAdapter<Autocomplete> {
    private ArrayList<Autocomplete> autocompleteList;
    private RequestQueue requestQueue;
    private SearchPage searchPage;

    public AutocompleteAdapter(@NonNull Context context, int resource, ArrayList<Autocomplete> autocompleteList) {
        super(context, resource);
        this.autocompleteList = autocompleteList;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return autoFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int phraseIndex = position;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.autocomplete_item,
                    parent, false);
        }
        TextView displaySymbol = convertView.findViewById(R.id.displaySymbol);
        TextView description = convertView.findViewById(R.id.description);

        displaySymbol.setText(autocompleteList.get(position).getDisplaySymbol() + " | ");
        description.setText(autocompleteList.get(position).getDescription());

        return convertView;
    }

    private Filter autoFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
//            fetchAutocomplete(charSequence.toString());
//            searchPage = new SearchPage(charSequence.toString());
//            autocompleteList = searchPage.getAutocompleteItems();
            FilterResults results = new FilterResults();
            ArrayList<Autocomplete> suggestions = new ArrayList<>();
            suggestions.addAll(autocompleteList);

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            clear();
            addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Autocomplete) resultValue).getDisplaySymbol();
        }
    };

//    private void fetchAutocomplete(String input) {
//        String HOST = "https://hw8-backend-server.wl.r.appspot.com";
//        String autocompleteUrl = HOST + "/autocomplete/";
//        System.out.println("autocomplete entered");
//        Log.i("enter", "autocomplete");
//        JsonArrayRequest AutocompleteData = new JsonArrayRequest(
//                Request.Method.GET,
//                autocompleteUrl + input,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            for(int i = 0; i < response.length(); i++) {
//                                JSONObject item = response.getJSONObject(i);
//                                String displaySymbol = item.getString("displaySymbol");
//                                String description = item.getString("description");
//                                autocompleteList.add(new Autocomplete(displaySymbol, description));
//                            }
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                        System.out.println("autocomplete finished");
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError statesError) {
//                        Log.i("Error", "getAutocomplete() statesError: " + statesError.toString());
//                    }
//                }
//        );
//        System.out.println("end of fetchAutocomplete");
//    }
}
