package com.nvh.scrabble.view;

import com.nvh.scrabble.model.Grid;
import com.nvh.scrabble.model.Scrabble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Console {
    
    private static final Logger logger = LoggerFactory.getLogger(Console.class);
    
    public static void display(Grid g) {
        logger.info("   1   2   3   4   5   6   7   8   9   10  11  12  13  14  15" + "\n");
        logger.info(" ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐" + "\n");

        for (int i = 0; i < 29; i++) {
            if (i % 2 == 0) {

                logger.info(Scrabble.alphabet.charAt(i / 2) + "│");
                for (int j = 0; j < 15; j++)
                    if (g.getCoordinates(j, i / 2) != ' '
                            && g.getCoordinates(j, i / 2) != '#'
                            ) {
                        if (g.getBonus(j, i / 2) == 0)
                            logger.info(" " + g.getCoordinates(j, i / 2) + "*│");
                        else logger.info(" " + g.getCoordinates(j, i / 2) + " │");
                    } else logger.info("   " + "│");
            } else
                logger.info("\n" + " ├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤" + "\n");
        }
        logger.info("\n" + " └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘");
    }
}
