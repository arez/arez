package org.realityforge.arez.browser.extras;

import elemental2.dom.DomGlobal;
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.PreDispose;

/**
 * This is a simple abstraction over browser location as a hash.
 * The model exposes the observable values for the location as the application sees it via
 * {@link #getLocation()}, the way the browser sees it via {@link #getBrowserLocation()}.
 * The application code should define an autorun that monitors the location as the browser
 * sees it and update the location as the application sees it via {@link #changeLocation(String)}
 * if the browser location is valid. Otherwise the browser location should be reset to the application
 * location.
 */
@Container( singleton = true )
public class BrowserLocation
{
  private final EventListener _listener = this::updateBrowserLocation;

  /**
   * The location according to the application.
   */
  @Nonnull
  private String _location = "";
  /**
   * The location according to the browser.
   */
  @Nonnull
  private String _browserLocation = "";
  /**
   * The location that the application is attempting to update the browser to.
   */
  @Nonnull
  private String _targetLocation = "";

  /**
   * Create the model object.
   *
   * @return the BrowserLocation instance.
   */
  public static BrowserLocation create()
  {
    return new Arez_BrowserLocation();
  }

  BrowserLocation()
  {
  }

  @PostConstruct
  final void postConstruct()
  {
    DomGlobal.window.addEventListener( "hashchange", _listener, false );
  }

  @PreDispose
  final void onDispose()
  {
    DomGlobal.window.removeEventListener( "hashchange", _listener, false );
  }

  /**
   * Change the target location to the specified parameter.
   * This will ultimately result in a side-effect that updates the browsers location.
   * This location parameter should not include "#" as the first character.
   *
   * @param targetLocation the location to change to.
   */
  @Action
  public void changeLocation( @Nonnull final String targetLocation )
  {
    _targetLocation = targetLocation;
    if ( targetLocation.equals( getBrowserLocation() ) )
    {
      setLocation( targetLocation );
    }
    else
    {
      //TODO: This next line should be an @Autorun when supported
      setHash( targetLocation );
    }
  }

  /**
   * Revert the browsers location to the application location.
   */
  @Action
  public void resetBrowserLocation()
  {
    changeLocation( getLocation() );
  }

  /**
   * Return the location as the application sees it.
   * This return value does not include a "#" as the first character.
   *
   * @return the location.
   */
  @Observable
  @Nonnull
  public String getLocation()
  {
    return _location;
  }

  @Observable
  void setLocation( @Nonnull final String location )
  {
    _location = Objects.requireNonNull( location );
  }

  @Observable
  @Nonnull
  public String getBrowserLocation()
  {
    return _browserLocation;
  }

  void setBrowserLocation( @Nonnull final String browserLocation )
  {
    _browserLocation = Objects.requireNonNull( browserLocation );
  }

  @Action
  void updateBrowserLocation( @Nonnull final Event event )
  {
    final String location = getHash();
    setBrowserLocation( location );
    if ( _targetLocation.equals( location ) )
    {
      setLocation( location );
    }
  }

  @Nonnull
  private String getHash()
  {
    final String hash = JsObjects.get( DomGlobal.window.location, "hash" );
    return null == hash ? "" : hash.substring( 1 );
  }

  private void setHash( @Nonnull final String hash )
  {
    if ( 0 == hash.length() )
    {
      /*
       * This code is needed to remove the stray #.
       * See https://stackoverflow.com/questions/1397329/how-to-remove-the-hash-from-window-location-url-with-javascript-without-page-r/5298684#5298684
       */
      DomGlobal.window.history.pushState( "",
                                          DomGlobal.document.title,
                                          JsObjects.<String>get( DomGlobal.window.location, "pathname" ) +
                                          JsObjects.<String>get( DomGlobal.window.location, "search" ) );
    }
    else
    {
      JsObjects.set( DomGlobal.window.location, "hash", hash );
    }
  }
}
