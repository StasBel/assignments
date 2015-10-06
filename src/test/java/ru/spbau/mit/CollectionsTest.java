package ru.spbau.mit;

import org.junit.Test;
import java.util.Arrays;
import java.util.BitSet;

import static org.junit.Assert.assertEquals;

public class CollectionsTest {
    static final Predicate<Integer> LESSTHENFIVE = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer argument) {
            return argument < 5;
        }
    };

    static final Function2<Integer, Integer, Integer> DIFF = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer argument1, Integer argument2) {
            return argument1 - argument2;
        }
    };

    Iterable<Integer> a = Arrays.asList(1, 7, 2, 3, 4, 6, 5, 2, 2);

    @Test
    public void testMap() {
        Iterable<Boolean> result = Collections.map(LESSTHENFIVE, a);
        Iterable<Boolean> trueResult = Arrays.asList(true, false, true, true, true, false, false, true, true);
        assertEquals(result, trueResult);
    }

    @Test
    public void testFilter() {
        Iterable<Integer> result = Collections.filter(LESSTHENFIVE, a);
        Iterable<Integer> trueResult = Arrays.asList(1, 2, 3, 4, 2, 2);
        assertEquals(result, trueResult);
    }

    @Test
    public void testTakeWhile() {
        Iterable<Integer> result = Collections.takeWhile(LESSTHENFIVE, a);
        Iterable<Integer> trueResult = Arrays.asList(1);
        assertEquals(result, trueResult);
    }

    @Test
    public void testTakeUnless() {
        Iterable<Integer> result = Collections.takeUnless(LESSTHENFIVE, a);
        Iterable<Integer> trueResult = Arrays.asList();
        assertEquals(result, trueResult);
    }

    @Test
    public void testFoldl() {
        Integer result = Collections.foldl(DIFF, 100, a);
        Integer trueResult = 68;
        assertEquals(result, trueResult);
    }

    @Test
    public void testFoldr() {
        Integer result = Collections.foldr(DIFF, 100, a);
        Integer trueResult = -104;
        assertEquals(result, trueResult);
    }
}