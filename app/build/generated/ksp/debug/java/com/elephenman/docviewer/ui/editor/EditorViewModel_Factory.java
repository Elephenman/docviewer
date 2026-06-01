package com.elephenman.docviewer.ui.editor;

import com.elephenman.docviewer.data.repository.DocumentRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class EditorViewModel_Factory implements Factory<EditorViewModel> {
  private final Provider<DocumentRepository> documentRepositoryProvider;

  public EditorViewModel_Factory(Provider<DocumentRepository> documentRepositoryProvider) {
    this.documentRepositoryProvider = documentRepositoryProvider;
  }

  @Override
  public EditorViewModel get() {
    return newInstance(documentRepositoryProvider.get());
  }

  public static EditorViewModel_Factory create(
      Provider<DocumentRepository> documentRepositoryProvider) {
    return new EditorViewModel_Factory(documentRepositoryProvider);
  }

  public static EditorViewModel newInstance(DocumentRepository documentRepository) {
    return new EditorViewModel(documentRepository);
  }
}
