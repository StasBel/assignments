package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Function1Test {
    static final Function1<Integer, Integer> DOUBLE = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer argument) {
            return argument * argument;
        }
    };

    static final Function1<Integer, Integer> TRIPLE = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer argument) {
            return argument * argument * argument;
        }
    };

    @Test
    public void testApply() {
        assertEquals((Integer)9, DOUBLE.apply(3));
        assertEquals((Integer)27, TRIPLE.apply(3));
    }

    @Test
    public void testCompose() {
        assertEquals((Integer)729, DOUBLE.compose(TRIPLE).apply(3));
    }
}
