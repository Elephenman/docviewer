package com.elephenman.docviewer.ui.viewer;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ViewerViewModel_Factory implements Factory<ViewerViewModel> {
  @Override
  public ViewerViewModel get() {
    return newInstance();
  }

  public static ViewerViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ViewerViewModel newInstance() {
    return new ViewerViewModel();
  }

  private static final class InstanceHolder {
    private static final ViewerViewModel_Factory INSTANCE = new ViewerViewModel_Factory();
  }
}
