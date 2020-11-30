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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.biketour.HomeActivity;
import com.app.biketour.R;
import com.app.biketour.Utillity;
import com.app.biketour.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {

    private static String TAG = "BikeTour App - User Profile";
    private String mJsonString;

    ImageButton ibtnTopPopupMenu;
    Button btnLogout, btnChangeProfile, btnChangePassword;
    SharedPreferences pref;
    EditText firstName, lastName, contactNumber, changePassword, changePasswordConfirm;
    TextView userEmail;
    String ProfileFirstName, ProfileLastName, ProfileContactNumber, ProfilePasswordHolder, ProfilePasswordConfirmHolder;
    Boolean CheckProfile, CheckPassord ;

    BottomNavigationView bottomNavigationView;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Visible top popup menu icon
        ibtnTopPopupMenu = (ImageButton) (getActivity()).findViewById(R.id.ibtnMenu);
        ibtnTopPopupMenu.setVisibility(View.VISIBLE);

        bottomNavigationView = (getActivity()).findViewById(R.id.nav_view);

        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);

        userEmail = (TextView) root.findViewById(R.id.tv_user_email);
        userEmail.setText("Email : " + pref.getString("email",""));

        firstName = (EditText) root.findViewById(R.id.et_profile_first_name);
        firstName.setText(pref.getString("firstname",""));

        lastName = (EditText) root.findViewById(R.id.et_profile_last_name);
        lastName.setText(pref.getString("lastname",""));

        contactNumber = (EditText) root.findViewById(R.id.et_profile_phone);
        contactNumber.setText(pref.getString("contactnumber",""));

        // Check login state
        if(!pref.contains("email") && !pref.contains("firstname")){
            // Redirect login fragment
            ((HomeActivity)getActivity()).replaceFragment(UserLoginFragment.newInstance("profile"));
        }

        btnChangeProfile = (Button) root.findViewById(R.id.btn_profile_save);
        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckChangeProfileEmpty();
                if(CheckProfile){
                    UserProfileFunction(pref.getString("email",""), ProfileFirstName, ProfileLastName, ProfileContactNumber);
                }
                else {
                    Utillity.showMessage(getActivity(),"WARNING", "Please fill all form fields.");
                }
            }
        });

        changePassword = (EditText) root.findViewById(R.id.et_change_pwd);
        changePasswordConfirm = (EditText) root.findViewById(R.id.et_change_confirm_pwd);

        btnChangePassword = (Button) root.findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckChangePasswordEmpty();
                if(CheckProfile){
                    if(CheckPassord) {
                        UserPasswordFunction(pref.getString("email",""), ProfilePasswordHolder);
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

        btnLogout = (Button) root.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                ((HomeActivity)getActivity()).replaceFragment(HomeFragment.newInstance());
            }
        });

        return root;
    }

    public void CheckChangeProfileEmpty(){
        ProfileFirstName = firstName.getText().toString();
        ProfileLastName = lastName.getText().toString();
        ProfileContactNumber = contactNumber.getText().toString();

        if(TextUtils.isEmpty(ProfileFirstName) || TextUtils.isEmpty(ProfileLastName)) {
            CheckProfile = false;
        }
        else {
            CheckProfile = true ;
        }
    }

    public void CheckChangePasswordEmpty(){
        ProfilePasswordHolder = changePassword.getText().toString();
        ProfilePasswordConfirmHolder = changePasswordConfirm.getText().toString();

        if(TextUtils.isEmpty(ProfilePasswordHolder) || TextUtils.isEmpty(ProfilePasswordConfirmHolder)) {
            CheckProfile = false;
        }
        else {
            CheckProfile = true ;

            if(!ProfilePasswordHolder.equals(ProfilePasswordConfirmHolder)) {
                CheckPassord = false ;
            }
            else {
                CheckPassord = true ;
            }
        }
    }

    public void UserProfileFunction(String useremail, String firstname, String lastname, String contactnumber){

        class UserProfileClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);

                if(httpResponseMsg == null){
                    Utillity.showMessage(getActivity(),"WARNING", "Please Try Again");
                }
                else{
                    mJsonString = httpResponseMsg;
                    checkResult("Profile");
                }
            }

            @Override
            protected String doInBackground(String... params) {
                List<String> paramList = new ArrayList<>();

                paramList.add(params[0]);
                paramList.add(params[1]);
                paramList.add(params[2]);
                paramList.add(params[3]);

                return Utillity.getJsonData("Profile", null, null, paramList);
            }
        }

        UserProfileClass userClass = new UserProfileClass();
        userClass.execute(useremail, firstname, lastname, contactnumber);
    }

    public void UserPasswordFunction(String useremail, String changepassword){

        class UserPasswordClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);

                if(httpResponseMsg == null){
                    Utillity.showMessage(getActivity(),"WARNING", "Please Try Again");
                }
                else{
                    mJsonString = httpResponseMsg;
                    checkResult("Password");
                }
            }

            @Override
            protected String doInBackground(String... params) {

                return Utillity.getJsonData("Password", params[0], params[1], null);
            }
        }

        UserPasswordClass userClass = new UserPasswordClass();
        userClass.execute(useremail, changepassword);
    }

    private void checkResult(String sType){

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            // Display Tour Product
            JSONArray jsonArrayNews = jsonObject.getJSONArray(sType);

            if (jsonArrayNews.length() > 0) {
                JSONObject jsonList = jsonArrayNews.getJSONObject(0);

                if(jsonList.getString("result").equals("success")){
                    if (sType.equals("Profile")) {
                        SharedPreferences.Editor editor = pref.edit();
                        //editor.putString("email",jsonList.getString("email"));
                        editor.putString("firstname", jsonList.getString("firstname"));
                        editor.putString("lastname", jsonList.getString("lastname"));
                        editor.putString("contactnumber", jsonList.getString("contactnumber"));
                        editor.commit();
                    }
                    Utillity.showMessage(getActivity(),"SUCCESS", "Update " + sType + " Success");
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
