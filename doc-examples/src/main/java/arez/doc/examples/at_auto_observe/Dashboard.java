package arez.doc.examples.at_auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.LinkType;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ArezComponent
public abstract class Dashboard
{
  // The session should stay alive while the dashboard is alive.
  @AutoObserve
  @Nonnull
  final Session _session;
  //DOC ELIDE START

  Dashboard( @Nonnull final Session session )
  {
    _session = session;
  }

  //DOC ELIDE END

  // Retargeting the observable property automatically updates the auto-observer.
  @AutoObserve
  @Observable
  @Nullable
  public abstract Workspace getWorkspace();
  //DOC ELIDE START

  public abstract void setWorkspace( @Nullable final Workspace workspace );
  //DOC ELIDE END

  // Lazy references are force-resolved when auto-observed.
  @AutoObserve
  @Reference( load = LinkType.LAZY )
  @Nullable
  public abstract Workspace getPinnedWorkspace();

  @ReferenceId
  @Observable
  @Nullable
  public abstract Integer getPinnedWorkspaceId();
  //DOC ELIDE START

  public abstract void setPinnedWorkspaceId( @Nullable final Integer pinnedWorkspaceId );
  //DOC ELIDE END
}
