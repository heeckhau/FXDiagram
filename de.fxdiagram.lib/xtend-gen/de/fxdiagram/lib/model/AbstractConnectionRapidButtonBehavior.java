package de.fxdiagram.lib.model;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import de.fxdiagram.lib.buttons.RapidButtonBehavior;
import de.fxdiagram.lib.chooser.ConnectedNodeChooser;
import java.util.Set;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Commodity class to add rapid-button-based exploration behavior to your {@link XNodes}
 * by only overriding a couple of template methods.
 * 
 * See the examples for usage scenarios.
 */
@SuppressWarnings("all")
public abstract class AbstractConnectionRapidButtonBehavior<HOST extends XNode, MODEL extends Object, KEY extends DomainObjectDescriptor> extends RapidButtonBehavior<HOST> {
  private Set<KEY> availableChoiceKeys = CollectionLiterals.<KEY>newLinkedHashSet();
  
  private Set<KEY> unavailableChoiceKeys = CollectionLiterals.<KEY>newHashSet();
  
  public AbstractConnectionRapidButtonBehavior(final HOST host) {
    super(host);
  }
  
  @Override
  public Class<? extends Behavior> getBehaviorKey() {
    return this.getClass();
  }
  
  @Override
  protected void doActivate() {
    super.doActivate();
    Iterable<MODEL> _initialModelChoices = this.getInitialModelChoices();
    final Function1<MODEL, KEY> _function = (MODEL it) -> {
      return this.getChoiceKey(it);
    };
    Iterable<KEY> _map = IterableExtensions.<MODEL, KEY>map(_initialModelChoices, _function);
    Iterables.<KEY>addAll(this.availableChoiceKeys, _map);
    boolean _isEmpty = this.availableChoiceKeys.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      final RapidButtonAction addConnectionAction = new RapidButtonAction() {
        @Override
        public void perform(final RapidButton button) {
          final ConnectedNodeChooser chooser = AbstractConnectionRapidButtonBehavior.this.createChooser(button, AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys, AbstractConnectionRapidButtonBehavior.this.unavailableChoiceKeys);
          HOST _host = AbstractConnectionRapidButtonBehavior.this.getHost();
          XRoot _root = CoreExtensions.getRoot(_host);
          _root.setCurrentTool(chooser);
        }
        
        @Override
        public boolean isEnabled(final XNode host) {
          boolean _isEmpty = AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys.isEmpty();
          return (!_isEmpty);
        }
      };
      Iterable<RapidButton> _createButtons = this.createButtons(addConnectionAction);
      final Consumer<RapidButton> _function_1 = (RapidButton it) -> {
        this.add(it);
      };
      _createButtons.forEach(_function_1);
      HOST _host = this.getHost();
      XDiagram _diagram = CoreExtensions.getDiagram(_host);
      ObservableList<XConnection> _connections = _diagram.getConnections();
      InitializingListListener<XConnection> _initializingListListener = new InitializingListListener<XConnection>();
      final Procedure1<InitializingListListener<XConnection>> _function_2 = (InitializingListListener<XConnection> it) -> {
        final Procedure1<XConnection> _function_3 = (XConnection it_1) -> {
          DomainObjectDescriptor _domainObjectDescriptor = it_1.getDomainObjectDescriptor();
          boolean _remove = this.availableChoiceKeys.remove(_domainObjectDescriptor);
          if (_remove) {
            DomainObjectDescriptor _domainObjectDescriptor_1 = it_1.getDomainObjectDescriptor();
            this.unavailableChoiceKeys.add(((KEY) _domainObjectDescriptor_1));
          }
        };
        it.setAdd(_function_3);
        final Procedure1<XConnection> _function_4 = (XConnection it_1) -> {
          DomainObjectDescriptor _domainObjectDescriptor = it_1.getDomainObjectDescriptor();
          boolean _remove = this.unavailableChoiceKeys.remove(_domainObjectDescriptor);
          if (_remove) {
            DomainObjectDescriptor _domainObjectDescriptor_1 = it_1.getDomainObjectDescriptor();
            this.availableChoiceKeys.add(((KEY) _domainObjectDescriptor_1));
          }
        };
        it.setRemove(_function_4);
      };
      InitializingListListener<XConnection> _doubleArrow = ObjectExtensions.<InitializingListListener<XConnection>>operator_doubleArrow(_initializingListListener, _function_2);
      CoreExtensions.<XConnection>addInitializingListener(_connections, _doubleArrow);
    }
  }
  
  protected abstract Iterable<MODEL> getInitialModelChoices();
  
  protected abstract KEY getChoiceKey(final MODEL model);
  
  protected abstract XNode createNode(final KEY key);
  
  protected abstract Iterable<RapidButton> createButtons(final RapidButtonAction addConnectionAction);
  
  protected abstract ConnectedNodeChooser createChooser(final RapidButton button, final Set<KEY> availableChoiceKeys, final Set<KEY> unavailableChoiceKeys);
}
