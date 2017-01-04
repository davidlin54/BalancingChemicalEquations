package com.davidlin54.chemistry;

import android.util.Log;

import com.davidlin54.chemistry.models.ChemicalEquation;
import com.davidlin54.chemistry.models.Compound;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 2016-11-15.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainView mView;

    public MainPresenterImpl(MainView view) {
        mView = view;
    }

    @Override
    public void balanceEquation(String reactantsString, String productsString) {
        // check if reactants or products is an empty string
        if (reactantsString.replace(" ", "").replace("\\+", "").equals("") ||
                productsString.replace(" ", "").replace("\\+", "").equals("")) {
            if (reactantsString.replace(" ", "").replace("\\+", "").equals("")) {
                mView.setError(R.id.layoutReactants, R.string.reactant_empty_error);
            }

            if (productsString.replace(" ", "").replace("\\+", "").equals("")) {
                mView.setError(R.id.layoutProducts, R.string.products_empty_error);
            }

            return;
        }

        ChemicalEquation chemicalEquation = null;
        try {
             chemicalEquation = ChemicalEquation.buildEquation(
                    reactantsString.replace(" ", "").split("\\+"),
                    productsString.replace(" ", "").split("\\+"));
            chemicalEquation.balance();
            Map<Compound, Integer> reactants = chemicalEquation.getReactants();
            Map<Compound, Integer> products = chemicalEquation.getProducts();

            // get result string
            String result = "";

            Iterator<Map.Entry<Compound, Integer>> iterator = reactants.entrySet().iterator();
            for (int i = 0; i < reactants.size(); i++) {
                if (i != 0) {
                    result += " + ";
                }
                Map.Entry<Compound, Integer> entry = iterator.next();

                result += (entry.getValue() != 1 ? entry.getValue().toString() : "") + formatCompound(entry.getKey().toString());
            }

            iterator = products.entrySet().iterator();
            for (int i = 0; i < products.size(); i++) {
                if (i != 0) {
                    result += " + ";
                } else {
                    result += " → ";
                }
                Map.Entry<Compound, Integer> entry = iterator.next();

                result += (entry.getValue() != 1 ? entry.getValue().toString() : "") + formatCompound(entry.getKey().toString());
            }

            mView.setResults(result);
        } catch (Exception e) {
            e.printStackTrace();
            mView.setResults(e.getMessage());
        }
    }

    private String formatCompound(final String compound) {
        String resultString = "";
        boolean isNumber = false;
        for (int i = 0; i < compound.length(); i++) {
            char c = compound.charAt(i);

            if (c >= '0' && c <= '9') {
                if (!isNumber) {
                    resultString += "<sub><small><small>";
                    isNumber = true;
                }
            } else {
                if (isNumber) {
                    resultString += "</small></small></sub>";
                    isNumber = false;
                }
            }
            resultString += c;
        }

        if (isNumber) {
            resultString += "</small></small></sub>";
        }

        return resultString;
    }

}
