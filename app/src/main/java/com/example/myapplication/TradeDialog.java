package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class TradeDialog extends DialogFragment {
    private String name;
    private String ticker;
    private double crt_price;
    private TextView sharesTextView;
    private TextView avgTextView;
    private TextView totalTextView;
    private TextView changeTextView;
    private TextView marketValueTextView;
    private double left_money;
    private Context con;

    public TradeDialog(String name, String ticker, double crt_price, TextView sharesTextView, TextView avgTextView, TextView totalTextView, TextView changeTextView, TextView marketValueTextView, Context con) {
        this.name = name;
        this.ticker = ticker;
        this.crt_price = crt_price;
        this.sharesTextView = sharesTextView;
        this.avgTextView = avgTextView;
        this.totalTextView = totalTextView;
        this.changeTextView = changeTextView;
        this.marketValueTextView = marketValueTextView;
        this.con = con;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_trade, container, false);

        TextView titleTextView = view.findViewById(R.id.trade_title);
        EditText inputTextView = view.findViewById(R.id.trade_input);
        TextView calculationTextView = view.findViewById(R.id.trade_calculation);
        TextView leftTextView = view.findViewById(R.id.trade_left);
        Button buyBtn = view.findViewById(R.id.trade_buy);
        Button sellBtn = view.findViewById(R.id.trade_sell);

        SharedPreferences sp = TradeDialog.this.getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        left_money = Double.parseDouble(sp.getString("leftMoney", "25000.00"));

        titleTextView.setText("Trade " + name + " shares");
        int input = Integer.parseInt(String.valueOf(inputTextView.getText()));
        calculationTextView.setText(inputTextView.getText() + ".0*$" + String.format("%.2f", crt_price) + "/share = " + String.format("%.2f", input * crt_price) + "  ");
        leftTextView.setText("$" + String.format("%.2f", left_money) + " to buy " + ticker);

        inputTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int input = Integer.parseInt(String.valueOf(inputTextView.getText()));
                    calculationTextView.setText(inputTextView.getText() + ".0*$" + String.format("%.2f", crt_price) + "/share = " + String.format("%.2f", input * crt_price) + "  ");

                }
                catch (Error e) {
                    int input = 0;
                    calculationTextView.setText("0.0*$" + crt_price + "/share = " + input * crt_price + "  ");
                }
            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences sp = TradeDialog.this.getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sp.edit();

                String[] portfolioArray = sp.getString("portfolioList", "").split(",");
                ArrayList<String> portfolioList = new ArrayList<>(Arrays.asList(portfolioArray));

                String[] tradeInfo = sp.getString(ticker, "0, 0.00").split(",");
                int shares = Integer.parseInt(tradeInfo[0]);
                double totalCost = Double.parseDouble(tradeInfo[1]);
                int final_input = Integer.parseInt(String.valueOf(inputTextView.getText()));
                if(final_input > 0) {
                    if(final_input * crt_price <= left_money) {
                        shares += final_input;
                        totalCost += final_input * crt_price;
                        myEdit.putString(ticker, shares + ", " + totalCost);
                        left_money -= final_input * crt_price;
                        myEdit.putString("leftMoney", left_money + "");
                        if(!portfolioList.contains(ticker)) {
//                            String updatedportfolio = "";
//                            for(String listItem : portfolioList) {
//                                updatedportfolio = updatedportfolio + listItem + ",";
//                            }
//                            updatedportfolio += ticker;
                            String updatedPortfolio = sp.getString("portfolioList", "") + ticker + ",";
                            myEdit.putString("portfolioList", updatedPortfolio);
                        }
                        myEdit.apply();
                        dismiss();
                        CongDialog congDialog = new CongDialog("bought", final_input, ticker);
                        congDialog.show(((AppCompatActivity) con).getSupportFragmentManager(), "CongDialog");
                    }
                    else {
                        Toast.makeText(getContext(), "Not enough money to buy", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
//                View portfolioView = inflater.inflate(R.layout.portfolio, container, false);
                updatePortfolio();
            }
        });

        sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences sp = TradeDialog.this.getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sp.edit();

                String[] portfolioArray = sp.getString("portfolioList", "").split(",");
                ArrayList<String> portfolioList = new ArrayList<>(Arrays.asList(portfolioArray));

                String[] tradeInfo = sp.getString(ticker, "0, 0.00").split(",");
                int shares = Integer.parseInt(tradeInfo[0]);
                double totalCost = Double.parseDouble(tradeInfo[1]);
                int final_input = Integer.parseInt(String.valueOf(inputTextView.getText()));
                if(final_input > 0) {
                    if(final_input <= shares) {
                        shares -= final_input;
                        totalCost -= final_input * crt_price;
                        myEdit.putString(ticker, shares + ", " + totalCost);
                        left_money += final_input * crt_price;
                        myEdit.putString("leftMoney", left_money + "");

                        if(portfolioList.contains(ticker) && shares == 0) {
                            String updatedPortfolio = "";
                            for(String listItem : portfolioList) {
                                if(!listItem.equals(ticker)) {
                                    updatedPortfolio = updatedPortfolio + listItem + ",";
                                }
                            }
                            myEdit.putString("portfolioList", updatedPortfolio);
                        }
                            myEdit.apply();
                        dismiss();
                        CongDialog congDialog = new CongDialog("sold", final_input, ticker);
                        congDialog.show(((AppCompatActivity) con).getSupportFragmentManager(), "CongDialog");
                    }
                    else {
                        Toast.makeText(getContext(), "Not enough shares to sell", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
//                View portfolioView = inflater.inflate(R.layout.portfolio, container, false);
                updatePortfolio();
            }
        });
        return view;
    }

    private void updatePortfolio() {
//        TextView sharesTextView = portfolioView.findViewById(R.id.shares);
//        TextView avgTextView = portfolioView.findViewById(R.id.avg);
//        TextView totalTextView = portfolioView.findViewById(R.id.total);
//        TextView changeTextView = portfolioView.findViewById(R.id.change);
//        TextView marketValueTextView = portfolioView.findViewById(R.id.market);

        SharedPreferences sp = TradeDialog.this.getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String[] tradeInfo = sp.getString(ticker, "0, 0.00").split(",");
        int shares = Integer.parseInt(tradeInfo[0]);
        double totalCost = Double.parseDouble(tradeInfo[1]);
        sharesTextView.setText(shares + "");
        totalTextView.setText("$" + String.format("%.2f", totalCost));
        if(shares == 0) {
            avgTextView.setText("$0.00");

        }
        else {
            double avg = totalCost / shares;
            avgTextView.setText("$" + String.format("%.2f", avg));
        }
        double marketValue = crt_price * shares;
        marketValueTextView.setText("$" + String.format("%.2f", marketValue));
        double change = marketValue - totalCost;
        changeTextView.setText("$" + String.format("%.2f", change));
    }
}
