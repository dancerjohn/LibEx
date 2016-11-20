package org.libex.w3c.dom;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.getFirst;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Function;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

@ParametersAreNonnullByDefault
@NotThreadSafe
public class TypeCheckedNodeList<T extends Node>
        extends ForwardingList<T>
        implements List<T>, NodeList {

    public static <T extends Node> Function<NodeList, TypeCheckedNodeList<T>> toFilteredList(
            final Class<T> type)
    {
        return new Function<NodeList, TypeCheckedNodeList<T>>(){

            @Override
            public TypeCheckedNodeList<T> apply(
                    final NodeList nodeList)
            {
                return createFilteredList(nodeList, type);
            }
        };
    }

    public static <T extends Node> TypeCheckedNodeList<T> createFilteredList(
            final NodeList nodeList,
            final Class<T> type)
    {
        return new TypeCheckedNodeList<T>(nodeList, type, CheckType.SILENTLY_FILTER);
    }

    public static <T extends Node> Function<NodeList, TypeCheckedNodeList<T>> toCheckedList(
            final Class<T> type)
    {
        return new Function<NodeList, TypeCheckedNodeList<T>>() {

            @Override
            public TypeCheckedNodeList<T> apply(
                    final NodeList nodeList)
            {
                return createCheckedList(nodeList, type);
            }
        };
    }

    public static <T extends Node> TypeCheckedNodeList<T> createCheckedList(
            final NodeList nodeList,
            final Class<T> type)
    {
        return new TypeCheckedNodeList<T>(nodeList, type, CheckType.FAIL_ON_MISMATCH);
    }

    public static enum CheckType {
        SILENTLY_FILTER {
            @Override
            protected <T> ImmutableList<T> filter(
                    final IterableNodeList nodeList,
                    final Class<T> type)
            {
                return ImmutableList.copyOf(Iterables.filter(nodeList, type));
            }
        },
        FAIL_ON_MISMATCH {
            @Override
            protected <T> ImmutableList<T> filter(
                    final IterableNodeList nodeList,
                    final Class<T> type)
            {
                Node node = getFirst(Iterables.filter(nodeList, not(instanceOf(type))), null);
                if (node != null) {
                    throw new IllegalArgumentException(String.format("Found instance of %s when expecting only %s", node
                            .getClass().getSimpleName(), type.getSimpleName()));
                }

                return CheckType.SILENTLY_FILTER.filter(nodeList, type);
            }
        };

        protected abstract <T> ImmutableList<T> filter(
                final IterableNodeList nodeList,
                final Class<T> type);
    }

    private final ImmutableList<T> filteredList;

    private TypeCheckedNodeList(
            final NodeList nodeList,
            final Class<T> type,
            final CheckType typeCheck) {
        this.filteredList = typeCheck.filter(new IterableNodeList(nodeList), type);
    }

    @Override
    public int getLength()
    {
        return filteredList.size();
    }

    @Override
    public Node item(
            final int arg0)
    {
        return filteredList.get(arg0);
    }

    @Override
    protected List<T> delegate()
    {
        return filteredList;
    }
}
