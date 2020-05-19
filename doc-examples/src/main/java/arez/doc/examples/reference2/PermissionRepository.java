package arez.doc.examples.reference2;

import arez.annotations.ArezComponent;
import arez.component.internal.AbstractRepository;
import arez.doc.examples.reference.Permission;
import javax.annotation.Nullable;

@ArezComponent
public abstract class PermissionRepository
  extends AbstractRepository<Integer, arez.doc.examples.reference.Permission, PermissionRepository>
{
  //DOC ELIDE START
  //DOC ELIDE END
  @Nullable
  public Permission findById( final int id )
  {
    return findByArezId( id );
  }
  //DOC ELIDE START
  //DOC ELIDE END
}
