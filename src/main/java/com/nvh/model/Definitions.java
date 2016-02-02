package com.nvh.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Definitions {

    private static String line;
    public static final String[] dicoDef = new String[400000];

    public Definitions() {

        int i = 0;
        System.out.println(Dictionnaire.chemin + "/dicos/definitions");

        try {
            BufferedReader in = new BufferedReader(new FileReader(Dictionnaire.chemin + "/dicos/definitions.txt"));

            while ((line = in.readLine()).equals("*") != true) {

                dicoDef[i] = line;
                i++;
            }
            in.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getDefinition(String y) {
        String[] x = new String[45];
        x[0] = y;
        x[1] = y + "R";
        x[2] = y + "RE";
        if (y.endsWith("S") || y.endsWith("E")) x[3] = y.substring(0, y.length() - 1);
        else x[3] = " ";
        if (y.endsWith("IE")) x[4] = y.substring(0, y.length() - 2) + "YER";
        else x[4] = " ";
        if (y.endsWith("AI") || y.endsWith("NT") || y.endsWith("AS") || y.endsWith("AT"))
            x[5] = y.substring(0, y.length() - 2) + "ER";
        else x[5] = " ";
        if (y.endsWith("AIT") || y.endsWith("ANT") || y.endsWith("AIS") || y.endsWith("ONS") || y.endsWith("IEZ"))
            x[6] = y.substring(0, y.length() - 3) + "ER";
        else x[6] = " ";
        if (y.endsWith("AMES") || y.endsWith("ATES") || y.endsWith("IONS") || y.endsWith("ASSE"))
            x[7] = y.substring(0, y.length() - 4) + "ER";
        else x[7] = " ";
        x[8] = y.substring(0, y.length() - 1) + "R";
        x[9] = y.substring(0, y.length() - 1) + "ER";
        if (y.endsWith("NT") || y.endsWith("ES")) x[10] = y.substring(0, y.length() - 2) + "R";
        else x[10] = " ";
        if (y.endsWith("SSANT") || y.endsWith("SSENT") || y.endsWith("SSONT") || y.endsWith("SSAIS") || y.endsWith("SSAIT") || y.endsWith("SSIEZ"))
            x[11] = y.substring(0, y.length() - 5) + "R";
        else x[11] = " ";
        if (y.endsWith("IES")) x[12] = y.substring(0, y.length() - 3) + "YER";
        else x[12] = " ";
        if (y.endsWith("MES") || y.endsWith("TES") || y.endsWith("SSE") || y.endsWith("SES"))
            x[13] = y.substring(0, y.length() - 3) + "R";
        else x[13] = " ";
        if (y.endsWith("AS") || y.endsWith("AI")) x[14] = y.substring(0, y.length() - 2) + "E";
        else x[14] = " ";
        if (y.endsWith("UX")) x[15] = y.substring(0, y.length() - 2) + "L";
        else x[15] = " ";
        if (y.endsWith("E") || y.endsWith("U") || y.endsWith("S")) x[16] = y.substring(0, y.length() - 1) + "RE";
        else x[16] = " ";
        if (y.endsWith("SE")) x[17] = y.substring(0, y.length() - 2) + "X";
        else x[17] = " ";
        if (y.endsWith("AIT") || y.endsWith("IEZ") || y.endsWith("AIS")) x[18] = y.substring(0, y.length() - 3) + "E";
        else x[18] = " ";
        if (y.endsWith("IONS")) x[19] = y.substring(0, y.length() - 4) + "E";
        else x[19] = " ";
        if (y.endsWith("AIENT")) x[20] = y.substring(0, y.length() - 5) + "E";
        else x[20] = " ";
        if (y.endsWith("AS") || (y.endsWith("SE"))) x[21] = y.substring(0, y.length() - 2) + "R";
        else x[21] = " ";
        if (y.endsWith("E")) x[22] = y.substring(0, y.length() - 1) + "IR";
        else x[22] = " ";
        if (y.endsWith("AIS")) x[23] = y.substring(0, y.length() - 3) + "IR";
        else x[23] = " ";
        if (y.endsWith("VE")) x[24] = y.substring(0, y.length() - 2) + "F";
        else x[24] = " ";
        if (y.endsWith("IEZ")) x[25] = y.substring(0, y.length() - 3) + "RE";
        else x[25] = " ";
        if (y.endsWith("IONS")) x[26] = y.substring(0, y.length() - 4) + "RE";
        else x[26] = " ";
        if (y.endsWith("AIENT")) x[27] = y.substring(0, y.length() - 5) + "RE";
        else x[27] = " ";
        if (y.endsWith("AIT") || y.endsWith("ONS") || y.endsWith("AIS") || y.endsWith("VES"))
            x[28] = y.substring(0, y.length() - 3) + "RE";
        else x[28] = " ";
        if (y.endsWith("SSEZ")) x[29] = y.substring(0, y.length() - 4) + "R";
        else x[29] = " ";
        if (y.endsWith("EZ") || y.endsWith("NT") || y.endsWith("IS") || y.endsWith("IT") || y.endsWith("VE") || y.endsWith("ES") || y.endsWith("SE"))
            x[30] = y.substring(0, y.length() - 2) + "RE";
        else x[30] = " ";
        if (y.endsWith("AIT") || y.endsWith("ONS") || y.endsWith("AIS") || y.endsWith("VES") || y.endsWith("TES") || y.endsWith("SES") || y.endsWith("IEZ"))
            x[31] = y.substring(0, y.length() - 3) + "RE";
        else x[31] = " ";
        if (y.endsWith("IMES") || y.endsWith("ITES") || y.endsWith("SAIS") || y.endsWith("SAIT") || y.endsWith("SIEZ"))
            x[32] = y.substring(0, y.length() - 4) + "RE";
        else x[32] = " ";
        if (y.endsWith("ERENT")) x[33] = y.substring(0, y.length() - 5) + "RE";
        else x[33] = " ";
        if (y.endsWith("T") || y.endsWith("S")) x[34] = y.substring(0, y.length() - 1) + "DRE";
        else x[34] = " ";
        if (y.endsWith("A")) x[35] = y.substring(0, y.length() - 1);
        else x[35] = " ";
        if (y.endsWith("ES") || y.endsWith("EZ") || y.endsWith("RE") || y.endsWith("AI") || y.endsWith("AS"))
            x[36] = y.substring(0, y.length() - 2);
        else x[36] = " ";
        if (y.endsWith("AIT") || y.endsWith("ENT") || y.endsWith("AIS") || y.endsWith("ONS") || y.endsWith("ONT") || y.endsWith("IEZ") || y.endsWith("NES"))
            x[37] = y.substring(0, y.length() - 3);
        else x[37] = " ";
        if (y.endsWith("IONS")) x[38] = y.substring(0, y.length() - 4);
        else x[38] = " ";
        if (y.endsWith("AIENT")) x[39] = y.substring(0, y.length() - 5);
        else x[39] = " ";
        if (y.endsWith("AIENT")) x[40] = y.substring(0, y.length() - 5) + "ER";
        else x[40] = " ";
        if (y.endsWith("ASSENT")) x[41] = y.substring(0, y.length() - 6) + "ER";
        else x[41] = " ";
        if (y.endsWith("SES")) x[42] = y.substring(0, y.length() - 3) + "X";
        else x[42] = " ";
        x[43] = y.substring(0, y.length() - 1);
        int j = 0;
        while (x[j] != null) {
            if (x[j].length() < 2) j++;
            int i = 0;
            while (dicoDef[i] != null && x[j] != null) {
                if (x[j].regionMatches(true, 0, dicoDef[i], 0, x[j].length()) && dicoDef[i].indexOf(" ") == x[j].length())
                    return dicoDef[i];
                i++;
            }
            j++;
        }
        return x[0] + " D�finition inconnue";
    }
}
