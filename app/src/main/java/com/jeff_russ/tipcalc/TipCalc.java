package com.jeff_russ.tipcalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class TipCalc extends AppCompatActivity {

    // SAVE STATE!
    private static final String TOTAL_BILL = "TOTAL_BILL";
    private static final String CURRENT_TIP = "CURRENT_TIP";
    private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";

    // variable for active data:
    private double billBeforeTip;
    private double tipAmount;
    private double finalBill;

    // for access to UI objects:
    EditText billBeforeTipET;
    EditText tipAmountET;
    EditText finalBillET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calc);

        // restore state stuff:
        if(savedInstanceState == null){ // if this is our first startup
            billBeforeTip = 0.0;
            tipAmount = 0.15;
            finalBill = 0.0;
        }else{                          // we are coming from a restored state
            billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
            tipAmount     = savedInstanceState.getDouble(CURRENT_TIP);
            finalBill     = savedInstanceState.getDouble(TOTAL_BILL);
        }

        // initialize for access to UI objects:
        billBeforeTipET = (EditText) findViewById(R.id.billEditView);
        tipAmountET  = (EditText) findViewById(R.id.tipEditView);
        finalBillET     = (EditText) findViewById(R.id.finalEditView);

        // add change listeners
        billBeforeTipET.addTextChangedListener(billBeforeTipListener);
    }

    private TextWatcher billBeforeTipListener = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // check for exception:
            try{ billBeforeTip = Double.parseDouble(s.toString()); } // s is bill amount from UI input
            catch( NumberFormatException e ){ billBeforeTip = 0.0; }

            updateTipAndFinalBill(); // we will create this method below
        }
    };

    private void updateTipAndFinalBill(){   // this method is our main processing
        double tipAmount = Double.parseDouble(tipAmountET.getText().toString());
        double finalBill = billBeforeTip + (billBeforeTip * tipAmount); // calc final bill
        finalBillET.setText(String.format("%.02f", finalBill));         // update UI
    }

    // called when the device changes orientation, switched out to, etc.
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        // save our variables to constants
        outState.putDouble(TOTAL_BILL, finalBill);
        outState.putDouble(CURRENT_TIP, tipAmount);
        outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
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
