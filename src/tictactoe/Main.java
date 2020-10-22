package tictactoe;

import tictactoe.enums.CellsStatus;
import tictactoe.enums.PlayerType;
import tictactoe.enums.TurnResult;

import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int minValue = 1;
    private static final int maxValue = 3;
    private static TicTacToeCells cells;

    public static boolean checkBounds(int x, int y) {
        if (x < minValue || x > maxValue || y < minValue || y > maxValue) {
            return false;
        }
        return true;
    }

    private static boolean hasNextTurn(Coordinate coordinate) {
        CellsStatus cellsStatus = cells.checkResult();
        boolean result = false;
        switch (cellsStatus) {
            case HAS_3_X_IN_LINE:
                System.out.println("X wins");
                break;
            case HAS_3_O_IN_LINE:
                System.out.println("O wins");
                break;
            case ALL_CELLS_LOAD:
                System.out.println("Draw");
                break;
            case NOT_ALL_CELLS_LOAD:
//                System.out.println("Game not finished");
                result = true;
                break;
        }
        return result;
    }

    private static Coordinate getCoordinateByEasyAI() {
        int count = cells.getCountEmptyCells();
        Random random = new Random();
        int order = random.nextInt(count) + 1;
        return cells.getEmptyCellCoordinateByOrder(order);
    }

    private static Coordinate getCoordinateByMediumAI() {
        Coordinate coordinate;
        String nextValue = cells.getNextValue();

        coordinate = cells.getCellForWin(nextValue);

        if (coordinate != null) {
            return coordinate;
        }

        nextValue = cells.getOtherValue(nextValue);
        coordinate = cells.getCellForWin(nextValue);
        if (coordinate != null) {
            return coordinate;
        }

        return getCoordinateByEasyAI();
    }

    private static Coordinate getCoordinateByHardAI() {
        Minmax minmax = new Minmax(true, cells);
        minmax.process();
        return minmax.getBestScoreCoordinate();
    }

    private static TurnResult playerTurn(Scanner scanner) {
        System.out.print("Enter the coordinates: ");
        Coordinate coordinate = new Coordinate(scanner.nextLine());

        if (coordinate.x == -1 || coordinate.y == -1) {
            System.out.println("You should enter numbers!");
            return TurnResult.RE_TURN;
        } else if (!checkBounds(coordinate.x, coordinate.y)) {
            System.out.printf("Coordinates should be from %d to %d!\n", minValue, maxValue);
            return TurnResult.RE_TURN;
        } else if (!cells.checkCell(coordinate.x - 1, coordinate.y - 1)) {
            System.out.println("This cell is occupied! Choose another one!");
            return TurnResult.RE_TURN;
        }
        cells.addCell(coordinate.x - 1, coordinate.y - 1);
        System.out.println(cells.toString());

        if (!hasNextTurn(coordinate)) {
            return TurnResult.END_GAME;
        }
        return TurnResult.CONTINUE;
    }

    private static TurnResult computerTurn(PlayerType playerType) {
        System.out.printf("Making move level \"%s\"\n", playerType.name().toLowerCase());
        Coordinate coordinate = null;
        switch (playerType) {
            case EASY:
                coordinate = getCoordinateByEasyAI();
                break;
            case MEDIUM:
                coordinate = getCoordinateByMediumAI();
                break;
            case HARD:
                coordinate = getCoordinateByHardAI();
                break;
        }
        if (coordinate == null) {
            return TurnResult.ERROR;
        }
        cells.addCell(coordinate.x, coordinate.y);
        System.out.println(cells.toString());

        if (!hasNextTurn(coordinate)) {
            return TurnResult.END_GAME;
        }
        return TurnResult.CONTINUE;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PlayerType firstPlayerType = PlayerType.USER;
        PlayerType secondPlayerType = PlayerType.USER;
        while (true) {
            System.out.print("Input command: ");
            String[] commands = scanner.nextLine().split(" ");
            if (commands[0].equals("exit")) {
                return;
            } else if (commands.length < 3) {
                System.out.println("Bad parameters!");
            } else if (commands[0].equals("start")) {
                try {
                    firstPlayerType = PlayerType.valueOf(commands[1].toUpperCase());
                    secondPlayerType = PlayerType.valueOf(commands[2].toUpperCase());
                    break;
                } catch (Exception e) {
                    System.out.println("Bad parameters!");
                }
            } else {
                System.out.println("Bad parameters!");
            }
        }
//        System.out.print("Enter cells: ");
//        TicTacToeCells cells = new TicTacToeCells(scanner.nextLine());
        cells = new TicTacToeCells("_________");
        System.out.println(cells.toString());

        TurnResult turnResult;
        PlayerType playerType;

        while (true) {
            for (int i = 1; i <= 2; i++) {
                if (i == 1) {
                    playerType = firstPlayerType;
                } else {
                    playerType = secondPlayerType;
                }
                turnResult = TurnResult.RE_TURN;
                while (turnResult == TurnResult.RE_TURN) {
                    if (playerType == PlayerType.USER) {
                        turnResult = playerTurn(scanner);
                    } else {
                        turnResult = computerTurn(playerType);
                    }
                    switch (turnResult) {
                        case ERROR:
                        case END_GAME:
                            return;
                    }
                }
            }
        }
    }
}
