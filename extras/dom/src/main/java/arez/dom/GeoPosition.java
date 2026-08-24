package arez.dom;

import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import arez.Task;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import arez.annotations.ComputableValueRef;
import arez.annotations.ContextRef;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * A component that exposes the current geo position as an observable property. This component relies on the
 * underlying <a href="https://developer.mozilla.org/en-US/docs/Web/API/Geolocation_API">Geolocation API</a> and
 * it's usage is restricted in the same way as the underlying API (i.e. it is only available in secure contexts
 * and it asks user permission before providing data.).
 *
 * <pre>{@code
 * final GeoPosition geoPosition = GeoPosition.create();
 * Arez.context().observer( () -> consumePosition( geoPosition.getPosition() ) );
 * }</pre>
 */
@ArezComponent( requireId = Feature.DISABLE, disposeNotifier = Feature.DISABLE )
public abstract class GeoPosition
{
  @JsFunction
  private interface PositionCallback
  {
    void onPosition( GeolocationPosition position );
  }

  @JsFunction
  private interface PositionErrorCallback
  {
    void onError( GeolocationPositionError error );
  }

  @JsType( isNative = true, name = "GeolocationPosition", namespace = JsPackage.GLOBAL )
  private static class GeolocationPosition
  {
    @JsProperty( name = "coords" )
    native GeolocationCoordinates coords();
  }

  @JsType( isNative = true, name = "GeolocationCoordinates", namespace = JsPackage.GLOBAL )
  static class GeolocationCoordinates
  {
    @JsProperty( name = "accuracy" )
    native double accuracy();

    @JsProperty( name = "altitude" )
    native Double altitude();

    @JsProperty( name = "heading" )
    native Double heading();

    @JsProperty( name = "latitude" )
    native double latitude();

    @JsProperty( name = "longitude" )
    native double longitude();

    @JsProperty( name = "speed" )
    native Double speed();
  }

  @JsType( isNative = true, name = "GeolocationPositionError", namespace = JsPackage.GLOBAL )
  static class GeolocationPositionError
  {
    @JsProperty( name = "code" )
    native int code();

    @JsProperty( name = "message" )
    native String message();
  }

  @JsType( isNative = true, name = "Geolocation", namespace = JsPackage.GLOBAL )
  private static class Geolocation
  {
    @JsMethod
    native int watchPosition( PositionCallback successCallback, PositionErrorCallback errorCallback );

    @JsMethod
    native void clearWatch( int watchId );
  }

  @JsType( isNative = true, name = "Navigator", namespace = JsPackage.GLOBAL )
  private static class Navigator
  {
    @JsProperty( name = "geolocation" )
    native Geolocation geolocation();
  }

  @SuppressWarnings( "unused" )
  public static final class Status
  {
    /**
     * Position data is yet to start loading.
     */
    public static final int INITIAL = -2;
    /**
     * Position data is loading.
     */
    public static final int LOADING = -1;
    /**
     * No error acquiring position.
     */
    public static final int POSITION_LOADED = 0;
    /**
     * The acquisition of the geolocation information failed because the page didn't have the permission to do it.
     */
    public static final int PERMISSION_DENIED = 1;
    /**
     * The acquisition of the geolocation failed because at least one internal source of position returned an internal error.
     */
    public static final int POSITION_UNAVAILABLE = 2;
    /**
     * The time allowed to acquire the geolocation, defined by PositionOptions.timeout information was reached before the information was obtained.
     */
    public static final int TIMEOUT = 3;

    private Status()
    {
    }
  }

  @Nullable
  private Position _position;
  private int _status;
  @Nullable
  private String _errorMessage;
  private int _activateCount;
  private int _watcherId;

  /**
   * Create the GeoPosition component.
   *
   * @return the newly created GeoPosition component.
   */
  @Nonnull
  public static GeoPosition create()
  {
    return new Arez_GeoPosition();
  }

  GeoPosition()
  {
    _status = Status.INITIAL;
    _activateCount = 0;
  }

  /**
   * Return an immutable representation of the current position.
   * This will be null unless {@link #getStatus()} has returned a {@link Status#POSITION_LOADED} value.
   *
   * @return the current position as reported by the geolocation API.
   */
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  @Nullable
  public Position getPosition()
  {
    return _position;
  }

  /**
   * Return the status indicating whether the position is available.
   * It will be one of the values provided by {@link Status}.
   *
   * @return the status of the position data.
   */
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  public int getStatus()
  {
    return _status;
  }

  /**
   * Return the error message reported by the geolocation API when position could not be loaded else null.
   *
   * @return the error message if any.
   */
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  @Nullable
  public String getErrorMessage()
  {
    return _errorMessage;
  }

  @ComputableValueRef
  abstract ComputableValue<?> getPositionComputableValue();

  @ComputableValueRef
  abstract ComputableValue<?> getStatusComputableValue();

  @ComputableValueRef
  abstract ComputableValue<?> getErrorMessageComputableValue();

  @OnActivate
  void onPositionActivate()
  {
    activate();
  }

  @OnDeactivate
  void onPositionDeactivate()
  {
    deactivate();
  }

  @OnActivate
  void onStatusActivate()
  {
    activate();
  }

  @OnDeactivate
  void onStatusDeactivate()
  {
    deactivate();
  }

  @OnActivate
  void onErrorMessageActivate()
  {
    activate();
  }

  @OnDeactivate
  void onErrorMessageDeactivate()
  {
    deactivate();
  }

  private void activate()
  {
    if ( 0 == _activateCount )
    {
      context().task( Arez.areNamesEnabled() ? componentName() + ".setLoadingStatus" : null,
                      () -> setStatus( Status.LOADING ),
                      Task.Flags.DISPOSE_ON_COMPLETE );
      _watcherId = navigator().geolocation().watchPosition( e -> onSuccess( e.coords() ), this::onFailure );
    }
    _activateCount++;
  }

  private void deactivate()
  {
    _activateCount--;
    if ( 0 == _activateCount )
    {
      setStatus( Status.INITIAL );
      navigator().geolocation().clearWatch( _watcherId );
      _watcherId = 0;
    }
  }

  @Action
  void onFailure( @Nonnull final GeolocationPositionError e )
  {
    setStatus( e.code() );
    final String errorMessage = e.message();
    if ( !Objects.equals( errorMessage, _errorMessage ) )
    {
      _errorMessage = errorMessage;
      getErrorMessageComputableValue().reportPossiblyChanged();
    }
    if ( null != _position )
    {
      _position = null;
      getPositionComputableValue().reportPossiblyChanged();
    }
  }

  @Action
  void setStatus( final int status )
  {
    if ( status != _status )
    {
      _status = status;
      getStatusComputableValue().reportPossiblyChanged();
    }
  }

  @Action
  void onSuccess( @Nonnull final GeolocationCoordinates coords )
  {
    setStatus( Status.POSITION_LOADED );
    if ( null != _errorMessage )
    {
      _errorMessage = null;
      getErrorMessageComputableValue().reportPossiblyChanged();
    }
    _position =
      new Position( coords.accuracy(), coords.altitude(), coords.heading(), coords.latitude(), coords.longitude(), coords.longitude() );
    getPositionComputableValue().reportPossiblyChanged();
  }

  @ComponentNameRef
  abstract String componentName();

  @ContextRef
  abstract ArezContext context();

  @JsProperty( name = "navigator", namespace = JsPackage.GLOBAL )
  private static native Navigator navigator();
}
