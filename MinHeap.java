
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinHeap {

    private double[] heap; // array of heap entries
    private int lastIndex; // index of last entry
    private static final int DEFAULT_INITIAL_CAPACITY = 25;
    private static final double ALPHA = 999999;

    public MinHeap() {
	this(DEFAULT_INITIAL_CAPACITY); // call next constructor
    }

    public MinHeap(int initialCapacity) {
	@SuppressWarnings("unchecked")
	double[] tempHeap = new double[initialCapacity + 1];
	heap = tempHeap;
	lastIndex = 0;
    }

    /**
     * Constructor accepting an array of doubles
     *
     * @param an array of doubles
     */
    public MinHeap(double[] entries) {
	@SuppressWarnings("unchecked")
	double[] tempHeap = new double[entries.length + 1];
	heap = tempHeap;
	lastIndex = entries.length;

	// copy given array to data field
	for (int index = 0; index < entries.length; index++) {
	    heap[index + 1] = entries[index];
	}

	// create heap
	for (int rootIndex = lastIndex / 2; rootIndex > 0; rootIndex--) {
	    reHeapDown(rootIndex);
	}
    }

    /**
     * Guarantee that the heap has enough space for the next element
     */
    private void ensureCapacity() {
	//if the heap doesnt have enough space
	if ((lastIndex + 1) == heap.length) {
	    //Create a new array with a double size. Elements are copied over
	    heap = Arrays.copyOf(heap, heap.length * 2);
	}
    }

    /**
     * Insert a new element into the heap
     */
    public void insert(double newEntry) {
	//Make sure the heap has enough space
	ensureCapacity();

	//Put the new element at the end and increment the last index
	heap[++lastIndex] = newEntry;

	//Reheap up the new entry
	reHeapUp(lastIndex);
    }

    /**
     * This method reheaps up an element if it violates the heap
     *
     * @param the index of the element to reheap up
     */
    public void reHeapUp(int index) {
	double value = heap[index];
	int parentIndex = index / 2;
	while ((parentIndex > 0) && (value < heap[parentIndex])) {
	    heap[index] = heap[parentIndex];
	    index = parentIndex;
	    parentIndex = index / 2;
	}
	heap[index] = value;
    }

    /**
     * This method removes an element from the front of the heap
     *
     * @return the removed value
     */
    public double deleteMin() {
	double root = 0;
	if (!isEmpty()) {
	    //replace the deleted element by the element at the end of the heap
	    root = heap[1];
	    heap[1] = heap[lastIndex];

	    //decrement the last index and reheap down the root
	    lastIndex--;
	    reHeapDown(1);
	}
	//Return the deleted value
	return root;
    }

    /**
     * This method removes a value from the heap
     *
     * @return true if succeed, false if the value doesn't exist
     */
    public boolean delete(int index) {
	boolean found = false;
	for (int i = 1; i <= lastIndex; i++) {
	    if (index >= 1 && index <= lastIndex) {
		/*Decrease the key by subtracting it to a HUGE value
		 so that the node becomes the root*/
		decreaseKey(index, ALPHA);
		//Remove the root
		deleteMin();
		found = true;
	    }
	}
	//The value is not found
	return found;
    }

    /**
     * This method reheaps down a node if it violates the heap's rule
     *
     * @param the index of the node
     */
    private void reHeapDown(int rootIndex) {
	boolean done = false;
	double rootValue = heap[rootIndex];
	int leftChildIndex = rootIndex * 2;

	while (!done && (leftChildIndex <= lastIndex)) {
	    // assume left child smaller
	    int smallerChildIndex = leftChildIndex;

	    //If the right child exists, find out the smaller one between them
	    int rightChildIndex = leftChildIndex + 1;
	    if ((rightChildIndex <= lastIndex)
		    && (heap[rightChildIndex] < heap[smallerChildIndex])) {
		smallerChildIndex = rightChildIndex;
	    }
	    if (rootValue > heap[smallerChildIndex]) {
		heap[rootIndex] = heap[smallerChildIndex];
		rootIndex = smallerChildIndex;
		leftChildIndex = rootIndex * 2;
	    } else {
		done = true;
	    }
	}
	heap[rootIndex] = rootValue;
    }

    /**
     * Get the root value
     */
    public double getMin() {
	return heap[1];
    }

    /**
     * Check whether the heap is empty
     */
    public boolean isEmpty() {
	return lastIndex < 1;
    }

    /**
     * Get the number of elements in the heap
     */
    public int getSize() {
	return lastIndex;
    }

    /**
     * Remove all elements from the heap
     */
    public void clear() {
	lastIndex = 0;
    }

    /**
     * This method print the heap to the console
     */
    public String toString() {
	StringBuffer buffer = new StringBuffer();
	for (int i = 1; i <= lastIndex; i++) {
	    buffer.append(String.format("%.0f", heap[i]) + " ");
	}
	return buffer.toString();
    }

    /**
     * This method decreases the value of a key
     *
     * @param the index of the key
     * @param the decreasing amount
     */
    public void decreaseKey(int p, double delta) {
	heap[p] = heap[p] - delta;

	//Attempts to bring the decreased node up
	reHeapUp(p);
    }

    /**
     * This method decreases the value of a key
     *
     * @param the index of the key
     * @param the increasing amount
     */
    public void increaseKey(int p, double delta) {
	heap[p] = heap[p] + delta;

	//Attempts to bring the decreased node down
	reHeapDown(p);
    }

    public static void main(String[] args) {
	double[] arr = readFile("input.txt");
	MinHeap minHeap = new MinHeap(arr);
	System.out.println(minHeap.toString());

	//Read Script file
	new ScriptReader(minHeap, "script.txt");
    }

    public static double[] readFile(String fileName) {
	try {
	    BufferedReader buff = new BufferedReader(new FileReader(fileName));
	    StringTokenizer strTok = new StringTokenizer(buff.readLine(), " ");
	    double[] arr = new double[strTok.countTokens()];
	    for (int i = 0; i < arr.length; i++) {
		arr[i] = Double.parseDouble(strTok.nextToken());
	    }
	    return arr;
	} catch (Exception ex) {
	    Logger.getLogger(MinHeap.class.getName()).log(Level.SEVERE, null,
		    ex);
	}
	return null;
    }
}
