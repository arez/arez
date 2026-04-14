package arez.doc.examples.at_default_equality_comparator;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import javax.annotation.Nonnull;

@ArezComponent
abstract class MyModel
{
  @DefaultEqualityComparator( TagComparator.class )
  static final class Tag
  {
    @Nonnull
    private final String _value;

    Tag( @Nonnull final String value )
    {
      _value = value;
    }

    @Nonnull
    @Override
    public String toString()
    {
      return _value;
    }
  }

  static final class TagComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      return null == oldValue || null == newValue ?
             oldValue == newValue :
             oldValue.toString().equalsIgnoreCase( newValue.toString() );
    }
  }

  @Nonnull
  private Tag _searchTerm = new Tag( "" );

  @Observable( setterAlwaysMutates = false )
  @Nonnull
  Tag getSearchTerm()
  {
    return _searchTerm;
  }

  void setSearchTerm( @Nonnull final Tag searchTerm )
  {
    _searchTerm = searchTerm;
  }

  @Memoize
  @Nonnull
  Tag getNormalizedSearchTerm()
  {
    return new Tag( getSearchTerm().toString().trim() );
  }
}
