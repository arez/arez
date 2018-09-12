package arez.doc.examples.cascade_dispose2;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
public abstract class MyService
{
  @CascadeDispose
  final MySubService _subService = MySubService.create();

  //DOC ELIDE START
  @Action
  void myAction()
  {
  }
  //DOC ELIDE END
}
