package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ControllerImpl;
import com.comp301.a09akari.model.Puzzle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MessageView implements FXComponent {
  private final ControllerImpl controller;

  public MessageView(ControllerImpl controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    HBox layout = new HBox();
    Puzzle puzzle = controller.getActivePuzzle();
    if (controller.isSolved()) {
      Text title = new Text("Good Job! \u2713");
      title.setFill(Color.GREEN);
      title.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 25));
      layout.getChildren().add(title);
    }
    if (!controller.isSolved()) {
      Text title = new Text("Not yet! \u2716");
      title.setFill(Color.RED);
      title.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 25));
      layout.getChildren().add(title);
    }
    layout.setPadding(new Insets(10, 10, 10, 10));
    layout.setAlignment(Pos.TOP_CENTER);
    return layout;
  }
}
