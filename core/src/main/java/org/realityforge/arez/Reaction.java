package org.realityforge.arez;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Reactions are a special kind of derivations. Several things distinguishes them from normal reactive computations
 *
 * 1) They will always run, whether they are used by other computations or not.
 * This means that they are very suitable for triggering side effects like logging, updating the DOM and making network requests.
 * 2) They are not observable themselves
 * 3) They will always run after any 'normal' derivations
 * 4) They are allowed to change the state and thereby triggering themselves again, as long as they make sure the state propagates to a stable state in a reasonable amount of iterations.
 *
 * The state machine of a Reaction is as follows:
 *
 * 1) after creating, the reaction should be started by calling `runReaction` or by scheduling it (see also `autorun`)
 * 2) the `onInvalidate` handler should somehow result in a call to `this.track(someFunction)`
 * 3) all observables accessed in `someFunction` will be observed by this reaction.
 * 4) as soon as some of the dependencies has changed the Reaction will be rescheduled for another run (after the current mutation or transaction). `isScheduled` will yield true once a dependency is stale and during this period
 * 5) `onInvalidate` will be called, and we are back at step 1.
 */
public class Reaction
  extends IDerivation
  implements IReactionPublic
{
  private final String _mapid;
  // nodes we are looking at. Our value depends on these nodes
  private final ArrayList<IObservable> observing = new ArrayList<>();
  private final ArrayList<IObservable> newObserving = new ArrayList<>();
  private int _diffValue;
  private int _runId;
  private int _unboundDepsCount;
  private boolean _isDisposed;
  private boolean _isScheduled;
  private boolean _isTrackPending;
  private boolean _isRunning;
  @Nullable
  private IReactionErrorHandler _errorHandler;

  public Reaction( @Nonnull final ArezContext context, @Nonnull final String name )
  {
    super( context, name );
    _mapid = "#" + getContext().getNextId();
  }

  @Nonnull
  @Override
  public String getMapid()
  {
    return _mapid;
  }

  @Override
  public int getRunId()
  {
    return _runId;
  }

  @Override
  public int getUnboundDepsCount()
  {
    return _unboundDepsCount;
  }

  @Nullable
  @Override
  public ArrayList<IObservable> getNewObserving()
  {
    return newObserving;
  }

  public void onBecomeStale()
  {
    schedule();
  }

  public void schedule()
  {
    if ( !_isScheduled )
    {
      _isScheduled = true;
      getContext().scheduleReaction( this );
    }
  }

  public boolean isScheduled()
  {
    return _isScheduled;
  }

  public void reportExceptionInDerivation( @Nonnull final Throwable error )
  {
    if ( null != _errorHandler )
    {
      _errorHandler.onError( error, this );
    }
    else
    {
      getContext().reportExceptionInReaction( error, this );
    }
  }

  /**
   * internal, use schedule() if you intend to kick off a reaction
   */
  private void runReaction()
  {
    if ( !_isDisposed )
    {
      getContext().startBatch();
      _isScheduled = false;
      /*
      if ( shouldCompute() )
      {
        _isTrackPending = true;

        //TODO: this.onInvalidate();
      }
      */
      getContext().endBatch();
    }
  }

  @Override
  public void dispose()
  {
    if ( !_isDisposed )
    {
      _isDisposed = true;
      // if disposed while running, clean up later. Maybe not optimal, but rare case
      if ( !_isRunning )
      {
        getContext().startBatch();
        clearObserving();
        getContext().endBatch();
      }
    }
  }
}
