package com.conway.conway;

import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class Board {
    char board[][]; // board that holds spaces
    char aliveChar = '*';

    // constructors
    public Board() {
        board = new char[10][];
        board[0] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        board[1] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        board[2] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        board[3] = new char[]{'.', '.', '*', '*', '*', '.', '.', '.', '.', '.'};
        board[4] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        board[5] = new char[]{'.', '.', '.', '.', '.', '*', '*', '.', '.', '.'};
        board[6] = new char[]{'.', '.', '.', '.', '.', '*', '*', '.', '.', '.'};
        board[7] = new char[]{'.', '.', '.', '.', '.', '.', '.', '*', '*', '.'};
        board[8] = new char[]{'.', '.', '.', '.', '.', '.', '.', '*', '*', '.'};
        board[9] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
    }

    // sets board to be the given custom board
    public void customBoard(char givenBoard[][]) {
        board = givenBoard.clone();
    }

    @GetMapping("/randBoard")
    public Map<String, char[][]> genRandBoard() {
        int size = 100;
        board = new char[size][size];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Random rand = new Random();
                int r = rand.nextInt(1000);
                if (r <= 500) {
                    board[i][j] = aliveChar;
                } else
                    board[i][j] = '.';
            }
        }
        Map<String, char[][]> message = new HashMap<>();
        message.put("board", board);
        return message;
    }

    // returns shapes as a Map from the mongodb database
    @GetMapping("/getShapes")
    public Map<String, String> getShapes() {
        Map<String, String> shapes = new HashMap<>();
        MongoClient mongo = MongoClients.create();
        MongoDatabase shapeDatabase = mongo.getDatabase("shapes");
        MongoCollection<Document> shapeColl = shapeDatabase.getCollection("shapeCollection");
        MongoCursor<Document> cursor = shapeColl.find().iterator();

        try {
            while (cursor.hasNext()) {
                Document curr = cursor.next();
                for (String i : curr.keySet()) {
                    String obj = curr.get(i).toString();
                    shapes.put(i, obj);
                }
                curr.keySet();
            }
        } finally {
            cursor.close();
        }
        shapes.remove("_id");
        // should we have shapes saved in RLE format in the DB, and then have the html/js render it in-browser?
        return shapes;
    }

    // returns number of alive things surrounding coords
    // please note, there is likely some sort of unintended behavior around edges. again, this project is mostly just to learn
    // various java functions
    private int getSurroundingCount(int y, int x) {
        int c = 0;
        // check up/down/left/right
        if (y - 1 < 0) {
            if (board[board.length - 1][x] != '.') {
                c++;
            }
        } else { // if no need to wrap-around, do it the normal way
            if (board[y - 1][x] != '.') {
                c++;
            }
        }
        if (x - 1 < 0) {
            if (board[y][board[y].length - 1] != '.') {
                c++;
            }
        } else {
            if (board[y][x - 1] != '.') {
                c++;
            }
        }
        if (y + 1 >= board.length) {
            if (board[0][x] != '.') {
                c++;
            }
        } else {
            if (board[y + 1][x] != '.') {
                c++;
            }
        }
        if (x + 1 >= board[y].length) {
            if (board[y][0] != '.') {
                c++;
            }
        } else {
            if (board[y][x + 1] != '.') {
                c++;
            }
        }
        // check diagonals
        if (x - 1 < 0 || y - 1 < 0) { // top left

        } else {
            if (board[y - 1][x - 1] != '.') {
                c++;
            }
        }
        if (x + 1 >= board.length || y - 1 < 0) { // top right

        } else {
            if (board[y - 1][x + 1] != '.') {
                c++;
            }
        }
        if (x - 1 < 0 || y + 1 >= board.length) { // bottom left

        } else {
            if (board[y + 1][x - 1] != '.') {
                c++;
            }
        }
        if (y + 1 >= board.length || x + 1 >= board.length) { // bottom right

        } else {
            if (board[y + 1][x + 1] != '.') {
                c++;
            }
        }
        return c;
    }

    // returns true if
    // 1. alive cell has 2 or 3 neighbors
    // 2. dead cell has exactly 3 neighbors
    // returns false if
    // 1. alive cell has < 2 neighbors
    // 2. alive cell has > 3 neighbors
    // 3. dead cell does not have exactly 3 neighbors
    private boolean checkSurrounding(int y, int x) {
        char curr = board[y][x];
        int aliveCount = getSurroundingCount(y, x);
        // check for 3 neighbors
        if (curr == '.') {
            if (aliveCount == 3) {
                return true;
            }
        } else { // checking alive cell
            if (aliveCount < 2)
                return false;
            if (aliveCount > 3)
                return false;
            return true;
        }
        return false;
    }

    // returns board array in "message" - accessed as "response.data.board"
    @GetMapping("/board")
    public Map<String, char[][]> runTurn() {
        char[][] newBoard = new char[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (checkSurrounding(i, j) == true)
                    newBoard[i][j] = aliveChar;
                else
                    newBoard[i][j] = '.';
            }
        }
        board = newBoard;
        Map<String, char[][]> message = new HashMap<>();
        message.put("board", board);
        return message;
    }
}