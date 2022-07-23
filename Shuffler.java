package main;

public class Shuffler {
	public static final void shuffle(Object[] arr) {
		for (int i=0; i<arr.length; i++) {
			Object temp = arr[i];
			int j = (int)(Math.random()*arr.length);
			arr[i] = arr[j];
			arr[j] = temp;
		}
	}
	public static final void shuffle(int[] arr) {
		for (int i=0; i<arr.length; i++) {
			int temp = arr[i];
			int j = (int)(Math.random()*arr.length);
			arr[i] = arr[j];
			arr[j] = temp;
		}
	}
}
