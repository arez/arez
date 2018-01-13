package arez.doc.examples.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent
public class Person
{
  private final int _id;

  public Person( final int id )
  {
    _id = id;
  }

  @ComponentId
  public int getId()
  {
    return _id;
  }
  //DOC ELIDE START
  //DOC ELIDE END
}
