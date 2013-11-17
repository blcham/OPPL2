package org.coode.oppl.similarity;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Pair<O> {
    protected final O o1;
    protected final O o2;

    public Pair(O anOWLObject, O anotherOWLObject) {
        if (anOWLObject == null) {
            throw new NullPointerException("The first memebr of this pair cannot be null");
        }
        if (anotherOWLObject == null) {
            throw new NullPointerException(
                    "The second memebr of this pair cannot be null");
        }
        if (anotherOWLObject.equals(anOWLObject)) {
            throw new IllegalArgumentException(
                    "The pair is meant to be made of two distic entities");
        }
        o1 = anOWLObject;
        o2 = anotherOWLObject;
    }

    public Set<O> getMembers() {
        return new HashSet<O>(Arrays.asList(o1, o2));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + o1.hashCode() + o2.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Pair<?> other = (Pair<?>) obj;
        return o1.equals(other.o1) && o2.equals(other.o2) || o1.equals(other.o2)
                && o2.equals(other.o1);
    }

    public static <T> Set<Pair<T>> getAllPossiblePairs(Collection<? extends T> c) {
        Set<Pair<T>> toReturn = new HashSet<Pair<T>>();
        for (T t : c) {
            for (T anotherT : c) {
                if (!t.equals(anotherT)) {
                    toReturn.add(new Pair<T>(t, anotherT));
                }
            }
        }
        return toReturn;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", o1, o2);
    }
}