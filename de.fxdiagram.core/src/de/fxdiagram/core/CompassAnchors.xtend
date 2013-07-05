package de.fxdiagram.core

import javafx.geometry.Point2D

import static extension de.fxdiagram.core.Extensions.*

class CompassAnchors implements Anchors {
	
	XNode host
	int numAnchorsPerSide
	
	new(XNode host, int numAnchorsPerSide) {
		this.host = host
		this.numAnchorsPerSide = numAnchorsPerSide  
	}

	override getAnchor(double x, double y) {
		var currentDistance = Double.MAX_VALUE
		var Point2D currentAnchor = null 
		for(p: calculatePoints) {
			val candidateDistance = p.distance(x,y)
			if(candidateDistance < currentDistance) {
				currentAnchor = p
				currentDistance = candidateDistance
			}
		}
		currentAnchor	
	}

	protected def calculatePoints() {
		val bounds = host?.node?.boundsInLocal
		if (bounds != null) {
			val deltaX = (bounds.maxX + bounds.minX) / (numAnchorsPerSide + 1)
			val deltaY = (bounds.maxY + bounds.minY) / (numAnchorsPerSide + 1)
			val anchors = newArrayList
			for(i: 1..numAnchorsPerSide) {
				anchors += host.node.localToRoot(bounds.minX, bounds.minY + i * deltaY)
				anchors += host.node.localToRoot(bounds.maxX, bounds.minY + i * deltaY)
				anchors += host.node.localToRoot(bounds.minX + i * deltaX, bounds.minY)
				anchors += host.node.localToRoot(bounds.minX + i * deltaX, bounds.maxY)
			}
			anchors
		}
	}
	
}