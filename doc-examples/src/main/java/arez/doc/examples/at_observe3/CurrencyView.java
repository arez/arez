package arez.doc.examples.at_observe3;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class CurrencyView
{
  // A read-only observer that renders
  @Observe( executor = Executor.APPLICATION )
  public ReactNode render()
  {
    //Render component here
    //DOC ELIDE START
    return null;
    //DOC ELIDE END
  }

  @OnDepsChanged
  void onRenderDepsChanged()
  {
    // Schedule this component
    scheduleRender();
  }

  //DOC ELIDE START
  static class ReactNode
  {
  }

  private void scheduleRender()
  {
  }
  //DOC ELIDE END
}
