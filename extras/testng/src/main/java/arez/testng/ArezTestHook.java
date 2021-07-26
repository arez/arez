package arez.testng;

import arez.ActionFlags;
import arez.Arez;
import arez.ArezTestUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import static org.testng.Assert.*;

public final class ArezTestHook
  implements IHookable
{
  @Override
  public void run( final IHookCallBack icb, final ITestResult result )
  {
    final Method method = result.getMethod().getConstructorOrMethod().getMethod();
    final Object instance = result.getInstance();

    final boolean collectObserverErrors = null != method.getAnnotation( CollectObserverErrors.class );

    final ObserverErrorCollector collector;
    if ( collectObserverErrors )
    {
      linkObserverErrorCollectors( instance );
      collector = null;
    }
    else
    {
      collector = new ObserverErrorCollector( true );
      linkObserverErrorCollector( collector );
    }

    final ActionWrapper actionWrapper = getActionWrapperAnnotation( method );
    if ( null == actionWrapper || !actionWrapper.enable() )
    {
      icb.runTestMethod( result );
    }
    else
    {
      Arez.context().safeAction( result.getName(),
                                 () -> icb.runTestMethod( result ),
                                 ActionFlags.NO_VERIFY_ACTION_REQUIRED );
    }

    if ( null != collector )
    {
      final List<String> observerErrors = collector.getObserverErrors();
      if ( !observerErrors.isEmpty() )
      {
        fail( "Unexpected Observer Errors: " + String.join( "\n", observerErrors ) );
      }
    }
  }

  @Nullable
  private ActionWrapper getActionWrapperAnnotation( @Nonnull final Method method )
  {
    final ActionWrapper annotation = method.getAnnotation( ActionWrapper.class );
    if ( null != annotation )
    {
      return annotation;
    }
    else
    {
      return getActionWrapperAnnotation( method.getDeclaringClass() );
    }
  }

  @Nullable
  private ActionWrapper getActionWrapperAnnotation( @Nonnull final Class<?> type )
  {
    final ActionWrapper annotation = type.getAnnotation( ActionWrapper.class );
    if ( null != annotation )
    {
      return annotation;
    }
    else
    {
      final Class<?> superclass = type.getSuperclass();
      return null == superclass ? null : getActionWrapperAnnotation( superclass );
    }
  }

  private void linkObserverErrorCollectors( @Nonnull final Object instance )
  {
    for ( final Field field : instance.getClass().getDeclaredFields() )
    {
      if ( field.getType().equals( ObserverErrorCollector.class ) )
      {
        field.setAccessible( true );
        try
        {
          final ObserverErrorCollector collector = (ObserverErrorCollector) field.get( instance );
          collector.clear();
          linkObserverErrorCollector( collector );
        }
        catch ( final IllegalAccessException ignored )
        {
        }
      }
    }
  }

  private void linkObserverErrorCollector( @Nonnull final ObserverErrorCollector collector )
  {
    Arez.context().addObserverErrorHandler( collector::onObserverError );
  }
}
