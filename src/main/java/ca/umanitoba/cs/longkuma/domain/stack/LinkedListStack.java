package ca.umanitoba.cs.longkuma.domain.stack;

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
        checkLinkedListStack();
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
        checkLinkedListStack();
    }

    /**
     * Removes and returns the item at the top of the stack
     *
     * @return The item at the top of the stack
     * @throws EmptyStackException if the stack is empty
     */
    @Override
    public T pop() throws EmptyStackException {
        if (size == 0 && head == null) {
            throw new EmptyStackException("You cannot pop from an empty stack!");
        }
        Node returnNode = head;
        head = head.next;
        size--;
        checkLinkedListStack();
        return returnNode.data;
    }

    /**
     * Returns the number of items in the stack
     *
     * @return The number of items in the stack
     */
    @Override
    public int size() {
        checkLinkedListStack();
        return size;
    }

    /**
     * Checks if the stack is empty
     *
     * @return true if the stack is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        checkLinkedListStack();
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
        if (size == 0 && head == null) {
            throw new EmptyStackException("You cannot peek from an empty stack!");
        }
        checkLinkedListStack();
        return head.data;
    }

    /** ------------------ Class Invariants ------------------ **/
    /**
     * Verifies the stack's invariants:
     * 1. Size consistency: size equals the number of nodes reachable from head
     * 2. Head consistency: head == null iff size == 0
     * 3. Linked list integrity: no cycles, last node's next == null
     * 4. Non-null nodes
     * 5. Size >= 0
     */
    private void checkLinkedListStack() {
        assert size >= 0 : "Size cannot be negative";

        if (size == 0) {
            assert head == null : "Head must be null when stack is empty";
        } else {
            assert head != null : "Head cannot be null when stack has elements";
        }

        // Verify linked list integrity and node non-nullness
        Node current = head;
        int countedSize = 0;
        java.util.HashSet<Node> visited = new java.util.HashSet<>(); // detect cycles
        while (current != null) {
            assert !visited.contains(current) : "Cycle detected in stack";
            visited.add(current);
            assert current.data != null : "Node data cannot be null";
            current = current.next;
            countedSize++;
        }

        // Size consistency check
        assert countedSize == size : "Size mismatch: size=" + size + ", counted=" + countedSize;
    }
}
