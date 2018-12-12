package arez.dom;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDocument;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Wrapper of component to expose visibility state of document reactively.
 *
 * <h1>A very simple example</h1>
 * <pre>{@code
 * import arez.Arez;
 * import arez.dom.DocumentVisibility;
 * import com.google.gwt.core.client.EntryPoint;
 * import elemental2.dom.DomGlobal;
 *
 * public class DocumentVisibilityExample
 *   implements EntryPoint
 * {
 *   public void onModuleLoad()
 *   {
 *     final DocumentVisibility v = DocumentVisibility.create();
 *     Arez.context().observer( () -> DomGlobal.console.log( "Document Visibility: " + v.getVisibility() ) );
 *   }
 * }
 * }</pre>
 */
public final class DocumentVisibility
{
  /**
   * The visibility state of the document.
   */
  public enum Visibility
  {
    /**
     * The page content may be at least partially visible. In practice this means that the page is the foreground tab of a non-minimized window.
     */
    VISIBLE,
    /**
     * The page content is not visible to the user. In practice this means that the document is either a background tab or part of a minimized window, or the OS screen lock is active.
     */
    HIDDEN,
    /**
     * The page content is being prerendered and is not visible to the user (considered hidden for purposes of document.hidden). The document may start in this state, but will never transition to it from another value. Note: browser support is optional.
     */
    PRERENDER
  }

  /**
   * The underlying component performing the monitoring.
   */
  private final EventDrivenValue<HTMLDocument, String> _value;

  /**
   * Create component monitoring the default document.
   *
   * @return the new component.
   */
  @Nonnull
  public static DocumentVisibility create()
  {
    return create( DomGlobal.document );
  }

  /**
   * Create component monitoring specific document.
   *
   * @param document the document.
   * @return the new component.
   */
  @Nonnull
  public static DocumentVisibility create( @Nonnull final HTMLDocument document )
  {
    return new DocumentVisibility( Objects.requireNonNull( document ) );
  }

  private DocumentVisibility( @Nonnull final HTMLDocument document )
  {
    _value = EventDrivenValue.create( document, "visibilitychange", d -> d.visibilityState );
  }

  /**
   * Return the document that monitoring visibility state.
   *
   * @return the document.
   */
  @Nonnull
  public HTMLDocument getDocument()
  {
    return _value.getSource();
  }

  /**
   * Change the document that is having visibility state monitored.
   *
   * @param document the new document.
   */
  public void setDocument( @Nonnull final HTMLDocument document )
  {
    _value.setSource( document );
  }

  /**
   * Return the visibility state of the document as an enum.
   *
   * @return the visibility state as an enum.
   */
  @Nonnull
  public Visibility getVisibility()
  {
    return asVisibility( getVisibilityState() );
  }

  /**
   * Return the visibility state of the document as a string.
   *
   * @return the visibility state as a string.
   */
  @Nonnull
  public String getVisibilityState()
  {
    return _value.getValue();
  }

  /**
   * Convert the visibility state as an enum.
   *
   * @param state the state.
   * @return the visibility enum.
   */
  @Nonnull
  private Visibility asVisibility( @Nonnull final String state )
  {
    if ( "visible".equals( state ) )
    {
      return Visibility.VISIBLE;
    }
    else if ( "hidden".equals( state ) )
    {
      return Visibility.HIDDEN;
    }
    else
    {
      assert "prerender".equals( state );
      return Visibility.PRERENDER;
    }
  }
}
