import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_AnnotationsOnModel
  extends AnnotationsOnModel
  implements Disposable
{
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  @Nonnull
  private final ComputedValue<Integer> $$arez$$_someValue;

  public Arez_AnnotationsOnModel( @Nonnull final ArezContext $$arez$$_context )
  {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time =
      this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null );
    this.$$arez$$_someValue = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ?
                                                                         $$arez$$_id() + "someValue" :
                                                                         null, super::someValue, Objects::equals );
  }

  private String $$arez$$_id()
  {
    return "AnnotationsOnModel." + $$arez$$_id + ".";
  }

  @Override
  public void dispose()
  {
    $$arez$$_someValue.dispose();
    $$arez$$_time.dispose();
  }

  @Nonnull
  @Override
  public String getTime()
  {
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime( @Nonnull final String time )
  {
    if ( !Objects.equals( time, super.getTime() ) )
    {
      super.setTime( time );
      this.$$arez$$_time.reportChanged();
    }
  }

  @Override
  public void doStuff( @Nonnull final String time )
  {
    this.$$arez$$_context.safeProcedure( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "doStuff" : null,
                                         true,
                                         () -> super.doStuff( time ) );
  }

  @Nonnull
  @Override
  public Integer someValue()
  {
    return this.$$arez$$_someValue.get();
  }
}
