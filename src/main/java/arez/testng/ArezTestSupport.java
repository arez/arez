package arez.testng;

import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Disposable;
import arez.Function;
import arez.Observer;
import arez.Procedure;
import arez.SafeFunction;
import arez.SafeProcedure;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public interface ArezTestSupport
  extends IHookable
{
  @BeforeMethod
  default void preTest()
    throws Exception
  {
    BrainCheckTestUtil.resetConfig( false );
    ArezTestUtil.resetConfig( false );
  }

  @AfterMethod
  default void postTest()
  {
    ArezTestUtil.resetConfig( true );
    BrainCheckTestUtil.resetConfig( true );
  }

  @Override
  default void run( final IHookCallBack callBack, final ITestResult testResult )
  {
    new ArezTestHook().run( callBack, testResult );
  }

  @Nonnull
  default ArezContext context()
  {
    return Arez.context();
  }

  @Nonnull
  default Disposable pauseScheduler()
  {
    return context().pauseScheduler();
  }

  default void observer( @Nonnull final Procedure procedure )
  {
    context().observer( procedure, Observer.Flags.AREZ_OR_NO_DEPENDENCIES );
  }

  default void action( @Nonnull final Procedure action )
    throws Throwable
  {
    context().action( action );
  }

  default <T> T action( @Nonnull final Function<T> action )
    throws Throwable
  {
    return context().action( action );
  }

  default void safeAction( @Nonnull final SafeProcedure action )
  {
    context().safeAction( action );
  }

  default <T> T safeAction( @Nonnull final SafeFunction<T> action )
  {
    return context().safeAction( action );
  }
}
