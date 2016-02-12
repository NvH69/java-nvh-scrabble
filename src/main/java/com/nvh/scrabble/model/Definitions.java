package com.nvh.scrabble.model;

import com.nvh.scrabble.service.ResourceLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Definitions {

    public static final List<String> definitionsDictionary = new ArrayList<>();
    private static final String definitionsFile = "/dictionaries/definitions.txt";
    private static ResourceLoader resourceLoader = new ResourceLoader();
    public Definitions() {

        int i = 0;
//        Logger logger = LoggerFactory.getLogger(Definitions.class);
//        logger.info(definitionsFile.getFile());
        try {
            BufferedReader in = new BufferedReader(new FileReader(resourceLoader.getFileFromResource(definitionsFile)));

            String line;
            while (!(line = in.readLine()).equals("*")) {

                definitionsDictionary.add(line);
            }
            in.close();
        } catch (IOException e) {
//            logger.error(e.getMessage());
        }
    }

    public static String getDefinition(String word) {
        String[] x = new String[45];
        x[0] = word;
        x[1] = word + "R";
        x[2] = word + "RE";
        if (word.endsWith("S") || word.endsWith("E")) x[3] = word.substring(0, word.length() - 1);
        else x[3] = " ";
        if (word.endsWith("IE")) x[4] = word.substring(0, word.length() - 2) + "YER";
        else x[4] = " ";
        if (word.endsWith("AI") || word.endsWith("NT") || word.endsWith("AS") || word.endsWith("AT"))
            x[5] = word.substring(0, word.length() - 2) + "ER";
        else x[5] = " ";
        if (word.endsWith("AIT") || word.endsWith("ANT") || word.endsWith("AIS") || word.endsWith("ONS") || word.endsWith("IEZ"))
            x[6] = word.substring(0, word.length() - 3) + "ER";
        else x[6] = " ";
        if (word.endsWith("AMES") || word.endsWith("ATES") || word.endsWith("IONS") || word.endsWith("ASSE"))
            x[7] = word.substring(0, word.length() - 4) + "ER";
        else x[7] = " ";
        x[8] = word.substring(0, word.length() - 1) + "R";
        x[9] = word.substring(0, word.length() - 1) + "ER";
        if (word.endsWith("NT") || word.endsWith("ES")) x[10] = word.substring(0, word.length() - 2) + "R";
        else x[10] = " ";
        if (word.endsWith("SSANT") || word.endsWith("SSENT") || word.endsWith("SSONT") || word.endsWith("SSAIS") || word.endsWith("SSAIT") || word.endsWith("SSIEZ"))
            x[11] = word.substring(0, word.length() - 5) + "R";
        else x[11] = " ";
        if (word.endsWith("IES")) x[12] = word.substring(0, word.length() - 3) + "YER";
        else x[12] = " ";
        if (word.endsWith("MES") || word.endsWith("TES") || word.endsWith("SSE") || word.endsWith("SES"))
            x[13] = word.substring(0, word.length() - 3) + "R";
        else x[13] = " ";
        if (word.endsWith("AS") || word.endsWith("AI")) x[14] = word.substring(0, word.length() - 2) + "E";
        else x[14] = " ";
        if (word.endsWith("UX")) x[15] = word.substring(0, word.length() - 2) + "L";
        else x[15] = " ";
        if (word.endsWith("E") || word.endsWith("U") || word.endsWith("S")) x[16] = word.substring(0, word.length() - 1) + "RE";
        else x[16] = " ";
        if (word.endsWith("SE")) x[17] = word.substring(0, word.length() - 2) + "X";
        else x[17] = " ";
        if (word.endsWith("AIT") || word.endsWith("IEZ") || word.endsWith("AIS")) x[18] = word.substring(0, word.length() - 3) + "E";
        else x[18] = " ";
        if (word.endsWith("IONS")) x[19] = word.substring(0, word.length() - 4) + "E";
        else x[19] = " ";
        if (word.endsWith("AIENT")) x[20] = word.substring(0, word.length() - 5) + "E";
        else x[20] = " ";
        if (word.endsWith("AS") || (word.endsWith("SE"))) x[21] = word.substring(0, word.length() - 2) + "R";
        else x[21] = " ";
        if (word.endsWith("E")) x[22] = word.substring(0, word.length() - 1) + "IR";
        else x[22] = " ";
        if (word.endsWith("AIS")) x[23] = word.substring(0, word.length() - 3) + "IR";
        else x[23] = " ";
        if (word.endsWith("VE")) x[24] = word.substring(0, word.length() - 2) + "F";
        else x[24] = " ";
        if (word.endsWith("IEZ")) x[25] = word.substring(0, word.length() - 3) + "RE";
        else x[25] = " ";
        if (word.endsWith("IONS")) x[26] = word.substring(0, word.length() - 4) + "RE";
        else x[26] = " ";
        if (word.endsWith("AIENT")) x[27] = word.substring(0, word.length() - 5) + "RE";
        else x[27] = " ";
        if (word.endsWith("AIT") || word.endsWith("ONS") || word.endsWith("AIS") || word.endsWith("VES"))
            x[28] = word.substring(0, word.length() - 3) + "RE";
        else x[28] = " ";
        if (word.endsWith("SSEZ")) x[29] = word.substring(0, word.length() - 4) + "R";
        else x[29] = " ";
        if (word.endsWith("EZ") || word.endsWith("NT") || word.endsWith("IS") || word.endsWith("IT") || word.endsWith("VE") || word.endsWith("ES") || word.endsWith("SE"))
            x[30] = word.substring(0, word.length() - 2) + "RE";
        else x[30] = " ";
        if (word.endsWith("AIT") || word.endsWith("ONS") || word.endsWith("AIS") || word.endsWith("VES") || word.endsWith("TES") || word.endsWith("SES") || word.endsWith("IEZ"))
            x[31] = word.substring(0, word.length() - 3) + "RE";
        else x[31] = " ";
        if (word.endsWith("IMES") || word.endsWith("ITES") || word.endsWith("SAIS") || word.endsWith("SAIT") || word.endsWith("SIEZ"))
            x[32] = word.substring(0, word.length() - 4) + "RE";
        else x[32] = " ";
        if (word.endsWith("ERENT")) x[33] = word.substring(0, word.length() - 5) + "RE";
        else x[33] = " ";
        if (word.endsWith("T") || word.endsWith("S")) x[34] = word.substring(0, word.length() - 1) + "DRE";
        else x[34] = " ";
        if (word.endsWith("A")) x[35] = word.substring(0, word.length() - 1);
        else x[35] = " ";
        if (word.endsWith("ES") || word.endsWith("EZ") || word.endsWith("RE") || word.endsWith("AI") || word.endsWith("AS"))
            x[36] = word.substring(0, word.length() - 2);
        else x[36] = " ";
        if (word.endsWith("AIT") || word.endsWith("ENT") || word.endsWith("AIS") || word.endsWith("ONS") || word.endsWith("ONT") || word.endsWith("IEZ") || word.endsWith("NES"))
            x[37] = word.substring(0, word.length() - 3);
        else x[37] = " ";
        if (word.endsWith("IONS")) x[38] = word.substring(0, word.length() - 4);
        else x[38] = " ";
        if (word.endsWith("AIENT")) x[39] = word.substring(0, word.length() - 5);
        else x[39] = " ";
        if (word.endsWith("AIENT")) x[40] = word.substring(0, word.length() - 5) + "ER";
        else x[40] = " ";
        if (word.endsWith("ASSENT")) x[41] = word.substring(0, word.length() - 6) + "ER";
        else x[41] = " ";
        if (word.endsWith("SES")) x[42] = word.substring(0, word.length() - 3) + "X";
        else x[42] = " ";
        x[43] = word.substring(0, word.length() - 1);
        int j = 0;
        while (x[j] != null) {
            if (x[j].length() < 2) j++;
            for (String definition : definitionsDictionary) {
                if (x[j].regionMatches(true, 0, definition, 0, x[j].length()) && definition.indexOf(" ") == x[j].length())
                    return definition;
            }
            j++;
        }
        return x[0] + " Définition inconnue";
    }
}
