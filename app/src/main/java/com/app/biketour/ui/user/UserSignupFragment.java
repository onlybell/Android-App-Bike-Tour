package com.app.biketour.ui.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.app.biketour.HomeActivity;
import com.app.biketour.R;
import com.app.biketour.Utillity;
import com.app.biketour.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserSignupFragment extends Fragment {
    private static String TAG = "BikeTour App - User Profile";
    private String mJsonString;
    private static final String ARG_PARAM1 = "param";
    private String mParam;

    SharedPreferences pref;
    ImageButton ibtnTopPopupMenu;
    Button btnCancel, btnSignup;
    EditText signupFirstName, signupLastName, signupEmail, signupPassword, signupPasswordConfirm;
    String SignupFirstNameHolder, SignupLastNameHolder, SignupEmailHolder, SignupPasswordHolder, SignupPasswordConfirmHolder;
    Boolean CheckSignup, CheckPassord ;
    List<String> paramList = new ArrayList<>();


    public static UserSignupFragment newInstance(String param) {
        UserSignupFragment fragment = new UserSignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_signup, container, false);

        // Visible top popup menu icon
        ibtnTopPopupMenu = (ImageButton) getActivity().findViewById(R.id.ibtnMenu);
        ibtnTopPopupMenu.setVisibility(View.VISIBLE);

        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM1);
        }

        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);

        signupFirstName = (EditText) root.findViewById(R.id.et_signup_first_name);
        signupLastName = (EditText) root.findViewById(R.id.et_signup_last_name);
        signupEmail = (EditText) root.findViewById(R.id.et_signup_email);
        signupPassword = (EditText) root.findViewById(R.id.et_signup_pwd);
        signupPasswordConfirm = (EditText) root.findViewById(R.id.et_signup_confirm_pwd);

        btnSignup = (Button) root.findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckSignupEmpty();

                if(CheckSignup){
                    if(CheckPassord) {
                        UserSignupFunction(SignupFirstNameHolder, SignupLastNameHolder, SignupEmailHolder, SignupPasswordHolder);
                    }
                    else {
                        Utillity.showMessage(getActivity(),"WARNING", "Please check password.");
                    }
                }
                else {
                    Utillity.showMessage(getActivity(),"WARNING", "Please fill all form fields.");
                }
            }
        });

        btnCancel= (Button) root.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((HomeActivity)getActivity()).replaceFragment(HomeFragment.newInstance());
            }
        });
        return root;
    }

    public void CheckSignupEmpty(){
        SignupFirstNameHolder = signupFirstName.getText().toString();
        SignupLastNameHolder = signupLastName.getText().toString();
        SignupEmailHolder = signupEmail.getText().toString();
        SignupPasswordHolder = signupPassword.getText().toString();
        SignupPasswordConfirmHolder = signupPasswordConfirm.getText().toString();

        if(TextUtils.isEmpty(SignupFirstNameHolder) || TextUtils.isEmpty(SignupLastNameHolder) || TextUtils.isEmpty(SignupEmailHolder) || TextUtils.isEmpty(SignupPasswordHolder) || TextUtils.isEmpty(SignupPasswordConfirmHolder)) {
            CheckSignup = false;
        }
        else {
            CheckSignup = true ;

            if(!SignupPasswordHolder.equals(SignupPasswordConfirmHolder)) {
                CheckPassord = false ;
            }
            else {
                CheckPassord = true ;
            }
        }
    }

    public void UserSignupFunction(String firstname, String lastname, String email, String password){

        class UserSignupClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);

                if(httpResponseMsg == null){
                    Utillity.showMessage(getActivity(),"WARNING", "Invalid Email or Password Please Try Again");
                }
                else{
                    mJsonString = httpResponseMsg;
                    checkResult();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                paramList.add(params[0]);
                paramList.add(params[1]);
                paramList.add(params[2]);
                paramList.add(params[3]);

                return Utillity.getJsonData("Signup", null, null, paramList);

            }
        }

        UserSignupClass userLoginClass = new UserSignupClass();
        userLoginClass.execute(firstname, lastname, email, password);
    }

    private void checkResult(){

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            // Display Tour Product
            JSONArray jsonArrayNews = jsonObject.getJSONArray("Signup");

            if (jsonArrayNews.length() > 0) {
                JSONObject jsonList = jsonArrayNews.getJSONObject(0);

                if(jsonList.getString("result").equals("success")){

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email",jsonList.getString("email"));
                    editor.putString("firstname",jsonList.getString("firstname"));
                    editor.putString("lastname",jsonList.getString("lastname"));
                    editor.putString("contactnumber",jsonList.getString("contactnumber"));
                    editor.commit();

                    Utillity.showMessage(getActivity(),"SUCCESS", "Sign Up Success");

                    if (mParam.equals("saved")) {
                        ((HomeActivity)getActivity()).replaceFragment(UserSavedFragment.newInstance());
                    }
                    else if (mParam.equals("profile")) {
                        ((HomeActivity)getActivity()).replaceFragment(UserProfileFragment.newInstance());
                    }
                }
                else {
                    Utillity.showMessage(getActivity(),"WARNING", jsonList.getString("message"));
                }
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
