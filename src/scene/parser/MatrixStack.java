package scene.parser;

import scene.data.Matrix4f;

/**
 * Used for traversing the scenegraph, holds a stack of transformationmatrices, pushing one on top will multiply it with the current top
 */
public class MatrixStack {

	private StackElement top;

	public MatrixStack() {
		// Put "empty" matrix here
		Matrix4f startMatrix = new Matrix4f();
		startMatrix.setIdentity();

		top = new StackElement(startMatrix, null);
	}

	public Matrix4f peek() {
		return top.getData();
	}

	public Matrix4f pop() {
		if (isEmpty()) return null;
		Matrix4f temp = top.getData();
		top = top.next();
		return temp;
	}

	public void push(Matrix4f data) {
		// Don't store data itself, but store the product with current top!!
		Matrix4f elementData = data.multiply(top.getData());
		StackElement newElement = new StackElement(elementData, top);
		top = newElement;
	}

	public boolean isEmpty() {
		return top == null;
	}

	private class StackElement {

		private Matrix4f data;
		private StackElement next;

		public StackElement(Matrix4f data, StackElement next) {
			this.data = data;
			this.next = next;
		}

		public StackElement next() {
			return next;
		}

		public StackElement(Matrix4f data) {
			this.data = data;
		}

		public Matrix4f getData() {
			return data;
		}
	}

}
