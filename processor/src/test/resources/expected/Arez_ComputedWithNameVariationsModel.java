import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_ComputedWithNameVariationsModel extends ComputedWithNameVariationsModel {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final ComputedValue<String> $$arez$$_helper;

  @Nonnull
  private final ComputedValue<Boolean> $$arez$$_ready;

  @Nonnull
  private final ComputedValue<String> $$arez$$_foo;

  @Nonnull
  private final ComputedValue<Long> $$arez$$_time;

  public Arez_ComputedWithNameVariationsModel(@Nonnull final ArezContext $$arez$$_context) {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_helper = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "helper" : null, super::helper, Objects::equals );
    this.$$arez$$_ready = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "ready" : null, super::isReady, Objects::equals );
    this.$$arez$$_foo = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "foo" : null, super::myFooHelperMethod, Objects::equals );
    this.$$arez$$_time = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null, super::getTime, Objects::equals );
  }

  private String $$arez$$_id() {
    return "ComputedWithNameVariationsModel." + $$arez$$_id + ".";
  }

  @Override
  public String helper() {
    return this.$$arez$$_helper.get();
  }

  @Override
  public boolean isReady() {
    return this.$$arez$$_ready.get();
  }

  @Override
  public String myFooHelperMethod() {
    return this.$$arez$$_foo.get();
  }

  @Override
  public long getTime() {
    return this.$$arez$$_time.get();
  }
}
