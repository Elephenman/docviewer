package com.elephenman.docviewer.data.repository;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DocumentRepository_Factory implements Factory<DocumentRepository> {
  private final Provider<Context> contextProvider;

  public DocumentRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DocumentRepository get() {
    return newInstance(contextProvider.get());
  }

  public static DocumentRepository_Factory create(Provider<Context> contextProvider) {
    return new DocumentRepository_Factory(contextProvider);
  }

  public static DocumentRepository newInstance(Context context) {
    return new DocumentRepository(context);
  }
}
