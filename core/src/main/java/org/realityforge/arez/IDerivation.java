package org.realityforge.arez;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A derivation is everything that can be derived from the state (all the atoms) in a pure manner.
 * See https://medium.com/@mweststrate/becoming-fully-reactive-an-in-depth-explanation-of-mobservable-55995262a254#.xvbh6qd74
 */
public abstract class IDerivation
  extends DepTreeNode
{
	@Nonnull
  private IDerivationState _dependenciesState = IDerivationState.NOT_TRACKING;

	IDerivation( @Nonnull final ArezContext context, @Nonnull final String name )
	{
		super( context, name );
	}

	@Nullable
  public abstract ArrayList<IObservable> getNewObserving();

  @Nonnull
  public IDerivationState getDependenciesState()
  {
    return _dependenciesState;
  }

  /**
   * Id of the current run of a derivation. Each time the derivation is tracked
   * this number is increased by one. This number is globally unique
   */
  public abstract int getRunId();

  /**
   * amount of dependencies used by the derivation in this run, which has not been bound yet.
   */
  public abstract int getUnboundDepsCount();

  @Nonnull
  public abstract String getMapid();

  public abstract void onBecomeStale();

	void clearObserving()
	{
		invariantInBatch( "IDerivation.clearObserving" );

		//TODO: Avoid copying the array here and figure out a more efficient way to swap array in place
		final ArrayList<IObservable> observing = new ArrayList<>( getObserving() );
		getObserving().clear();
		for ( int i = observing.size() - 1; i >= 0; i-- )
		{
			final IObservable observable = observing.get( i );
			observable.removeObserver( this );
		}

		_dependenciesState = IDerivationState.NOT_TRACKING;
	}


/**
 * Finds out whether any dependency of the derivation has actually changed.
 * If dependenciesState is 1 then it will recalculate dependencies,
 * if any dependency changed it will propagate it by changing dependenciesState to 2.
 *
 * By iterating over the dependencies in the same order that they were reported and
 * stopping on the first change, all the recalculations are only called for ComputedValues
 * that will be tracked by derivation. That is because we assume that if the first x
 * dependencies of the derivation doesn't change then the derivation should run the same way
 * up until accessing x-th dependency.
 */
/*boolean shouldCompute() {
	switch (_dependenciesState) {
		case UP_TO_DATE: return false;
		case NOT_TRACKING:
			case STALE:
				return true;
		case POSSIBLY_STALE: {
			// no need for those computeds to be reported, they will be picked up in trackDerivedFunction.
			final IDerivation prevUntracked = getContext().untrackedStart();
			const obs = this.observing, l = obs.length;
			for (let i = 0; i < l; i++) {
				const obj = obs[i];
				if (isComputedValue(obj)) {
					try {
						obj.get();
					} catch (e) {
						// we are not interested in the value *or* exception at this moment, but if there is one, notify all
						getContext().untrackedEnd(prevUntracked);
						return true;
					}
					// if ComputedValue `obj` actually changed it will be computed and propagated to its observers.
					// and `derivation` is an observer of `obj`
					if ((derivation as any).dependenciesState === IDerivationState.STALE) {
						getContext().untrackedEnd(prevUntracked);
						return true;
					}
				}
			}
			changeDependenciesStateTo0(derivation);
			getContext().untrackedEnd(prevUntracked);
			return false;
		}
	}
}*/
}
