package com.elephenman.docviewer.ui.filebrowser;

import android.content.Context;
import com.elephenman.docviewer.data.repository.DocumentRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class FileBrowserViewModel_Factory implements Factory<FileBrowserViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<DocumentRepository> documentRepositoryProvider;

  public FileBrowserViewModel_Factory(Provider<Context> contextProvider,
      Provider<DocumentRepository> documentRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.documentRepositoryProvider = documentRepositoryProvider;
  }

  @Override
  public FileBrowserViewModel get() {
    return newInstance(contextProvider.get(), documentRepositoryProvider.get());
  }

  public static FileBrowserViewModel_Factory create(Provider<Context> contextProvider,
      Provider<DocumentRepository> documentRepositoryProvider) {
    return new FileBrowserViewModel_Factory(contextProvider, documentRepositoryProvider);
  }

  public static FileBrowserViewModel newInstance(Context context,
      DocumentRepository documentRepository) {
    return new FileBrowserViewModel(context, documentRepository);
  }
}
