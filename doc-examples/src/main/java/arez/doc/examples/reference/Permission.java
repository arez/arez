package arez.doc.examples.reference;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Objects;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class Permission
{
  //DOC ELIDE START
  private final int _id;
  private final int _groupId;
  @Nonnull
  private String _name;

  Permission( final int id, final int groupId, @Nonnull final String name )
  {
    _id = id;
    _groupId = groupId;
    _name = Objects.requireNonNull( name );
  }

  @ComponentId
  public int getId()
  {
    return _id;
  }

  @Observable
  @Nonnull
  public String getName()
  {
    return _name;
  }

  public void setName( @Nonnull final String name )
  {
    _name = name;
  }

  //DOC ELIDE END
  @Reference
  public abstract Group getGroup();

  @ReferenceId
  public int getGroupId()
  {
    return _groupId;
  }
}
