package de.fxdiagram.eclipse.xtext;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.xtext.AbstractXtextDescriptor;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import java.util.NoSuchElementException;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@ModelNode("elementID")
@SuppressWarnings("all")
public class XtextEObjectDescriptor<T extends EObject> extends AbstractXtextDescriptor<T> {
  public XtextEObjectDescriptor(final XtextEObjectID elementID, final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
    super(mappingConfigID, mappingID, provider);
    this.elementIDProperty.set(elementID);
  }
  
  @Override
  public <U extends Object> U withDomainObject(final Function1<? super T, ? extends U> lambda) {
    U _xblockexpression = null;
    {
      XtextDomainObjectProvider _provider = this.getProvider();
      XtextEObjectID _elementID = this.getElementID();
      final IEditorPart editor = _provider.getCachedEditor(_elementID, false, false);
      U _xifexpression = null;
      if ((editor instanceof XtextEditor)) {
        IXtextDocument _document = ((XtextEditor)editor).getDocument();
        final IUnitOfWork<U, XtextResource> _function = (XtextResource it) -> {
          U _xblockexpression_1 = null;
          {
            XtextEObjectID _elementID_1 = this.getElementID();
            ResourceSet _resourceSet = it.getResourceSet();
            final EObject element = _elementID_1.resolve(_resourceSet);
            _xblockexpression_1 = lambda.apply(((T) element));
          }
          return _xblockexpression_1;
        };
        _xifexpression = _document.<U>readOnly(_function);
      } else {
        XtextEObjectID _elementID_1 = this.getElementID();
        String _plus = ("Cannot open an Xtext editor for " + _elementID_1);
        throw new NoSuchElementException(_plus);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Override
  public Object openInEditor(final boolean select) {
    XtextDomainObjectProvider _provider = this.getProvider();
    XtextEObjectID _elementID = this.getElementID();
    return _provider.getCachedEditor(_elementID, select, select);
  }
  
  @Override
  public String getName() {
    XtextEObjectID _elementID = this.getElementID();
    QualifiedName _qualifiedName = _elementID.getQualifiedName();
    return _qualifiedName.getLastSegment();
  }
  
  @Override
  public boolean equals(final Object obj) {
    if ((obj instanceof XtextEObjectDescriptor<?>)) {
      boolean _and = false;
      boolean _equals = super.equals(obj);
      if (!_equals) {
        _and = false;
      } else {
        XtextEObjectID _elementID = this.getElementID();
        XtextEObjectID _elementID_1 = ((XtextEObjectDescriptor<?>)obj).getElementID();
        boolean _equals_1 = Objects.equal(_elementID, _elementID_1);
        _and = _equals_1;
      }
      return _and;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    int _hashCode = super.hashCode();
    XtextEObjectID _elementID = this.getElementID();
    int _hashCode_1 = _elementID.hashCode();
    int _multiply = (17 * _hashCode_1);
    return (_hashCode + _multiply);
  }
  
  @Override
  protected IResourceServiceProvider getResourceServiceProvider() {
    XtextEObjectID _elementID = this.getElementID();
    return _elementID.getResourceServiceProvider();
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public XtextEObjectDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(elementIDProperty, XtextEObjectID.class);
  }
  
  private ReadOnlyObjectWrapper<XtextEObjectID> elementIDProperty = new ReadOnlyObjectWrapper<XtextEObjectID>(this, "elementID");
  
  public XtextEObjectID getElementID() {
    return this.elementIDProperty.get();
  }
  
  public ReadOnlyObjectProperty<XtextEObjectID> elementIDProperty() {
    return this.elementIDProperty.getReadOnlyProperty();
  }
}
