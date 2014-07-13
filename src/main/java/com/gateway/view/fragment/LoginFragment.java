package com.gateway.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gateway.GatewayApp;
import com.gateway.R;
import com.gateway.event.LoginEvent;
import com.gateway.event.NowLoggedInEvent;
import com.gateway.network.LoginService;
import com.gateway.view.SuccessErrorButton;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class LoginFragment extends BaseFragment {

    private final String STATE_USERNAME_TEXT = "username_text_state";
    private final String STATE_PASSWORD_TEXT = "password_text_state";
    private final String STATE_USERNAME_ERROR = "username_error_state";
    private final String STATE_PASSWORD_ERROR = "password_error_state";
    private final String STATE_BUTTON = "button_state";

    @InjectView(R.id.login_page_username) EditText mUsername;
    @InjectView(R.id.login_page_password) EditText mPassword;
    @InjectView(R.id.login_page_login_btn) SuccessErrorButton mLoginBtn;

    @Inject LoginService mLoginService;

    public static LoginFragment newInstance() { return new LoginFragment(); }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(STATE_USERNAME_TEXT, mUsername.getText());
        outState.putCharSequence(STATE_PASSWORD_TEXT, mPassword.getText());
        outState.putCharSequence(STATE_USERNAME_ERROR, mUsername.getError());
        outState.putCharSequence(STATE_PASSWORD_ERROR, mPassword.getError());
        outState.putInt(STATE_BUTTON, mLoginBtn.getProgress());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mUsername.setText(savedInstanceState.getCharSequence(STATE_USERNAME_TEXT));
            mPassword.setText(savedInstanceState.getCharSequence(STATE_PASSWORD_TEXT));
            mUsername.setError(savedInstanceState.getCharSequence(STATE_USERNAME_ERROR));
            mPassword.setError(savedInstanceState.getCharSequence(STATE_PASSWORD_ERROR));
            mLoginBtn.setProgress(savedInstanceState.getInt(STATE_BUTTON));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.inject(this, rootView);
        mLoginBtn.setIndeterminateProgressMode(true);

        return rootView;
    }

    /**
     * Click listener for the login button; Validates input before logging in.
     */
    @OnClick(R.id.login_page_login_btn)
    public void validateLogin() {

        clearErrors();

        if (isEmpty(mUsername.getText().toString())) {
            setError(mUsername, "Username cannot be empty");
            return;
        }

        if (isEmpty(mPassword.getText().toString())) {
            setError(mPassword, "Password cannot be empty");
            return;
        }

        login();
    }

    /**
     * Sends login credentials to the {@code LoginService}
     * and updates the view to show loading progress
     */
    private void login() {

        // update login button to show loading
        mLoginBtn.showLoading();

        mLoginService.login(
            mUsername.getText().toString(),
            mPassword.getText().toString()
        );
    }

    /**
     * Called from the {@code LoginService} after login completes.
     * @param loginEvent
     */
    @Subscribe
    public void onLoginEvent(LoginEvent loginEvent) {

        // update the button state based on a success or failure
        if (loginEvent.isSuccess()) {
            mLoginBtn.showSuccess();

            // update the login manager state
            ((GatewayApp)getActivity().getApplication()).login(mUsername.getText().toString());

            // notify everyone else of a successful login after 1 second
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBus.post(new NowLoggedInEvent());
                }
            }, 2000);
        } else {
            mLoginBtn.showError();

            // set a timer to switch back to the regular button state after a few seconds
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoginBtn.showIdle();
                }
            }, 3000);
        }
    }

    /**
     * Hides any EditText errors that had been set previously
     */
    private void clearErrors() {
        mUsername.setError(null);
        mUsername.clearFocus();
        mPassword.setError(null);
        mPassword.clearFocus();
    }

    private void setError(EditText editText, String error) {
        editText.setError(error);
        editText.requestFocus();
    }
}
