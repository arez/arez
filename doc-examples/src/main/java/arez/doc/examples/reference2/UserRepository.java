package arez.doc.examples.reference2;

import arez.annotations.ArezComponent;
import arez.component.internal.AbstractRepository;
import arez.doc.examples.reference.User;
import javax.annotation.Nullable;

@ArezComponent
public abstract class UserRepository
  extends AbstractRepository<Integer, arez.doc.examples.reference.User, UserRepository>
{
  //DOC ELIDE START
  //DOC ELIDE END
  @Nullable
  public User findById( final int id )
  {
    return findByArezId( id );
  }
  //DOC ELIDE START
  //DOC ELIDE END
}
