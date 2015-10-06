package ru.spbau.mit;

import java.util.Objects;

public abstract class Predicate<X> extends Function1<X, Boolean> {
    public final static Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object argument) {
            return true;
        }
    };

    public final static Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object argument) {
            return false;
        }
    };

    public <X2 extends X> Predicate<X2> or(final Predicate<? super X2> g) {
        final Predicate<X> f = this;

        return new Predicate<X2>() {
            @Override
            public Boolean apply(X2 argument2) {
                return f.apply(argument2) || g.apply(argument2);
            }
        };
    }

    public <X2 extends X> Predicate<X2> and(final Predicate<? super X2> g) {
        final Predicate<X> f = this;

        return new Predicate<X2>() {
            @Override
            public Boolean apply(X2 argument2) {
                return f.apply(argument2) && g.apply(argument2);
            }
        };
    }

    public Predicate<X> not() {
        final Predicate<X> f = this;

        return new Predicate<X>() {
            @Override
            public Boolean apply(X argument) {
                return !f.apply(argument);
            }
        };
    }
}
