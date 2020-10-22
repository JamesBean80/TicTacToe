package tictactoe;

import tictactoe.enums.CellsStatus;

import java.util.*;

public class Minmax {
    private HashMap<Coordinate, Integer> coordinatesScore = new HashMap();
    private boolean isMaxPlayer;
    private TicTacToeCells cells;

    public Minmax(boolean isMaxPlayer, TicTacToeCells cells) {
        this.isMaxPlayer = isMaxPlayer;
        this.cells = cells;
    }

    public Integer process() {
        List<Coordinate> emptyCells = cells.getEmptyCellsCoordinate();
        Integer score = null;

        for (Coordinate coordinate : emptyCells) {
            TicTacToeCells newCells = cells.copy();
            newCells.addCell(coordinate.x, coordinate.y);
            CellsStatus result = newCells.checkResult();

            if (result == CellsStatus.HAS_3_O_IN_LINE || result == CellsStatus.HAS_3_X_IN_LINE) {
                coordinatesScore.put(coordinate, isMaxPlayer ? 1 : -1);
            } else if (result == CellsStatus.ALL_CELLS_LOAD) {
                coordinatesScore.put(coordinate, 0);
            } else {
                Minmax minmax = new Minmax(!isMaxPlayer, newCells);
                coordinatesScore.put(coordinate, minmax.process());
            }

            if (score == null ||
                    (isMaxPlayer && score < coordinatesScore.get(coordinate)) ||
                    (!isMaxPlayer && score > coordinatesScore.get(coordinate))
            ) {
                score = coordinatesScore.get(coordinate);
            }
        }

        return score;
    }

    Coordinate getBestScoreCoordinate() {
        Integer score = null;
        Coordinate coordinate = null;
        List<Coordinate> bestScoreCoordinates = new ArrayList();

        for (Map.Entry<Coordinate, Integer> entrySet : coordinatesScore.entrySet()) {
            if (score == null ||
                    (isMaxPlayer && score < entrySet.getValue()) ||
                    (!isMaxPlayer && score > entrySet.getValue())
            ) {
                score = entrySet.getValue();
            }
        }

        for (Map.Entry<Coordinate, Integer> entrySet : coordinatesScore.entrySet()) {
            if (score == entrySet.getValue()) {
                bestScoreCoordinates.add(entrySet.getKey());
            }
        }

        if (bestScoreCoordinates.size() == 1) {
            coordinate = bestScoreCoordinates.get(0);
        } else if (bestScoreCoordinates.size() > 1) {
            Random random = new Random();
            int index = random.nextInt(bestScoreCoordinates.size());
            coordinate = bestScoreCoordinates.get(index);
        }

        return coordinate;
    }
}
