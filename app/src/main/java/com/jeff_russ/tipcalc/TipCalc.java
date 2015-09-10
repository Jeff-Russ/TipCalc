package com.jeff_russ.tipcalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class TipCalc extends AppCompatActivity {

    // Saved states as strings.
    private static final String BILL_BEFORE_TIP = "BILL_BEFORE_TIP";
    private static final String TIP_PERCENT = "TIP_PERCENT";
    private static final String FINAL_BILL = "FINAL_BILL";
    private static final String TIP_AMOUNT = "TIP_AMOUNT";

    // active states as doubles
    private double billBeforeTip;
    private double tipPercent;
    private double finalBill;
    private double tipAmount;

    // declare java versions of UI elements:
    EditText billBeforeTipET;
    EditText tipPercentET;
    EditText finalBillET;
    EditText tipAmountET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);         // call to super
        setContentView(R.layout.activity_tip_calc); // associate with xml layout

        // restore state stuff:
        if(savedInstanceState == null){    // if this is our first startup:
            billBeforeTip = 0.0;    // set
            tipPercent = 15;        // to
            finalBill = 0.0;        // default
            tipAmount = 0.0;        // values
        }else{                                  // we are coming from a restored state:
            billBeforeTip = savedInstanceState.getDouble(BILL_BEFORE_TIP);  // restore
            tipPercent    = savedInstanceState.getDouble(TIP_PERCENT);      // from our
            finalBill     = savedInstanceState.getDouble(FINAL_BILL);       // string
            tipAmount     = savedInstanceState.getDouble(TIP_AMOUNT);       // constants
        }

        // Point our java object to the UI elements:
        billBeforeTipET = (EditText) findViewById(R.id.billEditView);
        tipPercentET    = (EditText) findViewById(R.id.percentEditView);
        finalBillET     = (EditText) findViewById(R.id.finalEditView);
        tipAmountET     = (EditText) findViewById(R.id.tipEditView);

        // add change listeners for future user input:
        billBeforeTipET.addTextChangedListener(billBeforeTipListener);
        // I'm not sure if we need this for tipAmountET
    }

    private TextWatcher billBeforeTipListener = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // check for exception (not sure why we need this, maybe if parse fails?):
            try{ billBeforeTip = Double.parseDouble(s.toString()); } // s is bill amount from UI input
            catch( NumberFormatException e ){ billBeforeTip = 0.0; }

            updateTipAndFinalBill(); // we will create this method below
        }
    };

    // this method is our main processing of the final amount
    private void updateTipAndFinalBill(){
        double tipPercent = Double.parseDouble(tipAmountET.getText().toString());
        double tipAmount = billBeforeTip * tipPercent * 100;    // get tip amount
        double finalBill = billBeforeTip + tipAmount;          // calc final bill

        tipAmountET.setText(String.format("%.02f", tipAmount)); // update tipAmount UI
        finalBillET.setText(String.format("%.02f", finalBill)); // update billAmount UI
    }

    // called when the device changes orientation, switched out to other app, etc.
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        // save our variables to constants
        outState.putDouble(BILL_BEFORE_TIP, billBeforeTip);
        outState.putDouble(TIP_PERCENT, tipPercent);
        outState.putDouble(FINAL_BILL, finalBill);
        outState.putDouble(TIP_AMOUNT, tipAmount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tip_calc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
