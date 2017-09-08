import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_UnsafeProcedureActionModel
  extends UnsafeProcedureActionModel
{
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  public Arez_UnsafeProcedureActionModel( @Nonnull final ArezContext $$arez$$_context )
  {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
  }

  private String $$arez$$_id()
  {
    return "UnsafeProcedureActionModel." + $$arez$$_id + ".";
  }

  @Override
  public void doStuff( final long time )
    throws Exception
  {
    $$arez$$_context.procedure( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "doStuff" : null,
                                true,
                                () -> super.doStuff( time ) );
  }
}
