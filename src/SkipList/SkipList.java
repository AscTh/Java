package SkipList;

import java.util.*;

public class SkipList<K extends Comparable<K>, V> extends AbstractMap<K, V> {
    static class Node<K, V> {
        Node(Map.Entry<K, V> data, final int depth) {
            this.data = data;
            this.next = new ArrayList<Node<K, V>>() {{
                for (int i = 0; i < depth; ++i)
                    add(null);
            }};
        }

        List<Node<K, V>> next;
        Map.Entry<K, V> data;
    }

    public SkipList(int levels) {
        this.depth = levels;
        this.first = new Node<K, V>(null, levels);
    }

    @Override
    public boolean isEmpty() {
        return null == this.first.next.get(0);
    }

    @Override
    public boolean containsKey(Object o) {
        K key = (K) o;
        return this.containsKey(key);
    }

    private boolean containsKey(K key) {
        List<Node<K, V>> skips = skip(key);
        Node<K, V> target = skips.get(0).next.get(0);
        return null != target && 0 == key.compareTo(target.data.getKey());
    }

    @Override
    public V get(Object o) {
        K key = (K) o;
        return this.get(key);
    }

    private V get(K key) {
        List<Node<K, V>> skips = skip(key);
        Node<K, V> target = skips.get(0).next.get(0);
        if (null != target && 0 == key.compareTo(target.data.getKey()))
            return target.data.getValue();
        return null;
    }

    @Override
    public int size() {
        int s = 0;
        Node<K, V> curr = this.first;
        while (null != curr.next.get(0)) {
            ++s;
            curr = curr.next.get(0);
        }
        return s;
    }

    @Override
    public Set<K> keySet() {
        Set<K> kS = new LinkedHashSet<>();
        Node<K, V> curr = this.first;
        while (null != curr.next.get(0)) {
            kS.add(curr.next.get(0).data.getKey());
            curr = curr.next.get(0);
        }
        return kS;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new SkipList.EntrySet();
    }

    @Override
    public V put(K key, V value) {
        List<Node<K, V>> skips = skip(key);
        Node<K, V> target = skips.get(0).next.get(0);
        if (null != target && 0 == key.compareTo(target.data.getKey()))
            return this.update(skips, value);
        this.add(skips, key, value);
        return null;
    }

    @Override
    public V remove(Object o) {
        K key = (K) o;
        return this.remove(key);
    }

    private V remove(K key) {
        List<Node<K, V>> skips = skip(key);
        Node<K, V> target = skips.get(0).next.get(0);
        if (null != target && 0 == key.compareTo(target.data.getKey()))
            return this.remove(skips);
        return null;
    }

    @Override
    public void clear() {
        for (int i = 0; i < depth; ++i)
            this.first.next.set(i, null);
    }

    private List<Node<K, V>> skip(K key) {
        List<Node<K, V>> skipArray = new ArrayList<Node<K, V>>() {{
            for (int i = 0; i < depth; ++i)
                add(null);
        }};
        int i = depth - 1;
        Node<K, V> current = this.first;
        while (i >= 0) {
            if (current.next.get(i) != null && 1 == key.compareTo(current.next.get(i).data.getKey()))
                current = current.next.get(i);
            else {
                skipArray.set(i, current);
                --i;
            }
        }
        return skipArray;
    }

    private V update(List<Node<K, V>> skipArray, V value) {
        if (null != value)
            return skipArray.get(0).next.get(0).data.setValue(value);
        return this.remove(skipArray);
    }

    private void add(List<Node<K, V>> skipArray, K key, V value) {
        Node<K, V> newNode = new Node<K, V>(new AbstractMap.SimpleEntry<>(key, value), depth);
        int l = new Random().nextInt(depth), i = 0;
        while (i < depth && l >= 0) {
            newNode.next.set(i, skipArray.get(i).next.get(i));
            skipArray.get(i).next.set(i, newNode);
            ++i;
            --l;
        }
    }

    private V remove(List<Node<K, V>> skipArray) {
        Node<K, V> target = skipArray.get(0).next.get(0);
        for (int i = depth - 1; i >= 0; --i)
            if (target == skipArray.get(i).next.get(i))
                skipArray.get(i).next.set(i, target.next.get(i));
        return target.data.getValue();
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        @Override
        public boolean isEmpty() {
            return SkipList.this.isEmpty();
        }

        @Override
        public int size() {
            return SkipList.this.size();
        }

        @Override
        public boolean contains(Object o) {
            Map.Entry<K, V> e = (Map.Entry<K, V>) o;
            V v = SkipList.this.get(e.getKey());
            return null != v && v.equals(e.getValue());
        }

        @Override
        public boolean add(Map.Entry<K, V> e) {
            V v = SkipList.this.put(e.getKey(), e.getValue());
            return v == null;
        }

        @Override
        public boolean remove(Object o) {
            Map.Entry<K, V> e = (Map.Entry<K, V>) o;
            V v = SkipList.this.remove(e.getKey());
            return v != null;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntrySetIterator();
        }

        private class EntrySetIterator implements Iterator<Map.Entry<K, V>> {
            EntrySetIterator() {
                this.curr = SkipList.this.first;
            }

            public boolean hasNext() {
                return null != curr.next.get(0);
            }

            public Map.Entry<K, V> next() {
                curr = curr.next.get(0);
                return curr.data;
            }

            public void remove() {
                if (null == curr.data)
                    throw new IllegalStateException();
                SkipList.this.remove(curr.data.getKey());
            }

            private SkipList.Node<K, V> curr;
        }
    }

    private int depth;
    private Node<K, V> first;
}