package arez.component;

import arez.AbstractTest;
import arez.SafeProcedure;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class DisposeNotifierTest
  extends AbstractTest
{
  static class BasicDisposeNotifier
    implements DisposeNotifier
  {
    @Override
    public void addOnDisposeListener( @Nonnull final Object key,
                                      @Nonnull final SafeProcedure action,
                                      final boolean errorIfDuplicate )
    {
    }

    @Override
    public void removeOnDisposeListener( @Nonnull final Object key, final boolean errorIfMissing )
    {
    }
  }

  @Test
  public void asDisposeNotifier()
  {
    final BasicDisposeNotifier notifier = new BasicDisposeNotifier();

    assertEquals( DisposeNotifier.asDisposeNotifier( notifier ), notifier );
  }

  @Test
  public void asDisposeNotifier_whenNotTrackable()
  {
    assertInvariantFailure( () -> DisposeNotifier.asDisposeNotifier( "XXXX" ),
                            "Arez-0178: Object passed to asDisposeNotifier does not implement DisposeNotifier. Object: XXXX" );
  }
}
