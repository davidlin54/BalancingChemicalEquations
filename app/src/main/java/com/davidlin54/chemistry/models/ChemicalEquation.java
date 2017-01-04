package com.davidlin54.chemistry.models;

import com.davidlin54.chemistry.exceptions.BalancedEquationException;
import com.davidlin54.chemistry.BalancingChemicalEquations;
import com.davidlin54.chemistry.exceptions.InvalidMatrixSizeException;
import com.davidlin54.chemistry.R;
import com.davidlin54.chemistry.exceptions.ProductException;
import com.davidlin54.chemistry.exceptions.ReactantException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 2016-11-21.
 */

public class ChemicalEquation extends Matrix {
    private Map<Compound, Integer> mReactantMap = new LinkedHashMap<>();
    private Map<Compound, Integer> mProductMap = new LinkedHashMap<>();

    private ChemicalEquation(Fraction[] matrix) throws InvalidMatrixSizeException {
        super(matrix);
    }

    private ChemicalEquation(Fraction[][] matrix) throws InvalidMatrixSizeException {
        super(matrix);
    }

    private ChemicalEquation(Matrix toCopy) {
        super(toCopy);
    }

    public static ChemicalEquation buildEquation(String[] reactantsString, String[] productsString) throws InvalidMatrixSizeException, BalancedEquationException, ReactantException, ProductException {
        Map<Compound, Integer> reactants = new LinkedHashMap<>();
        Map<Compound, Integer> products = new LinkedHashMap<>();

        Map<Element, Boolean> elementSet = new LinkedHashMap<>();
        List[] elementAtomsList = new List[reactantsString.length + productsString.length];

        int count = 0;

        for (String reactantString : reactantsString) {
            // check if compound is valid, if not throw a reactant exception
            Compound compound = null;
            try {
                compound = new Compound(reactantString);
            } catch (IllegalArgumentException e) {
                throw new ReactantException(e.getMessage());
            }

            reactants.put(compound, null);
            // get set of all elements in the equation
            for (Map.Entry<Element, Integer> element : compound.getElements().entrySet()) {
                elementSet.put(element.getKey(), false);
            }

            List<Integer> atomsList = new ArrayList<>();
            for (Element element : elementSet.keySet()) {
                int atoms = compound.getElements().get(element) == null ? 0 : compound.getElements().get(element);
                atomsList.add(atoms);
            }

            elementAtomsList[count] = atomsList;
            count++;
        }

        for (String productString : productsString) {
            // check if compound is valid, if not throw a product exception
            Compound compound = null;
            try {
                compound = new Compound(productString);
            } catch (IllegalArgumentException e) {
                throw new ProductException(e.getMessage());
            }

            products.put(compound, null);

            for (Map.Entry<Element, Integer> element : compound.getElements().entrySet()) {
                // check if products introduce a new element; if so, throw error
                if (!elementSet.containsKey(element.getKey())) {
                    throw new BalancedEquationException(BalancingChemicalEquations.getContext().getString(R.string.balanced_cannot_error));
                }
            }

            List<Integer> atomsList = new ArrayList<>();
            for (Element element : elementSet.keySet()) {
                // used to check if product contains all the elements in reactant
                int atoms = 0;
                if (compound.getElements().get(element) != null) {
                    atoms = compound.getElements().get(element);
                    elementSet.put(element, true);
                }
                atomsList.add(atoms);
            }

            elementAtomsList[count] = atomsList;
            count++;
        }

        for (Map.Entry<Element, Boolean> entry : elementSet.entrySet()) {
            if (!entry.getValue()) {
                throw new BalancedEquationException(BalancingChemicalEquations.getContext().getString(R.string.balanced_cannot_error));
            }
        }

        // build the atoms matrix
        Fraction[][] atomsMatrix = new Fraction[elementSet.size()][elementAtomsList.length];
        for (int i = 0; i < elementAtomsList.length; i++) {
            for (int j = 0; j < elementSet.size(); j++) {
                if (j < elementAtomsList[i].size()) {
                    atomsMatrix[j][i] = new Fraction((int) elementAtomsList[i].get(j));
                } else {
                    atomsMatrix[j][i] = new Fraction(0);
                }
            }
        }

        ChemicalEquation equation = new ChemicalEquation(atomsMatrix);
        equation.mReactantMap = reactants;
        equation.mProductMap = products;
        return equation;
    }

    // get the nullity of the equation
    private int nullity(int rank) {
        return mColumns - rank;
    }

    // the nullspace of the matrix is the coefficients of the compounds
    private Fraction[] nullSpace() throws InvalidMatrixSizeException, BalancedEquationException {
        Matrix rowEchelonForm = rowEchelonForm();
        int rank = rowEchelonForm.rank(true);

        // check if equation cannot be balanced or there are infinite solutions
        if (nullity(rank) == 0) {
            throw new BalancedEquationException(BalancingChemicalEquations.getContext().getString(R.string.balanced_cannot_error));
        } else if (nullity(rank) >= 2) {
            throw new BalancedEquationException(BalancingChemicalEquations.getContext().getString(R.string.balanced_infinite_error));
        }

        Fraction[] nullSpace = new Fraction[rank];
        for (int i = 0; i < rank; i++) {
            nullSpace[i] = rowEchelonForm.mMatrix[i][mColumns - 1];
        }

        return nullSpace;
    }

    public void balance() throws InvalidMatrixSizeException, BalancedEquationException {
        Fraction[] nullSpace = nullSpace();
        Fraction[] coefficients = new Fraction[nullSpace.length + 1];
        // add the last coefficient to the list
        coefficients[nullSpace.length] = new Fraction(-1);

        long lowestCommonDenominator = 1;
        for (int i = 0; i < nullSpace.length; i++) {
            Fraction coefficient = nullSpace[i];
            if (lowestCommonDenominator % coefficient.getDenominator() != 0) {
                lowestCommonDenominator = findLCM(lowestCommonDenominator, coefficient.getDenominator());
            }
            coefficients[i] = coefficient;
        }

        Fraction lcdFraction = new Fraction(lowestCommonDenominator);
        // multiply by lowest common denominator then reduce to ensure all are integers
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = coefficients[i].multiply(lcdFraction);
            coefficients[i].reduce();
        }

        int count = 0;
        for (Compound reactant : mReactantMap.keySet()) {
            mReactantMap.put(reactant, (int) Math.abs(coefficients[count].getNumerator()));
            count++;
        }

        for (Compound product : mProductMap.keySet()) {
            mProductMap.put(product, (int) Math.abs(coefficients[count].getNumerator()));
            count++;
        }
    }

    public Map<Compound, Integer> getReactants() {
        return mReactantMap;
    }

    public Map<Compound, Integer> getProducts() {
        return mProductMap;
    }

    // find greatest common denominator
    private long findGCD(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    // find lease common multiple
    private long findLCM(long a, long b) {
        return (a*b)/findGCD(a, b);
    }
}
