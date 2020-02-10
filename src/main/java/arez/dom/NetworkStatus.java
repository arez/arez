package arez.dom;

import arez.ComputableValue;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.DepType;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;
import java.util.Date;
import javax.annotation.Nonnull;

/**
 * An observable model that declares state that tracks when the user is "online".
 * The online state is essentially a reflection of the browsers "navigator.onLine"
 * value. If an observer is observing the model, the model listens for changes from
 * the browser and updates the online state as appropriate. However if there is no
 * observer for the state, the model will not listen to to the browser events so as
 * not to have any significant performance impact.
 *
 * <h1>A very simple example</h1>
 * <pre>{@code
 * import com.google.gwt.core.client.EntryPoint;
 * import elemental2.dom.DomGlobal;
 * import arez.Arez;
 * import arez.networkstatus.NetworkStatus;
 *
 * public class NetworkStatusExample
 *   implements EntryPoint
 * {
 *   public void onModuleLoad()
 *   {
 *     final NetworkStatus networkStatus = NetworkStatus.create();
 *     Arez.context().observer( () -> {
 *       final String message = "Network Status: " + ( networkStatus.isOnLine() ? "Online" : "Offline" );
 *       DomGlobal.console.log( message );
 *       if ( !networkStatus.isOnLine() )
 *       {
 *         DomGlobal.console.log( "Offline since: " + networkStatus.getLastChangedAt() );
 *       }
 *     } );
 *   }
 * }
 * }</pre>
 */
@ArezComponent
public abstract class NetworkStatus
{
  private final EventListener _listener = e -> updateOnlineStatus( getOnLineComputableValue() );

  /**
   * Create an instance of NetworkStatus.
   *
   * @return the NetworkStatus instance.
   */
  @Nonnull
  public static NetworkStatus create()
  {
    return new Arez_NetworkStatus( new Date() );
  }

  NetworkStatus()
  {
  }

  /**
   * Return true if the browser is online, false otherwise.
   *
   * @return true if the browser is online, false otherwise.
   */
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  public boolean isOnLine()
  {
    return DomGlobal.navigator.onLine;
  }

  /**
   * Return the last time at which online status changed.
   * This will default to the time the component was created, otherwise
   * the time at which the online status was changed.
   *
   * @return the last time at which online status changed.
   */
  @Observable
  @Nonnull
  public abstract Date getLastChangedAt();

  abstract void setLastChangedAt( @Nonnull Date lastChangedAt );

  @ComputableValueRef
  abstract ComputableValue<?> getOnLineComputableValue();

  @OnActivate
  final void onOnLineActivate()
  {
    DomGlobal.window.addEventListener( "online", _listener );
    DomGlobal.window.addEventListener( "offline", _listener );
  }

  @OnDeactivate
  final void onOnLineDeactivate()
  {
    DomGlobal.window.removeEventListener( "online", _listener );
    DomGlobal.window.removeEventListener( "offline", _listener );
  }

  @Action
  void updateOnlineStatus( @Nonnull final ComputableValue<?> computableValue )
  {
    computableValue.reportPossiblyChanged();
    setLastChangedAt( new Date() );
  }
}
