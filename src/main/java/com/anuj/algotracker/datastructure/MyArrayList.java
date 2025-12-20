package com.anuj.algotracker.datastructure;

import java.util.Arrays;

public class MyArrayList<T> {

    // Internal array to store elements
    private Object[] data;

    // How many elements are actually stored
    private int size;

    // Initial capacity
    private static final int DEFAULT_CAPACITY = 10;

    public MyArrayList() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Return number of elements
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Add element at the end
    public void add(T element) {
        // If array is full, grow it
        if (size == data.length) {
            grow();
        }
        data[size] = element;
        size++;
    }

    // Get element by index
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", size: " + size);
        }
        return (T) data[index];
    }

    // Just double the size of the internal array
    private void grow() {
        int newCapacity = data.length * 2;
        data = Arrays.copyOf(data, newCapacity);
    }
}
