package ru.spbau.mit;

public abstract class Function2<X1, X2, Y> {
    public abstract Y apply(X1 argument1, X2 argument2);

    public <Z> Function2<X1, X2, Z> compose(final Function1<? super Y, Z> g) {
        final Function2<X1, X2, Y> f = this;

        return new Function2<X1, X2, Z>() {
            @Override
            public Z apply(X1 argument1, X2 argument2) {
                return g.apply(f.apply(argument1, argument2));
            }
        };
    }

    public Function1<X2, Y> bind1(final X1 argument1) {
        final Function2<X1, X2, Y> f = this;

        return new Function1<X2, Y>() {
            @Override
            public Y apply(X2 argument2) {
                return f.apply(argument1, argument2);
            }
        };
    }

    public Function1<X1, Y> bind2(final X2 argument2) {
        final Function2<X1, X2, Y> f = this;

        return new Function1<X1, Y>() {
            @Override
            public Y apply(X1 argument1) {
                return f.apply(argument1, argument2);
            }
        };
    }

    public Function1<X1, Function1<X2, Y>> curry() {
        final Function2<X1, X2, Y> f = this;

        return new Function1<X1, Function1<X2, Y>>() {
            @Override
            public Function1<X2, Y> apply(final X1 argument) {
                return f.bind1(argument);
            }
        };
    }
}
