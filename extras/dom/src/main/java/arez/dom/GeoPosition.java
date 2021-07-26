package arez.dom;

import akasha.GeolocationCoordinates;
import akasha.GeolocationPositionError;
import akasha.WindowGlobal;
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

/**
 * A component that exposes the current geo position as an observable property. This component relies on the
 * underlying <a href="https://developer.mozilla.org/en-US/docs/Web/API/Geolocation_API">Geolocation API</a> and
 * it's usage is restricted in the same way as the underlying API (i.e. it is only available in secure contexts
 * and it asks user permission before providing data.).
 *
 * <pre>{@code
 * EventDrivenValue<Window, Integer> innerWidth = EventDrivenValue.create( window, "resize", () -> window.innerWidth )
 * }</pre>
 *
 * <p>It is important that the code not add a listener to the underlying event source until there is an
 * observer accessing the <code>"value"</code> observable defined by the EventDrivenValue class. The first
 * observer that observes the observable will result in an event listener being added to the event source
 * and this listener will not be removed until there is no observers left observing the value. This means
 * that a component that is not being used has very little overhead.</p>
 */
@ArezComponent( requireId = Feature.DISABLE, disposeNotifier = Feature.DISABLE )
public abstract class GeoPosition
{
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
    public static final int PERMISSION_DENIED = GeolocationPositionError.PERMISSION_DENIED;
    /**
     * The acquisition of the geolocation failed because at least one internal source of position returned an internal error.
     */
    public static final int POSITION_UNAVAILABLE = GeolocationPositionError.POSITION_UNAVAILABLE;
    /**
     * The time allowed to acquire the geolocation, defined by PositionOptions.timeout information was reached before the information was obtained.
     */
    public static final int TIMEOUT = GeolocationPositionError.TIMEOUT;

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
      _watcherId = WindowGlobal.navigator().geolocation().watchPosition( e -> onSuccess( e.coords() ), this::onFailure );
    }
    _activateCount++;
  }

  private void deactivate()
  {
    _activateCount--;
    if ( 0 == _activateCount )
    {
      setStatus( Status.INITIAL );
      WindowGlobal.navigator().geolocation().clearWatch( _watcherId );
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
}
