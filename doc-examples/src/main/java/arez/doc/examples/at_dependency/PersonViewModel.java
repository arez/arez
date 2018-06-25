package arez.doc.examples.at_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ArezComponent
public abstract class PersonViewModel
{
  @Nonnull
  private final Person _person;
  //DOC ELIDE START

  public PersonViewModel( @Nonnull final Person person )
  {
    _person = person;
  }

  //DOC ELIDE END

  // Let imagine there is a lot more logic and state on the view model
  // to justify it's existence rather than just having view layer directly
  // accessing underlying entities

  @Dependency
  @Nonnull
  public final Person getPerson()
  {
    // This reference is immutable and the network replication
    // layer is responsible for managing the lifecycle of person
    // component and may dispose it when the Person entity is deleted
    // on the server which should trigger this view model being disposed.
    return _person;
  }

  /**
   * The Job entity is likewise controlled by the server
   * and can be updated, removed on the server and replicated to the web
   * browser. In this scenario the current job is just removed from the
   * person view model.
   */
  @Dependency( action = Dependency.Action.SET_NULL )
  @Observable
  @Nullable
  public abstract Job getCurrentJob();
  //DOC ELIDE START

  public abstract void setCurrentJob( @Nullable final Job currentJob );
  //DOC ELIDE END
}
