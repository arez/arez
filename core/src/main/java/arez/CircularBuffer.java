package arez;

import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A circular buffer implementation.
 */
@SuppressWarnings( "Duplicates" )
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

  void addFirst( @Nonnull final T object )
  {
    tryGrowBeforeAdd();
    insertFirst( Objects.requireNonNull( object ) );
  }

  private void tryGrowBeforeAdd()
  {
    final int newSize = size() + 1;
    if ( newSize > _elements.length )
    {
      grow( newSize );
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
    if ( 0 == size() )
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

  void truncate( @Nonnegative final int size )
  {
    if ( _elements.length > size )
    {
      shrink( size );
    }
  }

  /**
   * Insert specified object at head of the buffer.
   *
   * @param object the object.
   */
  private void insertFirst( @Nonnull final T object )
  {
    if ( 0 == _head )
    {
      _head = _elements.length - 1;
      _isWrappedBuffer = true;
    }
    else
    {
      _head--;
    }
    _elements[ _head ] = object;
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

  /**
   * Increase the size of the buffer.
   */
  private void grow( @Nonnegative final int minCapacity )
  {
    int newSize = _elements.length;
    while ( newSize < minCapacity )
    {
      newSize = ( ( newSize - 1 ) * 2 ) + 1;
    }

    @SuppressWarnings( "unchecked" )
    final T[] elements = (T[]) new Object[ newSize ];

    int j = 0;
    final int size = size();
    for ( int i = 0; i < size; i++ )
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

  /**
   * Decrease the size of the buffer.
   */
  private void shrink( @Nonnegative final int maxCapacity )
  {
    @SuppressWarnings( "unchecked" )
    final T[] elements = (T[]) new Object[ maxCapacity ];

    int j = 0;
    final int size = Math.min( size(), maxCapacity );
    for ( int i = 0; i < size; i++ )
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
