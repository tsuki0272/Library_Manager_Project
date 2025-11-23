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

    public LinkedListStack() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public void push(T item) {
        Node newHead = new Node(item);
        newHead.next = head;
        head = newHead;
        size++;
    }

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

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public T peek() throws EmptyStackException {
        if(size == 0 && head == null) {
            throw new EmptyStackException("You cannot peek from an empty stack!");
        }
        Node returnNode = head;
        return returnNode.data;
    }
}
