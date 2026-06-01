package com.elephenman.docviewer.ui;

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
public final class DocumentViewModel_Factory implements Factory<DocumentViewModel> {
  @Override
  public DocumentViewModel get() {
    return newInstance();
  }

  public static DocumentViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DocumentViewModel newInstance() {
    return new DocumentViewModel();
  }

  private static final class InstanceHolder {
    private static final DocumentViewModel_Factory INSTANCE = new DocumentViewModel_Factory();
  }
}
