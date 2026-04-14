package arez;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A utility class to make it easy to do fast existence checks when there are large lists.
 *
 * @param <T> The element type
 */
final class FastList<T>
{
  @Nonnull
  private final List<T> _values = new ArrayList<>();
  @Nonnull
  private final Set<T> _set = new HashSet<>();

  void forEach( @Nonnull Consumer<? super T> action )
  {
    _values.forEach( v -> {
      if ( null != v )
      {
        action.accept( v );
      }
    } );
  }

  int size()
  {
    return _values.size();
  }

  boolean isEmpty()
  {
    return 0 == size();
  }

  @Nonnull
  Stream<T> stream()
  {
    return _values.stream().filter( Objects::nonNull );
  }

  void clear()
  {
    _values.clear();
    _set.clear();
  }

  @SuppressWarnings( "SuspiciousMethodCalls" )
  boolean contains( @Nonnull final Object o )
  {
    return _set.contains( o );
  }

  @Nullable
  T get( final int index )
  {
    return _values.get( index );
  }

  void set( final int index, @Nonnull final T element )
  {
    if ( _set.contains( element ) )
    {
      _values.remove( element );
    }
    _values.set( index, element );
    _set.add( element );
  }

  void remove( final int index )
  {
    final T element = _values.remove( index );
    if ( null != element )
    {
      _set.remove( element );
    }
  }

  void remove( @Nonnull final T element )
  {
    if ( _set.remove( element ) )
    {
      _values.remove( element );
    }
  }

  void add( @Nonnull final T element )
  {
    if ( !_set.contains( element ) )
    {
      _values.add( element );
      _set.add( element );
    }
  }
}
