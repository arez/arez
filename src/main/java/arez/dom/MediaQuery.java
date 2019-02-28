package arez.dom;

import arez.ComputableValue;
import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.DepType;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import elemental2.dom.DomGlobal;
import elemental2.dom.MediaQueryList;
import elemental2.dom.MediaQueryListListener;
import elemental2.dom.Window;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * An observable model that indicates whether a window matches a CSS media query.
 *
 * <h1>A very simple example</h1>
 * <pre>{@code
 * import arez.Arez;
 * import arez.mediaquery.MediaQuery;
 * import com.google.gwt.core.client.EntryPoint;
 * import elemental2.dom.DomGlobal;
 *
 * public class MediaQueryExample
 *   implements EntryPoint
 * {
 *   public void onModuleLoad()
 *   {
 *     final MediaQuery mediaQuery = MediaQuery.create( "(max-width: 600px)" );
 *     Arez.context().observer( () ->
 *                                DomGlobal.document.querySelector( "#status" ).textContent =
 *                                  "Screen size Status: " + ( mediaQuery.matches() ? "Narrow" : "Wide" ) );
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
  private MediaQueryList _mediaQueryList;
  private boolean _active;

  /**
   * Create an instance of MediaQuery.
   *
   * @param query the CSS media query to match.
   * @return the MediaQuery instance.
   */
  @Nonnull
  public static MediaQuery create( @Nonnull final String query )
  {
    return create( DomGlobal.window, query );
  }

  /**
   * Create an instance of MediaQuery.
   *
   * @param window the window to test.
   * @param query  the CSS media query to match.
   * @return the MediaQuery instance.
   */
  @Nonnull
  public static MediaQuery create( @Nonnull final Window window, @Nonnull final String query )
  {
    return new Arez_MediaQuery( window, query );
  }

  MediaQuery( @Nonnull final Window window, @Nonnull final String query )
  {
    _window = Objects.requireNonNull( window );
    _mediaQueryList = _window.matchMedia( Objects.requireNonNull( query ) );
    _listener = e -> notifyOnMatchChange();
  }

  /**
   * Return the window against which the MediaQuery will run.
   *
   * @return the window to test.
   */
  @Nonnull
  public final Window getWindow()
  {
    return _window;
  }

  /**
   * Return the media query to test against window.
   *
   * @return the associated media query.
   */
  @Nonnull
  @Observable
  public String getQuery()
  {
    return _mediaQueryList.media;
  }

  /**
   * Change the media query to test against.
   * If the component is active then invoking this will ensure that the listener is updated to listen
   * to new query.
   *
   * @param query the CSS media query.
   */
  public void setQuery( @Nonnull final String query )
  {
    if ( _active )
    {
      unbindListener();
    }
    _mediaQueryList = _window.matchMedia( Objects.requireNonNull( query ) );
    if ( _active )
    {
      bindListener();
    }
  }

  /**
   * Return true if the media query matches, false otherwise.
   *
   * @return true if the media query matches, false otherwise.
   */
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  public boolean matches()
  {
    // Observe query so that this is re-calculated if query changes
    getQuery();
    return _mediaQueryList.matches;
  }

  @ComputableValueRef
  abstract ComputableValue getMatchesComputableValue();

  @OnActivate
  final void onMatchesActivate()
  {
    _active = true;
    bindListener();
  }

  @OnDeactivate
  final void onMatchesDeactivate()
  {
    _active = false;
    unbindListener();
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

  private void bindListener()
  {
    _mediaQueryList.addListener( _listener );
  }

  private void unbindListener()
  {
    _mediaQueryList.removeListener( _listener );
  }
}
