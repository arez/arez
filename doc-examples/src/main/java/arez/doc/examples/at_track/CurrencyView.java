package arez.doc.examples.at_track;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class CurrencyView
{
  // A read-only observer that renders
  @Observed( executor = Executor.APPLICATION )
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
    // Schedule this compon
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
