import java.lang.reflect.UndeclaredThrowableException;
import java.text.ParseException;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_UnsafeSpecificProcedureActionModel
  extends UnsafeSpecificProcedureActionModel
{
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  public Arez_UnsafeSpecificProcedureActionModel( @Nonnull final ArezContext $$arez$$_context )
  {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
  }

  private String $$arez$$_id()
  {
    return "UnsafeSpecificProcedureActionModel." + $$arez$$_id + ".";
  }

  @Override
  public void doStuff( final long time )
    throws ParseException
  {
    try
    {
      this.$$arez$$_context.procedure( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "doStuff" : null,
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
