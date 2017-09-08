import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Observable;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_OverrideNamesInModel
  extends OverrideNamesInModel
{
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_myField;

  public Arez_OverrideNamesInModel( @Nonnull final ArezContext $$arez$$_context )
  {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_myField =
      $$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "myField" : null );
  }

  private String $$arez$$_id()
  {
    return "MyContainer." + $$arez$$_id + ".";
  }

  @Override
  public long getTime()
  {
    this.$$arez$$_myField.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime( final long time )
  {
    if ( time != super.getTime() )
    {
      super.setTime( time );
      this.$$arez$$_myField.reportObserved();
    }
  }
}
