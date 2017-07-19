package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArezContext
{
  private static final Logger LOG = Logger.getLogger( ArezContext.class.getName() );
  /**
   * Currently running derivation
   */
  @Nullable
  IDerivation _trackingDerivation;

  /**
   * Are we running a computation currently? (not a reaction)
   */
  private int _computationDepth;

  /**
   * Each time a derivation is tracked, it is assigned a unique run-id
   */
  private int _runId;

  /**
   * Last ID allocated to an element within the system.
   */
  private int _sequenceID;

  /**
   * Are we in a batch block? (and how many of them)
   */
  private int _inBatch;

  /**
   * Observables that don't have observers anymore, and are about to be
   * suspended, unless somebody else accesses it in the same batch
   */
  private final ArrayList<IObservable> _pendingUnobservations = new ArrayList<>();

  /**
   * List of scheduled, not yet executed, reactions.
   */
  private final ArrayList<Reaction> _pendingReactions = new ArrayList<>();

  /**
   * Are we currently processing reactions?
   */
  private boolean _isRunningReactions;

  /**
   * Is it allowed to change observables at this point?
   * In general, MobX doesn't allow that when running computations and React.render.
   * To ensure that those functions stay pure.
   */
  private boolean _allowStateChanges = true;
  /**
   * If strict mode is enabled, state changes are by default not allowed
   */
  private boolean _strictMode;

  /**
   * Used by createTransformer to detect that the global state has been reset.
   */
  private int _resetId = 0;

  /**
   * Spy callbacks
   */
  //spyListeners: {(change: any): void}[] = [];

  /**
   * Globally attached error handlers that react specifically to errors in reactions
   */
  private ArrayList<IReactionErrorHandler> _globalReactionErrorHandlers = new ArrayList<>();

  public boolean isComputingDerivation()
  {
    return _trackingDerivation != null;
  }

  public int getNextId()
  {
    return ++_sequenceID;
  }

  public int getInBatch()
  {
    return _inBatch;
  }

  /**
   * Batch starts a transaction, at least for purposes of memoizing ComputedValues when nothing else does.
   * During a batch `onBecomeUnobserved` will be called at most once per observable.
   * Avoids unnecessary recalculations.
   */
  public void startBatch()
  {
    _inBatch++;
  }

  public void endBatch()
  {
    if ( --_inBatch == 0 )
    {
      runReactions();
      // the batch is actually about to finish, all unobserving should happen here.
      for ( final IObservable observable : _pendingUnobservations )
      {
        observable.resetPendingUnobservation();
        if ( !observable.hasObservers() )
        {
          observable.onBecomeUnobserved();
          // NOTE: onBecomeUnobserved might push to `pendingUnobservations`
        }
      }
      _pendingUnobservations.clear();
    }
  }

  @Nullable
  IDerivation untrackedStart()
  {
    final IDerivation current = _trackingDerivation;
    _trackingDerivation = null;
    return current;
  }

  void untrackedEnd( @Nullable final IDerivation derivation )
  {
    _trackingDerivation = derivation;
  }


  public void scheduleReaction( @Nonnull Reaction reaction )
  {
    _pendingReactions.add( Objects.requireNonNull( reaction ) );
    runReactions();
  }

  void addPendingUnobservations( @Nonnull final IObservable observable )
  {
    _pendingUnobservations.add( observable );
  }

  //let reactionScheduler: (fn: () => void) => void = f => f();

  private void runReactions()
  {
    // Trampolining, if runReactions are already running, new reactions will be picked up
    if ( _inBatch > 0 || _isRunningReactions )
    {
      return;
    }
    //reactionScheduler(runReactionsHelper);
  }


  private void runReactionsHelper()
  {
    _isRunningReactions = true;
  /*
  const allReactions = globalState.pendingReactions;
	let iterations = 0;

	// While running reactions, new reactions might be triggered.
	// Hence we work with two variables and check whether
	// we converge to no remaining reactions after a while.
	while (allReactions.length > 0) {
		if (++iterations === MAX_REACTION_ITERATIONS) {
			console.error(`Reaction doesn't converge to a stable state after ${MAX_REACTION_ITERATIONS} iterations.`
				+ ` Probably there is a cycle in the reactive function: ${allReactions[0]}`);
			allReactions.splice(0); // clear reactions
		}
		let remainingReactions = allReactions.splice(0);
		for (let i = 0, l = remainingReactions.length; i < l; i++)
			remainingReactions[i].runReaction();
	}
  */
    _isRunningReactions = false;
  }

  public void reportExceptionInReaction( @Nonnull final Throwable error, @Nonnull final IDerivation derivation )
  {
    LOG.warning( "Encountered an uncaught exception that was thrown by a reaction " +
                 "or observer component, in: '" + derivation + "'" );
    _globalReactionErrorHandlers.forEach( h -> h.onError( error, derivation ) );
  }
}
