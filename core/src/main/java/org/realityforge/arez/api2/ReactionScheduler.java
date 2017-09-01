package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.TestOnly;

/**
 * The scheduler is responsible for scheduling observer reactions.
 *
 * <p>When state has changed or potentially changed, observers are en-queued onto {@link #_pendingObservers}.
 * Observers are processed in rounds. Each round involves processing the number of observers that are
 * pending at the start of the round. Processing observers can schedule more observers and thus another
 * round is scheduled if more observers are scheduled during the round.</p>
 *
 * If {@link #_maxReactionRounds} is not <tt>0</tt> and the number of rounds exceeds the value of
 * {@link #_maxReactionRounds} then it is assumed that we have a runaway observer that does not
 * terminate or stabilize and a failure is triggered.
 */
final class ReactionScheduler
{
  private static final int DEFAULT_MAX_REACTION_ROUNDS = 100;
  @Nonnull
  private final ArezContext _context;
  /**
   * Observers that have been scheduled but are not yet running.
   *
   * In future this should be a circular buffer.
   */
  @Nonnull
  private final ArrayList<Observer> _pendingObservers = new ArrayList<>();
  /**
   * The current reaction round.
   */
  @Nonnegative
  private int _currentReactionRound;
  /**
   * The number of reactions left in the current round.
   */
  @Nonnegative
  private int _remainingReactionsInCurrentRound;
  /**
   * The maximum number of iterations that can be triggered in sequence without triggering an error. Set this
   * to 0 to disable check, otherwise trigger
   */
  @Nonnegative
  private int _maxReactionRounds = DEFAULT_MAX_REACTION_ROUNDS;

  ReactionScheduler( @Nonnull final ArezContext context )
  {
    _context = Objects.requireNonNull( context );
  }

  @Nonnegative
  final int getMaxReactionRounds()
  {
    return _maxReactionRounds;
  }

  final void setMaxReactionRounds( @Nonnegative final int maxReactionRounds )
  {
    Guards.invariant( () -> maxReactionRounds >= 0,
                      () -> String.format( "Attempting to set maxReactionRounds to negative value %d.",
                                           maxReactionRounds ) );
    _maxReactionRounds = maxReactionRounds;
  }

  final boolean isRunningReactions()
  {
    return 0 != _currentReactionRound;
  }

  final void scheduleReaction( @Nonnull final Observer observer )
  {
    Guards.invariant( observer::hasReaction,
                      () -> String.format(
                        "Attempting to schedule observer named '%s' when observer has no reaction.",
                        observer.getName() ) );
    Guards.invariant( () -> !_pendingObservers.contains( observer ),
                      () -> String.format(
                        "Attempting to schedule observer named '%s' when observer is already pending.",
                        observer.getName() ) );
    _pendingObservers.add( Objects.requireNonNull( observer ) );
  }

  final void runPendingObservers()
  {
    // If we are already running observers, then newly scheduled observers will
    // be picked up by that process, otherwise lets start scheduler.
    if ( 0 == _currentReactionRound )
    {
      observerScheduler();
    }
  }

  private boolean runObserver()
  {
    final int pendingObserverCount = _pendingObservers.size();
    // If we have reached the last observer in this round then
    // determine if we need any more rounds and if we do ensure
    if ( 0 == _remainingReactionsInCurrentRound )
    {
      if ( 0 == pendingObserverCount )
      {
        _currentReactionRound = 0;
        return false;
      }
      else if ( _currentReactionRound + 1 > _maxReactionRounds )
      {
        _currentReactionRound = 0;
        onRunawayReactionsDetected();
        return false;
      }
      else
      {
        _currentReactionRound = _currentReactionRound + 1;
        _remainingReactionsInCurrentRound = pendingObserverCount;
      }
    }
    /*
     * If we get to here there are still observers that need processing and we have not
     * exceeded our round budget. So we pop the last observer off the list and process it.
     *
     * NOTE: The selection of the "last" observer is arbitrary and we could choose the first
     * or any other. However we select the last as it is the most efficient and does not
     * involve any memory allocations or copies. If we were using a circular buffer we could
     * easily have chosen the first. (This may be a better option as it means we could have a
     * lower value for _maxReactionRounds as processing would be width first rather than depth
     * first.)
     */
    _remainingReactionsInCurrentRound--;
    final Observer observer = _pendingObservers.remove( pendingObserverCount - 1 );
    invokeObserver( observer );
    return true;
  }

  private void invokeObserver( @Nonnull final Observer observer )
  {
    final String name = ArezConfig.enableNames() ? observer.getName() : null;
    final TransactionMode mode = observer.getMode();
    final Reaction reaction = observer.getReaction();
    try
    {
      //TODO: getContext().transaction( name, mode, observer, () -> reaction.react( observer ) );
    }
    catch ( final Throwable t )
    {
      getContext().getObserverErrorHandler().onObserverError( observer, ObserverError.REACTION_ERROR, t );
    }
  }

  private void onRunawayReactionsDetected()
  {
    final List<String> observerNames =
      ArezConfig.checkInvariants() && ArezConfig.verboseErrorMessages() ?
      _pendingObservers.stream().map( Node::getName ).collect( Collectors.toList() ) :
      null;

    if ( ArezConfig.purgeReactionsWhenRunawayDetected() )
    {
      _pendingObservers.clear();
    }

    Guards.fail( () ->
                   String.format(
                     "Runaway reaction(s) detected. Observers still running after %d rounds. Current observers include: %s",
                     _maxReactionRounds,
                     String.valueOf( observerNames ) ) );
  }

  final int observerScheduler()
  {
    int observersScheduled = 0;
    while ( runObserver() )
    {
      observersScheduled++;
    }
    return observersScheduled;
  }

  @Nonnull
  final ArezContext getContext()
  {
    return _context;
  }

  @TestOnly
  @Nonnull
  public ArrayList<Observer> getPendingObservers()
  {
    return _pendingObservers;
  }

  @TestOnly
  final int getCurrentReactionRound()
  {
    return _currentReactionRound;
  }

  @TestOnly
  final int getRemainingReactionsInCurrentRound()
  {
    return _remainingReactionsInCurrentRound;
  }
}
