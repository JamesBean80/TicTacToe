package tictactoe;

public class Coordinate {
    public int x;
    public int y;

    public Coordinate(String coordinate) {
        String[] values = coordinate.trim().split(" ");
        try {
            x = Integer.valueOf(values[0].trim());
            y = Integer.valueOf(values[1].trim());
        } catch (NumberFormatException e) {
            x = -1;
            y = -1;
        } catch (ArrayIndexOutOfBoundsException e) {
            x = -1;
            y = -1;
        }
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
