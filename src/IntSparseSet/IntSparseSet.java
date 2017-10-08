package IntSparseSet;

import java.util.*;

public class IntSparseSet extends AbstractSet<Integer> {
    public IntSparseSet(int low, int high) {
        this.low = low;
        this.high = high;
        this.count = 0;
        this.dense = new Integer[high - low + 1];
        this.sparse = new int[high - low + 1];
        for (int pos = 0; pos < sparse.length; ++pos)
            sparse[pos] = high - low;
    }

    @Override
    public boolean isEmpty() {
        return 0 == count;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void clear() {
        count = 0;
    }

    private boolean contains(Integer i) {
        return !(i < low || i >= high) && sparse[i - low] <= count && Objects.equals(dense[sparse[i - low]], i);
    }

    @Override
    public boolean add(Integer i) {
        if (i < low || i >= high || this.contains(i))
            return false;
        dense[count] = i;
        sparse[i - low] = count;
        ++count;
        return true;
    }

    private boolean remove(Integer i) {
        if (!this.contains(i))
            return false;
        sparse[dense[count - 1] - low] = sparse[i - low];
        this.swapDense(sparse[i - low], count - 1);
        sparse[i - low] = count - 1;
        --count;
        return true;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new IntSpareSetIterator();
    }

    private void swapDense(int i, int j) {
        Integer tmp = dense[i];
        dense[i] = dense[j];
        dense[j] = tmp;
    }

    private class IntSpareSetIterator implements Iterator<Integer> {
        IntSpareSetIterator() {
            this.pos = 0;
        }

        public boolean hasNext() {
            return pos < count;
        }

        public Integer next() {
            return dense[pos++];
        }

        public void remove() {
            if (0 == pos)
                throw new IllegalStateException();
            IntSparseSet.this.remove(dense[pos - 1]);
        }

        private int pos;
    }

    private int low, high, count;
    private int[] sparse;
    private Integer[] dense;
}