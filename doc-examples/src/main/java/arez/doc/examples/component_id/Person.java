package arez.doc.examples.component_id;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent
public abstract class Person
{
  private final int _id;

  Person( final int id )
  {
    _id = id;
  }

  @ComponentId
  public int getId()
  {
    return _id;
  }

  //DOC ELIDE START
  @Action
  public void ignored()
  {
  }
  //DOC ELIDE END
}
