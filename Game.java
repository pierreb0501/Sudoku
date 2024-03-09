import java.util.*;
import java.lang.*;
import java.io.*;


public class Game {

    public Board sudoku;

    public class Cell{
        private int row = 0;
        private int column = 0;

        public Cell(int row, int column) {
            this.row = row;
            this.column = column;
        }
        public int getRow() {
            return row;
        }
        public int getColumn() {
            return column;
        }
    }

    public class Region{
        private Cell[] matrix;
        private int num_cells;
        public Region(int num_cells) {
            this.matrix = new Cell[num_cells];
            this.num_cells = num_cells;
        }
        public Cell[] getCells() {
            return matrix;
        }
        public void setCell(int pos, Cell element){
            matrix[pos] = element;
        }

    }

    public class Board{
        private int[][] board_values;
        private Region[] board_regions;
        private int num_rows;
        private int num_columns;
        private int num_regions;

        public Board(int num_rows,int num_columns, int num_regions){
            this.board_values = new int[num_rows][num_columns];
            this.board_regions = new Region[num_regions];
            this.num_rows = num_rows;
            this.num_columns = num_columns;
            this.num_regions = num_regions;
        }

        public int[][] getValues(){
            return board_values;
        }
        public int getValue(int row, int column) {
            return board_values[row][column];
        }
        public Region getRegion(int index) {
            return board_regions[index];
        }
        public Region[] getRegions(){
            return board_regions;
        }
        public void setValue(int row, int column, int value){
            board_values[row][column] = value;
        }
        public void setRegion(int index, Region initial_region) {
            board_regions[index] = initial_region;
        }
        public void setValues(int[][] values) {
            board_values = values;
        }

    }

    public int[][] solver() {
        solve();
        return sudoku.getValues();
    }

    public boolean solve(){
        for (int row = 0; row < sudoku.num_rows; row++){
            for (int col = 0; col < sudoku.num_columns; col++){
                if (sudoku.getValue(row,col) == -1){
                    Region region = findRegionByCell(row,col);
                    for (int num = 1; num <= region.num_cells; num++){
                        if (isValidPlacement(row,col,num,region)){
                            sudoku.setValue(row,col,num);
                            if (solve()){
                                return true;
                            }else{
                                sudoku.setValue(row,col,-1);
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidPlacement (int row,int col,int num,Region region){
        return !isInRegion(region,num) && !isAdjacent(row,col,num);
    }

    private boolean isInRegion (Region region, int num){
        for (Cell cell: region.getCells()){
            if (sudoku.getValue(cell.getRow(), cell.getColumn()) == num){
                return true;
            }
        }
        return false;
    }

    private boolean isAdjacent (int row, int col, int num){
        int[] rowCheck = {-1,-1,-1,0,0,1,1,1};
        int[] colCheck = {-1,0,1,-1,1,-1,0,1};
        for (int i = 0; i < rowCheck.length; i++){
            int newRow = row + rowCheck[i];
            int newCol = col + colCheck[i];

            if (newRow >= 0 && newRow < sudoku.num_rows && newCol >= 0 && newCol < sudoku.num_columns){
                if (sudoku.getValue(newRow, newCol) == num){
                    return true;
                }
            }
        }
        return false;
    }

    private Region findRegionByCell(int row, int col){
        for (Region region : sudoku.getRegions()){
            for (Cell cell : region.getCells()){
                if (cell.getRow() == row && cell.getColumn() == col){
                    return region;
                }
            }
        }
        return null;
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int rows = sc.nextInt();
        int columns = sc.nextInt();
        int[][] board = new int[rows][columns];
        //Reading the board
        for (int i=0; i<rows; i++){
            for (int j=0; j<columns; j++){
                String value = sc.next();
                if (value.equals("-")) {
                    board[i][j] = -1;
                }else {
                    try {
                        board[i][j] = Integer.valueOf(value);
                    }catch(Exception e) {
                        System.out.println("Ups, something went wrong");
                    }
                }
            }
        }
        int regions = sc.nextInt();
        Game game = new Game();
        game.sudoku = game.new Board(rows, columns, regions);
        game.sudoku.setValues(board);
        for (int i=0; i< regions;i++) {
            int num_cells = sc.nextInt();
            Game.Region new_region = game.new Region(num_cells);
            for (int j=0; j< num_cells; j++) {
                String cell = sc.next();
                String value1 = cell.substring(cell.indexOf("(") + 1, cell.indexOf(","));
                String value2 = cell.substring(cell.indexOf(",") + 1, cell.indexOf(")"));
                Game.Cell new_cell = game.new Cell(Integer.valueOf(value1)-1,Integer.valueOf(value2)-1);
                new_region.setCell(j, new_cell);
            }
            game.sudoku.setRegion(i, new_region);
        }
        int[][] answer = game.solver();
        for (int i=0; i<answer.length;i++) {
            for (int j=0; j<answer[0].length; j++) {
                System.out.print(answer[i][j]);
                if (j<answer[0].length -1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }



}