package com.anuj.algotracker.datastructure;
/**
 * the idea for using this linked list:
 * "Store a list of recently solved problems by the user in a linked list
 * to efficiently add/remove problems as they are solved."
 */
public class MyLinkedList<T> {

    // Node class to represent each element
    class Node {
        T data;
        Node next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }

    // add at first 
    public void addFirst(T data) {
        Node newNode = new Node(data);
        size++;
        if (head == null) {
            head = tail = newNode;
            return;
        }
        newNode.next = head; // link new node to previous head
        head = newNode;
    }

    // add at last
    public void addLast(T data) {
        Node newNode = new Node(data);
        size++;
        if (head == null) {
            head = tail = newNode;
            return;
        }
        tail.next = newNode;
        tail = newNode;
    }

    // add at middle/index (0-based) 
    public void addMiddle(int index, T data) {
        if (index == 0) {
            addFirst(data);
            return;
        }
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", size: " + size);
        }

        Node newNode = new Node(data);
        size++;

        Node temp = head;
        int i = 0;
        while (i < index - 1) {
            temp = temp.next;
            i++;
        }

        newNode.next = temp.next;
        temp.next = newNode;
    }

    // remove first node
    public T removeFirst() {
        if (size == 0) {
            System.out.println("ll is empty");
            return null;
        } else if (size == 1) {
            T value = head.data;
            head = tail = null;
            size = 0;
            return value;
        }
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }

    // remove last node
    public T removeLast() {
        if (size == 0) {
            System.out.println("ll is empty");
            return null;
        } else if (size == 1) {
            T value = head.data;
            head = tail = null;
            size = 0;
            return value;
        }

        Node temp = head;
        int prev = 0;
        while (prev < size - 2) {
            temp = temp.next;
            prev++;
        }

        T data = tail.data;
        temp.next = null;
        tail = temp;
        size--;
        return data;
    }

    // get element at index (0-based) – we need this for reading
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", size: " + size);
        }

        Node temp = head;
        int i = 0;
        while (i < index) {
            temp = temp.next;
            i++;
        }
        return temp.data;
    }
}
