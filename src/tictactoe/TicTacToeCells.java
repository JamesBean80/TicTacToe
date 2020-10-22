package tictactoe;

import tictactoe.enums.CellsStatus;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeCells {
    private static final int minValue = 0;
    private static final int maxValue = 2;

    private String[][] cells;

    private TicTacToeCells(String[][] cells) {
        this.cells = cells;
    }

    public TicTacToeCells(String cellsString) {
        int size = maxValue + 1;
        int counter = 0;
        cells = new String[size][size];
        char[] chars = cellsString.trim().toCharArray();

        for (int j = maxValue; j >= minValue; j--) {
            for (int i = minValue; i <= maxValue; i++) {
                cells[i][j] = String.valueOf(chars[counter])
                        .toUpperCase()
                        .replace("Х", "X")
                        .replace("О", "O");
                counter++;
            }
        }
    }

    public TicTacToeCells copy() {
        int size = maxValue + 1;
        String[][] newCells = new String[size][size];

        for (int j = maxValue; j >= minValue; j--) {
            for (int i = minValue; i <= maxValue; i++) {
                newCells[i][j] = cells[i][j];
            }
        }

        return new TicTacToeCells(newCells);
    }

    public boolean checkCell(int x, int y) {
        String value = cells[x][y];
        if (!value.equals("_")) {
            return false;
        }
        return true;
    }

    public String getOtherValue(String value) {
        String result = "_";
        if (value.equals("X")) {
            result = "O";
        } else if (value.equals("O")) {
            result = "X";
        }
        return result;
    }

    public String getNextValue() {
        int xCount = 0;
        int oCount = 0;
        for (int i = minValue; i <= maxValue; i++) {
            for (int j = minValue; j <= maxValue; j++) {
                if (cells[i][j].equals("X")) {
                    xCount++;
                } else if (cells[i][j].equals("O")) {
                    oCount++;
                }
            }
        }
        String value = "_";
        if (xCount == oCount) {
            value = "X";
        } else if (xCount - oCount == 1) {
            value = "O";
        }
        return value;
    }

    public void addCell(int x, int y) {
        cells[x][y] = getNextValue();
    }

    public Coordinate getCellForWin(String value) {
        int countValue;
        Coordinate coordinate;
        for (int i = minValue; i <= maxValue; i++) {
            countValue = 0;
            coordinate = null;
            for (int j = minValue; j <= maxValue; j++) {
                if (cells[i][j].equals(value)) {
                    countValue++;
                } else if (cells[i][j].equals("_")) {
                    coordinate = new Coordinate(i, j);
                }
            }
            if (countValue == 2 && coordinate != null) {
                return coordinate;
            }
        }
        for (int j = minValue; j <= maxValue; j++) {
            countValue = 0;
            coordinate = null;
            for (int i = minValue; i <= maxValue; i++) {
                if (cells[i][j].equals(value)) {
                    countValue++;
                } else if (cells[i][j].equals("_")) {
                    coordinate = new Coordinate(i, j);
                }
            }
            if (countValue == 2 && coordinate != null) {
                return coordinate;
            }
        }
        countValue = 0;
        coordinate = null;
        for (int i = minValue; i <= maxValue; i++) {
            if (cells[i][i].equals(value)) {
                countValue++;
            } else if (cells[i][i].equals("_")) {
                coordinate = new Coordinate(i, i);
            }
        }
        if (countValue == 2 && coordinate != null) {
            return coordinate;
        }
        countValue = 0;
        coordinate = null;
        for (int i = minValue; i <= maxValue; i++) {
            if (cells[i][maxValue - i].equals(value)) {
                countValue++;
            } else if (cells[i][maxValue - i].equals("_")) {
                coordinate = new Coordinate(i, maxValue - i);
            }
        }
        if (countValue == 2 && coordinate != null) {
            return coordinate;
        }
        return null;
    }

    private boolean checkThreeInLine(String value) {
        int count;
        for (int i = minValue; i <= maxValue; i++) {
            count = 0;
            for (int j = minValue; j <= maxValue; j++) {
                if (cells[i][j].equals(value)) {
                    count++;
                }
            }
            if (count == 3) {
                return true;
            }
        }
        for (int j = minValue; j <= maxValue; j++) {
            count = 0;
            for (int i = minValue; i <= maxValue; i++) {
                if (cells[i][j].equals(value)) {
                    count++;
                }
            }
            if (count == 3) {
                return true;
            }
        }
        count = 0;
        for (int i = minValue; i <= maxValue; i++) {
            if (cells[i][i].equals(value)) {
                count++;
            }
        }
        if (count == 3) {
            return true;
        }
        count = 0;
        for (int i = minValue; i <= maxValue; i++) {
            if (cells[i][maxValue - i].equals(value)) {
                count++;
            }
        }
        if (count == 3) {
            return true;
        }
        return false;
    }

    private boolean checkFullLoad() {
        int count = 0;
        for (int i = minValue; i <= maxValue; i++) {
            for (int j = minValue; j <= maxValue; j++) {
                if (cells[i][j].equals("X") || cells[i][j].equals("O")) {
                    count++;
                }
            }
        }
        return count == 9;
    }

    public CellsStatus checkResult() {
        if (checkThreeInLine("X")) {
            return CellsStatus.HAS_3_X_IN_LINE;
        } else if (checkThreeInLine("O")) {
            return CellsStatus.HAS_3_O_IN_LINE;
        } else if (checkFullLoad()) {
            return CellsStatus.ALL_CELLS_LOAD;
        }
        return CellsStatus.NOT_ALL_CELLS_LOAD;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("---------\n");
        for (int j = maxValue; j >= minValue; j--) {
            builder.append("| ");
            for (int i = minValue; i <= maxValue; i++) {
                builder.append(cells[i][j].replace("_", " "));
                builder.append(" ");
            }
            builder.append("|\n");
        }
        builder.append("---------");

        return builder.toString();
    }

    public int getCountEmptyCells() {
        int count = 0;
        for (int i = minValue; i <= maxValue ; i++) {
            for (int j = minValue; j <= maxValue ; j++) {
                if (cells[i][j].equals("_")) {
                    count++;
                }
            }

        }
        return count;
    }

    /**
     *
     * @param order - cell with value "_" order number from (minValue,minValue) to (maxValue,maxValue). x adds first, y - second.
     * @return coordinate in cells bounds, if empty cell with order number found. Else null.
     */
    public Coordinate getEmptyCellCoordinateByOrder(int order) {
        int count = 0;
        for (int j = minValue; j <= maxValue ; j++) {
            for (int i = minValue; i <= maxValue ; i++) {
                if (cells[i][j].equals("_")) {
                    count++;
                    if (count == order) {
                        return new Coordinate(i, j);
                    }
                }
            }

        }
        return null;
    }

    public List<Coordinate> getEmptyCellsCoordinate() {
        ArrayList<Coordinate> emptyCells = new ArrayList<>();
        for (int j = minValue; j <= maxValue ; j++) {
            for (int i = minValue; i <= maxValue ; i++) {
                if (cells[i][j].equals("_")) {
                    emptyCells.add(new Coordinate(i, j));
                }
            }

        }
        return emptyCells;
    }
}
