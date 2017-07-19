package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nonnull;

public final class FastList<E>
  implements List<E>
{
  private final ArrayList<E> _list = new ArrayList<>();
  private final HashMap<E, Integer> _indexes = new HashMap<>();

  @Override
  public int size()
  {
    return _list.size();
  }

  @Override
  public boolean isEmpty()
  {
    return _list.isEmpty();
  }

  @Override
  public boolean contains( final Object o )
  {
    return _list.contains( o );
  }

  @Override
  public Iterator<E> iterator()
  {
    return _list.iterator();
  }

  @Override
  public Object[] toArray()
  {
    return _list.toArray();
  }

  @SuppressWarnings( "SuspiciousToArrayCall" )
  @Override
  public <T> T[] toArray( @Nonnull final T[] a )
  {
    return _list.toArray( a );
  }

  @Override
  public boolean add( final E e )
  {
    return _list.add( e );
  }

  @Override
  public boolean remove( final Object o )
  {
    return _list.remove( o );
  }

  @Override
  public boolean containsAll( @Nonnull final Collection<?> c )
  {
    return _list.containsAll( c );
  }

  @Override
  public boolean addAll( @Nonnull final Collection<? extends E> c )
  {
    return _list.addAll( c );
  }

  @Override
  public boolean addAll( final int index, @Nonnull final Collection<? extends E> c )
  {
    return _list.addAll( index, c );
  }

  @Override
  public boolean removeAll( @Nonnull final Collection<?> c )
  {
    return _list.removeAll( c );
  }

  @Override
  public boolean retainAll( @Nonnull final Collection<?> c )
  {
    return _list.retainAll( c );
  }

  @Override
  public void clear()
  {
    _list.clear();
  }

  @Override
  public E get( final int index )
  {
    return _list.get( index );
  }

  @Override
  public E set( final int index, final E element )
  {
    return _list.set( index, element );
  }

  @Override
  public void add( final int index, final E element )
  {
    _list.add( index, element );
  }

  @Override
  public E remove( final int index )
  {
    return _list.remove( index );
  }

  @Override
  public int indexOf( final Object o )
  {
    return _list.indexOf( o );
  }

  @Override
  public int lastIndexOf( final Object o )
  {
    return _list.lastIndexOf( o );
  }

  @Override
  public ListIterator<E> listIterator()
  {
    return _list.listIterator();
  }

  @Override
  public ListIterator<E> listIterator( final int index )
  {
    return _list.listIterator();
  }

  @Override
  public List<E> subList( final int fromIndex, final int toIndex )
  {
    return _list.subList( fromIndex, toIndex );
  }
}
