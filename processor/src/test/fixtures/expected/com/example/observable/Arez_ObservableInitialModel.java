package com.example.observable;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.ObservableValue;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class Arez_ObservableInitialModel extends ObservableInitialModel implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  @Nullable
  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ObservableValue<String> $$arez$$_name;

  private String $$arezd$$_name;

  @Nonnull
  private final ObservableValue<String> $$arez$$_firstName;

  private String $$arezd$$_firstName;

  @Nonnull
  private final ObservableValue<Integer> $$arez$$_age;

  private int $$arezd$$_age;

  @Nonnull
  private final ObservableValue<String> $$arez$$_nickname;

  @Nullable
  private String $$arezd$$_nickname;

  Arez_ObservableInitialModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_observable_ObservableInitialModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_observable_ObservableInitialModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arezd$$_name = Objects.requireNonNull( ObservableInitialModel.INITIAL_NAME );
    this.$$arezd$$_firstName = Objects.requireNonNull( ObservableInitialModel.INITIAL_FIRST_NAME );
    this.$$arezd$$_age = ObservableInitialModel.getInitialAge();
    this.$$arezd$$_nickname = ObservableInitialModel.initialNickname();
    this.$$arez$$_name = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".name" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_name ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_name = v : null );
    this.$$arez$$_firstName = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".firstName" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_firstName ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_firstName = v : null );
    this.$$arez$$_age = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".age" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_age ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_age = v : null );
    this.$$arez$$_nickname = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".nickname" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_nickname ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_nickname = v : null );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  private int $$arezi$$_id() {
    assert null != this.$$arezi$$_kernel;
    return this.$$arezi$$_kernel.getId();
  }

  @Override
  @Nonnull
  public Integer getArezId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_observable_ObservableInitialModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'getArezId' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return $$arezi$$_id();
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action,
      final boolean errorIfDuplicate) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_observable_ObservableInitialModel'" );
    }
    this.$$arezi$$_kernel.addOnDisposeListener( key, action, errorIfDuplicate );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key, final boolean errorIfMissing) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_observable_ObservableInitialModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.removeOnDisposeListener( key, errorIfMissing );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_observable_ObservableInitialModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_observable_ObservableInitialModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_name.dispose();
    this.$$arez$$_firstName.dispose();
    this.$$arez$$_age.dispose();
    this.$$arez$$_nickname.dispose();
  }

  @Override
  @Nonnull
  String getName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getName' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_name.reportObserved();
    return this.$$arezd$$_name;
  }

  @Override
  void setName(@Nonnull final String name) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setName' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_name.preReportChanged();
    final String $$arezv$$_currentValue = this.$$arezd$$_name;
    assert null != name;
    if ( !Objects.equals( name, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_name = name;
      this.$$arez$$_name.reportChanged();
    }
  }

  @Override
  @Nonnull
  String getFirstName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getFirstName' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_firstName.reportObserved();
    return this.$$arezd$$_firstName;
  }

  @Override
  void setFirstName(@Nonnull final String firstName) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setFirstName' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_firstName.preReportChanged();
    final String $$arezv$$_currentValue = this.$$arezd$$_firstName;
    assert null != firstName;
    if ( !Objects.equals( firstName, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_firstName = firstName;
      this.$$arez$$_firstName.reportChanged();
    }
  }

  @Override
  int getAge() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getAge' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_age.reportObserved();
    return this.$$arezd$$_age;
  }

  @Override
  void setAge(final int age) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setAge' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_age.preReportChanged();
    final int $$arezv$$_currentValue = this.$$arezd$$_age;
    if ( age != $$arezv$$_currentValue ) {
      this.$$arezd$$_age = age;
      this.$$arez$$_age.reportChanged();
    }
  }

  @Override
  @Nullable
  String getNickname() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getNickname' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_nickname.reportObserved();
    return this.$$arezd$$_nickname;
  }

  @Override
  void setNickname(@Nullable final String nickname) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setNickname' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_nickname.preReportChanged();
    final String $$arezv$$_currentValue = this.$$arezd$$_nickname;
    if ( !Objects.equals( nickname, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_nickname = nickname;
      this.$$arez$$_nickname.reportChanged();
    }
  }

  @Override
  public String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + this.$$arezi$$_kernel.getName() + "]";
    } else {
      return super.toString();
    }
  }
}
