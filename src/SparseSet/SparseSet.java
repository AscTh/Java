package SparseSet;

import java.util.*;

interface Hintable {
    void setHint(int hint);
    int hint();
}

public class SparseSet<T extends Hintable> extends AbstractSet<T> {
    private ArrayList<T> dense = new ArrayList<>();
    private int count = 0;

    @Override
    public boolean add(T x) {
        if (contains(x))
            return false;
        x.setHint(count++);
        dense.add(x);
        return true;
    }

    @Override
    public boolean remove(Object arg) {
        T x = (T) arg;
        if (contains(x)) {
            dense.set(x.hint(), dense.get(--count));
            dense.get(x.hint()).setHint(x.hint());
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Object arg) {
        T x = (T) arg;
        return x.hint() < count && dense.get(x.hint()) == x;
    }

    @Override
    public void clear() {
        count = 0;
    }

    @Override
    public int size() {
        return count;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int i = -1;

            @Override
            public boolean hasNext() {
                return i < count - 1;
            }

            @Override
            public T next() {
                return dense.get(++i);
            }

            public void remove() {
                SparseSet.this.remove(dense.get(i));
            }
        };
    }
}