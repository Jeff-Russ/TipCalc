package com.jeff_russ.tipcalc;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class TipCalc extends Activity {

    // Constants used when saving and restoring
    private static final String TOTAL_BILL = "TOTAL_BILL";
    private static final String CURRENT_TIP = "CURRENT_TIP";
    private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";

    private double billBeforeTip;   // Users bill before tip
    private double tipAmount;       // Tip amount
    private double finalBill;       // Bill plus Tip

    private int[] checklistValues = new int[12]; // Sum of all radio buttons and check boxes

    long secondsYouWaited = 0;          // The number of seconds you spent waiting for the waitress

    // DECLARE UI OBJ  ---------------

    EditText billBeforeTipET;       // R.id.billEditText
    EditText tipAmountET;           // R.id.tipEditText
    EditText finalBillET;           // R.id.finalBillEditText

    private SeekBar tipSeekBar;     // R.id.changeTipSeekBar

    CheckBox friendlyCheckBox;      // R.id.friendlyCheckBox
    CheckBox specialsCheckBox;      // R.id.specialsCheckBox
    CheckBox opinionCheckBox;       // R.id.opinionCheckBox

    RadioGroup availableRadioGroup; // R.id.availableRadioGroup

    RadioButton availableBadRadio;  // R.id.availableBadRadioGroup
    RadioButton availableOKRadio;   // R.id.availableOKGroup
    RadioButton availableGoodRadio; // R.id.availableGoodRadioGroup

    Spinner problemsSpinner;        // R.id.problemsSpinner

    Button startChronometerButton;  // R.id.startChronometerButton
    Button pauseChronometerButton;  // R.id.pauseChronometerButton
    Button resetChronometerButton;  // R.id.resetChronometerButton

    TextView timeWaitingTextView;       // TextView for the chronometer
    Chronometer timeWaitingChronometer; // R.id.timeWaitingChronometer

    // END OF DECLARE UI OBJ ---------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calc); // Inflate the GUI

        // Check if app just started, or if it is being restored
        if (savedInstanceState == null) {   // Just started
            billBeforeTip = 0.0;
            tipAmount = .15;
            finalBill = 0.0;
        } else {                 // App is being restored
            billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
            tipAmount = savedInstanceState.getDouble(CURRENT_TIP);
            finalBill = savedInstanceState.getDouble(TOTAL_BILL);
        }

        // INITIALIZE EVERYTHING ---------------

        billBeforeTipET = (EditText) findViewById(R.id.billEditText);   // Users bill before tip
        tipAmountET = (EditText) findViewById(R.id.tipEditText);        // Tip amount
        finalBillET = (EditText) findViewById(R.id.finalBillEditText);  // Bill plus tip

        tipSeekBar = (SeekBar) findViewById(R.id.changeTipSeekBar);

        friendlyCheckBox = (CheckBox) findViewById(R.id.friendlyCheckBox);
        specialsCheckBox = (CheckBox) findViewById(R.id.specialsCheckBox);
        opinionCheckBox = (CheckBox) findViewById(R.id.opinionCheckBox);

        availableRadioGroup = (RadioGroup) findViewById(R.id.availableRadioGroup);

        availableBadRadio = (RadioButton) findViewById(R.id.availableBadRadio);
        availableOKRadio = (RadioButton) findViewById(R.id.availableOKRadio);
        availableGoodRadio = (RadioButton) findViewById(R.id.availableGoodRadio);

        problemsSpinner = (Spinner) findViewById(R.id.problemsSpinner);
        problemsSpinner.setPrompt("Problem Solving");       // adds text label

        startChronometerButton = (Button) findViewById(R.id.startChronometerButton);
        pauseChronometerButton = (Button) findViewById(R.id.pauseChronometerButton);
        resetChronometerButton = (Button) findViewById(R.id.resetChronometerButton);

        timeWaitingTextView = (TextView) findViewById(R.id.timeWaitingTextView);
        timeWaitingChronometer = (Chronometer) findViewById(R.id.timeWaitingChronometer);


        // ADD LISTENERS ---------------

        billBeforeTipET.addTextChangedListener(billBeforeTipListener); // Bill has changed
        tipSeekBar.setOnSeekBarChangeListener(tipSeekBarListener);     // user adjusted bar
        setUpIntroCheckBoxes();             // Add change listeners to check boxes in method
        addChangeListenerToRadios();        // Add ChangeListener To Radio buttons in method
        addItemSelectedListenerToSpinner(); // Add ItemSelectedListener To Spinner in method
        setButtonOnClickListeners();        // Add setOnClickListeners for buttons in method
    }
       // SET UP LISTENERS ---------------

    // Called when the bill before tip amount is changed
    private TextWatcher billBeforeTipListener = new TextWatcher(){
        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            try{ // Change the billBeforeTip to the new input
                billBeforeTip = Double.parseDouble(arg0.toString());
            }catch(NumberFormatException e){
                billBeforeTip = 0.0;
            }
            updateTipAndFinalBill();
        }
        @Override public void afterTextChanged(Editable arg0) { /* TODO Auto-gen stub*/ }
        @Override public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {/* TODO Auto-gen stub*/ }
    };

    // THE METHOD to update the tip amount and add tip to bill to find the final bill amount
    private void updateTipAndFinalBill(){
        double tipAmount = Double.parseDouble(tipAmountET.getText().toString()); // Get tip amount
        // The bill before tip amount was set in billBeforeTipListener Get the bill plus the tip
        double finalBill = billBeforeTip + (billBeforeTip * tipAmount);
        // Set the total bill amount including the tip Convert into a 2 decimal place String
        finalBillET.setText(String.format("%.02f", finalBill));
    }

    // Called when a device changes in some way. For example, when a keyboard is popped out, or when
    // the device is rotated. Used to save state information that you'd like to be made available.
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putDouble(TOTAL_BILL, finalBill);
        outState.putDouble(CURRENT_TIP, tipAmount);
        outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
    }

    private OnSeekBarChangeListener tipSeekBarListener = new OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
            tipAmount = (tipSeekBar.getProgress()) * .01;   // Get the value set on the SeekBar
            tipAmountET.setText(String.format("%.02f", tipAmount)); // tipAmountET from SeekBar val
            updateTipAndFinalBill(); // Update all the other EditTexts
        }
        @Override public void onStartTrackingTouch(SeekBar arg0) { /* TODO Auto-generated method stub*/ }
        @Override public void onStopTrackingTouch(SeekBar arg0) { /* TODO Auto-generated method stub*/ }
    };

    // ---- NEW STUFF ----------

    private void setUpIntroCheckBoxes(){    // Add ChangeListener to the friendlyCheckBox
        friendlyCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // Use java ternary operator to set the right values for
                // each item on the waitress check box checklist
                checklistValues[0] = (friendlyCheckBox.isChecked())?4:0;
                setTipFromWaitressChecklist();  // Calculate tip using the waitress checklist options
                updateTipAndFinalBill();        // Update all the other EditTexts
            }
        });

        // Add ChangeListener to the specialsCheckBox
        specialsCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // Use java ternary operator to set the right values for
                // each item on the waitress check box checklist
                checklistValues[1] = (specialsCheckBox.isChecked())?1:0;
                setTipFromWaitressChecklist();  // Calculate tip using the waitress checklist options
                updateTipAndFinalBill();        // Update all the other EditTexts
            }
        });

        // Add ChangeListener to the opinionCheckBox
        opinionCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // Use java ternary operator to set the right values for
                // each item on the waitress check box checklist
                checklistValues[2] = (opinionCheckBox.isChecked())?2:0;
                setTipFromWaitressChecklist();  // Calculate tip using the waitress checklist options
                updateTipAndFinalBill();        // Update all the other EditTexts
            }
        });
    }

    // Calculate tip using the waitress checklist options
    private void setTipFromWaitressChecklist(){
        int checklistTotal = 0;
        // Cycle through all the checklist values to calculate a total amount based on waitress performance
        for(int item : checklistValues){ checklistTotal += item; }
        // Set tipAmountET
        tipAmountET.setText(String.format("%.02f", checklistTotal * .01));
    }

    private void addChangeListenerToRadios(){
        // Setting the listeners on the RadioGroups and handling them in the same location
        availableRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {   // checkedId is the RadioButton selected
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Use java ternary operator to set the right values for
                // each item on the waitress radiobutton checklist
                checklistValues[3] = (availableBadRadio.isChecked())?-1:0;
                checklistValues[4] = (availableOKRadio.isChecked())?2:0;
                checklistValues[5] = (availableGoodRadio.isChecked())?4:0;
                // Calculate tip using the waitress checklist options
                setTipFromWaitressChecklist();  // Calculate tip using the waitress checklist options
                updateTipAndFinalBill();        // Update all the other EditTexts
            }
        });
    }

    // Adds Spinner ItemSelectedListener
    private void addItemSelectedListenerToSpinner(){
        problemsSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                checklistValues[6] = (String.valueOf(problemsSpinner.getSelectedItem()).equals("Bad"))?-1:0;
                checklistValues[7] = (String.valueOf(problemsSpinner.getSelectedItem()).equals("OK"))?3:0;
                checklistValues[8] = (String.valueOf(problemsSpinner.getSelectedItem()).equals("Good"))?6:0;
                setTipFromWaitressChecklist();  // Calculate tip using the waitress checklist options
                updateTipAndFinalBill();        // Update all the other EditTexts
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { /* TODO Auto-generated method stub*/ }
        });
    }

    // Adds ClickListeners for buttons so they can control  the chronometer
    private void setButtonOnClickListeners(){
        startChronometerButton.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                int stoppedMilliseconds = 0; // Holds the number of milliseconds paused
                // Get time from the chronometer
                String chronoText = timeWaitingChronometer.getText().toString();
                String array[] = chronoText.split(":");
                if (array.length == 2) {    // Find the seconds
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                            + Integer.parseInt(array[1]) * 1000;
                } else if (array.length == 3) { // Find the minutes
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                            + Integer.parseInt(array[1]) * 60 * 1000
                            + Integer.parseInt(array[2]) * 1000;
                }

                // Amount of time elapsed since the start button was pressed, minus the time paused
                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);

                // Set the number of seconds you have waited This would be set for minutes in the
                // real world obviously. That can be found in array[2]
                secondsYouWaited = Long.parseLong(array[1]);
                updateTipBasedOnTimeWaited(secondsYouWaited);
                timeWaitingChronometer.start(); // Start the chronometer
            }
        });
        pauseChronometerButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                timeWaitingChronometer.stop();
            }
        });

        resetChronometerButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime());
                secondsYouWaited = 0;   // Reset milliseconds waited back to 0
            }
        });
    }

    private void updateTipBasedOnTimeWaited(long secondsYouWaited){

        // If spent < 10 sec, add 2 to the tip. If spent > 10 sec, subtract 2
        checklistValues[9] = (secondsYouWaited > 10)?-2:2;
        // Calculate tip using the waitress checklist options
        setTipFromWaitressChecklist();  // Calculate tip using the waitress checklist options
        updateTipAndFinalBill();        // Update all the other EditTexts
    }

    // ---- END OF NEW STUFF ----------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}