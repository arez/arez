import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_ReadOnlyActionModel
  extends ReadOnlyActionModel
{
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  public Arez_ReadOnlyActionModel( @Nonnull final ArezContext $$arez$$_context )
  {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
  }

  private String $$arez$$_id()
  {
    return "ReadOnlyActionModel." + $$arez$$_id + ".";
  }

  @Override
  public int queryStuff( final long time )
  {
    return this.$$arez$$_context.safeFunction( this.$$arez$$_context.areNamesEnabled() ?
                                               $$arez$$_id() + "queryStuff" :
                                               null,
                                               false,
                                               () -> super.queryStuff( time ) );
  }
}
