package arez;

import java.util.Objects;
import javax.annotation.Nullable;

/**
 * Equality comparator that delegates to {@link Objects#equals(Object, Object)}.
 */
public final class ObjectsEqualsComparator
  implements EqualityComparator
{
  /**
   * Shared reusable instance.
   */
  public static final ObjectsEqualsComparator INSTANCE = new ObjectsEqualsComparator();

  private ObjectsEqualsComparator()
  {
  }

  @Override
  public boolean areEqual( @Nullable final Object oldValue, @Nullable final Object newValue )
  {
    return Objects.equals( oldValue, newValue );
  }
}
