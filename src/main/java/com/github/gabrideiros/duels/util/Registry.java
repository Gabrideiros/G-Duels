package com.github.gabrideiros.duels.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Registry<V> {

    private List<V> elements;

    public Registry() {
        this.elements = new LinkedList<>();
    }

    public <E> List<E> cachedMap(Function<V, E> function) {
        final List<E> copy = new LinkedList<>();

        for (V element : this.elements) {
            copy.add(function.apply(element));
        }
        return copy;
    }

    public boolean contains(V element) {
        return this.elements.contains(element);
    }

    public void add(V element) {
        this.elements.add(element);
    }

    @SafeVarargs
    public final void add(V... elementArg) {
        this.elements.addAll(Arrays.asList(elementArg));
    }

    public void removeCachedElement(V element) {
        this.elements.remove(element);
    }

    @SafeVarargs
    public final boolean remove(V... elementArg) {
        return this.elements.removeAll(Arrays.asList(elementArg));
    }

    public V get(int index) {
        return this.elements.get(index);
    }

    public V get(Predicate<V> predicate) {
        if (this.elements == null) this.elements = new ArrayList<>();

        for (V element : this.elements) {
            if (predicate.test(element))
                return element;
        }
        return null;
    }

    public Optional<V> findCached(Predicate<V> predicate) {
        return Optional.ofNullable(get(predicate));
    }

    public Optional<V> findAndRemove(Predicate<V> predicate) {
        final Optional<V> optional = findCached(predicate);
        optional.ifPresent(this::removeCachedElement);
        return optional;
    }

    public List<V> getAll() {
        return this.elements;
    }

    public int size() {
        return this.elements.size();
    }

    public void removeIf(Predicate<V> predicate) {
        for (V element : elements) {
            if (predicate.test(element)) removeCachedElement(element);
        }
    }
}