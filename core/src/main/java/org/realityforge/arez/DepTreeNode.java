package org.realityforge.arez;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.Contract;

public abstract class DepTreeNode
{
  @Nonnull
  private final ArezContext _context;
  @Nonnull
  private final String _name;
  private ArrayList<IObservable> _observing = new ArrayList<>();
  private Map<IObservable, Integer> _observingIndexes = new HashMap<>();

  public DepTreeNode( @Nonnull final ArezContext context, @Nonnull final String name )
  {
    _context = Objects.requireNonNull( context );
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  public String getName()
  {
    return _name;
  }

  @Nonnull
  protected final ArrayList<IObservable> getObserving()
  {
    return _observing;
  }

  @Override
  public String toString()
  {
    return getName();
  }

  @Nonnull
  protected final ArezContext getContext()
  {
    return _context;
  }

  protected final void invariant( @Nonnull final Supplier<Boolean> check, @Nonnull final String message )
  {
    if ( ArezConfig.CHECK_INVARIANTS && !check.get() )
    {
      invariantFail( message );
    }
  }

  protected final void invariantInBatch( @Nonnull final String methodContext )
  {
    invariant( () -> getContext().getInBatch() > 0,
               methodContext + " should only occur inside batch" );
  }

  @Contract( "_ -> fail" )
  protected final void invariantFail( @Nonnull final String message )
  {
    if ( ArezConfig.CHECK_INVARIANTS )
    {
      throw new IllegalStateException( message );
    }
  }
}
