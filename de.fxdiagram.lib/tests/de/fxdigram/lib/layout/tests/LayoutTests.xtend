package de.fxdigram.lib.layout.tests

import de.fxdiagram.core.XNestedDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRootDiagram
import de.fxdiagram.lib.shapes.SimpleNode
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class LayoutTests extends Application {

	XNestedDiagram nestedDiagram

	XRootDiagram diagram

	def static main(String... args) {
		launch
	}

	override start(Stage stage) throws Exception {
		nestedDiagram = new XNestedDiagram => [
			contentsInitializer = [
				addNode(
					new SimpleNode("Foo") => [
						relocate(-100, -100)
						width = 65
						height = 40
					])
				addNode(
					new SimpleNode("Bar") => [
						relocate(100, 100)
					])
				scaleToFit
			]
		]
		diagram = new XRootDiagram
		stage.scene = new Scene(diagram, 1024, 768)
		val rectangleBorderPane = new StackPane
		diagram => [
			activate
			addNode(
				new XNode(rectangleBorderPane => [
					children += new Group => [ // why the hell is this additional group necessary
						children += nestedDiagram
					]
				]))
			nestedDiagram.activate
		]
		stage.show
		nestedDiagram.getNodes.forEach[printLayoutGeometry]
		nestedDiagram.printLayoutGeometry
		rectangleBorderPane.printLayoutGeometry

		nestedDiagram.getNodes.forEach[printSizes]
		nestedDiagram.printSizes
		rectangleBorderPane.printSizes

		nestedDiagram.getNodeLayer
	}

	def printLayoutGeometry(Node it) {
		println(it + ": " + layoutX + " " + layoutY + " " + layoutBounds)
	}

	def printSizes(Node it) {
		println("MinSize : " + minWidth(-1) + " x " + minHeight(-1))
		println("PrefSize: " + minWidth(-1) + " x " + minHeight(-1))
		println("MaxSize : " + minWidth(-1) + " x " + minHeight(-1))
	}

}
