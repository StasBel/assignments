package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Function2Test {
    static final Function1<Integer, Integer> DOUBLE = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer argument) {
            return argument * argument;
        }
    };

    static final Function2<Integer, Integer, Integer> SQRSUM = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer argument1, Integer argument2) {
            return argument1 * argument1 + argument2 * argument2;
        }
    };

    static final Function2<Integer, Integer, Integer> CBSUM = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer argument1, Integer argument2) {
            return argument1 * argument1 * argument1 + argument2 * argument2 * argument2;
        }
    };

    static final Function2<Integer, Integer, Integer> DIFF = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer argument1, Integer argument2) {
            return argument1 - argument2;
        }
    };

    @Test
    public void testApply() {
        assertEquals((Integer)8, SQRSUM.apply(2, 2));
        assertEquals((Integer)16, CBSUM.apply(2, 2));
    }

    @Test
    public void testCompose() {
        assertEquals((Integer)64, SQRSUM.compose(DOUBLE).apply(2, 2));
    }

    @Test
    public void testBind1() {
        assertEquals((Integer)(-6), DIFF.bind1(4).apply(10));
        assertEquals((Integer)(-16), DIFF.bind1(4).apply(20));
    }

    @Test
    public void testBind2() {
        assertEquals((Integer)6, DIFF.bind2(4).apply(10));
        assertEquals((Integer)16, DIFF.bind2(4).apply(20));
    }

    @Test
    public void testCurry() {
        Function1<Integer, Function1<Integer, Integer>> curry = SQRSUM.curry();
        assertEquals((Integer)8, curry.apply(2).apply(2));
    }
}

