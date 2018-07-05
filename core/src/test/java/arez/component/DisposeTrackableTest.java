package arez.component;

import arez.AbstractArezTest;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DisposeTrackableTest
  extends AbstractArezTest
{
  static class BasicDisposeTrackable
    implements DisposeTrackable
  {
    private final DisposeNotifier _disposeNotifier = new DisposeNotifier();

    @Nonnull
    @Override
    public DisposeNotifier getNotifier()
    {
      return _disposeNotifier;
    }
  }

  @Test
  public void getNotifier()
  {
    final BasicDisposeTrackable trackable = new BasicDisposeTrackable();

    assertEquals( DisposeTrackable.getNotifier( trackable ), trackable.getNotifier() );
    assertEquals( DisposeTrackable.getNotifier( new Object() ), null );
  }

  @Test
  public void asDisposeTrackable()
  {
    final BasicDisposeTrackable trackable = new BasicDisposeTrackable();

    assertEquals( DisposeTrackable.asDisposeTrackable( trackable ), trackable );
    assertEquals( DisposeTrackable.getNotifier( new Object() ), null );
  }

  @Test
  public void asDisposeTrackable_whenNotTrackable()
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> DisposeTrackable.asDisposeTrackable( "XXXX" ) );
    assertEquals( exception.getMessage(),
                  "Arez-0178: Object passed to asDisposeTrackable does not implement DisposeTrackable. Object: XXXX" );
  }
}
