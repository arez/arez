package arez;

import grim.annotations.OmitClinit;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 * Equality comparator that delegates to {@link Objects#deepEquals(Object, Object)}.
 */
@OmitClinit
public final class ObjectsDeepEqualsComparator
  implements EqualityComparator
{
  @Override
  public boolean areEqual( @Nullable final Object oldValue, @Nullable final Object newValue )
  {
    return Objects.deepEquals( oldValue, newValue );
  }
}
