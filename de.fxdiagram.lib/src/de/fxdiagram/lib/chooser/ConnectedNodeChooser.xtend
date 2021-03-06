package de.fxdiagram.lib.chooser

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import javafx.geometry.Point2D
import javafx.geometry.Side
import javafx.scene.Group

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

/**
 * A {@link AbstractBaseChooser} to add new nodes to the diagram and connect them 
 * to the given host {@link XNode}.
 * 
 * @see AbstractBaseChooser
 */
class ConnectedNodeChooser extends AbstractBaseChooser {

	@FxProperty double layoutDistance = 60

	XNode host
	
	XNode currentChoice 

	var ChooserConnectionProvider connectionProvider = [
		host, choice, choiceInfo | new XConnection(host, choice) 
	]
	
	XConnection currentConnection
	
	protected val Side layoutPosition
	
	new(XNode host, Side layoutPosition, ChoiceGraphics graphics) {
		super(graphics, layoutPosition.vertical)
		this.host = host
		this.layoutPosition = layoutPosition
	}
	
	override getRoot() {
		host.root
	}
	
	override getDiagram() {
		host.diagram
	}
	
	override getPosition() {
		new Point2D(host.layoutX, host.layoutY)
	}

	def void setConnectionProvider(ChooserConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider
	}

	override deactivate() {
		if (!getIsActive)
			return false
		removeConnection(currentConnection)
		super.deactivate
	}

	protected override adjustNewNode(XNode choice, double widthDelta, double heightDelta) {
		switch layoutPosition {
			case LEFT: 
				choice.layoutX = choice.layoutX - 0.5 * widthDelta
			case RIGHT:
				choice.layoutX = choice.layoutX + 0.5 * widthDelta
			case TOP:
				choice.layoutY = choice.layoutY - 0.5 * heightDelta
			case BOTTOM:
				choice.layoutY = choice.layoutY + 0.5 * heightDelta
		}
		choice.placementHint = layoutPosition
	}
	
	override getAdditionalShapesToAdd(XNode choice, DomainObjectDescriptor choiceInfo) {
		val result = #[connectChoice(choice, choiceInfo)]
		currentConnection = null
		return result
	}
	
	protected override setInterpolatedPosition(double interpolatedPosition) {
		super.setInterpolatedPosition(interpolatedPosition)
		if(!nodes.empty) {
			val choice = nodes.get(((getCurrentPosition + 0.5) as int) % nodes.size)	
			connectChoice(choice, choice.choiceInfo)		
		}
	}
	
	protected def connectChoice(XNode choice, DomainObjectDescriptor choiceInfo) {
		if(isActive && choice !== currentChoice) {
			currentChoice = choice
			val newConnection = connectionProvider.getConnection(host, choice, choiceInfo)
			if(newConnection != currentConnection) {
				removeConnection(currentConnection)
				currentConnection = newConnection
				addConnection(currentConnection)
			}
			choice.toFront
			currentConnection.toFront
		}
		currentConnection
	}
	
	protected def addConnection(XConnection connection) {
		if(connection != null && connection.source.diagram != null && connection.target.diagram != null) 
			diagram.connections += connection
	}

	protected def removeConnection(XConnection connection) {
		if(connection != null) {
			diagram.connections -= connection
			connection.source.outgoingConnections.remove(connection)
			connection.target.incomingConnections.remove(connection)
		}
	}

	protected override alignGroup(Group group, double maxWidth, double maxHeight) {
		group.layoutX = switch layoutPosition {
			case LEFT: host.layoutX - getLayoutDistance - 0.5 * maxWidth
			case RIGHT: host.layoutX + host.layoutBounds.width + getLayoutDistance + 0.5 * maxWidth
			default: host.layoutX + 0.5 * host.layoutBounds.width
		}
		group.layoutY = switch layoutPosition {
			case TOP: host.layoutY - getLayoutDistance - 0.5 * maxHeight
			case BOTTOM: host.layoutY + host.layoutBounds.height + getLayoutDistance + 0.5 * maxHeight
			default: host.layoutY + 0.5 * host.layoutBounds.height
		}
	}
}
