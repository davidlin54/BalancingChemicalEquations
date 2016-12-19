package com.davidlin54.chemistry.models;

import com.davidlin54.chemistry.BalancedEquationError;
import com.davidlin54.chemistry.BalancingChemicalEquations;
import com.davidlin54.chemistry.InvalidMatrixSizeException;
import com.davidlin54.chemistry.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public static ChemicalEquation buildEquation(List<String> reactantsString, List<String> productsString) throws InvalidMatrixSizeException {
        Map<Compound, Integer> reactants = new LinkedHashMap<>();
        Map<Compound, Integer> products = new LinkedHashMap<>();

        Set<Element> elementSet = new LinkedHashSet<>();
        List[] elementAtomsList = new List[reactantsString.size() + productsString.size()];

        int count = 0;

        for (String reactantString : reactantsString) {
            Compound compound = new Compound(reactantString);
            reactants.put(compound, null);
            // get set of all elements in the equation
            for (Map.Entry<Element, Integer> element : compound.getElements().entrySet()) {
                elementSet.add(element.getKey());
            }

            List<Integer> atomsList = new ArrayList<>();
            for (Element element : elementSet) {
                int atoms = compound.getElements().get(element) == null ? 0 : compound.getElements().get(element);
                atomsList.add(atoms);
            }

            elementAtomsList[count] = atomsList;
            count++;
        }

        for (String productString : productsString) {
            Compound compound = new Compound(productString);
            products.put(compound, null);

            // get set of all elements in the equation
            for (Map.Entry<Element, Integer> element : compound.getElements().entrySet()) {
                elementSet.add(element.getKey());
            }

            List<Integer> atomsList = new ArrayList<>();
            for (Element element : elementSet) {
                int atoms = compound.getElements().get(element) == null ? 0 : compound.getElements().get(element);
                atomsList.add(atoms);
            }

            elementAtomsList[count] = atomsList;
            count++;
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
    private Fraction[] nullSpace() throws InvalidMatrixSizeException, BalancedEquationError {
        Matrix rowEchelonForm = rowEchelonForm();
        int rank = rowEchelonForm.rank(true);

        // check if equation cannot be balanced or there are infinite solutions
        if (nullity(rank) == 0) {
            throw new BalancedEquationError(BalancingChemicalEquations.getContext().getString(R.string.balanced_cannot_error));
        } else if (nullity(rank) >= 2) {
            throw new BalancedEquationError(BalancingChemicalEquations.getContext().getString(R.string.balanced_infinite_error));
        }

        Fraction[] nullSpace = new Fraction[rank];
        for (int i = 0; i < rank; i++) {
            nullSpace[i] = rowEchelonForm.mMatrix[i][mColumns - 1];
        }

        return nullSpace;
    }

    public void balance() throws InvalidMatrixSizeException, BalancedEquationError {
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
