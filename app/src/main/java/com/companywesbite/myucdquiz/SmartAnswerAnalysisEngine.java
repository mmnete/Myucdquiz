package com.companywesbite.myucdquiz;

public class SmartAnswerAnalysisEngine {


    static double calcScore(String x, String y){
        if (x.length()<y.length()){
            System.out.println("a");
            return 1 - (double) calcDistance(x, y)/y.length();
        }
        if (x.length() >= y.length()) {
            System.out.println("b");
            System.out.println("CalcDistance:" + calcDistance(x, y));
            System.out.println("Length" + x.length());
            return 1 - (double) calcDistance(x,y)/x.length();
        }
        return 0;
    }

    static int calcDistance(String x, String y) {

        //Create a two dimensional int array with the length of the Strings
        int[][] calculationArray = new int[x.length() + 1][y.length() + 1];

        //Go through String x
        for (int i = 0; i <= x.length(); i++) {
            //Go through String y
            for (int j = 0; j <= y.length(); j++) {
                //in the first row/column write the String index
                if (i == 0) calculationArray[i][j] = j;
                else if (j == 0) calculationArray[i][j] = i;
                    // Calculate the minimum of the cost of the the basic actions
                else {
                    calculationArray[i][j] = min(
                            //Substitution cost
                            calculationArray[i - 1][j - 1] + substitutionCost(x.charAt(i - 1), y.charAt(j - 1)),
                            //Deletion cost
                            calculationArray[i - 1][j] + 1,
                            //Insertion cost
                            calculationArray[i][j - 1] + 1);
                }
            }
        }

        return calculationArray[x.length()][y.length()];
    }
    public static int substitutionCost(char a, char b) {
        //If a equals b, no cost, if it doesn't, cost of 1
        return a == b ? 0 : 1;
    }

    public static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

}
