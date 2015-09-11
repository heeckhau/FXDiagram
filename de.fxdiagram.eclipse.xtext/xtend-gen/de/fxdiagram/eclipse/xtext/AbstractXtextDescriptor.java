package de.fxdiagram.eclipse.xtext;

import com.google.inject.Injector;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.mapping.AbstractMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import org.eclipse.xtext.resource.IResourceServiceProvider;

@ModelNode
@SuppressWarnings("all")
public abstract class AbstractXtextDescriptor<ECLASS_OR_ESETTING extends Object> extends AbstractMappedElementDescriptor<ECLASS_OR_ESETTING> {
  public AbstractXtextDescriptor(final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
    super(mappingConfigID, mappingID, provider);
  }
  
  public void injectMembers(final Object object) {
    IResourceServiceProvider _resourceServiceProvider = this.getResourceServiceProvider();
    Injector _get = _resourceServiceProvider.<Injector>get(Injector.class);
    _get.injectMembers(object);
  }
  
  protected abstract IResourceServiceProvider getResourceServiceProvider();
  
  @Override
  public XtextDomainObjectProvider getProvider() {
    IMappedElementDescriptorProvider _provider = super.getProvider();
    return ((XtextDomainObjectProvider) _provider);
  }
  
  @Override
  public boolean equals(final Object obj) {
    boolean _and = false;
    if (!(obj instanceof AbstractXtextDescriptor<?>)) {
      _and = false;
    } else {
      boolean _equals = super.equals(obj);
      _and = _equals;
    }
    return _and;
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public AbstractXtextDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
