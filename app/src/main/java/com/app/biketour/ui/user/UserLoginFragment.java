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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserLoginFragment extends Fragment {
    private static String TAG = "BikeTour App - User Profile";
    private String mJsonString;
    private static final String ARG_PARAM1 = "param";
    private String mParam;

    ImageButton ibtnTopPopupMenu;
    Button btnLogin, btnSignup;
    SharedPreferences pref;
    EditText loginID, loginPassword;
    String EmailHolder, PasswordHolder;
    Boolean CheckLogin;

    public static UserLoginFragment newInstance(String param) {
        UserLoginFragment fragment = new UserLoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        // Visible top popup menu icon
        ibtnTopPopupMenu = (ImageButton) getActivity().findViewById(R.id.ibtnMenu);
        ibtnTopPopupMenu.setVisibility(View.VISIBLE);

        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM1);
        }

        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);

        loginID = (EditText) root.findViewById(R.id.et_login_id);
        loginPassword = (EditText) root.findViewById(R.id.et_login_pwd);

        btnLogin = (Button) root.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckLoginEmpty();
                if(CheckLogin){
                    UserLoginFunction(EmailHolder, PasswordHolder);
                }
                else {
                    Utillity.showMessage(getActivity(),"WARNING", "Please fill all form fields.");
                }
            }
        });

        btnSignup = (Button) root.findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).replaceFragment(UserSignupFragment.newInstance(mParam));
            }
        });

        return root;
    }

    public void CheckLoginEmpty(){
        EmailHolder = loginID.getText().toString();
        PasswordHolder = loginPassword.getText().toString();

        if(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)) {
            CheckLogin = false;
        }
        else {
            CheckLogin = true ;
        }
    }

    public void UserLoginFunction(String email, String password){

        class UserLoginClass extends AsyncTask<String,Void,String> {

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

                return Utillity.getJsonData("Login", params[0], params[1], null);

            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();
        userLoginClass.execute(email, password);
    }

    private void checkResult(){

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            // Display Tour Product
            JSONArray jsonArrayNews = jsonObject.getJSONArray("Login");

            if (jsonArrayNews.length() > 0) {
                JSONObject jsonList = jsonArrayNews.getJSONObject(0);

                if(jsonList.getString("result").equals("success")){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email",jsonList.getString("email"));
                    editor.putString("firstname",jsonList.getString("firstname"));
                    editor.putString("lastname",jsonList.getString("lastname"));
                    editor.putString("contactnumber",jsonList.getString("contactnumber"));
                    editor.commit();

                    Utillity.showMessage(getActivity(),"SUCCESS", "Login Success");

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
