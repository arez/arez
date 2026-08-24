package arez.dom;

import javax.annotation.Nonnull;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Factory for getting observable models that sizing of windows.
 *
 * <p>A very simple example</p>
 * <pre>{@code
 * import arez.Arez;
 * import arez.dom.EventDrivenValue;
 * import arez.dom.WindowSize;
 * import arez.dom.WindowSize.Window;
 * import com.google.gwt.core.client.EntryPoint;
 * import jsinterop.annotations.JsPackage;
 * import jsinterop.annotations.JsProperty;
 *
 * public class WindowSizeExample
 *   implements EntryPoint
 * {
 *   public void onModuleLoad()
 *   {
 *     final EventDrivenValue<Window, Integer> innerHeight = WindowSize.innerHeight( window() );
 *     final EventDrivenValue<Window, Integer> innerWidth = WindowSize.innerWidth( window() );
 *
 *     Arez.context().observer( () -> System.out.println(
 *       "Screen size: " + innerWidth.getValue() + " x " + innerHeight.getValue() ) );
 *   }
 *
 *   {@literal @}JsProperty( name = "window", namespace = JsPackage.GLOBAL )
 *   private static native Window window();
 * }
 * }</pre>
 */
public final class WindowSize
{
  /**
   * Minimal facade for the browser window sizing API.
   */
  @JsType( isNative = true, name = "Window", namespace = JsPackage.GLOBAL )
  public static class Window
    extends EventDrivenValue.EventTarget
  {
    protected Window()
    {
    }

    @JsProperty( name = "innerHeight" )
    native int innerHeight();

    @JsProperty( name = "innerWidth" )
    native int innerWidth();

    @JsProperty( name = "outerHeight" )
    native int outerHeight();

    @JsProperty( name = "outerWidth" )
    native int outerWidth();
  }

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
