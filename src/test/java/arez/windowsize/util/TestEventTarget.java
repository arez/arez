package arez.windowsize.util;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.EventTarget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class TestEventTarget
  implements EventTarget
{
  private final Map<String, ArrayList<EventListener>> _listeners = new HashMap<>();

  @Override
  public void addEventListener( final String type,
                                final EventListener listener,
                                final AddEventListenerOptionsUnionType options )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addEventListener( final String type, final EventListener listener )
  {
    getEventListenersByType( type ).add( listener );
  }

  @Override
  public boolean dispatchEvent( final Event evt )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeEventListener( final String type,
                                   final EventListener listener,
                                   final RemoveEventListenerOptionsUnionType options )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeEventListener( final String type, final EventListener listener )
  {
    getEventListenersByType( type ).remove( listener );
  }

  public ArrayList<EventListener> getEventListenersByType( final String type )
  {
    return _listeners.computeIfAbsent( type, t -> new ArrayList<>() );
  }
}
