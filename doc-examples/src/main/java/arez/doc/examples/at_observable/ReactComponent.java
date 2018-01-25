package arez.doc.examples.at_observable;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ReactComponent
{
  @Observable( expectSetter = false )
  protected Props props()
  {
    //Return the native props object here.
    //DOC ELIDE START
    return null;
    //DOC ELIDE END
  }

  //This will be overridden and implemented in the Arez subclass
  @ObservableRef
  abstract arez.Observable<Props> getPropsObservable();

  // This method is wrapped in an Action to ensure change is propagated
  // correctly in arez system.
  @Action
  protected void reportPropsChanged()
  {
    getPropsObservable().reportChanged();
  }

  // This method is invoked by the React runtime
  protected void componentWillReceiveProps( @Nonnull final Props nextProps )
  {
    reportPropsChanged();
    //DOC ELIDE START
    //DOC ELIDE END
  }

  //DOC ELIDE START
  static class Props
  {
  }
  //DOC ELIDE END
}
