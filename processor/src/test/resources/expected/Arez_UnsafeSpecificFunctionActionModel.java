import java.lang.reflect.UndeclaredThrowableException;
import java.text.ParseException;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_UnsafeSpecificFunctionActionModel
  extends UnsafeSpecificFunctionActionModel
{
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  public Arez_UnsafeSpecificFunctionActionModel( @Nonnull final ArezContext $$arez$$_context )
  {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
  }

  private String $$arez$$_id()
  {
    return "UnsafeSpecificFunctionActionModel." + $$arez$$_id + ".";
  }

  @Override
  public int doStuff( final long time )
    throws ParseException
  {
    try
    {
      return this.$$arez$$_context.function( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "doStuff" : null,
                                             true,
                                             () -> super.doStuff( time ) );
    }
    catch ( final ParseException e )
    {
      throw e;
    }
    catch ( final RuntimeException e )
    {
      throw e;
    }
    catch ( final Exception e )
    {
      throw new UndeclaredThrowableException( e );
    }
  }
}
