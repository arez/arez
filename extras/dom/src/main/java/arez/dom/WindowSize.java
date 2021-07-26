package arez.dom;

import akasha.Window;
import javax.annotation.Nonnull;

/**
 * Factory for getting observable models that sizing of windows.
 *
 * <h1>A very simple example</h1>
 * <pre>{@code
 * import arez.Arez;
 * import arez.dom.EventDrivenValue;
 * import arez.dom.WindowSize;
 * import com.google.gwt.core.client.EntryPoint;
 * import akasha.Global;
 * import akasha.Window;
 *
 * public class WindowSizeExample
 *   implements EntryPoint
 * {
 *   public void onModuleLoad()
 *   {
 *     final EventDrivenValue<Window, Integer> innerHeight = WindowSize.innerHeight( Global.window() );
 *     final EventDrivenValue<Window, Integer> innerWidth = WindowSize.innerWidth( Global.window() );
 *
 *     Arez.context().observer( () -> Global.document().querySelector( "#status" ).textContent =
 *       "Screen size: " + innerWidth.getValue() + " x " + innerHeight.getValue() );
 *   }
 * }
 * }</pre>
 */
public final class WindowSize
{
  private WindowSize()
  {
  }

  /**
   * Create an event driven observable component for window.innerWidth and window.innerHeight wrapped in dimension object.
   *
   * @param window the window.
   * @return the event driven observable component.
   */
  @Nonnull
  public static EventDrivenValue<Window, Dimension> inner( @Nonnull final Window window )
  {
    return EventDrivenValue.create( window, "resize", w -> new Dimension( w.innerWidth(), w.innerHeight() ) );
  }

  /**
   * Create an event driven observable component for window.innerHeight.
   *
   * @param window the window.
   * @return the event driven observable component.
   */
  @Nonnull
  public static EventDrivenValue<Window, Integer> innerHeight( @Nonnull final Window window )
  {
    return EventDrivenValue.create( window, "resize", Window::innerHeight );
  }

  /**
   * Create an event driven observable component for window.innerWidth.
   *
   * @param window the window.
   * @return the event driven observable component.
   */
  @Nonnull
  public static EventDrivenValue<Window, Integer> innerWidth( @Nonnull final Window window )
  {
    return EventDrivenValue.create( window, "resize", Window::innerWidth );
  }

  /**
   * Create an event driven observable component for window.outerHeight.
   *
   * @param window the window.
   * @return the event driven observable component.
   */
  @Nonnull
  public static EventDrivenValue<Window, Integer> outerHeight( @Nonnull final Window window )
  {
    return EventDrivenValue.create( window, "resize", Window::outerHeight );
  }

  /**
   * Create an event driven observable component for window.outerWidth.
   *
   * @param window the window.
   * @return the event driven observable component.
   */
  @Nonnull
  public static EventDrivenValue<Window, Integer> outerWidth( @Nonnull final Window window )
  {
    return EventDrivenValue.create( window, "resize", Window::outerWidth );
  }
}
