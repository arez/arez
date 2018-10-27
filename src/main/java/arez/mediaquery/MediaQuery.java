package arez.mediaquery;

import arez.ComputableValue;
import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;
import arez.annotations.DepType;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import elemental2.dom.DomGlobal;
import elemental2.dom.MediaQueryList;
import elemental2.dom.MediaQueryListListener;
import elemental2.dom.Window;
import java.util.Objects;
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
 * import arez.networkstatus.MediaQuery;
 *
 * public class NetworkStatusExample
 *   implements EntryPoint
 * {
 *   public void onModuleLoad()
 *   {
 *     final MediaQuery networkStatus = MediaQuery.create();
 *     Arez.context().autorun( () -> {
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
@ArezComponent( nameIncludesId = false )
public abstract class MediaQuery
{
  private final MediaQueryListListener _listener;
  @Nonnull
  private final Window _window;
  @Nonnull
  private final MediaQueryList _mediaQueryList;

  /**
   * Create an instance of MediaQuery.
   *
   * @return the MediaQuery instance.
   */
  @Nonnull
  public static MediaQuery create( @Nonnull final String mediaQuery )
  {
    return create( DomGlobal.window, mediaQuery );
  }

  @Nonnull
  public static MediaQuery create( @Nonnull final Window window, @Nonnull final String query )
  {
    return new Arez_MediaQuery( window, query );
  }

  MediaQuery( @Nonnull final Window window, @Nonnull final String query )
  {
    _window = Objects.requireNonNull( window );
    _mediaQueryList = _window.matchMedia( query );
    _listener = e -> notifyOnMatchChange();
  }

  @Nonnull
  public final Window getWindow()
  {
    return _window;
  }

  @Nonnull
  public final String getQuery()
  {
    return _mediaQueryList.media;
  }

  /**
   * Return true if the media query matches, false otherwise.
   *
   * @return true if the media query matches, false otherwise.
   */
  @Computed( depType = DepType.AREZ_OR_EXTERNAL )
  public boolean matches()
  {
    return _mediaQueryList.matches;
  }

  @ComputedValueRef
  abstract ComputableValue getMatchesComputableValue();

  @OnActivate
  final void onMatchesActivate()
  {
    _mediaQueryList.addListener( _listener );
  }

  @OnDeactivate
  final void onMatchesDeactivate()
  {
    _mediaQueryList.removeListener( _listener );
  }

  @Action
  void notifyOnMatchChange()
  {
    // According to notes in https://github.com/ReactTraining/react-media/blob/master/modules/MediaQueryList.js
    // Safari doesn't clear up listener with removeListener when the listener is already waiting in the event queue.
    // This code makes sure Having an active flag to make sure the change is not reported after computable is
    // deactivated and component is disposed.
    if ( !Disposable.isDisposed( this ) )
    {
      getMatchesComputableValue().reportPossiblyChanged();
    }
  }
}
