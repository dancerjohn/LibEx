package org.libex.cache;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.libex.hamcrest.IsMapContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.libex.collect.MapsEx;
import org.libex.test.TestBaseLocal;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.UncheckedExecutionException;

public class CacheLoaderLoadAllTest extends TestBaseLocal {
    
    private static final String KEY1 = "key1",
            KEY2 = "key2",
            KEY3 = "key3",
            KEY4 = "key4";

    private static final String result1 = "result1",
            result2 = "result2",
            result3 = "result3",
            result4 = "result4";

    private static final ImmutableMap<String, String> resultMap = ImmutableMap.of(KEY1, result1, KEY2, result2,
            KEY3, result3, KEY4, result4);
    private static final Function<String, String> toResultValue = Functions.forMap(resultMap);

    @Mock
    public CacheLoader<String, String> cacheLoader;

    Cache<String, String> cache;
    LoadingCache<String, String> batchCache;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        when(cacheLoader.loadAll(Mockito.any(Iterable.class)))
                .thenAnswer(new Answer<Map<String, String>>() {

                    @Override
                    public Map<String, String> answer(
                            final InvocationOnMock invocation) throws Throwable
                    {
                        Iterable<String> keys = (Iterable<String>) invocation.getArguments()[0];
                        return MapsEx.uniqueIndex(keys, Functions.<String> identity(), toResultValue);
                    }
                });
        
        cache = CacheBuilder.newBuilder().<String, String> build();
        batchCache = CacheBuilder.newBuilder().<String, String> build(cacheLoader);
    }

    @Test
    public void testGet_oneKeyNotCached() throws Exception
    {
        // test
        testGet(new GetArgs()
                .inputKeys(KEY1)
                .mapMatcher(containsInAnyOrder(ImmutableMap.of(KEY1, result1)))
                .argsToLoader(KEY1));
    }

    @Test
    public void testGet_oneKeyCachedViaGet() throws Exception
    {
        // setup
        batchCache.getAll(newArrayList(KEY1));

        // test
        testGet(new GetArgs()
                .inputKeys(KEY1)
                .mapMatcher(containsInAnyOrder(ImmutableMap.of(KEY1, result1)))
                .argsToLoader(KEY1));
    }

    @Test
    public void testGet_oneKeyCachedViaCache() throws Exception
    {
        // setup
        batchCache.put(KEY1, result1);

        // test
        testGet(new GetArgs()
                .inputKeys(KEY1)
                .mapMatcher(containsInAnyOrder(ImmutableMap.of(KEY1, result1)))
                .argsToLoader());
    }

    @Test
    public void testGet_twoKeysNotCached() throws Exception
    {
        // test
        testGet(new GetArgs()
                .inputKeys(KEY1, KEY2)
                .mapMatcher(containsInAnyOrder(ImmutableMap.of(KEY1, result1, KEY2, result2)))
                .argsToLoader(KEY1, KEY2));
    }

    @Test
    public void testGet_twoKeysCachedViaGet() throws Exception
    {
        // setup
        batchCache.getAll(newArrayList(KEY1, KEY2));

        // test
        testGet(new GetArgs()
                .inputKeys(KEY1, KEY2)
                .mapMatcher(containsInAnyOrder(ImmutableMap.of(KEY1, result1, KEY2, result2)))
                .argsToLoader(KEY1, KEY2));
    }

    @Test
    public void testGet_threeKeysSomeCached() throws Exception
    {
        // setup
        batchCache.getAll(newArrayList(KEY2, KEY4));
        // cache.put(KEY2, result2);
        // cache.put(KEY4, result4);

        // test
        testGet(new GetArgs()
                .inputKeys(KEY1, KEY2, KEY3, KEY4)
                .mapMatcher(
                        containsInAnyOrder(ImmutableMap.of(KEY1, result1, KEY2, result2, KEY3, result3, KEY4, result4)))
                .argsToLoader(KEY2, KEY4)
                .argsToLoader(KEY1, KEY3));
    }

    @Test
    public void testGet_threeKeysSomeCachedDirectly() throws Exception
    {
        // setup
        batchCache.put(KEY2, result2);
        batchCache.put(KEY4, result4);

        // test
        testGet(new GetArgs()
                .inputKeys(KEY1, KEY2, KEY3, KEY4)
                .mapMatcher(
                        containsInAnyOrder(ImmutableMap.of(KEY1, result1, KEY2, result2, KEY3, result3, KEY4, result4)))
                .argsToLoader(KEY1, KEY3));
    }

    @Test
    public void testGet_returnNullNotAllowed_defaultValue() throws Exception
    {
        // setup
        configureCacheLoaderToReturnNull();

        // expect
        expectedException.expect(InvalidCacheLoadException.class);
        // expectException(IllegalStateException.class, "");

        // test
        batchCache.getAll(newArrayList(KEY1, KEY2, KEY3));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGet_loaderThrowsException() throws Exception
    {
        // setup
        RuntimeException ex = new RuntimeException();

        reset(cacheLoader);
        when(cacheLoader.loadAll(Mockito.any(Iterable.class))).thenThrow(ex);

        // expect
        expectedException.expect(UncheckedExecutionException.class);
        expectedException.expectCause(sameInstance(ex));

        // test
        batchCache.getAll(newArrayList(KEY1, KEY2, KEY3));
    }

    private static class GetArgs {
        Iterable<String> inputKeys;
        Matcher<? super Map<String, String>> mapMatcher;
        List<String[]> argsToLoader = newArrayList();

        public GetArgs inputKeys(
                final String... inputKeys)
        {
            this.inputKeys = newArrayList(inputKeys);
            return this;
        }

        public GetArgs argsToLoader(
                final String... argsToLoader)
        {
            this.argsToLoader.add(argsToLoader);
            return this;
        }

        public GetArgs mapMatcher(
                final Matcher<? super Map<String, String>> mapMatcher)
        {
            this.mapMatcher = mapMatcher;
            return this;
        }
    }

    void testGet(
            final GetArgs args) throws Exception
    {
        // test
        Map<String, String> result = batchCache.getAll(args.inputKeys);

        // verify
        assertThat(result, args.mapMatcher);
        for (String[] call : args.argsToLoader) {
            if (call.length != 0) {
                verify(cacheLoader).loadAll(
                        Mockito.argThat(IsIterableContainingInAnyOrder
                                .containsInAnyOrder(call)));
            }
        }
        verifyNoMoreInteractions(cacheLoader);
    }

    @SuppressWarnings("unchecked")
    private void configureCacheLoaderToReturnNull() throws Exception
    {
        reset(cacheLoader);
        when(cacheLoader.loadAll(Mockito.any(Iterable.class)))
                .thenAnswer(new Answer<Map<String, String>>() {

                    @Override
                    public Map<String, String> answer(
                            final InvocationOnMock invocation) throws Throwable
                    {
                        Iterable<String> keys = (Iterable<String>) invocation.getArguments()[0];
                        Map<String, String> map = newHashMap(MapsEx.uniqueIndex(keys, Functions.<String> identity(),
                                toResultValue));

                        int i = 0;
                        for (String key : map.keySet()) {
                            if (i++ % 2 == 0) {
                                map.put(key, null);
                            }
                        }
                        return map;
                    }
                });
    }
}
