package org.realityforge.arez;

public class ArezContext
{
  private static final ArezContext c_instance = new ArezContext();

	/**
	 * Currently running derivation
	 */
	//trackingDerivation: IDerivation | null = null;

	/**
	 * Are we running a computation currently? (not a reaction)
	 */
	private int _computationDepth;

	/**
	 * Each time a derivation is tracked, it is assigned a unique run-id
	 */
	private int _runId;

	/**
	 * Are we in a batch block? (and how many of them)
	 */
	private int _inBatch;

	/**
	 * Observables that don't have observers anymore, and are about to be
	 * suspended, unless somebody else accesses it in the same batch
	 *
	 * @type {IObservable[]}
	 */
	//pendingUnobservations: IObservable[] = [];

	/**
	 * List of scheduled, not yet executed, reactions.
	 */
	//pendingReactions: Reaction[] = [];

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
	//globalReactionErrorHandlers: ((error: any, derivation: IDerivation) => void)[] = [];


  public static ArezContext getInstance()
  {
    return c_instance;
  }
}
