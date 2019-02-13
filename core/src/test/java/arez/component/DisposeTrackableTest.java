package arez.component;

import arez.AbstractArezTest;
import arez.SafeProcedure;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DisposeTrackableTest
  extends AbstractArezTest
{
  static class BasicDisposeTrackable
    implements DisposeTrackable
  {
    @Override
    public void addOnDisposeListener( @Nonnull final Object key, @Nonnull final SafeProcedure action )
    {
    }

    @Override
    public void removeOnDisposeListener( @Nonnull final Object key )
    {
    }
  }

  @Test
  public void asDisposeTrackable()
  {
    final BasicDisposeTrackable trackable = new BasicDisposeTrackable();

    assertEquals( DisposeTrackable.asDisposeTrackable( trackable ), trackable );
  }

  @Test
  public void asDisposeTrackable_whenNotTrackable()
  {
    assertInvariantFailure( () -> DisposeTrackable.asDisposeTrackable( "XXXX" ),
                            "Arez-0178: Object passed to asDisposeTrackable does not implement DisposeTrackable. Object: XXXX" );
  }
}
