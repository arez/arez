package arez.dom.util;

import akasha.AddEventListenerOptions;
import akasha.Event;
import akasha.EventListener;
import akasha.EventListenerOptions;
import akasha.EventTarget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class TestEventTarget
  extends EventTarget
{
  @Nonnull
  private final Map<String, ArrayList<EventListener>> _listeners = new HashMap<>();

  @Override
  public void addEventListener( @Nonnull final String type,
                                @Nullable final EventListener callback,
                                @Nonnull final AddEventListenerOptions options )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addEventListener( @Nonnull final String type, @Nullable final EventListener listener )
  {
    getEventListenersByType( type ).add( listener );
  }

  @Override
  public boolean dispatchEvent( @Nonnull final Event evt )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeEventListener( @Nonnull final String type,
                                   @Nullable final EventListener callback,
                                   @Nonnull final EventListenerOptions options )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeEventListener( @Nonnull final String type, @Nullable final EventListener listener )
  {
    getEventListenersByType( type ).remove( listener );
  }

  @Nonnull
  public List<EventListener> getEventListenersByType( @Nonnull final String type )
  {
    return _listeners.computeIfAbsent( type, t -> new ArrayList<>() );
  }
}
