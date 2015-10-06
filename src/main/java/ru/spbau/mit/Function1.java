package ru.spbau.mit;

public abstract class Function1<X, Y> {
    public abstract Y apply(X argument);

    public <Z> Function1<X, Z> compose(final Function1<? super Y, ? extends Z> g) {
        final Function1<X, Y> f = this;

        return new Function1<X, Z>() {
            @Override
            public Z apply(X argument) {
                return g.apply(f.apply(argument));
            }
        };
    }
}