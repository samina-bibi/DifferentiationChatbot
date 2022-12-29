/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package differentiationchatbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Samin
 */
public class Parser {

    public Boolean ifDerivQ = false, ifTangQ = false, ifPerpendQ = false,
            ifCoordQ = false, ifMaxQ = false, ifMinQ = false, ifStatQ = false;
    public Boolean constant = false, power = false, product = false, eulers = false,
            natLog = false, trig = false, chain = false, quotient = false,
            productDiff = false, quotientDiff = false, chainDiff = false;

    String[] operands = {"+", "-", "/", "*", "(", ")"};

    Map<String, ArrayList> wordTypes = new HashMap<>();

    //For use by GUI
    String link;
    String response;

    Parser() {
        keywordsTokens();
        queryTokens();
    }

    private void addWordType(String token, ArrayList words) {
        wordTypes.put(token, words);
    }

    private void keywordsTokens() {
        /*Creating the keyword token so that the parser can identify 
        *these words to be keywords                 
         */
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("stationary");
        keywords.add("tangent");
        keywords.add("perpendicular");
        keywords.add("turning");
        keywords.add("differentiate");

        //Adds the keyword type to the Hash Map
        addWordType("k", keywords);
    }

    private void queryTokens() {
        /*Creating query tokens so any form of a query can be accepted*/
        ArrayList<String> queries = new ArrayList<>();
        queries.add("help");
        queries.add("resources");
        queries.add("HELP");
        queries.add("RESOURCES");
        queries.add("Help");
        queries.add("Resources");

        //Adds query type to Hash Map
        addWordType("q", queries);
    }

    private String tokenizeWord(String word) {
        //for each key in wordTypes see if the word is a valid word type.
        for (String key : wordTypes.keySet()) {
            if (wordTypes.get(key).contains(word)) {
                return key;
            }
        }
        //return u if the token is unknown.
        return "u";
    }

    private String tokenizeLine(String line) {
        String tokens = "";
        //Splits a given line into different words depending on whitespace.
        String[] words = line.split("\\s+");

        /*Whilst looping through words, it will check what type of token should 
        be assigned to a word (the only type of token so far is keywords 'k')*/
        for (String word : words) {
            tokens = tokens + tokenizeWord(word);
        }

        return tokens;
    }

    private String convertPhraseToRegex(String phrase) {
        String[] phraseSplit = phrase.split("");
        phrase = "";
        /*Surround each character, including spaces, with square brackets so 
        that it can be used in a regular expression*/
        for (int i = 0; i < phraseSplit.length; i++) {
            String temp = phraseSplit[i];
            phraseSplit[i] = "[" + temp + "]";
            phrase = phrase + phraseSplit[i];
        }
        return phrase;
    }

    public Boolean checkQueryValidity(String phrase) {
        //Asking for help or resources
        String tokensInLine = tokenizeLine(phrase);
        Boolean valid;

        if (tokensInLine.contains("q") && tokensInLine.contains("k")) {
            //Provide a link to specific type of questions, even reccomend type of topic to practice
            //Stationary, tangent, perpendicular, turning, stationary, differentiate
            String qType = "differentiating";
            if (phrase.contains("tangent")) {
                qType = "tangents to the curve";
            } else if (phrase.contains("perpendicular")) {
                qType = "perpendiculars to the curve";
            } else if (phrase.contains("turning")) {
                qType = "turing points of a curve";
            } else if (phrase.contains("stationary")) {
                qType = "stationary points of a curve"; 
            }             

            if (phrase.contains("help") || phrase.contains("Help") || phrase.contains("HELP")) {
                link = "https://www.youtube.com/channel/UCCgGyPD6MYQcHuMIc-Kv-Uw";
                response = "For help with " + qType + " use the link below.";
            } else if (phrase.contains("resources") || phrase.contains("Resources") || phrase.contains("RESOURCES")) {
                link = "https://www.mathsnetalevel.com/";
                response = "For more resources on " + qType + " use the link below.";
            }                        
            valid = true;
        } else if (tokensInLine.contains("q")) {
            //Is valid, so just provide general links
            if (phrase.contains("help") || phrase.contains("Help") || phrase.contains("HELP")) {
                link = "https://www.youtube.com/channel/UCCgGyPD6MYQcHuMIc-Kv-Uw";
                response = "For help with learning topics, use the link below.";                
            } else if (phrase.contains("resources") || phrase.contains("Resources") || phrase.contains("RESOURCES")) {
                link = "https://www.mathsnetalevel.com/";
                response = "For more resources, use the buttons on the main menu to get randomly generated questions."
                        + "\nOr use the link below for exam style questions.";
            }
            valid = true;
        } else {
            valid = false;
        }

        return valid;
    }

    public Boolean checkQuestionValidity(String input) {

        //Checking if it maches the form of 'finding the derivative', see App.3
        String derivQ = convertPhraseToRegex("Differentiate the following with respect to x, ")
                + "[A-Za-z0-9\\p{Punct} ]*[\\p{Punct}]";
        Pattern rDerivQ = Pattern.compile(derivQ);
        Matcher mDerivQ = rDerivQ.matcher(input);
        ifDerivQ = mDerivQ.matches();

        //Checking if it mathces form of 'finding the tangent, see App. 3
        String tangQ = convertPhraseToRegex("Find the equation of the tangent to the curve y = ")
                + "[A-Za-z0-9\\p{Punct} ]*" + convertPhraseToRegex(" at the point ")
                + "[(][0-9\\.{Punct}]*[, ]*[0-9\\.{Punct}]*[)][\\p{Punct}]";
        Pattern rTangQ = Pattern.compile(tangQ);
        Matcher mTangQ = rTangQ.matcher(input);
        ifTangQ = mTangQ.matches();

        //Checking if it matches form of 'finding the perpendicular', see App. 3
        String perpendQ = convertPhraseToRegex("Find the equation of the perpendicular to the curve y = ")
                + "[A-Za-z0-9\\p{Punct} ]*" + convertPhraseToRegex(" at the point ")
                + "[(][0-9\\.{Punct}]*[, ]*[0-9\\.{Punct}]*[)][\\p{Punct}]";
        Pattern rPerpendQ = Pattern.compile(perpendQ);
        Matcher mPerpendQ = rPerpendQ.matcher(input);
        ifPerpendQ = mPerpendQ.matches();

        //Checking if it matches form of 'finding the coordinates of a point', see App. 3
        String coordQ = convertPhraseToRegex("Find the point on the curve y = ")
                + "[x0-9\\^{Punct}\\-{Punct}\\+{Punct}\\/{Punct} ]*" + convertPhraseToRegex(" when the gradient is equal to ")
                + "[0-9\\/{Punct}\\-{Punct}\\.{Punct}]*[\\p{Punct}]";
        Pattern rCoordQ = Pattern.compile(coordQ);
        Matcher mCoordQ = rCoordQ.matcher(input);
        ifCoordQ = mCoordQ.matches();

        //Checking if it matches form of 'fiding max/min/stationary points', see App. 3
        String maxQ = convertPhraseToRegex("Find the maximum point of the curve y = ")
                + "[x0-9\\^{Punct}\\-{Punct}\\+{Punct}\\/{Punct} ]*[\\p{Punct}]";
        Pattern rMax = Pattern.compile(maxQ);
        Matcher mMax = rMax.matcher(input);
        ifMaxQ = mMax.matches();

        String minQ = convertPhraseToRegex("Find the minimum point of the curve y = ")
                + "[x0-9\\^{Punct}\\-{Punct}\\+{Punct}\\/{Punct} ]*[\\p{Punct}]";
        Pattern rMin = Pattern.compile(minQ);
        Matcher mMin = rMin.matcher(input);
        ifMinQ = mMin.matches();

        String stationaryQ = convertPhraseToRegex("Find the stationary point of the curve y = ")
                + "[A-Za-z0-9\\p{Punct} ]*[\\p{Punct}]";
        Pattern rStationary = Pattern.compile(stationaryQ);
        Matcher mStationary = rStationary.matcher(input);
        ifStatQ = mStationary.matches();

        return ifDerivQ || ifTangQ || ifPerpendQ || ifCoordQ || ifMaxQ || ifMinQ || ifStatQ;
    }

    public String identifyEquation(String input) {
        //Call this after checking validity of the input
        //Look at App. 3 for question forms and where the equation is in the sentence
        String equation = "";
        if (ifDerivQ) {
            equation = identifyEq(input, ",", ".");
        } else if (ifTangQ || ifPerpendQ) {
            equation = identifyEq(input, "=", "a");
        } else if (ifCoordQ) {
            equation = identifyEq(input, "=", "w");
        } else if (ifMaxQ || ifMinQ || ifStatQ) {
            equation = identifyEq(input, "=", ".");
        }

        return equation;
    }

    private String identifyEq(String input, String flagOne, String flagTwo) {
        String equation = "";
        //Splitting the input into characters in order to identify flags/equation
        String[] splitInput = input.split("");

        int i = 0;
        //Increasing i until the location of flagOne is found
        while (!splitInput[i].equals(flagOne)) {
            i++;
        }

        //Increasing i so that the flag is not included in the equation
        i = i + 2;

        //adding to equation until the second flag
        while (!splitInput[i].equals(flagTwo)) {
            equation = equation + splitInput[i];
            i++;
        }

        return equation;
    }

    public double[] identifyCoords(String input) {
        //Call this after confirming tangent/perpendicular question
        String[] splitInput = input.split("");

        int i = splitInput.length - 3;
        //Getting the y value
        String tempTwo = "";
        while (!splitInput[i].equals(",")) {
            tempTwo = tempTwo + splitInput[i];
            i--;
        }

        i--;

        String[] splitY = tempTwo.split("");
        tempTwo = splitY[splitY.length - 2];
        for (int j = splitY.length - 3; j > -1; j--) {
            tempTwo = tempTwo + splitY[j];
        }

        String tempOne = "";
        while (!splitInput[i].equals("(")) {
            tempOne = tempOne + splitInput[i];
            i--;
        }

        String[] splitX = tempOne.split("");
        tempOne = splitX[splitX.length - 1];
        for (int j = splitX.length - 2; j > -1; j--) {
            tempOne = tempOne + splitX[j];
        }

        double cOne = Double.valueOf(tempOne);
        double cTwo = Double.valueOf(tempTwo);

        double[] coords = new double[]{cOne, cTwo};

        return coords;
    }

    public double identifyGradientVal(String input) {
        Double gradient;
        String[] splitInput = input.split(" ");

        /*As only one type of question uses the gradient value, can say the 
        gradient is the last item in the array. (see App. 3 for question form)*/
        String tempG = splitInput[splitInput.length - 1];
        String[] tempSplit = tempG.split("");

        tempG = "";
        //This is for getting rid of the full stop that will be at the end of the gradient value
        for (int i = 0; i < tempSplit.length - 1; i++) {
            tempG = tempG + tempSplit[i];
        }

        gradient = Double.valueOf(tempG);
        return gradient;
    }

    public String identifyCoefficient(String term, String flag) {

        String[] splitTerm = term.split("");
        String coefficient = "";

        if (term.contains(flag)) {
            /*Loops through the term (splitTerm) until the flag is reached, 
        this will be the coefficient */
            if (splitTerm[0].equals(flag)) {
                coefficient = "1";
            } else if (splitTerm[0].equals("-") && splitTerm[1].equals(flag)) {
                coefficient = "-1";
            } else {
                int i = 0;
                while (!splitTerm[i].equals(flag)) {
                    coefficient = coefficient + splitTerm[i];
                    i++;
                }
            }
        } else {
            coefficient = term;
        }

        return coefficient;
    }

    public String identifyPower(String term) {

        String[] splitTerm = term.split("");
        String exponent = "";

        if (term.contains("^")) {

            int locationOfPower = 0;

            //Loops through the term to find the symbol for exponent
            for (int i = 0; i < splitTerm.length; i++) {
                if (splitTerm[i].equals("^")) {
                    locationOfPower = i;
                }
            }

            //1 is added to go to the character after the exponent
            for (int j = locationOfPower + 1; j < splitTerm.length; j++) {
                exponent = exponent + splitTerm[j];
            }
        } else {
            exponent = "1";
        }

        return exponent;

    }

    public ArrayList splitEquation(String equation) {

        //Need to store terms in an array
        ArrayList<String> terms = new ArrayList<>();

        //No need to split the terms down now if the terms uses these rules - quotient, product or chain
        if (identifyIfQuotient(equation) || identifyIfProduct(equation) || identifyIfChain(equation)) {
            terms.add(equation);
        } else if (productDiff(equation)) {
            //(f1)(f2)+(g1)(g2)
            String f1, f2, g1, g2;
            String[] splitEquation = equation.split("");

            //Finding f1
            //Know that the first index must be an open bracket, so start at i = 1
            int i = 2;
            f1 = splitEquation[1];
            while (!splitEquation[i].equals(")")) {
                f1 = f1 + splitEquation[i];
                i++;
            }

            i = i + 2;

            //Finding f2
            f2 = splitEquation[i];
            i++;
            while (!splitEquation[i].equals(")")) {
                f2 = f2 + splitEquation[i];
                i++;
            }

            i = i + 3;

            //Finding g1
            g1 = splitEquation[i];
            i++;
            while (!splitEquation[i].equals(")")) {
                g1 = g1 + splitEquation[i];
                i++;
            }

            i = i + 2;

            //Finding g2
            g2 = splitEquation[i];
            i++;
            while (!splitEquation[i].equals(")")) {
                g2 = g2 + splitEquation[i];
                i++;
            }

            terms.add(f1);
            terms.add(f2);
            terms.add(g1);
            terms.add(g2);

        } else if (quotientDiff(equation)) {
            //(f1)(f2) - (g1)(g2)/(h)^2
            String[] splitEquation = equation.split("");
            String f1, f2, g1, g2, h;

            //Finding f1
            f1 = splitEquation[1];
            int i = 2;
            while (!splitEquation[i].equals(")")) {
                f1 = f1 + splitEquation[i];
                i++;
            }

            //Finding f2
            i = i + 2;
            f2 = splitEquation[i];
            i++;
            while (!splitEquation[i].equals(")")) {
                f2 = f2 + splitEquation[i];
                i++;
            }

            //Finding g1
            i = i + 3;
            g1 = splitEquation[i];
            i++;
            while (!splitEquation[i].equals(")")) {
                g1 = g1 + splitEquation[i];
                i++;
            }

            //Finding g2
            i = i + 2;
            g2 = splitEquation[i];
            i++;
            while (!splitEquation[i].equals(")")) {
                g2 = g2 + splitEquation[i];
                i++;
            }

            //Finding h
            i = i + 2;
            h = splitEquation[i];
            i++;
            for (int j = i; j < splitEquation.length; j++) {
                h = h + splitEquation[j];
            }

            terms.add(f1);
            terms.add(f2);
            terms.add(g1);
            terms.add(g2);
            terms.add(h);

        } else {
            //Splits the terms according to white space
            String[] equationNoWhiteSpace = equation.split(" ");

            /*If the first term is not positive, then record the 
            first character of the equation (which is '-') */
            if (!equationNoWhiteSpace[0].equals(operands[1])) {
                terms.add(operands[0] + equationNoWhiteSpace[0]);
            }

            //Loop through and add terms, along with their +ve/-ve sign
            for (int i = 1; i < equationNoWhiteSpace.length - 1; i = i + 2) {
                terms.add(equationNoWhiteSpace[i] + equationNoWhiteSpace[i + 1]);
            }

            //Now go through each element and get rid of '+'        
            for (int i = 0; i < terms.size(); i++) {
                if (terms.get(i).contains("+")) {
                    String temp = terms.get(i);
                    String[] tempSplit = temp.split("");
                    temp = "";
                    for (int j = 1; j < tempSplit.length; j++) {
                        /*The new term are all the characters of the 
                        item, excecpt the '+ */
                        temp = temp + tempSplit[j];
                    }
                    terms.set(i, temp);
                }
            }
        }

        return terms;
    }

    private Boolean identifyIfQuotient(String equation) {
        //(any series of letters and operands)/(any series of letters and operands)
        String p = "[(][xsintaco0-9+-/*^ ]*[)][/][(][xsintaco0-9+-/*^ ]*[)]";

        Pattern r = Pattern.compile(p);

        Matcher m = r.matcher(equation);

        quotient = m.matches();

        return m.matches();

    }

    private Boolean identifyIfProduct(String equation) {
        //(any series of letters and operands)(any series of letters and operands)
        String p = "[(][x0-9+-/*^ ]*[)][(][x0-9+-/*^ ]*[)]";

        Pattern r = Pattern.compile(p);

        Matcher m = r.matcher(equation);

        product = m.matches();

        return m.matches();
    }

    private Boolean identifyIfChain(String equation) {
        //(any series of letters and operands)^any series of digits        
        String p1 = "[(][x0-9+-/*^ ]*[)][\\^{Punct}][\\d/]*";
        Pattern r1 = Pattern.compile(p1);
        Matcher m1 = r1.matcher(equation);

        //any series of letters and numbers(any series of letters and operands)^any series of digits        
        String p2 = "[\\d\\/{Punct}]*[(][x0-9+-/*^ ]*[)][\\^{Punct}][\\d/]*";
        Pattern r2 = Pattern.compile(p2);
        Matcher m2 = r2.matcher(equation);

        chain = m1.matches() || m2.matches();

        return m1.matches() || m2.matches();
    }

    private Boolean identifyIfTrig(String term) {
        //sin(kx)
        String p1 = "[s][i][n][(][x0-9/]*[)]";
        //ksin(kx)
        String pa1 = "[0-9/]*[s][i][n][(][x0-9/]*[)]";
        //-sin(kx)
        String pat1 = "[-][s][i][n][(][x0-9/]*[)]";
        //-ksin(kx)
        String patt1 = "[-][0-9/]*[s][i][n][(][x0-9/]*[)]";

        Pattern r1 = Pattern.compile(p1);
        Pattern ra1 = Pattern.compile(pa1);
        Pattern rat1 = Pattern.compile(pat1);
        Pattern ratt1 = Pattern.compile(patt1);

        Matcher m1 = r1.matcher(term);
        Matcher ma1 = ra1.matcher(term);
        Matcher mat1 = rat1.matcher(term);
        Matcher matc1 = ratt1.matcher(term);

        //cos(kx)
        String p2 = "[c][o][s][(][x0-9/]*[)]";
        //kcos(kx)
        String pa2 = "[0-9/]*[c][o][s][(][x0-9/]*[)]";
        //-cos(kx)
        String pat2 = "[-][c][o][s][(][x0-9/]*[)]";
        //-kcos(kx)
        String patt2 = "[-][0-9/]*[c][o][s][(][x0-9/]*[)]";

        Pattern r2 = Pattern.compile(p2);
        Pattern ra2 = Pattern.compile(pa2);
        Pattern rat2 = Pattern.compile(pat2);
        Pattern ratt2 = Pattern.compile(patt2);

        Matcher m2 = r2.matcher(term);
        Matcher ma2 = ra2.matcher(term);
        Matcher mat2 = rat2.matcher(term);
        Matcher matc2 = ratt2.matcher(term);

        //tan(kx)
        String p3 = "[t][a][n][(][x0-9]*[)]";
        //ktan(kx)
        String pa3 = "[0-9]*[t][a][n][(][x0-9]*[)]";
        //-tan(kx)
        String pat3 = "[-][t][a][n][(][x0-9]*[)]";
        //-ktan(kx)
        String patt3 = "[-][0-9]*[t][a][n][(][x0-9]*[)]";

        Pattern r3 = Pattern.compile(p3);
        Pattern ra3 = Pattern.compile(pa3);
        Pattern rat3 = Pattern.compile(pat3);
        Pattern ratt3 = Pattern.compile(patt3);

        Matcher m3 = r3.matcher(term);
        Matcher ma3 = ra3.matcher(term);
        Matcher mat3 = rat3.matcher(term);
        Matcher matc3 = ratt3.matcher(term);

        trig = m1.matches() || ma1.matches() || mat1.matches() || matc1.matches()
                || m2.matches() || ma2.matches() || mat2.matches() || matc2.matches()
                || m3.matches() || ma3.matches() || mat3.matches() || matc3.matches();

        return trig;
    }

    private Boolean identifyIfEuler(String term) {
        //e^x or e^-x
        String p1 = "[e][\\p{Punct}]*[x]";
        //e^kx or e^-kx
        String p2 = "[e][\\p{Punct}]*[\\d/]*[x]";
        //-e^x or -e^-x
        String p3 = "[-][e][\\p{Punct}]*[x]";
        //-e^kx or -e^-kx
        String p4 = "[-][e][\\p{Punct}]*[\\d/]*[x]";

        //ke^x or ke^-x
        String p5 = "[\\d/]*[e][\\p{Punct}]*[x]";
        //ke^kx or ke^-kx
        String p6 = "[\\d/]*[e][\\p{Punct}]*[\\d/]*[x]";
        //-ke^x or -ke^-x
        String p7 = "[-][\\d/]*[e][\\p{Punct}]*[x]";
        //-ke^kx or -ke^-kx
        String p8 = "[-][\\d/]*[e][\\p{Punct}]*[\\d/]*[x]";

        Pattern r1 = Pattern.compile(p1);
        Pattern r2 = Pattern.compile(p2);
        Pattern r3 = Pattern.compile(p3);
        Pattern r4 = Pattern.compile(p4);
        Pattern r5 = Pattern.compile(p5);
        Pattern r6 = Pattern.compile(p6);
        Pattern r7 = Pattern.compile(p7);
        Pattern r8 = Pattern.compile(p8);

        Matcher m1 = r1.matcher(term);
        Matcher m2 = r2.matcher(term);
        Matcher m3 = r3.matcher(term);
        Matcher m4 = r4.matcher(term);
        Matcher m5 = r5.matcher(term);
        Matcher m6 = r6.matcher(term);
        Matcher m7 = r7.matcher(term);
        Matcher m8 = r8.matcher(term);

        eulers = m1.matches() || m2.matches() || m3.matches() || m4.matches() || m5.matches() || m6.matches() || m7.matches() || m8.matches();

        return eulers;
    }

    private Boolean identifyIfNatLog(String term) {
        //ln(x)
        String p1 = "[l][n][(][x][)]";
        //lnx
        String p2 = "[l][n][x]";

        Pattern r1 = Pattern.compile(p1);
        Pattern r2 = Pattern.compile(p2);

        Matcher m1 = r1.matcher(term);
        Matcher m2 = r2.matcher(term);

        natLog = m1.matches() || m2.matches();

        return m1.matches() || m2.matches();
    }

    private Boolean identifyIfConstant(String term) {
        //Any series of digits
        String p = "[0-9]*";

        Pattern r = Pattern.compile(p);

        Matcher m = r.matcher(term);

        constant = m.matches();

        return m.matches();
    }

    private Boolean identifyIfPowerRule(String term) {
        //kx^p, k is coefficient, p is power
        String p1 = "[0-9/]*[x][\\^\\-{Punct}]*[0-9/]*";
        //x^p
        String p2 = "[x][\\^\\-{Punct}]*[0-9/]*";
        //x
        String p3 = "[x]";
        //kx
        String p4 = "[0-9/]*[x]";
        //-kx^p
        String p5 = "[\\-{Punct}][0-9/]*[x][\\^\\-{Punct}]*[0-9/]*";
        //-x^p
        String p6 = "[\\-{Punct}][x][\\^\\-{Punct}]*[0-9/]*";
        //-x
        String p7 = "[\\-{Punct}][x]";
        //-kx
        String p8 = "[\\-{Punct}][0-9/]*[x]";

        Pattern r1 = Pattern.compile(p1);
        Pattern r2 = Pattern.compile(p2);
        Pattern r3 = Pattern.compile(p3);
        Pattern r4 = Pattern.compile(p4);
        Pattern r5 = Pattern.compile(p5);
        Pattern r6 = Pattern.compile(p6);
        Pattern r7 = Pattern.compile(p7);
        Pattern r8 = Pattern.compile(p8);

        Matcher m1 = r1.matcher(term);
        Matcher m2 = r2.matcher(term);
        Matcher m3 = r3.matcher(term);
        Matcher m4 = r4.matcher(term);
        Matcher m5 = r5.matcher(term);
        Matcher m6 = r6.matcher(term);
        Matcher m7 = r7.matcher(term);
        Matcher m8 = r8.matcher(term);

        power = m1.matches() || m2.matches() || m3.matches() || m4.matches() || m5.matches() || m6.matches() || m7.matches() || m8.matches();

        //If p3 or p4 is matched, change the term to have the exponent be ^1.
        return power;
    }

    private Boolean productDiff(String equation) {
        //Checking if the differential mathces the same structure as the differential of an equation that has used the product rule.
        String p = "[(][x0-9+-/*^ ]*[)][(][x0-9+-/*^ ]*[)][+][(][x0-9+-/*^ ]*[)][(][x0-9+-/*^ ]*[)]";
        Pattern r = Pattern.compile(p);
        Matcher m = r.matcher(equation);

        productDiff = m.matches();

        return m.matches();
    }

    private Boolean quotientDiff(String equation) {
        //Checking if the differential matches the same structure as the differential of an equation that has used the quotient rule.
        String p = "[(][x0-9+-/*^ ]*[)][(][x0-9+-/*^ ]*[)][-][(][x0-9+-/*^ ]*[)][(][x0-9+-/*^ ]*[)][\\/{Punct}][(][x0-9+-/*^ ]*[)][\\^{Punct}][2]";
        Pattern r = Pattern.compile(p);
        Matcher m = r.matcher(equation);

        quotientDiff = m.matches();

        return m.matches();
    }

    private Boolean chainDiff(String equation) {
        //Checking if the differential matches the same structure as the differential of an equation that had used the chain rule
        //k()()^c
        String p = "[0-9]*[(][x0-9+-/*^ ]*[)][(][x0-9+-/*^ ]*[)][\\^{Punct}][0-9]*";
        Pattern r = Pattern.compile(p);
        Matcher m = r.matcher(equation);

        chainDiff = m.matches();

        return m.matches();
    }

    public void identifyRule(String term) {
        identifyIfConstant(term);
        identifyIfPowerRule(term);
        identifyIfTrig(term);
        identifyIfEuler(term);
        identifyIfNatLog(term);
        identifyIfProduct(term);
        identifyIfChain(term);
        identifyIfQuotient(term);
        productDiff(term);
        quotientDiff(term);
        chainDiff(term);
    }

    public String tidyCoefficient(String coefficient) {
        String newCoefficient;

        String[] splitCoefficient = coefficient.split("");
        //Get numbers before the decimal point
        int i = 0;
        String coefficientLeft = "";
        while (!splitCoefficient[i].equals(".")) {
            coefficientLeft = coefficientLeft + splitCoefficient[i];
            i++;
        }
        //Get numbers after the decimal point
        i++;
        String coefficientRight = "";
        for (int j = i; j < splitCoefficient.length; j++) {
            coefficientRight = coefficientRight + splitCoefficient[j];
        }

        if (!coefficientRight.equals("0")) {
            newCoefficient = coefficient;
        } else {
            newCoefficient = coefficientLeft;
        }

        return newCoefficient;
    }

    public String tidyExponent(String exponent) {
        String newExponent;
        String[] splitExp = exponent.split("");

        //Get numbers before the decimal point
        int i = 0;
        String expLeft = "";
        while (!splitExp[i].equals(".")) {
            expLeft = expLeft + splitExp[i];
            i++;
        }

        //Get numbers after the decimal point
        i++;
        String expRight = "";
        for (int j = i; j < splitExp.length; j++) {
            expRight = expRight + splitExp[j];
        }

        if (!expRight.equals("0")) {
            newExponent = exponent;
        } else {
            newExponent = expLeft;
        }

        return newExponent;
    }
}
