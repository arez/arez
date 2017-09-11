import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_BasicModelWithDifferentAccessLevels
  extends BasicModelWithDifferentAccessLevels
  implements Disposable
{
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  @Nonnull
  private final Observable $$arez$$_value;

  Arez_BasicModelWithDifferentAccessLevels( @Nonnull final ArezContext $$arez$$_context )
  {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time =
      this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null );
    this.$$arez$$_value = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ?
                                                                  $$arez$$_id() + "value" :
                                                                  null );
  }

  protected Arez_BasicModelWithDifferentAccessLevels( @Nonnull final ArezContext $$arez$$_context, final String value )
  {
    super( value );
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time =
      this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null );
    this.$$arez$$_value = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ?
                                                                  $$arez$$_id() + "value" :
                                                                  null );
  }

  public Arez_BasicModelWithDifferentAccessLevels( @Nonnull final ArezContext $$arez$$_context,
                                                   final String value,
                                                   final long time )
  {
    super( value, time );
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time =
      this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null );
    this.$$arez$$_value = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ?
                                                                  $$arez$$_id() + "value" :
                                                                  null );
  }

  private String $$arez$$_id()
  {
    return "BasicModelWithDifferentAccessLevels." + $$arez$$_id + ".";
  }

  @Override
  public void dispose()
  {
    $$arez$$_time.dispose();
    $$arez$$_value.dispose();
  }

  @Override
  protected long getTime()
  {
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime( final long time )
  {
    if ( time != super.getTime() )
    {
      super.setTime( time );
      this.$$arez$$_time.reportChanged();
    }
  }

  @Override
  String getValue()
  {
    this.$$arez$$_value.reportObserved();
    return super.getValue();
  }

  @Override
  public void setValue( final String value )
  {
    if ( !Objects.equals( value, super.getValue() ) )
    {
      super.setValue( value );
      this.$$arez$$_value.reportChanged();
    }
  }

  @Override
  void doAction3()
  {
    this.$$arez$$_context.safeProcedure( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "doAction3" : null,
                                         true,
                                         () -> super.doAction3() );
  }

  @Override
  protected void doAction2()
  {
    this.$$arez$$_context.safeProcedure( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "doAction2" : null,
                                         true,
                                         () -> super.doAction2() );
  }

  @Override
  public void doAction()
  {
    this.$$arez$$_context.safeProcedure( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "doAction" : null,
                                         true,
                                         () -> super.doAction() );
  }
}
