package com.davidlin54.chemistry;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {

    private EditText mReactantsInput;
    private EditText mProductsInput;
    private TextInputLayout mReactantsLayout;
    private TextInputLayout mProductsLayout;
    private TextView mResults;
    private Button mBalanceButton;

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mReactantsInput = (EditText) findViewById(R.id.etReactants);
        mProductsInput = (EditText) findViewById(R.id.etProducts);
        mReactantsLayout = (TextInputLayout) findViewById(R.id.layoutReactants);
        mProductsLayout = (TextInputLayout) findViewById(R.id.layoutProducts);

        mReactantsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mReactantsLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mProductsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mProductsLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mResults = (TextView) findViewById(R.id.tvResult);
        mBalanceButton = (Button) findViewById(R.id.btnBalance);
        mBalanceButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // clear focus and lower keyboard
                        clearInputFocus();

                        mPresenter.balanceEquation(mReactantsInput.getText().toString(), mProductsInput.getText().toString());
                    }
                }
        );

        mPresenter = new MainPresenterImpl(this);
    }


    @Override
    public void setResults(String result) {
        mResults.setText(Html.fromHtml(result));
    }

    @Override
    public void setError(int resId, int errorTextResId) {
        setError(resId, getString(errorTextResId));
    }

    @Override
    public void setError(int resId, String errorText) {
        if (resId == R.id.layoutReactants) {
            mReactantsLayout.setError(errorText);
        } else {
            mProductsLayout.setError(errorText);
        }

        setResults("");
    }

    private void clearInputFocus() {
        final View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
