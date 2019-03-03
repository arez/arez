package arez;

import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A circular buffer implementation.
 */
final class CircularBuffer<T>
{
  /**
   * The underlying object array.
   */
  private T[] _elements;
  /**
   * The pointer to first element.
   */
  private int _head;
  /**
   * The pointer to last element.
   */
  private int _tail;
  /**
   * Flag indicating whether buffer is wrapped around.
   */
  private boolean _isWrappedBuffer;

  /**
   * Create a buffer with specified initial capacity.
   *
   * @param initialCapacity the initial capacity of the buffer.
   */
  @SuppressWarnings( "unchecked" )
  CircularBuffer( final int initialCapacity )
  {
    assert initialCapacity > 0;
    _elements = (T[]) new Object[ initialCapacity ];
  }

  void clear()
  {
    _head = 0;
    _tail = 0;
    _isWrappedBuffer = false;
  }

  /**
   * Return the current capacity of the buffer.
   * The buffer may grow.
   *
   * @return the current capacity of the buffer.
   */
  int getCapacity()
  {
    return _elements.length;
  }

  int size()
  {
    if ( _isWrappedBuffer )
    {
      return _elements.length - _head + _tail;
    }
    else
    {
      return _tail - _head;
    }
  }

  void add( @Nonnull final T object )
  {
    tryGrowBeforeAdd();
    insertLast( Objects.requireNonNull( object ) );
  }

  private void tryGrowBeforeAdd()
  {
    final int currentSize = size();
    if ( currentSize + 1 > _elements.length )
    {
      final int newSize = ( ( _elements.length - 1 ) * 2 ) + 1;
      @SuppressWarnings( "unchecked" )
      final T[] elements = (T[]) new Object[ newSize ];
      int j = 0;
      for ( int i = 0; i < currentSize; i++ )
      {
        final int index = ( _head + i ) % _elements.length;
        elements[ j ] = _elements[ index ];
        _elements[ index ] = null;
        j++;
      }

      _elements = elements;
      _head = 0;
      _tail = j;
      _isWrappedBuffer = false;
    }
  }

  @Nullable
  T get( final int index )
  {
    if ( index >= size() )
    {
      return null;
    }
    else
    {
      final int offset = ( _head + index ) % _elements.length;
      return _elements[ offset ];
    }
  }

  @Nullable
  T pop()
  {
    if ( isEmpty() )
    {
      return null;
    }
    final T result = _elements[ _head ];
    _elements[ _head ] = null;

    _head++;
    if ( _head >= _elements.length )
    {
      _head = 0;
      _isWrappedBuffer = false;
    }

    return result;
  }

  boolean isEmpty()
  {
    return 0 == size();
  }

  /**
   * Insert specified object at tail of the buffer.
   *
   * @param object the object.
   */
  private void insertLast( @Nonnull final T object )
  {
    _elements[ _tail ] = object;
    _tail++;
    if ( _tail >= _elements.length )
    {
      _tail = 0;
      _isWrappedBuffer = true;
    }
  }

  /*
   * This method is very inefficient and should only be used in invariant checking code.
   */
  boolean contains( final T value )
  {
    final int size = size();
    for ( int i = 0; i < size; i++ )
    {
      if ( value == get( i ) )
      {
        return true;
      }
    }
    return false;
  }

  /*
   * This method is very inefficient and should only be used in invariant checking code.
   */
  Stream<T> stream()
  {
    assert Arez.shouldCheckInvariants() || Arez.shouldCheckApiInvariants();
    final int size = size();
    @SuppressWarnings( "unchecked" )
    final T[] elements = (T[]) new Object[ size ];
    for ( int i = 0; i < size; i++ )
    {
      elements[ i ] = get( i );
    }
    return Stream.of( elements );
  }
}
