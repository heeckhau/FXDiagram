package de.fxdiagram.mapping.execution;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.ConnectionMappingCall;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.EntryCall;
import de.fxdiagram.mapping.execution.InterpreterContext;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class ConnectionEntryCall<RESULT extends Object, ARG extends Object> implements EntryCall<ARG> {
  @Accessors(AccessorType.PUBLIC_GETTER)
  private ConnectionMappingCall<RESULT, ARG> mappingCall;
  
  private ConnectionMapping<RESULT> mapping;
  
  public ConnectionEntryCall(final Function1<? super ARG, ? extends RESULT> selector, final ConnectionMapping<RESULT> mapping) {
    ConnectionMappingCall<RESULT, ARG> _connectionMappingCall = new ConnectionMappingCall<RESULT, ARG>(selector, mapping);
    this.mappingCall = _connectionMappingCall;
    this.mapping = mapping;
  }
  
  @Override
  public XDiagramConfig getConfig() {
    AbstractMapping<RESULT> _mapping = this.mappingCall.getMapping();
    return _mapping.getConfig();
  }
  
  @Override
  public String getText() {
    AbstractMapping<RESULT> _mapping = this.mappingCall.getMapping();
    String _displayName = _mapping.getDisplayName();
    String _plus = (_displayName + " (");
    AbstractMapping<RESULT> _mapping_1 = this.mappingCall.getMapping();
    XDiagramConfig _config = _mapping_1.getConfig();
    String _label = _config.getLabel();
    String _plus_1 = (_plus + _label);
    return (_plus_1 + ")");
  }
  
  @Override
  public void execute(final ARG domainObject, final XDiagramConfigInterpreter interpreter, final InterpreterContext context) {
    boolean _and = false;
    NodeMappingCall<?, RESULT> _source = this.mapping.getSource();
    boolean _notEquals = (!Objects.equal(_source, null));
    if (!_notEquals) {
      _and = false;
    } else {
      NodeMappingCall<?, RESULT> _target = this.mapping.getTarget();
      boolean _notEquals_1 = (!Objects.equal(_target, null));
      _and = _notEquals_1;
    }
    if (_and) {
      final Procedure1<XConnection> _function = (XConnection it) -> {
      };
      interpreter.<RESULT, ARG>execute(this.mappingCall, domainObject, _function, context);
    }
  }
  
  @Pure
  public ConnectionMappingCall<RESULT, ARG> getMappingCall() {
    return this.mappingCall;
  }
}
