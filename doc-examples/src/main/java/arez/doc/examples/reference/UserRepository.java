package arez.doc.examples.reference;

import arez.annotations.ArezComponent;
import arez.component.internal.AbstractRepository;
import javax.annotation.Nullable;

@ArezComponent
public abstract class UserRepository
  extends AbstractRepository<Integer, User, UserRepository>
{
  //DOC ELIDE START
  //DOC ELIDE END
  @Nullable
  public final User findById( final int id )
  {
    return findByArezId( id );
  }
  //DOC ELIDE START
  //DOC ELIDE END
}
