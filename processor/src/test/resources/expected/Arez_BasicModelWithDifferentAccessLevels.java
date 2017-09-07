import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Observable;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_BasicModelWithDifferentAccessLevels
  extends BasicModelWithDifferentAccessLevels
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
      $$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null );
    this.$$arez$$_value =
      $$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "value" : null );
  }

  protected Arez_BasicModelWithDifferentAccessLevels( @Nonnull final ArezContext $$arez$$_context, final String value )
  {
    super( value );
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time =
      $$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null );
    this.$$arez$$_value =
      $$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "value" : null );
  }

  public Arez_BasicModelWithDifferentAccessLevels( @Nonnull final ArezContext $$arez$$_context,
                                                   final String value,
                                                   final long time )
  {
    super( value, time );
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time =
      $$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null );
    this.$$arez$$_value =
      $$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "value" : null );
  }

  private String $$arez$$_id()
  {
    return "BasicModelWithDifferentAccessLevels." + $$arez$$_id + ".";
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
      this.$$arez$$_time.reportObserved();
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
      this.$$arez$$_value.reportObserved();
    }
  }
}
