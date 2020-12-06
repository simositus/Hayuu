package com.tubes.sandangin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tubes.sandangin.MainActivity;
import com.tubes.sandangin.R;
import com.tubes.sandangin.database.DB_Handler;
import com.tubes.sandangin.database.SessionManager;
import com.tubes.sandangin.fragments.Account;
import com.tubes.sandangin.fragments.BlankFragment;
import com.tubes.sandangin.interfaces.FinishActivity;
import com.tubes.sandangin.pojo.User;
import com.tubes.sandangin.utils.Constants;
import com.tubes.sandangin.utils.Util;

public class ProfilEdit extends Fragment{

    EditText name, email, password, mobile;
    Button btnUpdate;
    ImageView back, showpassword;
    boolean isPasswordShown = false;
    FinishActivity finishActivityCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        finishActivityCallback = (FinishActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profil_edit, container, false);

        setIds(view);
        setClickListeners();

        return view;
    }

    // Set Ids
    private void setIds(View view) {
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        mobile = view.findViewById(R.id.mobile);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        back = view.findViewById(R.id.back);
        showpassword = view.findViewById(R.id.showpassword);
    }

    // Set Click Listeners
    private void setClickListeners() {
        // Update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {

                // Set Values To User Model
                User user = new User();
                user.setName(name.getText().toString());
                user.setMobile(mobile.getText().toString());
                user.setPassword(password.getText().toString());

                // Validate Fields
                if (user.getName().trim().length() > 0) {
                    if (user.getEmail().trim().length() > 0) {
                        if (user.getMobile().trim().length() > 0) {
                            if (user.getPassword().trim().length() > 0) {

                                // Update User
                                DB_Handler db_handler = new DB_Handler(getActivity());
                                long isInserted = db_handler.registerUser(user.getName(), user.getEmail(), user.getMobile(), user.getPassword());
                                if (isInserted != -1) {
                                    // Save Session
                                    SessionManager sessionManager = new SessionManager(getActivity());
                                    sessionManager.saveSession(Constants.SESSION_PASSWORD, user.getPassword());

                                    // Load Main Activity
                                    Intent i = new Intent(getActivity(), Account.class);
                                    startActivity(i);
                                    finishActivityCallback.finishActivity();
                                } else {
                                    showErrorToastEmailExists();
                                }

                            } else {
                                showErrorToast(getActivity().getResources().getString(R.string.password));
                            }
                        } else {
                            showErrorToast(getActivity().getResources().getString(R.string.mobile));
                        }
                    }  else {
                        showErrorToast(getActivity().getResources().getString(R.string.email));
                    }
                }  else {
                showErrorToast(getActivity().getResources().getString(R.string.name));
                }
            }
        });

        // Back Button Click
        back.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                ft.replace(R.id.fragment, new BlankFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        // Show Password
        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordShown) {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    showpassword.setImageResource(R.drawable.ic_eye_off_grey600_24dp);
                    isPasswordShown = false;
                } else {
                    password.setTransformationMethod(null);
                    showpassword.setImageResource(R.drawable.ic_eye_white_24dp);
                    isPasswordShown = true;
                }
            }
        });
    }

    // Show Error Toast
    private void showErrorToast(String value) {
        Toast.makeText(getActivity(), value + getResources().getString(R.string.BlankError), Toast.LENGTH_LONG).show();
    }

    // Show Error Toast - Email Exists
    private void showErrorToastEmailExists() {
        Toast.makeText(getActivity(), R.string.EmailExistsError, Toast.LENGTH_LONG).show();
    }
}

