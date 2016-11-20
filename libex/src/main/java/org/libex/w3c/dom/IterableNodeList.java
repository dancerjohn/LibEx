package org.libex.w3c.dom;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides an {@link Iterable} view of a {@link NodeList}
 */
@Immutable
@ThreadSafe
public class IterableNodeList implements List<Node>, NodeList {

    private final NodeList nodeList;

    public IterableNodeList(
            final NodeList nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public Iterator<Node> iterator()
    {
        return new NodeListIterator(nodeList);
    }

    /**
     * @see NodeList#getLength()
     */
    @Override
    public int getLength()
    {
        return nodeList.getLength();
    }

    /**
     * @see NodeList#item(int)
     */
    @Override
    @Nullable
    public Node item(
            final int index)
    {
        return nodeList.item(index);
    }

    /**
     * Provides an {@link Iterator} over a {@code NodeList}
     */
    @NotThreadSafe
    private static class NodeListIterator implements Iterator<Node>, ListIterator<Node> {

        private final NodeList nodeList;
        private final int size;
        private int index;

        public NodeListIterator(
                final NodeList nodeList) {
            this(nodeList, 0);
        }

        public NodeListIterator(
                final NodeList nodeList,
                final int startIndex) {
            this.index = startIndex;
            this.nodeList = nodeList;
            this.size = nodeList.getLength();
        }

        @Override
        public boolean hasNext()
        {
            return index < size;
        }

        @Override
        @Nullable
        public Node next()
        {
            return nodeList.item(index++);
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasPrevious()
        {
            return index > 0;
        }

        @Override
        public Node previous()
        {
            return nodeList.item(--index);
        }

        @Override
        public int nextIndex()
        {
            return index + 1;
        }

        @Override
        public int previousIndex()
        {
            return index - 1;
        }

        @Override
        public void set(
                final Node e)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(
                final Node e)
        {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public int size()
    {
        return getLength();
    }

    @Override
    public boolean isEmpty()
    {
        return getLength() == 0;
    }

    @Override
    public boolean contains(
            final Object o)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(
            final T[] a)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(
            final Node e)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(
            final Object o)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(
            final Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(
            final Collection<? extends Node> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(
            final int index,
            final Collection<? extends Node> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(
            final Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(
            final Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node get(
            final int index)
    {
        return item(index);
    }

    @Override
    public Node set(
            final int index,
            final Node element)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(
            final int index,
            final Node element)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node remove(
            final int index)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(
            final Object o)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(
            final Object o)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<Node> listIterator()
    {
        return new NodeListIterator(nodeList);
    }

    @Override
    public ListIterator<Node> listIterator(
            final int index)
    {
        return new NodeListIterator(nodeList, index);
    }

    @Override
    public List<Node> subList(
            final int fromIndex,
            final int toIndex)
    {
        throw new UnsupportedOperationException();
    }
}