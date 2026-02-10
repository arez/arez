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
  @Override
  public boolean areEqual( @Nullable final Object oldValue, @Nullable final Object newValue )
  {
    return Objects.equals( oldValue, newValue );
  }
}
