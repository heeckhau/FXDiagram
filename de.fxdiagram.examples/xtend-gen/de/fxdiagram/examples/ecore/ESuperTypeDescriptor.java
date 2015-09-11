package de.fxdiagram.examples.ecore;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.CachedDomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.examples.ecore.ESuperTypeHandle;
import de.fxdiagram.examples.ecore.EcoreDomainObjectProvider;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.xbase.lib.Extension;

@ModelNode
@SuppressWarnings("all")
public class ESuperTypeDescriptor extends CachedDomainObjectDescriptor<ESuperTypeHandle> {
  public ESuperTypeDescriptor(final ESuperTypeHandle it, @Extension final EcoreDomainObjectProvider provider) {
    super(it, 
      ((provider.getId(it.getSubType()) + "=") + Integer.valueOf(it.getSubType().getEAllSuperTypes().indexOf(it.getSuperType()))), provider);
  }
  
  @Override
  public ESuperTypeHandle resolveDomainObject() {
    ESuperTypeHandle _xblockexpression = null;
    {
      String _id = this.getId();
      final URI uri = URI.createURI(_id);
      URI _trimFragment = uri.trimFragment();
      String _string = _trimFragment.toString();
      final EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(_string);
      String _fragment = uri.fragment();
      final int posEquals = _fragment.indexOf("=");
      String _xifexpression = null;
      if ((posEquals == (-1))) {
        _xifexpression = uri.fragment();
      } else {
        String _fragment_1 = uri.fragment();
        _xifexpression = _fragment_1.substring(0, posEquals);
      }
      final String fragment = _xifexpression;
      Resource _eResource = ePackage.eResource();
      final EObject eObject = _eResource.getEObject(fragment);
      final EClass eClass = ((EClass) eObject);
      EList<EClass> _eAllSuperTypes = eClass.getEAllSuperTypes();
      String _fragment_2 = uri.fragment();
      String _substring = _fragment_2.substring((posEquals + 1));
      int _parseInt = Integer.parseInt(_substring);
      EClass _get = _eAllSuperTypes.get(_parseInt);
      _xblockexpression = new ESuperTypeHandle(eClass, _get);
    }
    return _xblockexpression;
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public ESuperTypeDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
