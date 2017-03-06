package gbc.sa.vansales.activities;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Survey;
/**
 * Created by Rakshit on 06-Mar-17.
 */
public class SurveyActivity extends AppCompatActivity {
    private ArrayList<Survey> arrayList = new ArrayList<>();
    LinearLayout surveyLayout;
    private static int viewsCount = 0;
    private List<View> allViews = new ArrayList<View>();
    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
            "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
            "Android", "iPhone", "WindowsMobile" };
    LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // To set Margin for the child Views
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        surveyLayout = (LinearLayout) findViewById(R.id.surveyLayout);
        generateSurveyData();
        generateView();
    }

    private ArrayList<Survey> generateSurveyData(){
        for(int i=0;i<8;i++){
            Survey survey = new Survey();
            survey.setSrNo(String.valueOf(i + 1));
            survey.setQuestionID(String.valueOf(i + 100));
            survey.setQuestionText("Test Question" + (i+1));
            if(i==0){
                survey.setResponseType(App.ANSWER_TYPE_INPUT);
            }
            else if(i==1){
                survey.setResponseType(App.ANSWER_TYPE_SELECT);
            }
            else if(i==2){
                survey.setResponseType(App.ANSWER_TYPE_RADIO);
                survey.setResponseOptions("Option1,Option2,Option3");
            }
            else if(i==3){
                survey.setResponseType(App.ANSWER_TYPE_CHECKBOX);
                survey.setResponseOptions("Option1,Option2,Option3");
            }
            else if(i==4){
                survey.setResponseType(App.ANSWER_TYPE_INPUT);
            }
            else if(i==5){
                survey.setResponseType(App.ANSWER_TYPE_SELECT);
            }
            else if(i==6){
                survey.setResponseType(App.ANSWER_TYPE_RADIO);
                survey.setResponseOptions("Option1,Option2,Option3");
            }
            else if(i==7){
                survey.setResponseType(App.ANSWER_TYPE_CHECKBOX);
                survey.setResponseOptions("Option1,Option2,Option3");
            }
            arrayList.add(survey);
        }


        return arrayList;
    }

    private void generateView(){
        for(Survey survey:arrayList){
            createTextView(survey.getQuestionText());
            if(survey.getResponseType().equals(App.ANSWER_TYPE_INPUT)){
                createEditText("");
            }
            else if(survey.getResponseType().equals(App.ANSWER_TYPE_SELECT)){
                createSpinner(values);
            }
            else if(survey.getResponseType().equals(App.ANSWER_TYPE_RADIO)){
                //createEditText("");
                String[]options = survey.getResponseOptions().split(",");
                createRadioGroup(options);
            }
            else if(survey.getResponseType().equals(App.ANSWER_TYPE_CHECKBOX)){
                String[]options = survey.getResponseOptions().split(",");
                for(int i=0;i<options.length;i++){
                    createCheckBox(options[i]);
                }
            }
        }
    }

    public void createTextView(String hint) {
        TextView textView = new TextView(this);
        textView.setId(viewsCount++);
        textView.setText(hint);
        allViews.add(textView);
        surveyLayout.addView(textView,params);
    }

    public void createEditText(String hint) {
        EditText editText = new EditText(this);
        editText.setId(viewsCount++);
        editText.setHint(hint);
        allViews.add(editText);
        surveyLayout.addView(editText,params);
    }

    public void createSpinner(String[] spinnerList) {

        Spinner spinner = new Spinner(this);
        spinner.setId(viewsCount++);
        //spinner.setBackgroundResource(R.drawable.dropdown_normal_holo_light);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        spinner.setAdapter(spinnerArrayAdapter);
        allViews.add(spinner);
        surveyLayout.addView(spinner,params);
    }

    public void createRadioGroup(String[] radioGroupOptions) {
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);//or RadioGroup.VERTICAL
        for(int i=0; i<radioGroupOptions.length; i++){
            RadioButton rb  = new RadioButton(this);
            rb.setId(viewsCount++);
            rb.setText(radioGroupOptions[i]);
            radioGroup.addView(rb);
        }
        allViews.add(radioGroup);
        surveyLayout.addView(radioGroup,params);
    }

    public void createCheckBox(String label) {
        final CheckBox checkBox = new CheckBox(this);
        checkBox.setId(viewsCount++);
        checkBox.setText(label);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        allViews.add(checkBox);
        surveyLayout.addView(checkBox,params);
    }
}
