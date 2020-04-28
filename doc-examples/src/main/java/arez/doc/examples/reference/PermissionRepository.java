package arez.doc.examples.reference;

import arez.annotations.ArezComponent;
import arez.component.internal.AbstractRepository;
import javax.annotation.Nullable;

@ArezComponent
public abstract class PermissionRepository
  extends AbstractRepository<Integer, Permission, PermissionRepository>
{
  //DOC ELIDE START
  //DOC ELIDE END
  @Nullable
  public final Permission findById( final int id )
  {
    return findByArezId( id );
  }
  //DOC ELIDE START
  //DOC ELIDE END
}
