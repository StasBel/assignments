package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Function2Test {
    private static final Function1<Integer, Integer> DOUBLE = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer argument) {
            return argument * argument;
        }
    };

    private static final Function2<Integer, Integer, Integer> SQR_SUM = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer argument1, Integer argument2) {
            return argument1 * argument1 + argument2 * argument2;
        }
    };

    private static final Function2<Integer, Integer, Integer> CB_SUM = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer argument1, Integer argument2) {
            return argument1 * argument1 * argument1 + argument2 * argument2 * argument2;
        }
    };

    private static final Function2<Integer, Integer, Integer> DIFF = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer argument1, Integer argument2) {
            return argument1 - argument2;
        }
    };

    @Test
    public void testApply() {
        assertEquals((Integer)8, SQR_SUM.apply(2, 2));
        assertEquals((Integer)16, CB_SUM.apply(2, 2));
    }

    @Test
    public void testCompose() {
        assertEquals((Integer)64, SQR_SUM.compose(DOUBLE).apply(2, 2));
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
        Function1<Integer, Function1<Integer, Integer>> curry = SQR_SUM.curry();
        assertEquals((Integer)8, curry.apply(2).apply(2));
    }
}

