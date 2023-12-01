package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private final PuzzleLibrary library;
  private final List<ModelObserver> modelObservers;
  private boolean[][] lamps;
  private int index;
  private Puzzle puzzle;

  public ModelImpl(PuzzleLibrary library) {
    if (library == null) {
      throw new IllegalArgumentException();
    }
    this.library = library;
    this.index = 0;
    this.puzzle = library.getPuzzle(0);
    this.lamps = new boolean[puzzle.getHeight()][puzzle.getWidth()];
    this.modelObservers = new ArrayList<>();
    this.resetPuzzle();
  }

  @Override
  public void addLamp(int r, int c) {
    if (r > getActivePuzzle().getHeight() - 1
        || c > getActivePuzzle().getWidth() - 1
        || r < 0
        || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }
    this.lamps[r][c] = true;
    notify(this);
  }

  @Override
  public void removeLamp(int r, int c) {
    if (r > getActivePuzzle().getHeight() - 1
        || c > getActivePuzzle().getWidth() - 1
        || r < 0
        || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }
    this.lamps[r][c] = false;
    notify(this);
  }

  @Override
  public boolean isLit(int r, int c) {
    if (r < 0 || c < 0 || r > this.puzzle.getHeight() - 1 || c > this.puzzle.getWidth() - 1) {
      throw new IndexOutOfBoundsException();
    }
    if (this.puzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }
    for (int o = c; o < this.puzzle.getWidth(); o++) {
      if (this.lamps[r][o]) {
        return true;
      } else if (this.puzzle.getCellType(r, o) != CellType.CORRIDOR) {
        break;
      }
    }
    for (int o = c; o >= 0; o--) {
      if (this.lamps[r][o]) {
        return true;
      } else if (this.puzzle.getCellType(r, o) != CellType.CORRIDOR) {
        break;
      }
    }
    for (int i = r; i < puzzle.getHeight(); i++) {
      if (this.lamps[i][c]) {
        return true;
      } else if (this.puzzle.getCellType(i, c) != CellType.CORRIDOR) {
        break;
      }
    }
    for (int i = r; i >= 0; i--) {
      if (this.lamps[i][c]) {
        return true;
      } else if (this.puzzle.getCellType(i, c) != CellType.CORRIDOR) {
        break;
      }
    }

    return false;
  }

  @Override
  public boolean isLamp(int r, int c) {
    if (r > getActivePuzzle().getHeight() - 1
        || c > getActivePuzzle().getWidth() - 1
        || r < 0
        || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }
    return this.lamps[r][c];
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    if (r > getActivePuzzle().getHeight() - 1
        || c > getActivePuzzle().getWidth() - 1
        || r < 0
        || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (!(this.lamps[r][c])) {
      throw new IllegalArgumentException();
    }
    for (int i = r - 1; i >= 0; i--) {
      if (this.lamps[i][c]) {
        return true;
      } else if (getActivePuzzle().getCellType(i, c) != CellType.CORRIDOR) {
        break;
      }
    }
    for (int i = r + 1; i < getActivePuzzle().getHeight(); i++) {
      if (this.lamps[i][c]) {
        return true;
      } else if (getActivePuzzle().getCellType(i, c) != CellType.CORRIDOR) {
        break;
      }
    }
    for (int o = c + 1; o < getActivePuzzle().getWidth(); o++) {
      if (this.lamps[r][o]) {
        return true;
      } else if (getActivePuzzle().getCellType(r, o) != CellType.CORRIDOR) {
        break;
      }
    }
    for (int o = c - 1; o >= 0; o--) {
      if (this.lamps[r][o]) {
        return true;
      } else if (getActivePuzzle().getCellType(r, o) != CellType.CORRIDOR) {
        break;
      }
    }
    return false;
  }

  @Override
  public Puzzle getActivePuzzle() {
    return this.library.getPuzzle(this.index);
  }

  @Override
  public int getActivePuzzleIndex() {
    return this.index;
  }

  @Override
  public void setActivePuzzleIndex(int index) {
    if (index < 0 || index >= getPuzzleLibrarySize()) {
      throw new IndexOutOfBoundsException();
    }
    this.index = index;
    this.puzzle = this.library.getPuzzle(index);
    resetPuzzle();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return this.library.size();
  }

  @Override
  public void resetPuzzle() {
    this.lamps = new boolean[getActivePuzzle().getHeight()][getActivePuzzle().getWidth()];
    notify(this);
  }

  @Override
  public boolean isSolved() {
    for (int i = 0; i < this.puzzle.getHeight(); i++) {
      for (int j = 0; j < this.puzzle.getWidth(); j++) {
        if (this.puzzle.getCellType(i, j) == CellType.CLUE) {
          if (!isClueSatisfied(i, j)) {
            return false;
          }
        }
        if (this.puzzle.getCellType(i, j) == CellType.CORRIDOR) {
          if (!isLit(i, j)) {
            return false;
          }
          if (isLamp(i, j) && isLampIllegal(i, j)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    if (r > getActivePuzzle().getHeight() - 1
        || c > getActivePuzzle().getWidth() - 1
        || r < 0
        || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException();
    }
    int numClues = this.puzzle.getClue(r, c);
    int adjacentLamps = 0;
    if (r > 0 && this.lamps[r - 1][c]) {
      adjacentLamps++;
    }
    if (r < this.puzzle.getHeight() - 1 && this.lamps[r + 1][c]) {
      adjacentLamps++;
    }
    if (c > 0 && this.lamps[r][c - 1]) {
      adjacentLamps++;
    }
    if (c < this.puzzle.getWidth() - 1 && this.lamps[r][c + 1]) {
      adjacentLamps++;
    }
    return (adjacentLamps == numClues);
  }

  @Override
  public void addObserver(ModelObserver observer) {
    this.modelObservers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    this.modelObservers.remove(observer);
  }

  private void notify(ModelImpl model) {
    for (ModelObserver i : this.modelObservers) {
      i.update(model);
    }
  }
}
