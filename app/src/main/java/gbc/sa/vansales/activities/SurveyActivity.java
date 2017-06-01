package gbc.sa.vansales.activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.Survey;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 06-Mar-17.
 */
public class SurveyActivity extends AppCompatActivity {
    private ArrayList<Survey> arrayList = new ArrayList<>();
    LinearLayout surveyLayout;
    private static int viewsCount = 0;
    private List<View> allViews = new ArrayList<View>();
    /*String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
            "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
            "Android", "iPhone", "WindowsMobile" };*/
    String[] values = new String[] { "Berain", "Fayha", "Aquafina",
            "Oasis", "Lulu", "Bisleri", "Mai Dubai"};
    LinearLayout.LayoutParams params;
    ImageView iv_back;
    TextView tv_top_header;
    Button btn_Save;
    Customer object;
    ArrayList<CustomerHeader> customers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Survey");
        // To set Margin for the child Views
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        surveyLayout = (LinearLayout) findViewById(R.id.surveyLayout);
        btn_Save = (Button)findViewById(R.id.btnProcess);
        generateSurveyData();
        generateView();
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customers = CustomerHeaders.get();
        CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
        TextView tv_customer_name = (TextView) findViewById(R.id.tv_customer_id);
        TextView tv_customer_address = (TextView) findViewById(R.id.tv_customer_address);
        TextView tv_customer_pobox = (TextView) findViewById(R.id.tv_customer_pobox);
        TextView tv_customer_contact = (TextView) findViewById(R.id.tv_customer_contact);
        if (!(customerHeader == null)) {
            tv_customer_name.setText(StringUtils.stripStart(customerHeader.getCustomerNo(), "0") + " " + UrlBuilder.decodeString(customerHeader.getName1()));
            tv_customer_address.setText(UrlBuilder.decodeString(customerHeader.getStreet()));
            tv_customer_pobox.setText(getString(R.string.pobox) + " " + customerHeader.getPostCode());
            tv_customer_contact.setText(customerHeader.getPhone());
        } else {
            tv_customer_name.setText(StringUtils.stripStart(object.getCustomerID(),"0") + " " + UrlBuilder.decodeString(object.getCustomerName().toString()));
            tv_customer_address.setText(object.getCustomerAddress().toString());
            tv_customer_pobox.setText("");
            tv_customer_contact.setText("");
        }
    }

    private ArrayList<Survey> generateSurveyData(){
        String questions[] = new String[]{"Tell us something about the product","Which brand do you prefer","How frequently do you buy the product",
        "What best describes the brand?","Additional Feedback"};
        for(int i=0;i<5;i++){
            Survey survey = new Survey();
            survey.setSrNo(String.valueOf(i + 1));
            survey.setQuestionID(String.valueOf(i + 100));
            //survey.setQuestionText("Test Question" + (i+1));
            survey.setQuestionText(questions[i].toString());
            if(i==0){
                survey.setResponseType(App.ANSWER_TYPE_INPUT);
            }
            else if(i==1){
                survey.setResponseType(App.ANSWER_TYPE_SELECT);
            }
            else if(i==2){
                survey.setResponseType(App.ANSWER_TYPE_RADIO);
                //survey.setResponseOptions("Option1,Option2,Option3");
                survey.setResponseOptions("Twice in a week,Once in week,No pattern");
            }
            else if(i==3){
                survey.setResponseType(App.ANSWER_TYPE_CHECKBOX);
                //survey.setResponseOptions("Option1,Option2,Option3");
                survey.setResponseOptions("Value for Money,Better than other brands,Taste");
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
