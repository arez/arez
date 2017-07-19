package org.realityforge.arez;

import java.util.ArrayList;
import javax.annotation.Nonnull;

public abstract class IObservable
  extends DepTreeNode
{
  private boolean _pendingUnobservation;

  public IObservable( @Nonnull final ArezContext context,
                      @Nonnull final String name )
  {
    super( context, name );
  }

  public abstract int getDiffValue();

  /**
   * Id of the derivation *run* that last accessed this observable.
   * If this id equals the *run* id of the current derivation,
   * the dependency is already established
   */
  public abstract int getLastAccessedBy();

  // Used to avoid redundant propagations
  public abstract IDerivationState getLowestObserverState();


  // Used so that observable can only be added to pendingUnobservations at most once per batch.
  public boolean isPendingUnobservation()
  {
    return _pendingUnobservation;
  }

  void resetPendingUnobservation()
  {
    _pendingUnobservation = false;
  }

  @Nonnull
  public abstract ArrayList<IDerivation> getObservers();

  public boolean hasObservers()
  {
    return getObservers().size() > 0;
  }

  public abstract void onBecomeUnobserved();

  public void removeObserver( @Nonnull final IDerivation node )
  {
    invariantInBatch( "IObservable.removeObserver" );
    final ArrayList<IDerivation> observers = getObservers();
    if ( observers.remove( node ) )
    {
      invariantFail( "Failed to remove specified node " + node );
    }
    if ( observers.isEmpty() )
    {
      queueForUnobservation();
    }
  }

  private void queueForUnobservation()
  {
    if ( !_pendingUnobservation )
    {
      _pendingUnobservation = true;
      getContext().addPendingUnobservations( this );
    }
  }
}
