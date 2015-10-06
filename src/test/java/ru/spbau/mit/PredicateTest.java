package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class PredicateTest {
    private static final Predicate<Integer> NOT_ZERO = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer argument) {
            return argument != 0;
        }
    };

    private static final Predicate<Integer> MORE_THEN_TEN = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer argument) {
            return argument > 10;
        }
    };

    private static final Predicate<Integer> LESS_THEN_FIVE = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer argument) {
            return argument < 5;
        }
    };

    @Test
    public void testConsts() {
        assertTrue(Predicate.ALWAYS_TRUE.apply(null));
        assertFalse(Predicate.ALWAYS_FALSE.apply(null));
    }

    @Test
    public void testOr() {
        assertFalse(MORE_THEN_TEN.or(LESS_THEN_FIVE).apply(7));
        assertFalse(MORE_THEN_TEN.or(LESS_THEN_FIVE).apply(10));
        assertFalse(MORE_THEN_TEN.or(LESS_THEN_FIVE).apply(5));
        assertTrue(MORE_THEN_TEN.or(LESS_THEN_FIVE).apply(12));
    }

    @Test
    public void testAnd() {
        assertFalse(MORE_THEN_TEN.and(LESS_THEN_FIVE).apply(7));
        assertFalse(MORE_THEN_TEN.and(LESS_THEN_FIVE).apply(10));
        assertFalse(MORE_THEN_TEN.and(LESS_THEN_FIVE).apply(5));
        assertFalse(MORE_THEN_TEN.and(LESS_THEN_FIVE).apply(12));
        assertTrue(NOT_ZERO.and(LESS_THEN_FIVE).apply(3));
        assertTrue(MORE_THEN_TEN.and(LESS_THEN_FIVE.not()).apply(15));
    }

    @Test
    public void testNot() {
        assertTrue(MORE_THEN_TEN.not().apply(7));
        assertFalse(MORE_THEN_TEN.not().apply(20));
        assertFalse(MORE_THEN_TEN.not().apply(100));
        assertTrue(NOT_ZERO.not().apply(0));
        assertFalse(NOT_ZERO.not().apply(-282));
        assertTrue(NOT_ZERO.not().and(NOT_ZERO.not()).apply(0));
    }

    @Test
    public void testLazy() {
        assertTrue(NOT_ZERO.not().or(null).apply(0));
        assertFalse(NOT_ZERO.and(null).apply(0));
    }
}
