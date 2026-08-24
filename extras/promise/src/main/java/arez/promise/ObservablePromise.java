package arez.promise;

import arez.Arez;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import java.util.Objects;
import javax.annotation.Nonnull;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import static org.realityforge.braincheck.Guards.*;

/**
 * An observable model that wraps a Promise and exposes observable state that track
 * the state of the promise. The observable exposes the state of the promise as well
 * as the value that it resolves to or the error it was rejected with as observable
 * properties.
 *
 * <p>A very simple example</p>
 * <pre>{@code
 * import arez.Arez;
 * import arez.promise.ObservablePromise;
 * import arez.promise.ObservablePromise.Promise;
 * import com.google.gwt.core.client.EntryPoint;
 *
 * public class Example
 *   implements EntryPoint
 * {
 *   public void onModuleLoad()
 *   {
 *     final Promise<Response> promise = fetch( "https://example.com/" );
 *     final ObservablePromise<Response, Object> observablePromise = ObservablePromise.create( promise );
 *     Arez.context().observer( () -> System.out.println( "Promise Status: " + observablePromise.getState() ) );
 *   }
 * }
 * }</pre>
 *
 * @param <T> the type of the value that the promise will resolve to.
 * @param <E> the type of the error if the promise is rejected.
 */
@ArezComponent( requireId = Feature.DISABLE )
public abstract class ObservablePromise<T, E>
{
  /**
   * Minimal facade for a JavaScript promise.
   *
   * @param <T> the type of value produced by the promise.
   */
  @JsType( isNative = true, name = "Promise", namespace = JsPackage.GLOBAL )
  public static class Promise<T>
  {
    protected Promise()
    {
    }

    @JsMethod
    static native <V> Promise<V> resolve( V value );

    @JsMethod
    static native <V> Promise<V> reject( Object error );

    @JsMethod
    native <V> Promise<V> then( OnFulfilledCallback<? super T, V> callback );

    @JsMethod( name = "catch" )
    native <V> Promise<V> catch_( OnRejectedCallback<V> callback );
  }

  @JsFunction
  private interface OnFulfilledCallback<T, V>
  {
    Promise<V> onFulfilled( T value );
  }

  @JsFunction
  private interface OnRejectedCallback<V>
  {
    Promise<V> onRejected( Object error );
  }

  /**
   * The state of the promise.
   */
  public enum State
  {
    PENDING, FULFILLED, REJECTED
  }

  /**
   * The underlying promise.
   * This is not converted to a local variable to make it easy to debug scenarios from within the
   * browsers DevTools.
   */
  @SuppressWarnings( "FieldCanBeLocal" )
  private final Promise<T> _promise;
  /**
   * The state of the promise. Starts as {@link State#PENDING} and then transitions to either
   * {@link State#FULFILLED} or {@link State#REJECTED}.
   */
  @Nonnull
  private State _state;
  /**
   * The value that the promise resolved to. This is not valid unless the state is {@link State#FULFILLED}.
   */
  private T _value;
  /**
   * The error that the promise was rejected with. This is not valid unless the state is {@link State#REJECTED}.
   */
  private E _error;

  /**
   * Create the observable model that wraps specified promise.
   *
   * @param <T>     the type of the value that the promise will resolve to.
   * @param <E>     the type of the error if the promise is rejected.
   * @param promise the promise to wrap.
   * @return the ObservablePromise
   */
  @Nonnull
  public static <T, E> ObservablePromise<T, E> create( @Nonnull final Promise<T> promise )
  {
    return new Arez_ObservablePromise<>( promise );
  }

  ObservablePromise( @Nonnull final Promise<T> promise )
  {
    _state = State.PENDING;
    _promise = Objects.requireNonNull( promise );
    _promise.then( this::onFulfilled ).catch_( this::onRejected );
  }

  /**
   * Return the promise state.
   *
   * @return the promise state.
   */
  @Observable
  @Nonnull
  public State getState()
  {
    return _state;
  }

  void setState( @Nonnull final State state )
  {
    _state = Objects.requireNonNull( state );
  }

  /**
   * Return the value that the promise was resolved to.
   * This should NOT be called if the state is not {@link State#FULFILLED} and will result in an invariant
   * failure if invariants are enabled.
   *
   * @return the value that the promise was resolved to.
   */
  @Observable
  public T getValue()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> _state == State.FULFILLED,
                    () -> "Arez-0165: ObservablePromise.getValue() called when the promise is not in " +
                          "fulfilled state. State: " + _state + ", Promise: " + _promise );
    }
    return _value;
  }

  void setValue( final T value )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> _state == State.FULFILLED,
                 () -> "Arez-0166: ObservablePromise.setValue() called when promise is in incorrect state. " +
                       "State: " + _state + ", Promise: " + _promise );
    }
    _value = value;
  }

  /**
   * Return the error that the promise was rejected with.
   * This should NOT be called if the state is not {@link State#REJECTED} and will result in an invariant
   * failure if invariants are enabled.
   *
   * @return the error that the promise was rejected with.
   */
  @Observable
  public E getError()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> _state == State.REJECTED,
                    () -> "ObservablePromise.getError() called when the promise is not in " +
                          "rejected state. State: " + _state + ", Promise: " + _promise );
    }
    return _error;
  }

  void setError( final E error )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> _state == State.REJECTED,
                 () -> "ObservablePromise.setError() called when promise is in incorrect state. " +
                       "State: " + _state + ", Promise: " + _promise );
    }
    _error = error;
  }

  @Action
  @Nonnull
  Promise<T> onFulfilled( final T value )
  {
    setState( State.FULFILLED );
    setValue( value );
    return Promise.resolve( value );
  }

  @Action
  Promise<Object> onRejected( @Nonnull final Object error )
  {
    setState( State.REJECTED );
    setError( Js.uncheckedCast( error ) );
    return Promise.reject( error );
  }
}
