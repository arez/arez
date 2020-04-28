package arez.doc.examples.reference2;

import arez.annotations.ArezComponent;
import arez.component.internal.AbstractRepository;
import arez.doc.examples.reference.Group;
import javax.annotation.Nullable;

@ArezComponent
public abstract class GroupRepository
  extends AbstractRepository<Integer, arez.doc.examples.reference.Group, GroupRepository>
{
  //DOC ELIDE START
  //DOC ELIDE END
  @Nullable
  public final Group findById( final int id )
  {
    return findByArezId( id );
  }
  //DOC ELIDE START
  //DOC ELIDE END
}
