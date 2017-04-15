package com.example.timyu.timyu_fyp.Activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.timyu.timyu_fyp.R;


import ernestoyaquello.com.verticalstepperform.*;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;


public class RecommandFormActivity extends AppCompatActivity implements VerticalStepperForm{

    private VerticalStepperFormLayout verticalStepperForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand_form);

        String[] mySteps = {"Name", "Email", "Phone Number"};
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);

        // Finding the view
        verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);

        // Setting up and initializing the form
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, mySteps, this, this)
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(true) // It is true by default, so in this case this line is not necessary
                .init();
    }

    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case 0:
                //view = createNameStep();
                break;
            case 1:
                view = createEmailStep();
                break;
            case 2:
                //view = createPhoneNumberStep();
                break;
        }
        return view;
    }


    private View createEmailStep() {
        // In this case we generate the view by inflating a XML file
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout emailLayoutContent = (LinearLayout) inflater.inflate(R.layout.support_simple_spinner_dropdown_item, null, false);
        EditText email = (EditText) emailLayoutContent.findViewById(R.id.email);
        return emailLayoutContent;
    }




    @Override
    public void onStepOpening(int stepNumber) {

    }

    @Override
    public void sendData() {

    }
}
