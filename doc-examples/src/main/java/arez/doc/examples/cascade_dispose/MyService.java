package arez.doc.examples.cascade_dispose;

import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.PreDispose;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class MyService
{
  @CascadeDispose
  @Nonnull
  final MySubService _subService = MySubService.create();

  @PreDispose
  void preDispose()
  {
    Disposable.dispose( _subService );
  }

  //DOC ELIDE START
  @Action
  void myAction()
  {
  }
  //DOC ELIDE END
}
