package com.anuj.algotracker.datastructure;

/**
 * Simple generic circular queue implementation using array.
 * Based on Apna College logic but modified for objects & reusability.
 * idea for using this queue:
 * “Give me the next N problems to practice (in order) using my own queue
 * implementation.”
 */
public class MyQueue<T> {

    private Object[] cirArr; // internal circular array
    private int rear;
    private int front;
    private int size; // capacity

    public MyQueue(int capacity) {
        this.size = capacity;
        this.cirArr = new Object[capacity];
        this.rear = -1;
        this.front = -1;
    }

    public int size() {
        return size;
    }

    // Default constructor with small default size
    public MyQueue() {
        this(10);
    }

    public boolean isEmpty() {
        return front == -1 && rear == -1;
    }

    public boolean isFull() {
        return (rear + 1) % size == front;
    }

    // Enqueue (add element)
    public void enqueue(T data) {
        if (isFull()) {
            System.out.println("Queue is full, cannot add: " + data);
            return;
        }
        // first element added
        if (front == -1) {
            front = 0;
        }
        rear = (rear + 1) % size;
        cirArr[rear] = data;
    }

    // Dequeue (remove element)
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty, cannot remove");
            return null;
        }

        T result = (T) cirArr[front];

        // only one element left
        if (rear == front) {
            rear = front = -1;
        } else {
            front = (front + 1) % size;
        }

        return result;
    }

    // Peek front element
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty, nothing to peek");
            return null;
        }
        return (T) cirArr[front];
    }

    // Get current number of elements (approximation)
    public int currentSize() {
        if (isEmpty())
            return 0;
        if (rear >= front)
            return rear - front + 1;
        else
            return size - (front - rear - 1);
    }
}
