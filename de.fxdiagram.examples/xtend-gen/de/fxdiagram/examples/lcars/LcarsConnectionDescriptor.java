package de.fxdiagram.examples.lcars;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.CachedDomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.examples.lcars.LcarsModelProvider;

@ModelNode
@SuppressWarnings("all")
public class LcarsConnectionDescriptor extends CachedDomainObjectDescriptor<String> {
  public LcarsConnectionDescriptor(final String fieldName, final LcarsModelProvider provider) {
    super(fieldName, fieldName, provider);
  }
  
  @Override
  public String resolveDomainObject() {
    return this.getId();
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public LcarsConnectionDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
