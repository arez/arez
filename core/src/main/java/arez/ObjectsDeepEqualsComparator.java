package arez;

import java.util.Objects;
import javax.annotation.Nullable;

/**
 * Equality comparator that delegates to {@link Objects#deepEquals(Object, Object)}.
 */
public final class ObjectsDeepEqualsComparator
  implements EqualityComparator
{
  /**
   * Shared reusable instance.
   */
  public static final ObjectsDeepEqualsComparator INSTANCE = new ObjectsDeepEqualsComparator();

  private ObjectsDeepEqualsComparator()
  {
  }

  @Override
  public boolean areEqual( @Nullable final Object oldValue, @Nullable final Object newValue )
  {
    return Objects.deepEquals( oldValue, newValue );
  }
}
