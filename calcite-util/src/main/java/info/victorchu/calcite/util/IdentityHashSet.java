package info.victorchu.calcite.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

public class IdentityHashSet<E> extends AbstractSet<E> {
    private final Map<E, Boolean> map;
    
    public IdentityHashSet() {
        map = new IdentityHashMap<>();
    }
    
    public IdentityHashSet(Collection<? extends E> c) {
        map = new IdentityHashMap<>(Math.max(c.size() * 2, 16));
        addAll(c);
    }
    
    @Override
    public int size() {
        return map.size();
    }
    
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }
    
    @Override
    public boolean add(E e) {
        return map.put(e, Boolean.TRUE) == null;
    }
    
    @Override
    public boolean remove(Object o) {
        return map.remove(o) != null;
    }
    
    @Override
    public void clear() {
        map.clear();
    }
    
    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }
}

