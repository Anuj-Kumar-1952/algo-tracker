package com.anuj.algotracker.datastructure;

import java.util.Arrays;

/**
 * Very simple stack implementation (LIFO) using array.
 */
public class MyStack<T> {

    private Object[] data;
    private int top; // index of next free slot (also number of elements)

    private static final int DEFAULT_CAPACITY = 10;

    public MyStack() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.top = 0;
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public int size() {
        return top;
    }

    // push element on top
    public void push(T element) {
        if (top == data.length) {
            grow();
        }
        data[top] = element;
        top++;
    }

    // pop element from top
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        top--;
        T value = (T) data[top];
        data[top] = null; // avoid memory leak
        return value;
    }

    // peek element at top (without removing)
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return (T) data[top - 1];
    }

    private void grow() {
        int newCapacity = data.length * 2;
        data = Arrays.copyOf(data, newCapacity);
    }
}
