package com.defrag.struct;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DisjointSetTest {

    @Test
    public void testMake_When_ElementDoesNotExist() {
        DisjointSet<Integer> set = new DisjointSet<>();

        Integer element = 1;
        set.make(element);

        assertEquals(1, set.presenters.size());
        assertEquals(element, set.presenters.get(element));

        assertEquals(1, set.ranks.size());
        assertEquals(Integer.valueOf(0), set.ranks.get(element));
    }

    @Test
    public void testMake_When_ElementExists() {
        DisjointSet<Integer> set = new DisjointSet<>();

        Integer element = 1;
        set.make(element);
        // do the same thing
        set.make(element);

        assertEquals(1, set.presenters.size());

        Map.Entry<Integer, Integer> presenterEntry = set.presenters.entrySet().iterator().next();
        assertEquals(element, presenterEntry.getKey());
        assertEquals(element, presenterEntry.getValue());

        assertEquals(1, set.ranks.size());

        Map.Entry<Integer, Integer> rankEntry = set.ranks.entrySet().iterator().next();
        assertEquals(element, rankEntry.getKey());
        assertEquals(Integer.valueOf(0), rankEntry.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFind_When_ElementDoesNotExist() {
        DisjointSet<Integer> set = new DisjointSet<>();

        set.make(1);

        set.find(2);

        fail("The element mustn't exist");
    }

    @Test
    public void testFind_When_ElementExists() {
        DisjointSet<Integer> set = new DisjointSet<>();

        Integer first = 1;
        set.make(first);

        Integer second = 2;
        set.make(second);

        Integer third = 3;
        set.make(third);

        set.union(first, second);
        set.union(second, third);

        Integer result = set.find(second);
        assertEquals(second, result);

        // to check heuristics right away

        // path compression
        assertEquals(3, set.presenters.size());

        assertEquals(second, set.presenters.get(first));
        assertEquals(second, set.presenters.get(second));
        assertEquals(second, set.presenters.get(third));

        // union by rank
        assertEquals(3, set.ranks.size());

        assertEquals(Integer.valueOf(0), set.ranks.get(first));
        assertEquals(Integer.valueOf(1), set.ranks.get(second));
        assertEquals(Integer.valueOf(0), set.ranks.get(third));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testElementsInTheSameSet_When_ElementDoesNotExist() {
        DisjointSet<Integer> set = new DisjointSet<>();

        Integer firstElement = 1;
        set.make(firstElement);

        Integer secondElement = 2;
        set.make(secondElement);

        set.union(firstElement, secondElement);

        set.elementsInTheSameSet(3);

        fail("The elements mustn't exist");
    }

    @Test
    public void testElementsInTheSameSet_When_ElementExists() {
        DisjointSet<Integer> set = new DisjointSet<>();

        Integer firstInput = 1;
        set.make(firstInput);

        Integer secondInput = 2;
        set.make(secondInput);

        set.union(firstInput, secondInput);

        List<Integer> result = set.elementsInTheSameSet(secondInput);

        assertEquals(2, result.size());

        Integer firstOutput = result.get(0);
        assertEquals(firstInput, firstOutput);

        Integer secondOutput = result.get(1);
        assertEquals(secondInput, secondOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnion_When_FirstDoesNotExist() {
        DisjointSet<Integer> set = new DisjointSet<>();

        // forgot to make set
        Integer first = 1;

        Integer second = 2;
        set.make(second);

        set.union(first, second);

        fail("The elements mustn't be union");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnion_When_SecondDoesNotExist() {
        DisjointSet<Integer> set = new DisjointSet<>();

        Integer first = 1;
        set.make(first);

        // forgot to make set
        Integer second = 2;

        set.union(first, second);

        fail("The elements mustn't be union");
    }

    @Test
    public void testUnion_When_BothOfFirstAndSecondExist() {
        DisjointSet<Integer> set = new DisjointSet<>();

        Integer first = 1;
        set.make(first);

        Integer second = 2;
        set.make(second);

        set.union(first, second);

        // to check heuristics right away

        // path compression
        assertEquals(2, set.presenters.size());

        assertEquals(second, set.presenters.get(first));
        assertEquals(second, set.presenters.get(second));

        // union by rank
        assertEquals(2, set.ranks.size());

        assertEquals(Integer.valueOf(0), set.ranks.get(first));
        assertEquals(Integer.valueOf(1), set.ranks.get(second));
    }
}