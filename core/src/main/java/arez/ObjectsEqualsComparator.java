package arez;

import grim.annotations.OmitClinit;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 * Equality comparator that delegates to {@link Objects#equals(Object, Object)}.
 */
@OmitClinit
public final class ObjectsEqualsComparator
  implements EqualityComparator
{
  // Class exists to avoid <clinit> on outer class
  public static final class Type
  {
    /**
     * Shared reusable instance.
     */
    public static final ObjectsEqualsComparator INSTANCE = new ObjectsEqualsComparator();

    private Type()
    {
    }
  }

  private ObjectsEqualsComparator()
  {
  }

  @Override
  public boolean areEqual( @Nullable final Object oldValue, @Nullable final Object newValue )
  {
    return Objects.equals( oldValue, newValue );
  }
}
