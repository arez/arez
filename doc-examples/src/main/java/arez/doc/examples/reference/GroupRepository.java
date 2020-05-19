package arez.doc.examples.reference;

import arez.annotations.ArezComponent;
import arez.component.internal.AbstractRepository;
import javax.annotation.Nullable;

@ArezComponent
public abstract class GroupRepository
  extends AbstractRepository<Integer, Group, GroupRepository>
{
  //DOC ELIDE START
  //DOC ELIDE END
  @Nullable
  public Group findById( final int id )
  {
    return findByArezId( id );
  }
  //DOC ELIDE START
  //DOC ELIDE END
}
