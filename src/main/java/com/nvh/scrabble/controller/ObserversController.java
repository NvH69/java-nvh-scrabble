package com.nvh.scrabble.controller;

import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.view.MainWindow;

import java.util.Observer;

class ObserversController {

    static void swingObserversImplementer(Scrabble game) {
        game.addObserver((Observer) MainWindow.scoreFrame);
        game.addObserver((Observer) MainWindow.messageLabel);
        game.addObserver((Observer) MainWindow.solutionsFrame);
        game.addObserver((Observer) MainWindow.gridFrame);
        game.addObserver((Observer) MainWindow.lettersPane);
        game.addObserver((Observer) MainWindow.progressionFrame);
    }
}
