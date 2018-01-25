package arez.doc.examples.at_track;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public abstract class CurrencyView
{
  // A read-only observer that renders
  @Track
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
