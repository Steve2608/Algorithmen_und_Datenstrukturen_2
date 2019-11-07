package UE3;

import java.util.NoSuchElementException;

public interface MyPriorityQueue<T extends Comparable<T>> {
    boolean isEmpty();          //Returns true if the PQ is empty; false otherwise

    int size();                 //Returns the current size of the PQ

    void insert(T elem)     //Inserts a new element into the PQ
            throws IllegalArgumentException;

    T removeMin()           //Removes the min element from the PQ and returns it
            throws NoSuchElementException;

    T min()                 //Returns the min element from the PQ
            throws NoSuchElementException;

    Object[] toArray();         //Returns an array representation of the PQ
}