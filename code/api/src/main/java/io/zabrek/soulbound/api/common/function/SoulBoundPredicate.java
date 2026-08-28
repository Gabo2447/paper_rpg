package io.zabrek.soulbound.api.common.function;

import io.zabrek.soulbound.api.SoulBoundException;

import java.util.Objects;

/**
 * A {@link java.util.function.Predicate} that may throw a {@link SoulBoundException}.
 *
 * @param <T> The type of the input argument
 * @since 2.0.0
 */
@FunctionalInterface
public interface SoulBoundPredicate<T> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param value the argument to test
     * @return {@code true} if the input argument match the predicate,
     * otherwise {@code false}
     * @throws SoulBoundException when the method execution fails
     * @since 2.0.0
     */
    boolean test(T value) throws SoulBoundException;

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code false}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ANDed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * AND of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     * @since 2.0.0
     */
    default SoulBoundPredicate<T> and(final SoulBoundPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (T t) -> test(t) && other.test(t);
    }

    /**
     * Returns a predicate that represents the logical negation of this
     * predicate.
     *
     * @return a predicate that represents the logical negation of this
     * predicate
     * @since 2.0.0
     */
    default SoulBoundPredicate<T> negate() {
        return (T t) -> !test(t);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code true}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ORed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * OR of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     * @since 2.0.0
     */
    @SuppressWarnings("PMD.ShortMethodName")
    default SoulBoundPredicate<T> or(final SoulBoundPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (T t) -> test(t) || other.test(t);
    }
}
