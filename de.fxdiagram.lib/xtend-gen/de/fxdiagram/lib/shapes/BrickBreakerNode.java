package de.fxdiagram.lib.shapes;

import brickbreaker.Config;
import brickbreaker.Main;
import brickbreaker.Main.MainFrame;
import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.binding.DoubleExpressionExtensions;
import de.fxdiagram.lib.shapes.FlipNode;
import de.fxdiagram.lib.shapes.RectangleBorderPane;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class BrickBreakerNode extends XNode {
  public BrickBreakerNode() {
    FlipNode _flipNode = new FlipNode();
    final Procedure1<FlipNode> _function = new Procedure1<FlipNode>() {
        public void apply(final FlipNode it) {
          RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
          final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
              public void apply(final RectangleBorderPane it) {
                ObservableList<Node> _children = it.getChildren();
                Text _text = new Text();
                final Procedure1<Text> _function = new Procedure1<Text>() {
                    public void apply(final Text it) {
                      it.setText("BrickBreaker");
                      it.setTextOrigin(VPos.TOP);
                      Insets _insets = new Insets(10, 20, 10, 20);
                      StackPane.setMargin(it, _insets);
                    }
                  };
                Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
                _children.add(_doubleArrow);
              }
            };
          RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
          it.setFront(_doubleArrow);
          Pane _pane = new Pane();
          final Procedure1<Pane> _function_1 = new Procedure1<Pane>() {
              public void apply(final Pane it) {
                ObservableList<Node> _children = it.getChildren();
                Group _group = new Group();
                final Procedure1<Group> _function = new Procedure1<Group>() {
                    public void apply(final Group it) {
                      ObservableList<Node> _children = it.getChildren();
                      Group _createRoot = BrickBreakerNode.this.createRoot();
                      _children.add(_createRoot);
                      DoubleProperty _scaleXProperty = it.scaleXProperty();
                      DoubleProperty _widthProperty = BrickBreakerNode.this.widthProperty();
                      DoubleBinding _divide = DoubleExpressionExtensions.operator_divide(_widthProperty, Config.SCREEN_WIDTH);
                      _scaleXProperty.bind(_divide);
                      DoubleProperty _scaleYProperty = it.scaleYProperty();
                      DoubleProperty _heightProperty = BrickBreakerNode.this.heightProperty();
                      DoubleBinding _divide_1 = DoubleExpressionExtensions.operator_divide(_heightProperty, Config.SCREEN_HEIGHT);
                      _scaleYProperty.bind(_divide_1);
                    }
                  };
                Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
                _children.add(_doubleArrow);
              }
            };
          Pane _doubleArrow_1 = ObjectExtensions.<Pane>operator_doubleArrow(_pane, _function_1);
          it.setBack(_doubleArrow_1);
        }
      };
    FlipNode _doubleArrow = ObjectExtensions.<FlipNode>operator_doubleArrow(_flipNode, _function);
    this.setNode(_doubleArrow);
  }
  
  public Group createRoot() {
    try {
      Group _xblockexpression = null;
      {
        Config.initialize();
        Group _group = new Group();
        final Procedure1<Group> _function = new Procedure1<Group>() {
            public void apply(final Group it) {
              Rectangle _rectangle = new Rectangle();
              final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
                  public void apply(final Rectangle it) {
                    it.setWidth(Config.SCREEN_WIDTH);
                    it.setHeight(Config.SCREEN_HEIGHT);
                  }
                };
              Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
              it.setClip(_doubleArrow);
            }
          };
        final Group root = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
        Main _main = new Main();
        final Main main = _main;
        final Constructor<MainFrame> constructor = MainFrame.class.getDeclaredConstructor(Main.class, Group.class);
        constructor.setAccessible(true);
        final MainFrame mainFrame = constructor.newInstance(main, root);
        Field[] _declaredFields = Main.class.getDeclaredFields();
        final Function1<Field,Boolean> _function_1 = new Function1<Field,Boolean>() {
            public Boolean apply(final Field it) {
              String _name = it.getName();
              boolean _equals = Objects.equal(_name, "mainFrame");
              return Boolean.valueOf(_equals);
            }
          };
        Iterable<Field> _filter = IterableExtensions.<Field>filter(((Iterable<Field>)Conversions.doWrapArray(_declaredFields)), _function_1);
        final Field mainFrameField = IterableExtensions.<Field>head(_filter);
        mainFrameField.setAccessible(true);
        mainFrameField.set(main, mainFrame);
        mainFrame.changeState(MainFrame.SPLASH);
        _xblockexpression = (root);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
