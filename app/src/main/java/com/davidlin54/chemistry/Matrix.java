package com.davidlin54.chemistry;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by David on 2016-11-09.
 */

public class Matrix {
    protected double[][] mMatrix;
    protected int mRows;
    protected int mColumns;

    public Matrix(double[][] matrix) throws InvalidMatrixSizeException {
        // check if column/row = 0
        if (matrix.length == 0 || matrix[0].length == 0) {
            throw new InvalidMatrixSizeException();
        }

        mRows = matrix.length;
        mColumns = matrix[0].length;

        // check if 2d array is rectangular
        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i].length != mColumns) {
                throw new InvalidMatrixSizeException();
            }
        }

        mMatrix = matrix;
    }

    public Matrix(double[] matrix) throws InvalidMatrixSizeException {
        // check if column/row = 0
        if (matrix.length == 0) {
            throw new InvalidMatrixSizeException();
        }

        mRows = 1;
        mColumns = matrix.length;

        mMatrix = new double[1][mColumns];
        mMatrix[0] = matrix;
    }

    public Matrix(Matrix toCopy) {
        mRows = toCopy.mRows;
        mColumns = toCopy.mColumns;
        mMatrix = new double[mRows][mColumns];
        for (int i = 0; i < mRows; i++) {
            mMatrix[i] = Arrays.copyOf(toCopy.mMatrix[i], toCopy.mColumns);
        }
    }

    private boolean isSameSize(Matrix matrix) {
        return mRows == matrix.mRows && mColumns == matrix.mColumns;
    }

    private boolean isSquareMatrix() {
        return mRows == mColumns;
    }

    public Matrix add(Matrix matrix) throws InvalidMatrixSizeException {
        // check if same size, otherwise invalid operation
        if (!isSameSize(matrix)) {
            throw new InvalidMatrixSizeException();
        }
        double[][] result = new double[mRows][mColumns];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                result[i][j] = mMatrix[i][j] + matrix.mMatrix[i][j];
            }
        }
        return new Matrix(result);
    }

    public Matrix subtract(Matrix matrix) throws InvalidMatrixSizeException {
        // check if same size, otherwise invalid operation
        if (!isSameSize(matrix)) {
            throw new InvalidMatrixSizeException();
        }
        double[][] result = new double[mRows][mColumns];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                result[i][j] = mMatrix[i][j] - matrix.mMatrix[i][j];
            }
        }
        return new Matrix(result);
    }

    private double determinant(double[][] matrixArray) {
        int determinant = 0;
        int length = matrixArray.length;
        if (length == 1) {
            return matrixArray[0][0];
        } else if (length == 2) {
            return matrixArray[0][0] * matrixArray[1][1] - matrixArray[0][1] * matrixArray[1][0];
        } else {
            for (int i = 0; i < length; i++) {
                int detSign = (int) Math.pow(-1, i);
                double[][] detArray = new double[length - 1][length - 1];
                // left and right side of the current column
                for (int j = 1; j < length; j++) {
                    for (int k = 0; k < length; k++) {
                        if (k != i) {
                            detArray[j - 1][k - (k > i ? 1 : 0)] = matrixArray[j][k];
                        }
                    }
                }

                determinant += detSign * matrixArray[0][i] * determinant(detArray);
            }
        }

        return determinant;
    }

    public double determinant() throws InvalidMatrixSizeException {
        // check if square matrix, otherwise invalid operation
        if (!isSquareMatrix()) {
            throw new InvalidMatrixSizeException();
        }
        return determinant(mMatrix);
    }

    public boolean isInvertible() throws InvalidMatrixSizeException {
        return determinant() == 0;
    }

    public Matrix inverse(double determinant) throws InvalidMatrixSizeException {
        // if determinant = 0, then invalid operation
        if (determinant == 0) {
            return null;
        }

        double[][] copy = new double[mRows][mColumns];
        double[][] result = new double[mRows][mColumns];

        // setup identity matrix
        for (int i = 0; i < mRows; i++) {
            copy[i] = Arrays.copyOf(mMatrix[i], mColumns);
            result[i][i] = 1;
        }

        for (int i = 0; i < mRows; i++) {
            // check pivot value
            if (copy[i][i] == 0) {
                for (int j = i + 1; j < mRows; j++) {
                    if (copy[j][i] != 0) {
                        double[] temp = copy[i];
                        copy[i] = copy[j];
                        copy[j] = temp;
                        break;
                    }
                }
            }

            double pivotValue = copy[i][i];

            // reduce row
            for (int j = 0; j < mRows; j++) {
                copy[i][j] /= pivotValue;
                result[i][j] /= pivotValue;
            }

            // eliminate other rows
            for (int j = 0; j < mRows; j++) {
                if (j != i) {
                    double denom = copy[j][i];
                    for (int k = 0; k < mColumns; k++) {
                        copy[j][k] -= copy[i][k] * denom;
                        result[j][k] -= result[i][k] * denom;
                    }
                }
            }
        }

        return new Matrix(result);
    }

    public Matrix inverse() throws InvalidMatrixSizeException {
        double determinant = determinant();
        return inverse(determinant);
    }

    public Matrix multiply(double multiplier) {
        Matrix result = new Matrix(this);

        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                result.mMatrix[i][j] *= multiplier;
            }
        }

        return result;
    }

    public Matrix multiply(Matrix multiplier) throws InvalidMatrixSizeException {
        // check if cols = multiplier.rows, otherwise invalid operation
        if (mColumns != multiplier.mRows) {
            throw new InvalidMatrixSizeException();
        }

        double[][] result = new double[mRows][multiplier.mColumns];

        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < multiplier.mColumns; j++) {
                for (int k = 0; k < mColumns; k++) {
                    result[i][j] += mMatrix[i][k] * multiplier.mMatrix[k][j];
                }
            }
        }

        return new Matrix(result);
    }

    public Matrix rowEchelonForm() throws InvalidMatrixSizeException {
        double[][] result = new double[mRows][mColumns];

        for (int i = 0; i < mRows; i++) {
            result[i] = Arrays.copyOf(mMatrix[i], mColumns);
        }

        int i2 = 0;
        for (int i = 0; i < mRows && i2 < mColumns; i++, i2++) {
            // check pivot value
            if (result[i][i2] == 0) {
                for (int j = i + 1; j < mRows; j++) {
                    if (result[j][i2] != 0) {
                        double[] temp = result[i];
                        result[i] = result[j];
                        result[j] = temp;
                        break;
                    }
                }
            }

            // recheck pivot value
            if (result[i][i2] == 0) {
                i--;
            } else {
                double pivotValue = result[i][i2];

                // reduce row
                for (int j = 0; j < mColumns; j++) {
                    result[i][j] /= pivotValue;
                }

                // eliminate other rows
                for (int j = 0; j < mRows; j++) {
                    if (j != i) {
                        double denom = result[j][i2];
                        for (int k = 0; k < mColumns; k++) {
                            result[j][k] -= result[i][k] * denom;
                        }
                    }
                }
            }
        }

        return new Matrix(result);
    }

    protected int rank() {
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                if (mMatrix[i][j] != 0) {
                    break;
                } else if (j == mColumns - 1) {
                    return i;
                }
            }
        }
        return mRows;
    }

    public int rank(boolean isReduced) throws InvalidMatrixSizeException {
        if (isReduced) {
            return rank();
        }
        return rowEchelonForm().rank();
    }

    @Override
    public String toString() {
        String result = "";

        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                result += mMatrix[i][j] + " ";
            }
            result += "\n";
        }

        return result;
    }
}
