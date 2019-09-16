package com.defrag.struct;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represent Disjoint-Set data structure.
 *
 * "Union by rank" and "compress-path" heuristics are using as well.
 *
 * @param <T> type of element
 */
public class DisjointSet<T> {

    final Map<T, T> presenters = new HashMap<>();
    final Map<T, Integer> ranks = new HashMap<>();

    public void make(T element) {
        presenters.put(element, element);
        ranks.put(element, 0);
    }

    public void union(T first, T second) {
        checkIfContains(first);
        checkIfContains(second);

        T presenterForTheFirst = find(first);
        T presenterForTheSecond = find(second);

        if (presenterForTheFirst == presenterForTheSecond) {
            return;
        }

        int rankForTheFirst = ranks.get(presenterForTheFirst);
        int rankForTheSecond = ranks.get(presenterForTheSecond);

        if (rankForTheFirst > rankForTheSecond) {
            presenters.put(presenterForTheSecond, presenterForTheFirst);
        } else {
            presenters.put(presenterForTheFirst, presenterForTheSecond);
            if (rankForTheFirst == rankForTheSecond) {
                ranks.put(presenterForTheSecond, rankForTheSecond + 1);
            }
        }
    }

    public T find(T element) {
        checkIfContains(element);

        T current = element;
        while (true) {
            T presenter = presenters.get(current);
            if (presenter == current) {
                break;
            }
            current = presenter;
        }

        T presenter = current;
        compressPaths(presenter, element);

        return current;
    }

    public List<T> elementsInTheSameSet(T element) {
        checkIfContains(element);

        T elementPresenter = find(element);

        return presenters
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(elementPresenter))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void checkIfContains(T element) {
        if (!presenters.containsKey(element)) {
            throw new IllegalArgumentException("Current element doesn't contain in the set");
        }
    }

    private void compressPaths(T commonPresenter, T element) {
        T current = element;
        while (!current.equals(commonPresenter)) {
            T presenter = presenters.get(current);
            presenters.put(current, commonPresenter);
            current = presenter;
        }
    }
}
