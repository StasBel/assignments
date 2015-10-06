package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Collections {
    public static <X, Y> Iterable<Y> map(final Function1<? super X, ? extends Y> f, final Iterable<X> a) {
        List<Y> result = new ArrayList<>();
        for (X element : a) {
            result.add(f.apply(element));
        }
        return result;
    }

    public static <X> Iterable<X> filter(final Predicate<? super X> p, final Iterable<X> a) {
        List<X> result = new ArrayList<>();
        for (X element : a) {
            if (p.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }

    public static <X> Iterable<X> takeWhile(final Predicate<? super X> p, final Iterable<X> a) {
        List<X> result = new ArrayList<>();
        for (X element : a) {
            if (!p.apply(element)) {
                break;
            }
            result.add(element);
        }
        return result;
    }

    public static <X> Iterable<X> takeUnless(final Predicate<? super X> p, final Iterable<X> a) {
        List<X> result = new ArrayList<>();
        for (X element : a) {
            if (p.apply(element)) {
                break;
            }
            result.add(element);
        }
        return result;
    }

    public static <X, Y> Y foldl(final Function2<? super Y, ? super X, ? extends Y> f, Y x, final Iterable<X> a) {
        for (X element : a) {
            x = f.apply(x, element);
        }
        return x;
    }

    public static <X, Y> Y foldr(final Function2<? super X, ? super Y, ? extends Y> f, Y x, final Iterable<X> a) {
        LinkedList<X> copy = new LinkedList<>();
        for (X element : a) {
            copy.addLast(element);
        }
        for (Iterator<X> iterator = copy.descendingIterator(); iterator.hasNext();) {
            x = f.apply(iterator.next(), x);
        }
        return x;
    }
}
