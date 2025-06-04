package com.backend.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class FibonacciHeap<T> {
    private Node<T> min;
    private int size;
    private Map<T, Node<T>> nodeMap;

    public FibonacciHeap() {
        this.min = null;
        this.size = 0;
        this.nodeMap = new HashMap<>();
    }

    public void insert(T element, double priority) {
        Node<T> node = new Node<>(element, priority);
        nodeMap.put(element, node);

        if (min == null) {
            min = node;
        } else {
            addToRootList(node);
            if (node.priority < min.priority) {
                min = node;
            }
        }
        size++;
    }

    public T extractMin() {
        if (min == null) {
            return null;
        }

        Node<T> z = min;

        // Add all of min's children to the root list
        if (z.child != null) {
            Node<T> child = z.child;
            Node<T> current = child;
            do {
                Node<T> next = current.right;
                addToRootList(current);
                current.parent = null;
                current = next;
            } while (current != child);
        }

        // Remove min from the root list
        removeFromRootList(z);

        if (z == z.right) {
            min = null;
        } else {
            min = z.right;
            consolidate();
        }

        size--;
        nodeMap.remove(z.element);
        return z.element;
    }
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public List<T> extractTopN(int n) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < n && !isEmpty(); i++) {
            result.add(extractMin());
        }
        return result;
    }

    private void addToRootList(Node<T> node) {
        if (min == null) {
            min = node;
            node.left = node;
            node.right = node;
        } else {
            node.right = min.right;
            node.left = min;
            min.right.left = node;
            min.right = node;
        }
    }

    private void removeFromRootList(Node<T> node) {
        if (node == node.right) {
            min = null;
        } else {
            node.left.right = node.right;
            node.right.left = node.left;
            if (node == min) {
                min = node.right;
            }
        }
    }

    private void consolidate() {
        // Maximum degree is O(log n)
        int maxDegree = (int) Math.floor(Math.log(size) / Math.log(1.618)) + 1;
        @SuppressWarnings("unchecked")
        Node<T>[] degreeArray = (Node<T>[]) new Node[maxDegree];

        // Process all nodes in the root list
        List<Node<T>> rootList = new ArrayList<>();
        if (min != null) {
            Node<T> current = min;
            do {
                rootList.add(current);
                current = current.right;
            } while (current != min);
        }

        for (Node<T> node : rootList) {
            Node<T> x = node;
            int degree = x.degree;

            while (degreeArray[degree] != null) {
                Node<T> y = degreeArray[degree];
                if (x.priority > y.priority) {
                    Node<T> temp = x;
                    x = y;
                    y = temp;
                }

                link(y, x);
                degreeArray[degree] = null;
                degree++;
            }

            degreeArray[degree] = x;
        }

        // Reconstruct the root list and find the new min
        min = null;
        for (Node<T> node : degreeArray) {
            if (node != null) {
                if (min == null) {
                    min = node;
                    node.left = node;
                    node.right = node;
                } else {
                    addToRootList(node);
                    if (node.priority < min.priority) {
                        min = node;
                    }
                }
            }
        }
    }

    private void link(Node<T> y, Node<T> x) {
        // Remove y from root list
        removeFromRootList(y);

        // Make y a child of x
        if (x.child == null) {
            x.child = y;
            y.left = y;
            y.right = y;
        } else {
            y.right = x.child.right;
            y.left = x.child;
            x.child.right.left = y;
            x.child.right = y;
        }

        y.parent = x;
        x.degree++;
    }


    private class Node<E> {
        private E element;           // The element stored in the node
        private double priority;     // Priority value (lower = higher priority)
        private int degree;          // Number of children
        // This field is used in the link and extractMin methods
        @SuppressWarnings("unused") // False positive - field is used in link() and extractMin()
        private Node<E> parent;      // Parent node
        private Node<E> child;       // Child node
        private Node<E> left;        // Left sibling
        private Node<E> right;       // Right sibling

        Node(E element, double priority) {
            this.element = element;
            this.priority = priority;
            this.degree = 0;
            this.parent = null;
            this.child = null;
            this.left = this;
            this.right = this;
        }
    }
}
