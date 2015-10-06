package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PredicateTest {
    static final Predicate<Integer> NOTZERO = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer argument) {
            return argument != 0;
        }
    };

    static final Predicate<Integer> MORETHENTEN = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer argument) {
            return argument > 10;
        }
    };

    static final Predicate<Integer> LESSTHENFIVE = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer argument) {
            return argument < 5;
        }
    };

    @Test
    public void testConsts() {
        assertEquals(true, Predicate.ALWAYS_TRUE.apply(null));
        assertEquals(false, Predicate.ALWAYS_FALSE.apply(null));
    }

    @Test
    public void testOr() {
        assertEquals(false, MORETHENTEN.or(LESSTHENFIVE).apply(7));
        assertEquals(false, MORETHENTEN.or(LESSTHENFIVE).apply(10));
        assertEquals(false, MORETHENTEN.or(LESSTHENFIVE).apply(5));
        assertEquals(true, MORETHENTEN.or(LESSTHENFIVE).apply(12));
    }

    @Test
    public void testAnd() {
        assertEquals(false, MORETHENTEN.and(LESSTHENFIVE).apply(7));
        assertEquals(false, MORETHENTEN.and(LESSTHENFIVE).apply(10));
        assertEquals(false, MORETHENTEN.and(LESSTHENFIVE).apply(5));
        assertEquals(false, MORETHENTEN.and(LESSTHENFIVE).apply(12));
        assertEquals(true, NOTZERO.and(LESSTHENFIVE).apply(3));
        assertEquals(true, MORETHENTEN.and(LESSTHENFIVE.not()).apply(15));
    }

    @Test
    public void testNot() {
        assertEquals(true, MORETHENTEN.not().apply(7));
        assertEquals(false, MORETHENTEN.not().apply(20));
        assertEquals(false, MORETHENTEN.not().apply(100));
        assertEquals(true, NOTZERO.not().apply(0));
        assertEquals(false, NOTZERO.not().apply(-282));
        assertEquals(true, NOTZERO.not().and(NOTZERO.not()).apply(0));
    }
}
