package linearAlgebra;

import java.util.Arrays;

public class Matrix {

	//Data
		private double[][] data;
		
		/**
		 * Constructs a new matrix object with the given number of rows and columns, initializing each entry to zero.
		 * @param rows
		 * @param columns
		 */
		public Matrix(int rows, int columns) {
			data = new double[rows][columns];
		}
		
		/**
		 * Constructs a new matrix object containing the same entries as the given two-dimensional array.
		 * @param sum
		 */
		public Matrix(double[][] sum) {
			
			int rows = sum.length;
			int cols = sum[0].length;
			for(int i = 1; i < rows; i++) if(sum[i].length != cols) throw new IllegalArgumentException(
					"Matrix object cannot be created from a non-rectangular array."
				);
			
			data = new double[rows][cols];
			
			for(int j = 0; j < rows; j++) {
				
				for(int i = 0; i < cols; i++) {
					
					data[j][i] = sum[j][i];
					
				}
				
			}
			
		}

		/**
		 * Sets the value of the given row and column equal to the given double value.
		 * @param row
		 * @param col
		 * @param val
		 */
		public void set(int row, int col, double val) {
			data[row][col] = val;
		}
		
		/**
		 * Returns the value of the entry located at the intersection of the given
		 * row and column indexes. 
		 * @param row
		 * @param col
		 * @return value
		 */
		public double get(int row, int col) {
			return data[row][col];
		}
		
		/**
		 * Returns a new double array containing the entries of this
		 * matrix.
		 * @return double array
		 */
		public double[][] toArray() {
			
			double[][] copyOfMatrix = new double[data.length][data[0].length];
			
			for(int i = 0; i < data.length; i++)
				copyOfMatrix[i] = Arrays.copyOf(data[i], data[i].length);
			
			return copyOfMatrix;
			
		}
		
		/**
		 * Returns the number of rows in this matrix.
		 * @return number of rows
		 */
		public int getRowCount() {
			return data.length;
		}
		
		/**
		 * Returns the number of columns in this matrix.
		 * @return number of columns
		 */
		public int getColCount() {
			return data[0].length;	
		}
		
		/**
		 * Returns a new matrix object equal to the result of right-multiplying the
		 * given matrix with the current matrix.
		 * @param factor
		 * @return product
		 */
		public Matrix multiply(Matrix factor) {
			
			if(this.getColCount() != factor.getRowCount()) 
				throw new IllegalArgumentException("Cannot multiply two matrices if the "
						+ "column count of the first does not equal the row count of the"
						+ "second.");
			
			Matrix product = new Matrix(this.getRowCount(), factor.getColCount());
			
			for(int j = 0; j < product.getRowCount(); j++)
				for(int i = 0; i < product.getColCount(); i++)
					product.set(j, i, this.getRow(j).dot(factor.getCol(i)));
			
			return product;
			
		}
		
		/**
		 * Returns a new vector object equal to the result of multiplying the given vector
		 * with the current matrix
		 * @param factor
		 * @return product
		 */
		public Vector multiply(Vector factor) {
			
			if(this.getColCount() != factor.size()) 
				throw new IllegalArgumentException("Cannot multiply a matrix and a vector "
						+ "if the column count of the matrix does not equal the size of "
						+ "the vector");
			
			Vector product = new Vector(this.getRowCount());
			
			for(int i = 0; i < product.size(); i++)
				product.set(i, factor.dot(this.getRow(i)));
			
			return product;
			
		}
		
		/**
		 * Private helper method returning a vector representation of
		 * the row of this matrix at the given index. Used by the muliply()
		 * methods.
		 * @param rowIndex
		 * @return row vector
		 */
		private Vector getRow(int rowIndex) {
			return new Vector(data[rowIndex]);
		}
		
		/**
		 * Private helper method returning a vector representation of
		 * the column of this matrix at the given index. Used by the
		 * multiply(Matrix factor) method.
		 * @param rowIndex
		 * @return column vector
		 */
		private Vector getCol(int colIndex) {
			Vector column = new Vector(this.getRowCount());
			for(int i = 0; i < column.size(); i++) {
				column.set(i, this.get(i, colIndex));
			}
			return column;
		}
		
		/**
		 * Returns a new Matrix object equal to the result of adding all corresponding
		 * elements in the current and given matrices.
		 * @param addend
		 * @return sum
		 */
		public Matrix add(Matrix addend) {
			
			if(addend.getRowCount() != this.getRowCount() || addend.getColCount() != this.getColCount())
				throw new IllegalArgumentException("Matrices of unequal size cannot be added.");
			
			Matrix sum = new Matrix(this.getRowCount(), this.getColCount());
			
			for(int r = 0, c = 0; r < this.getRowCount();) {
				
				sum.set(r, c, this.get(r, c) + addend.get(r, c));
				
				c++;
				if(c == this.getColCount()) {
					c = 0;
					r++;
				}
				
			}
			
			return sum;
			
		}
		
		/**
		 * Returns a new Matrix object equal to the result of scaling
		 * every entry of the current matrix by the given scaling factor.
		 * @param scalingFactor
		 * @return scaled matrix
		 */
		public Matrix scale(double scalingFactor) {
			
			Matrix product = new Matrix(this.getRowCount(), this.getColCount());
			
			for(int r = 0, c = 0; r < this.getRowCount();) {
				
				product.set(r, c, scalingFactor * this.get(r, c));
				
				c++;
				if(c == this.getColCount()) {
					c = 0;
					r++;
				}
			}
			
			return product;
			
		}
		
		public boolean equals(Matrix other) {
			
			int R = this.getRowCount();
			if(other.getRowCount() != R) return false;
			int C = this.getColCount();
			if(other.getColCount() != C) return false;
			
			for(int r = 0; r < R; r++) {
				
				for(int c = 0; c < C; c++) {
					
					if(this.data[r][c] != other.data[r][c]) return false;
					
				}
				
			}
			
			return true;
			
		}
		
		@Override
		/**
		 * Returns a string representation of the matrix by calling the toString() method
		 * from java.util.Arrays on each row of the matrix, and seperating them with line
		 * breaks.
		 */
		public String toString() {
			
			StringBuilder message = new StringBuilder();
			
			for(int i = 0; i < data.length - 1; i++) {
				message.append(Arrays.toString(data[i]) + "\n");
			}
			
			message.append(Arrays.toString(data[data.length - 1]));
			
			return message.toString();
			
		}
	
}
