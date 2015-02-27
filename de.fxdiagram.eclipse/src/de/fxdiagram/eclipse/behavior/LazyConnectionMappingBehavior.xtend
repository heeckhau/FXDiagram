package de.fxdiagram.eclipse.behavior

import de.fxdiagram.core.XNode
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.buttons.RapidButtonAction
import de.fxdiagram.lib.buttons.RapidButtonBehavior
import de.fxdiagram.lib.chooser.CarusselChoice
import de.fxdiagram.lib.chooser.ConnectedNodeChooser
import de.fxdiagram.lib.chooser.CoverFlowChoice
import de.fxdiagram.eclipse.mapping.AbstractConnectionMappingCall
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor
import de.fxdiagram.eclipse.mapping.NodeMapping
import de.fxdiagram.eclipse.mapping.XDiagramConfigInterpreter
import de.fxdiagram.eclipse.shapes.INodeWithLazyMappings
import java.util.List
import javafx.geometry.Side

import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

/**
 * A {@link RapidButtonBehavior} to add lazy connection mappings to a node.
 * 
 * @see AbstractConnectionMappingCall
 */
class LazyConnectionMappingBehavior<ARG> extends RapidButtonBehavior<XNode> {
	
	List<LazyConnectionRapidButtonAction<?, ARG>> actions = newArrayList

	static def <T> addLazyBehavior(XNode node, IMappedElementDescriptor<T> descriptor) {
		if(descriptor.mapping instanceof NodeMapping<?>) {
			val nodeMapping = descriptor.mapping as NodeMapping<T>
			var LazyConnectionMappingBehavior<T> lazyBehavior = null 
			val lazyOutgoing = nodeMapping.outgoing.filter[lazy]
			if(!lazyOutgoing.empty) {
				lazyBehavior = lazyBehavior ?: new LazyConnectionMappingBehavior<T>(node)
				for(out : lazyOutgoing) 
					lazyBehavior.addConnectionMappingCall(out, new XDiagramConfigInterpreter, true, getButtonSides(node, out))
			}
			val lazyIncoming = nodeMapping.incoming.filter[lazy]
			if(!lazyIncoming.empty) {
				lazyBehavior = lazyBehavior ?: new LazyConnectionMappingBehavior<T>(node)
				for(in : lazyIncoming) 
					lazyBehavior.addConnectionMappingCall(in, new XDiagramConfigInterpreter, false, getButtonSides(node, in))
			}
			if(lazyBehavior != null)
				node.addBehavior(lazyBehavior)
		}
	}
	
	def static getButtonSides(XNode node, AbstractConnectionMappingCall<?, ?> out) {
		if(node instanceof INodeWithLazyMappings) 
			node.getButtonSides(out.connectionMapping)
		else
			# [TOP, BOTTOM, LEFT, RIGHT]
	}

	new(XNode host) {
		super(host)
	}

	def addConnectionMappingCall(AbstractConnectionMappingCall<?, ARG> mappingCall, XDiagramConfigInterpreter configInterpreter, boolean hostIsSource, Side... sides) {
		actions += createAction(mappingCall, configInterpreter, hostIsSource, sides)
	}
	
	protected def createAction(AbstractConnectionMappingCall<?, ARG> mappingCall, XDiagramConfigInterpreter configInterpreter, boolean hostIsSource, Side... sides) {
		val action = new LazyConnectionRapidButtonAction(mappingCall, configInterpreter, hostIsSource)
		for(side: sides) 
			add(new RapidButton(host, side, mappingCall.getImage(side), action))
		action
	}
	
	override protected doActivate() {
		super.doActivate()
	}
}

class LazyConnectionRapidButtonAction<MODEL, ARG> extends RapidButtonAction {
	
	XDiagramConfigInterpreter configInterpreter

	AbstractConnectionMappingCall<MODEL, ARG> mappingCall

	boolean hostIsSource

	new(AbstractConnectionMappingCall<MODEL, ARG> mappingCall, XDiagramConfigInterpreter configInterpreter, boolean hostIsSource) {
		this.mappingCall = mappingCall
		this.configInterpreter = configInterpreter
		this.hostIsSource = hostIsSource
	}
	
	override isEnabled(XNode host) {
		val hostDescriptor = host.domainObject as IMappedElementDescriptor<ARG>
		val diagram = host.diagram
		if(diagram == null) 
			return false
		val existingConnectionDescriptors = diagram.connections.map[domainObject].toSet
		val result = hostDescriptor.withDomainObject[ 
			domainArgument |
			val connectionDomainObjects = configInterpreter.select(mappingCall, domainArgument)
			for(connectionDomainObject: connectionDomainObjects) {
				val connectionDescriptor = configInterpreter.getDescriptor(connectionDomainObject, mappingCall.connectionMapping)
				if(existingConnectionDescriptors.add(connectionDescriptor)) {
					val nodeMappingCall = (mappingCall.connectionMapping.source ?: mappingCall.connectionMapping.target)
					val nodeDomainObjects = configInterpreter.select(nodeMappingCall, connectionDomainObject)
					if(!nodeDomainObjects.empty) 
						return true
				}
			}
			return false
		]
		return if(result == null) false else result
	}
	
	override perform(RapidButton button) {
		val chooser = createChooser(button)
		chooser.populateChooser(button.host)
		button.host.root.currentTool = chooser
	}
	
	def protected createChooser(RapidButton button) {
		val position = button.position
		val chooser = if(position.vertical) {
			new ConnectedNodeChooser(button.host, position, new CarusselChoice)
		} else {
			new ConnectedNodeChooser(button.host, position, new CoverFlowChoice)			
		}
		chooser
	}
	
	protected def populateChooser(ConnectedNodeChooser chooser, XNode host) {
		val hostDescriptor = host.domainObject as IMappedElementDescriptor<ARG>
		val existingConnectionDescriptors = host.diagram.connections.map[domainObject].toSet
		hostDescriptor.withDomainObject[ 
			domainArgument |
			val connectionDomainObjects = configInterpreter.select(mappingCall, domainArgument)
			connectionDomainObjects.forEach [ connectionDomainObject |
				val connectionDescriptor = configInterpreter.getDescriptor(connectionDomainObject, mappingCall.connectionMapping)
				if(existingConnectionDescriptors.add(connectionDescriptor)) {
					val nodeMappingCall = (mappingCall.connectionMapping.source ?: mappingCall.connectionMapping.target)
					val nodeDomainObjects = configInterpreter.select(nodeMappingCall, connectionDomainObject)
					nodeDomainObjects.forEach [	
						chooser.addChoice(createNode(nodeMappingCall.nodeMapping), connectionDescriptor)
					]
				}
			]
			chooser.connectionProvider = [ thisNode, thatNode, connectionDesc |
				val descriptor = connectionDesc as IMappedElementDescriptor<MODEL>
				mappingCall.connectionMapping.createConnection(descriptor) => [
					if(hostIsSource) {
						source = thisNode
						target = thatNode
					} else {
						source = thatNode
						target = thisNode
					}
				]
			]	
			null
		]
	}
	
	protected def <NODE> createNode(Object nodeDomainObject, NodeMapping<?> nodeMapping) {
		if (nodeMapping.isApplicable(nodeDomainObject)) {
			val nodeMappingCasted = nodeMapping as NodeMapping<NODE>
			val descriptor = configInterpreter.getDescriptor(nodeDomainObject as NODE, nodeMappingCasted)
			val node = nodeMappingCasted.createNode(descriptor) 
			node
		} else { 
			null
		}
	}
}