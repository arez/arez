package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * The reaction scheduler is responsible for scheduling reactions.
 *
 * <p>When state has changed or potentially changed, reactions are en-queued onto {@link #_pendingReactions}.
 * Reactions are processed in rounds. Each round involves processing the number of reactions that are
 * pending at the start of the round. Processing reactions can produce more reactions and thus another
 * round is scheduled if there is more reactions generated.</p>
 *
 * If {@link #_maxReactionRounds} is not <tt>0</tt> and the number of rounds exceeds the value of
 * {@link #_maxReactionRounds} then it is assumed that we have a runaway reaction that does not
 * terminate or stabilize and a failure is triggered.
 */
final class ReactionScheduler
{
  private static final int DEFAULT_MAX_REACTION_ROUNDS = 100;
  @Nonnull
  private final ArezContext _context;
  /**
   * Reactions that have been scheduled but are not yet running.
   *
   * In future this should be a circular buffer.
   */
  @Nonnull
  private final ArrayList<Reaction> _pendingReactions = new ArrayList<>();
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

  final void scheduleReaction( @Nonnull final Reaction reaction )
  {
    Guards.invariant( () -> !_pendingReactions.contains( reaction ),
                      () -> String.format(
                        "Attempting to schedule reaction named '%s' when reaction is already pending.",
                        reaction.getName() ) );
    _pendingReactions.add( Objects.requireNonNull( reaction ) );
    runPendingReactions();
  }

  final void runPendingReactions()
  {
    // If we are already running reactions, then new reactions will
    // already be picked up. Otherwise lets start scheduler.
    if ( 0 == _currentReactionRound )
    {
      reactionScheduler();
    }
  }

  private boolean runReaction()
  {
    final int pendingReactionCount = _pendingReactions.size();
    // If we have reached the last reaction in this round then
    // determine if we need any more rounds and if we do ensure
    if ( 0 == _remainingReactionsInCurrentRound )
    {
      if ( 0 == pendingReactionCount )
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
        _remainingReactionsInCurrentRound = pendingReactionCount;
      }
    }
    /*
     * If we get to here there are still reactions that need processing and we have not
     * exceeded our round budget. So we pop the last reaction off the list and process it.
     *
     * NOTE: The selection of the "last" reaction is arbitrary and we could choose the first
     * or any other. However we select the last as it is the most efficient and does not
     * involve any memory allocations or copies. If we were using a circular buffer we could
     * easily have chosen the first. (This may be a better option as it means we could have a
     * lower value for _maxReactionRounds as processing would be width first rather than depth
     * first.)
     */
    _remainingReactionsInCurrentRound--;
    final Reaction reaction = _pendingReactions.remove( pendingReactionCount - 1 );
    invokeReaction( reaction );
    return true;
  }

  private void invokeReaction( @Nonnull final Reaction reaction )
  {
    final String name = ArezConfig.enableNames() ? reaction.getName() : null;
    final TransactionMode mode = reaction.getMode();
    final Action action = reaction.getAction();
    try
    {
      getContext().transaction( name, mode, reaction, action );
    }
    catch ( final Throwable t )
    {
      getContext().getObserverErrorHandler().onObserverError( reaction, ObserverError.REACTION_ERROR, t );
    }
  }

  private void onRunawayReactionsDetected()
  {
    final List<String> reactionNames =
      ArezConfig.checkInvariants() && ArezConfig.verboseErrorMessages() ?
      _pendingReactions.stream().map( Node::getName ).collect( Collectors.toList() ) :
      null;

    if ( ArezConfig.purgeReactionsWhenRunawayDetected() )
    {
      _pendingReactions.clear();
    }

    Guards.fail( () ->
                   String.format(
                     "Runaway reaction(s) detected. Reactions still running after %d rounds. Current reactions include: %s",
                     _maxReactionRounds,
                     String.valueOf( reactionNames ) ) );
  }

  final int reactionScheduler()
  {
    int reactionsScheduled = 0;
    while ( runReaction() )
    {
      reactionsScheduled++;
    }
    return reactionsScheduled;
  }

  @Nonnull
  final ArezContext getContext()
  {
    return _context;
  }
}
