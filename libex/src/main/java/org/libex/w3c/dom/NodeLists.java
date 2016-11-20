package org.libex.w3c.dom;

import static com.google.common.collect.Iterables.getFirst;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class NodeLists {

    private static final Predicate<NodeList> IS_EMPTY = new Predicate<NodeList>() {

        @Override
        public boolean apply(
                final NodeList input)
        {
            return isEmpty(input);
        }
    };

    public static Predicate<NodeList> isEmpty()
    {
        return IS_EMPTY;
    }

    public static boolean isEmpty(
            @Nullable final NodeList nodeList)
    {
        return nodeList != null && nodeList.getLength() > 0;
    }

    private static final Function<NodeList, IterableNodeList> TO_LIST = new Function<NodeList, IterableNodeList>() {

        @Override
        public IterableNodeList apply(
                final NodeList input)
        {
            if (input == null) {
                return null;
            } else {
                return new IterableNodeList(input);
            }
        }
    };

    public static Function<NodeList, IterableNodeList> toIterableNodeList()
    {
        return TO_LIST;
    }

    public static <T extends Node> Function<NodeList, TypeCheckedNodeList<T>> toTypeCheckedList(
            final Class<T> type)
    {
        return TypeCheckedNodeList.toCheckedList(type);
    }

    public static <T extends Node> Function<NodeList, TypeCheckedNodeList<T>> toTypeFilteredList(
            final Class<T> type)
    {
        return TypeCheckedNodeList.toFilteredList(type);
    }

    /**
     * Gets the first Node in the passed {@link NodeList} that is of the passed type
     * 
     * @param nodeList
     *            list from which to retrieve the first node
     * @param type
     *            the type of the node to return
     * @return an Optional containing the first Node of the specified type, if any
     * @param <T>
     *            type of node to return
     */
    public static <T extends Node> Optional<T> getFirstOf(
            final NodeList nodeList,
            final Class<T> type)
    {
        return Optional.fromNullable(
                getFirst(toTypeFilteredList(type).apply(nodeList),
                null));
    }

    /**
     * Returns the one and only element in the list (if more than one exists). If more than one Node
     * exists in the list, the passed exception is thrown
     * 
     * @param nodeList
     *            the list from which to retrieve the only element
     * @param type
     *            the type of node to retrieve
     * @param exception
     *            the exception to throw if list contains more than one Node
     * @return null if NodeList is empty, the single Node in the NodeList if it contains exactly one Node.
     * 
     * @throws ClassCastException
     *             if the single Node in the NodeList is not an instance of the passed type
     * @throws Exception
     *             throws the passed Exception if the NodeList contains more than one Node
     * @param <T>
     *            type of node to return
     */
    public static <T extends Node> Optional<T> getOnly(
            final NodeList nodeList,
            final Class<T> type,
            final Exception exception) throws Exception
    {
        if (nodeList.getLength() == 0) {
            return Optional.absent();
        } else if (nodeList.getLength() > 1) {
            throw exception;
        } else {
            @SuppressWarnings("unchecked")
            T result = (T) nodeList.item(0);
            if (!type.isInstance(result)) {
                throw new ClassCastException(String.format("Instance of type %s cannot be cast to %s", result.getClass(), type));
            }
            return Optional.of(result);
        }
    }

    /**
     * Returns the one and only element in the list (if more than one exists). If more than one Node
     * exists in the list, the passed exception is thrown
     * 
     * @param nodeList
     *            the list from which to retrieve the only element
     * @param type
     *            the type of node to retrieve
     * @param exception
     *            the exception to throw if list contains more than one Node
     * @return null if NodeList is empty, the single Node in the NodeList if it contains exactly one Node.
     * 
     * @throws ClassCastException
     *             if the single Node in the NodeList is not an instance of the passed type
     * @throws RuntimeException
     *             throws the passed RuntimeException if the NodeList contains more than one Node
     * @param <T>
     *            type of node to return
     */
    public static <T extends Node> Optional<T> getOnly(
            final NodeList nodeList,
            final Class<T> type,
            final RuntimeException exception) throws RuntimeException
    {
        try {
            return getOnly(nodeList, type, (Exception) exception);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private NodeLists() {
    }

}
