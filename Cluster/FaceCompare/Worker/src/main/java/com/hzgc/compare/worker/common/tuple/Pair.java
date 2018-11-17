package com.hzgc.compare.worker.common.tuple;

import java.io.Serializable;

public class Pair <A, B> implements Serializable {
    private A key;
    private B value;

    public Pair(A first, B second) {
        this.key = first;
        this.value = second;
    }

    public A getKey() {
        return key;
    }

    public B getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Pair) {
            Pair triplet = (Pair) o;
            if (key != null ? !key.equals(triplet.key) : triplet.key != null) return false;
            if (value != null ? !value.equals(triplet.value) : triplet.value != null) return false;
            return true;
        }
        return false;
    }
}
