package arez.dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public final class TestEventTarget
  extends EventDrivenValue.EventTarget
{
  @Nonnull
  private final Map<String, ArrayList<EventDrivenValue.EventListener>> _listeners = new HashMap<>();

  @Override
  void addEventListener( @Nonnull final String type, @Nonnull final EventDrivenValue.EventListener listener )
  {
    getEventListenersByType( type ).add( listener );
  }

  @Override
  void removeEventListener( @Nonnull final String type, @Nonnull final EventDrivenValue.EventListener listener )
  {
    getEventListenersByType( type ).remove( listener );
  }

  @Nonnull
  List<EventDrivenValue.EventListener> getEventListenersByType( @Nonnull final String type )
  {
    return _listeners.computeIfAbsent( type, t -> new ArrayList<>() );
  }
}
