package arez;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.BrainCheckConfig;
import static org.realityforge.braincheck.Guards.*;

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
  @Nullable
  private final ArezContext _context;
  /**
   * Observers that have been scheduled but are not yet running.
   */
  @Nonnull
  private final CircularBuffer<Observer>[] _pendingObservers;
  /**
   * The current reaction round.
   */
  private int _currentReactionRound;
  /**
   * The number of reactions left in the current round.
   */
  private int _remainingReactionsInCurrentRound;
  /**
   * The maximum number of iterations that can be triggered in sequence without triggering an error. Set this
   * to 0 to disable check, otherwise trigger
   */
  private int _maxReactionRounds = DEFAULT_MAX_REACTION_ROUNDS;

  @SuppressWarnings( "unchecked" )
  ReactionScheduler( @Nullable final ArezContext context )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      apiInvariant( () -> Arez.areZonesEnabled() || null == context,
                    () -> "Arez-0164: ReactionScheduler passed a context but Arez.areZonesEnabled() is false" );
    }
    _context = Arez.areZonesEnabled() ? Objects.requireNonNull( context ) : null;
    _pendingObservers = (CircularBuffer<Observer>[]) new CircularBuffer[ 4 ];
    _pendingObservers[ 0 ] = new CircularBuffer<>( 100 );
    _pendingObservers[ 1 ] = new CircularBuffer<>( 100 );
    _pendingObservers[ 2 ] = new CircularBuffer<>( 100 );
    _pendingObservers[ 3 ] = new CircularBuffer<>( 100 );
  }

  /**
   * Return the maximum number of rounds before runaway reaction is detected.
   *
   * @return the maximum number of rounds.
   */
  int getMaxReactionRounds()
  {
    return _maxReactionRounds;
  }

  /**
   * Set the maximum number of rounds before a runaway reaction is detected.
   *
   * @param maxReactionRounds the maximum number of rounds.
   */
  void setMaxReactionRounds( final int maxReactionRounds )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> maxReactionRounds >= 0,
                 () -> "Arez-0098: Attempting to set maxReactionRounds to negative " +
                       "value " + maxReactionRounds + "." );
    }
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
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> !_pendingObservers[ 0 ].contains( observer ) &&
                       !_pendingObservers[ 1 ].contains( observer ) &&
                       !_pendingObservers[ 2 ].contains( observer ) &&
                       !_pendingObservers[ 3 ].contains( observer ),
                 () -> "Arez-0099: Attempting to schedule observer named '" + observer.getName() +
                       "' when observer is already pending." );
    }
    observer.setScheduledFlag();
    _pendingObservers[ observer.getPriority().ordinal() ].add( Objects.requireNonNull( observer ) );
  }

  /**
   * Return true if reactions are currently running, false otherwise.
   *
   * @return true if reactions are currently running, false otherwise.
   */
  boolean isReactionsRunning()
  {
    return 0 != _currentReactionRound;
  }

  /**
   * If the schedule is not already running pending tasks then run pending observers until
   * complete or runaway reaction detected.
   */
  void runPendingTasks()
  {
    // Each reaction creates a top level transaction that attempts to run call
    // this method when it completes. Rather than allow this if it is detected
    // that we are running reactions already then just abort and assume the top
    // most invocation of runPendingObservers will handle scheduling
    if ( !isReactionsRunning() )
    {
      //noinspection StatementWithEmptyBody
      while ( runObserver() )
      {
      }
    }
  }

  /**
   * Execute the next pending observer if any.
   * <ul>
   * <li>
   * If there is any reactions left in this round then run the next reaction and consume a token.
   * </li>
   * <li>
   * If there are more rounds left in budget and more pending observers then start a new round,
   * allocating a number of tokens equal to the number of pending reactions, run the next reaction
   * and consume a token.
   * </li>
   * <li>
   * Otherwise runaway reactions detected, so act appropriately. (In development this means
   * purging pending observers and failing an invariant check)
   * </li>
   * </ul>
   *
   * @return true if an observer was ran, false otherwise.
   */
  boolean runObserver()
  {
    /*
     * All reactions expect to run as top level transactions so
     * there should be no transaction active.
     */
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> !getContext().isTransactionActive(),
                 () -> "Arez-0100: Invoked runObserver when transaction named '" +
                       getContext().getTransaction().getName() + "' is active." );
    }
    final int highestPriorityCount = _pendingObservers[ 0 ].size();
    final int highPriorityCount = _pendingObservers[ 1 ].size();
    final int normalPriorityCount = _pendingObservers[ 2 ].size();
    final int lowPriorityCount = _pendingObservers[ 3 ].size();
    final int pendingObserverCount = highestPriorityCount + highPriorityCount + normalPriorityCount + lowPriorityCount;
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
     * exceeded our round budget. So we pop an observer off the list and process it.
     *
     * NOTE: The selection of the first observer ensures that the same observer is not
     * scheduled multiple times within a single round. This means that when runaway reaction
     * detection code is active, the list of pending observers contains those observers
     * that have likely lead to the runaway reaction. This of course assumes that the observers
     * were scheduled by appending to the buffer.
     */
    _remainingReactionsInCurrentRound--;

    /*
     * Get the highest priority buffer that has observers in it.
     */
    final CircularBuffer<Observer> buffer =
      highestPriorityCount > 0 ? _pendingObservers[ 0 ] :
      highPriorityCount > 0 ? _pendingObservers[ 1 ] :
      normalPriorityCount > 0 ? _pendingObservers[ 2 ] :
      _pendingObservers[ 3 ];

    final Observer observer = buffer.pop();
    assert null != observer;
    observer.clearScheduledFlag();
    observer.invokeReaction();
    return true;
  }

  /**
   * Called when runaway reactions detected.
   * Depending on configuration will optionally purge the pending
   * observers and optionally fail an invariant check.
   */
  void onRunawayReactionsDetected()
  {
    final List<String> observerNames =
      Arez.shouldCheckInvariants() && BrainCheckConfig.verboseErrorMessages() ?
      Stream.concat( Stream.concat( _pendingObservers[ 0 ].stream(), _pendingObservers[ 1 ].stream() ),
                     Stream.concat( _pendingObservers[ 2 ].stream(), _pendingObservers[ 3 ].stream() ) )
        .map( Node::getName ).collect( Collectors.toList() ) :
      null;

    if ( ArezConfig.purgeReactionsWhenRunawayDetected() )
    {
      _pendingObservers[ 0 ].clear();
      _pendingObservers[ 1 ].clear();
      _pendingObservers[ 2 ].clear();
      _pendingObservers[ 3 ].clear();
    }

    if ( Arez.shouldCheckInvariants() )
    {
      fail( () -> "Arez-0101: Runaway reaction(s) detected. Observers still running after " + _maxReactionRounds +
                  " rounds. Current observers include: " + observerNames );
    }
  }

  @Nonnull
  ArezContext getContext()
  {
    return Arez.areZonesEnabled() ? Objects.requireNonNull( _context ) : Arez.context();
  }

  @Nonnull
  CircularBuffer<Observer> getPendingObservers()
  {
    return getPendingObservers( Priority.NORMAL );
  }

  @Nonnull
  CircularBuffer<Observer> getPendingObservers( @Nonnull final Priority priority )
  {
    return _pendingObservers[ priority.ordinal() ];
  }

  int getCurrentReactionRound()
  {
    return _currentReactionRound;
  }

  int getRemainingReactionsInCurrentRound()
  {
    return _remainingReactionsInCurrentRound;
  }

  void setCurrentReactionRound( final int currentReactionRound )
  {
    _currentReactionRound = currentReactionRound;
  }
}
