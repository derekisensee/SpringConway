package com.conway.conway;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class BoardTest {
    Board b;

    @BeforeEach
    void setUp() {
        b = new Board();
    }

    @Test
    void blinkerTest() {
        char customBoard[][] = new char[5][];
        customBoard[0] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        customBoard[1] = new char[]{'.', '.', '.', '.', '*', '.', '.', '.', '.', '.'};
        customBoard[2] = new char[]{'.', '.', '.', '.', '*', '.', '.', '.', '.', '.'};
        customBoard[3] = new char[]{'.', '.', '.', '.', '*', '.', '.', '.', '.', '.'};
        customBoard[4] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        b.customBoard(customBoard);

        char correctArray[][] = new char[5][];
        correctArray[0] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        correctArray[1] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        correctArray[2] = new char[]{'.', '.', '.', '*', '*', '*', '.', '.', '.', '.'};
        correctArray[3] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        correctArray[4] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};

        Map<String, char[][]> message;
        message = b.runTurn();
        char result[][] = message.get("board");
        Assert.assertEquals(correctArray, result);
    }

    @Test
    void blockTest() {
        char customBoard[][] = new char[5][];
        customBoard[0] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        customBoard[1] = new char[]{'.', '.', '.', '.', '*', '*', '.', '.', '.', '.'};
        customBoard[2] = new char[]{'.', '.', '.', '.', '*', '*', '.', '.', '.', '.'};
        customBoard[3] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        customBoard[4] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        b.customBoard(customBoard);

        char correctArray[][] = new char[5][];
        correctArray[0] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        correctArray[1] = new char[]{'.', '.', '.', '.', '*', '*', '.', '.', '.', '.'};
        correctArray[2] = new char[]{'.', '.', '.', '.', '*', '*', '.', '.', '.', '.'};
        correctArray[3] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        correctArray[4] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};

        Map<String, char[][]> message;
        message = b.runTurn();
        char result[][] = message.get("board");
        Assert.assertEquals(correctArray, result);
    }
}