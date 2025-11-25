package ca.umanitoba.cs.longkuma.logic.stack;

public class LinkedListStack<T> implements Stack<T> {

    private class Node {
        T data;
        Node next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    /**
     * Constructs an empty LinkedListStack
     */
    public LinkedListStack() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Pushes an item onto the top of the stack
     *
     * @param item The item to be pushed onto the stack
     */
    @Override
    public void push(T item) {
        Node newHead = new Node(item);
        newHead.next = head;
        head = newHead;
        size++;
    }

    /**
     * Removes and returns the item at the top of the stack
     *
     * @return The item at the top of the stack
     * @throws EmptyStackException if the stack is empty
     */
    @Override
    public T pop() throws EmptyStackException {
        if(size == 0 && head == null) {
            throw new EmptyStackException("You cannot pop from an empty stack!");
        }
        Node returnNode = head;
        head = head.next;
        size--;
        return returnNode.data;
    }

    /**
     * Returns the number of items in the stack
     *
     * @return The number of items in the stack
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if the stack is empty
     *
     * @return true if the stack is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the item at the top of the stack without removing it
     *
     * @return The item at the top of the stack
     * @throws EmptyStackException if the stack is empty
     */
    @Override
    public T peek() throws EmptyStackException {
        if(size == 0 && head == null) {
            throw new EmptyStackException("You cannot peek from an empty stack!");
        }
        Node returnNode = head;
        return returnNode.data;
    }
}