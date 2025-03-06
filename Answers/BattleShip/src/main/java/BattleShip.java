import java.util.Random;
import java.util.Scanner;

/**
 The BattleShip class manages the gameplay of the Battleship game between two players.
 It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {

    // Grid size for the game
    static final int GRID_SIZE = 10;

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner scanner = new Scanner(System.in);

    /**
     The main method that runs the game loop.
     It initializes the grids for both players, places ships randomly, and manages turns.
     The game continues until one player's ships are completely sunk.
     */
    public static void main(String[] args) {
        // Initialize grids for both players
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        // Place ships randomly on each player's grid
        placeShips(player1Grid);
        placeShips(player2Grid);

        // Variable to track whose turn it is
        boolean player1Turn = true;

        // Main game loop, runs until one player's ships are all sunk
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);

            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
    }

    static void initializeGrid(char[][] grid) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col] = '~';
            }
        }
    }

    static void placeShips(char[][] grid) {
        Random random = new Random();
        int[] SHIP_SIZES = {5, 4, 3, 2};

        for (int size : SHIP_SIZES) {
            boolean placed = false;
            while (!placed) {
                int row = random.nextInt(GRID_SIZE);
                int col = random.nextInt(GRID_SIZE);
                boolean horizontal = random.nextBoolean();

                if (canPlaceShip(grid, row, col, size, horizontal)) {
                    placeShip(grid, row, col, size, horizontal);
                    placed = true;
                }
            }
        }
    }

    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > GRID_SIZE) return false;
        } else {
            if (row + size > GRID_SIZE) return false;
        }

        for (int i = 0; i < size; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            if (grid[r][c] != '~') {
                return false;
            }
        }

        return true;
    }
    static void placeShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            for (int i = 0; i < size; i++) {
                if (col + i < grid.length) {
                    grid[row][col + i] = 'S';
                }
            }
        } else {
            
            for (int i = 0; i < size; i++) {
                if (row + i < grid.length) {
                    grid[row + i][col] = 'S';
                }
            }
        }
    }

    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        boolean validInput = false; int row, col; String input;
        while (!validInput) {
            System.out.print("Enter coordinates (for example C3): ");
            input = scanner.nextLine();
            if (isValidInput(input)) {
                row = Integer.parseInt(input.substring(1)) - 1;
                col = input.charAt(0) - 'A';

                if (trackingGrid[row][col] == 'X' || trackingGrid[row][col] == 'O') {
                    System.out.println("You've already attacked this location. Try again!");
                } else {
                    validInput = true;

                    if (opponentGrid[row][col] == 'S') {
                        System.out.println("Hit!");
                        opponentGrid[row][col] = 'X';
                        trackingGrid[row][col] = 'X';
                    } else {
                        System.out.println("Miss!");
                        trackingGrid[row][col] = 'O';
                    }
                }
            } else {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    static boolean isGameOver() {
        return allShipsSunk(player1Grid) || allShipsSunk(player2Grid);
    }

    static boolean allShipsSunk(char[][] grid) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] == 'S') {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean isValidInput(String input) {
        if (input.length() < 2 || input.length() > 3) {
            return false;
        }

        int col = input.charAt(0);
        if (col < 65 || col > 74) {
            return false;
        }

        String rowDigit = input.substring(1);
        for (int i = 0; i < rowDigit.length(); i++) {
            if (!Character.isDigit(rowDigit.charAt(i))) {
                return false;
            }
        }
        int row = Integer.parseInt(rowDigit);
        return row >= 1 && row <= 10;
    }

    static void printGrid(char[][] grid) {
        System.out.print("    ");
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
        }
        System.out.println();
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.printf("%2d  ", i + 1);
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}