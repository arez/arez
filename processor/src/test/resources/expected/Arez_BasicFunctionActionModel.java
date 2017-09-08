import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_BasicFunctionActionModel
  extends BasicFunctionActionModel
{
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  public Arez_BasicFunctionActionModel( @Nonnull final ArezContext $$arez$$_context )
  {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
  }

  private String $$arez$$_id()
  {
    return "BasicFunctionActionModel." + $$arez$$_id + ".";
  }

  @Override
  public int doStuff( final long time )
  {
    return this.$$arez$$_context.safeFunction( this.$$arez$$_context.areNamesEnabled() ?
                                               $$arez$$_id() + "doStuff" :
                                               null,
                                               true,
                                               () -> super.doStuff( time ) );
  }
}
