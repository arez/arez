package arez.processor;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.TypeElement;

final class RepositoryDescriptor
{
  @Nonnull
  private final ComponentDescriptor _component;
  @Nonnull
  private final List<TypeElement> _extensions;
  private final boolean _shouldRepositoryDefineCreate;
  private final boolean _shouldRepositoryDefineAttach;
  private final boolean _shouldRepositoryDefineDestroy;
  private final boolean _shouldRepositoryDefineDetach;
  /**
   * Flag controlling whether dagger module is created for repository.
   */
  @Nonnull
  private final String _daggerConfig;

  RepositoryDescriptor( @Nonnull final ComponentDescriptor component,
                        @Nonnull final List<TypeElement> extensions,
                        final boolean shouldRepositoryDefineCreate,
                        final boolean shouldRepositoryDefineAttach,
                        final boolean shouldRepositoryDefineDestroy,
                        final boolean shouldRepositoryDefineDetach,
                        @Nonnull final String daggerConfig )
  {
    _component = Objects.requireNonNull( component );
    _extensions = Objects.requireNonNull( extensions );
    _shouldRepositoryDefineCreate = shouldRepositoryDefineCreate;
    _shouldRepositoryDefineAttach = shouldRepositoryDefineAttach;
    _shouldRepositoryDefineDestroy = shouldRepositoryDefineDestroy;
    _shouldRepositoryDefineDetach = shouldRepositoryDefineDetach;
    _daggerConfig = Objects.requireNonNull( daggerConfig );
  }

  @Nonnull
  ComponentDescriptor getComponent()
  {
    return _component;
  }

  @Nonnull
  List<TypeElement> getExtensions()
  {
    return _extensions;
  }

  @Nonnull
  String getDaggerConfig()
  {
    return _daggerConfig;
  }

  boolean shouldRepositoryDefineCreate()
  {
    return _shouldRepositoryDefineCreate;
  }

  boolean shouldRepositoryDefineAttach()
  {
    return _shouldRepositoryDefineAttach;
  }

  boolean shouldRepositoryDefineDestroy()
  {
    return _shouldRepositoryDefineDestroy;
  }

  boolean shouldRepositoryDefineDetach()
  {
    return _shouldRepositoryDefineDetach;
  }
}
