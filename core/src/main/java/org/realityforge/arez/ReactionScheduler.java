package org.realityforge.arez;

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
  static final int DEFAULT_MAX_REACTION_ROUNDS = 100;
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

  /**
   * Return the maximum number of rounds before runaway reaction is detected.
   *
   * @return the maximum number of rounds.
   */
  @Nonnegative
  int getMaxReactionRounds()
  {
    return _maxReactionRounds;
  }

  /**
   * Set the maximum number of rounds before a runaway reaction is detected.
   *
   * @param maxReactionRounds the maximum number of rounds.
   */
  void setMaxReactionRounds( @Nonnegative final int maxReactionRounds )
  {
    Guards.invariant( () -> maxReactionRounds >= 0,
                      () -> String.format( "Attempting to set maxReactionRounds to negative value %d.",
                                           maxReactionRounds ) );
    _maxReactionRounds = maxReactionRounds;
  }

  /**
   * Return true if we are currently running reactions.
   *
   * @return true if we are currently running reactions.
   */
  boolean isRunningReactions()
  {
    return 0 != _currentReactionRound;
  }

  /**
   * Add the specified observer to the list of pending observers.
   * The observer must have a reaction and must not already be in
   * the list of pending observers.
   *
   * @param observer the observer.
   */
  void scheduleReaction( @Nonnull final Observer observer )
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

  boolean isReactionsRunning()
  {
    return 0 != _currentReactionRound;
  }

  int runPendingObservers()
  {
    int observersScheduled = 0;
    while ( runObserver() )
    {
      observersScheduled++;
    }
    return observersScheduled;
  }

  boolean runObserver()
  {
    /*
     * All reactions expect to run as top level transactions so
     * there should be no transaciton active.
     */
    Guards.invariant( () -> !getContext().isTransactionActive(),
                      () -> String.format( "Invoked runObserver when transaction named '%s' is active.",
                                           getContext().getTransaction().getName() ) );
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
     * NOTE: The selection of the first observer ensures that the same observer is not
     * scheduled multiple times within a single round. This means that when runaway reaction
     * detection code is active, the list of pending observers contains those observers
     * that have likely lead to the runaway reaction.
     *
     * However this is very inefficient as it involves memory allocations and/or copies. We should
     * move to using a circular buffer that avoids both of these scenarios.
     */
    _remainingReactionsInCurrentRound--;
    final Observer observer = _pendingObservers.remove( 0 );
    observer.clearScheduledFlag();
    invokeObserver( observer );
    return true;
  }

  /**
   * Run a reaction for the supplied observer.
   * The reaction is executed in a transaction with the name and mode defined
   * by the observer. If the reaction throws an exception, the exception is reported
   * to the context global ObserverErrorHandlers
   *
   * @param observer the observer to run reaction for.
   */
  void invokeObserver( @Nonnull final Observer observer )
  {
    final String name = ArezConfig.enableNames() ? observer.getName() : null;
    final TransactionMode mode = observer.getMode();
    final Reaction reaction = observer.getReaction();
    assert null != reaction;
    try
    {
      getContext().transaction( name, mode, observer, () -> reaction.react( observer ) );
    }
    catch ( final Throwable t )
    {
      getContext().getObserverErrorHandler().onObserverError( observer, ObserverError.REACTION_ERROR, t );
    }
  }

  /**
   * Called when runaway reactions detected.
   * Depending on configuration will optionally purge the pending
   * observers and optionally fail an invariant check.
   */
  void onRunawayReactionsDetected()
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

  @Nonnull
  ArezContext getContext()
  {
    return _context;
  }

  @TestOnly
  @Nonnull
  ArrayList<Observer> getPendingObservers()
  {
    return _pendingObservers;
  }

  @TestOnly
  int getCurrentReactionRound()
  {
    return _currentReactionRound;
  }

  @TestOnly
  int getRemainingReactionsInCurrentRound()
  {
    return _remainingReactionsInCurrentRound;
  }
}
