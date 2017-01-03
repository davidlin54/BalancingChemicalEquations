package com.davidlin54.chemistry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {

    private EditText mReactantsInput;
    private EditText mProductsInput;
    private TextView mResults;
    private Button mBalanceButton;

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mReactantsInput = (EditText) findViewById(R.id.etReactants);
        mProductsInput = (EditText) findViewById(R.id.etProducts);
        mResults = (TextView) findViewById(R.id.tvResult);
        mBalanceButton = (Button) findViewById(R.id.btnBalance);
        mBalanceButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
}
