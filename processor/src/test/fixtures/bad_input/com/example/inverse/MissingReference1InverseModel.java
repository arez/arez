package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class MissingReference1InverseModel
{
  @Inverse
  abstract @Nullable Collection<MyEntity> getMyEntity();

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( name = "NotMatch", inverseMultiplicity = Multiplicity.MANY )
    abstract @Nullable MissingReference1InverseModel getMissingReference1InverseModel();

    @ReferenceId( name = "NotMatch" )
    int getMissingReference1InverseModelId()
    {
      return 0;
    }
  }
}
