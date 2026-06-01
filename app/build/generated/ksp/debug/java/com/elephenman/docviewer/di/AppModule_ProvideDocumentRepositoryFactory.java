package com.elephenman.docviewer.di;

import android.content.Context;
import com.elephenman.docviewer.data.repository.DocumentRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideDocumentRepositoryFactory implements Factory<DocumentRepository> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideDocumentRepositoryFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DocumentRepository get() {
    return provideDocumentRepository(contextProvider.get());
  }

  public static AppModule_ProvideDocumentRepositoryFactory create(
      Provider<Context> contextProvider) {
    return new AppModule_ProvideDocumentRepositoryFactory(contextProvider);
  }

  public static DocumentRepository provideDocumentRepository(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDocumentRepository(context));
  }
}
