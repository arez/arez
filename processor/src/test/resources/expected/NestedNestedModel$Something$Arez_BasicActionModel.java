import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class NestedNestedModel$Something$Arez_BasicActionModel
  extends NestedNestedModel.Something.BasicActionModel
{
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  public NestedNestedModel$Something$Arez_BasicActionModel( @Nonnull final ArezContext $$arez$$_context )
  {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
  }

  private String $$arez$$_id()
  {
    return "BasicActionModel." + $$arez$$_id + ".";
  }

  @Override
  public void doStuff( final long time )
  {
    this.$$arez$$_context.safeProcedure( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "doStuff" : null,
                                         true,
                                         () -> super.doStuff( time ) );
  }
}
