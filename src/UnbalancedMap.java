/*
@author Jiaxi Chen
 */


import java.util.LinkedList;

public class UnbalancedMap<K extends Comparable<K>, V> implements IMap<K, V> {

    public UnbalancedMap(IMap<K, V> original) {
        clear();
        for (K k : original.keyset()) {
            add(k, original.getValue(k));
        }
    }

    public UnbalancedMap() {
        clear();
    }

    private static class Node<K extends Comparable<K>, V> {
        protected K key;
        protected V value;
        protected Node<K, V> left;
        protected Node<K, V> right;

        public Node(K k, V v) {
            key = k;
            value = v;
            left = null;
            right = null;
        }
    }

    private Node<K, V> root;
    private int size = 0;

    @Override
    public boolean contains(K key) {
        Node<K, V> current = root;
        while (current != null) {
            if (key.compareTo(current.key) < 0)
                current = current.left;
            else if (key.compareTo(current.key) > 0)
                current = current.right;
            else
                return true;
        }
        return false;
    }

    @Override
    public boolean add(K key, V value) {
        if (root == null) {
            root = new Node(key, value);
        } else {
            Node<K, V> parent = null;
            Node<K, V> current = root;
            while (current != null) {
                if (key.compareTo(current.key) < 0) {
                    parent = current;
                    current = current.left;
                } else if (key.compareTo(current.key) > 0) {
                    parent = current;
                    current = current.right;
                } else {
                    return false;
                }
            }
            if (key.compareTo(parent.key) < 0)
                parent.left = new Node(key, value);
            else
                parent.right = new Node(key, value);
        }
        size++;
        return true;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = root;
        while (current != null) {
            if (key.compareTo(current.key) < 0) {
                current = current.left;
            } else if (key.compareTo(current.key) > 0) {
                current = current.right;
            } else {
                return current.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (root == null);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public V delete(K key) {
        if (root == null)
            return null;
        Node<K, V> parent = null;
        Node<K, V> current = root;
        while (current != null) {
            if (key.compareTo(current.key) < 0) {
                parent = current;
                current = current.left;
            } else if (key.compareTo(current.key) > 0) {
                parent = current;
                current = current.right;
            } else
                break;
        }

        if (current == null)
            return null;

        //case1
        if (current.left == null) {
            if (parent == null)
                root = current.right;
            else {
                if (key.compareTo(parent.key) < 0)
                    parent.left = current.right;
                else
                    parent.right = current.right;
            }
        }

        //case2
        else {
            Node<K, V> parentOfMostRight = current;
            Node<K, V> mostRight = current.left;

            while (mostRight.right != null) {
                parentOfMostRight = mostRight;
                mostRight = mostRight.right;
            }
            current.key = mostRight.key;
            if (parentOfMostRight.right == mostRight)
                parentOfMostRight.right = mostRight.left;
            else
                parentOfMostRight.left = mostRight.left;
        }
        size--;
        return current.value;
    }

    private LinkedList<K> keys = new LinkedList<>();
    private LinkedList<V> values = new LinkedList<>();

    @Override
    public K getKey(V value) {
        keyset();
        values();
        if (values.contains(value))
            return keys.get(values.indexOf(value));
        else
            return null;
    }

    @Override
    public Iterable<K> getKeys(V value) {
        keyset();
        values();
        LinkedList<K> returns = new LinkedList<>();
        int i = 0;
        for (V v : values) {
            if (v.equals(value))
                returns.add(keys.get(i));
            i++;
        }
        return returns;
    }

    @Override
    public Iterable<K> keyset() {
        if (isEmpty()) {
            return keys;
        } else {
            keys = new LinkedList<>();
            setKey(root);
            return keys;
        }
    }

    private void setKey(Node<K, V> node) {
        if (node != null) {
            setKey(node.left);
            keys.add(node.key);
            setKey(node.right);
        }
    }

    @Override
    public Iterable<V> values() {
        if (isEmpty()) {
            return values;
        } else {
            values = new LinkedList<>();
            setValue(root);
            return values;
        }
    }

    private void setValue(Node<K, V> node) {
        if (node != null) {
            setValue(node.left);
            values.add(node.value);
            setValue(node.right);
        }
    }
}