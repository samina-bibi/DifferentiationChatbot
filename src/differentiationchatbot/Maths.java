/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package differentiationchatbot;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author Samin
 */
public class Maths {

    Parser parser = new Parser();
    String gDiff = "", fDiff = "", g = "", f = "";

    //Contructor
    Maths() {

    }

    public String identifyRule(String term) {
        String differentiatedTerm = "not found";

        parser.identifyRule(term);

        boolean eulersNo = parser.eulers;
        boolean trig = parser.trig;
        boolean log = parser.natLog;

        if (parser.constant) {
            differentiatedTerm = constantRule();
        } else if (parser.power) {
            differentiatedTerm = powerRule(term);
        } else if (eulersNo || trig || log) {
            int type = 0;
            if (eulersNo) {
                type = 1;
            } else if (trig) {
                type = 2;
            } else if (log) {
                type = 3;
            }
            differentiatedTerm = stanDiffs(term, type);
        } else if (parser.chain) {
            differentiatedTerm = chainRule(term);
        } else if (parser.product) {
            differentiatedTerm = productRule(term);
        } else if (parser.quotient) {
            differentiatedTerm = quotientRule(term);
        } else {
            return differentiatedTerm;
        }

        return differentiatedTerm;
    }

    private String constantRule() {
        String newTerm = "0";
        return newTerm;
    }

    private String stanDiffs(String term, int typeOfStanDiff) {
        String differential = "";

        switch (typeOfStanDiff) {
            case 1:
                //e^x or e^kx
                differential = eulersNo(term);
                break;
            case 2:
                //Trigonometric Functions (sin, cos or tan)
                differential = trigFunctions(term);
                break;
            case 3:
                differential = naturalLog();
                break;
            default:
                break;
        }

        return differential;
    }

    private String eulersNo(String term) {
        //Identifying the relevant values in order to obtain the new coeffient        
        //ke^cx, where k is being found
        String coefficient = parser.identifyCoefficient(term, "e");
        //ke^cx, where cx is being found
        String exponent = parser.identifyPower(term);
        //ke^cx, where c is being found
        String xCoefficient = parser.identifyCoefficient(exponent, "x");

        //Calculating the new coefficient to find the differential of the term.
        //First writing coefficients as floats
        double c;
        double cVal;
        double cxVal;
        if (coefficient.contains("/") && xCoefficient.contains("/")) {
            cVal = fractionToDecimal(coefficient);
            cxVal = fractionToDecimal(xCoefficient);
        } else if (xCoefficient.contains("/")) {
            cxVal = fractionToDecimal(xCoefficient);
            cVal = Float.valueOf(coefficient);
        } else if (coefficient.contains("/")) {
            cVal = fractionToDecimal(coefficient);
            cxVal = Float.valueOf(xCoefficient);
        } else {
            cVal = Float.valueOf(coefficient);
            cxVal = Float.valueOf(xCoefficient);
        }

        c = cVal * cxVal;

        //Getting the string value of the new coefficient
        String newCoefficient = String.valueOf(c);
        if (!newCoefficient.contains(".0")) {
            newCoefficient = decimalToFraction(newCoefficient);
        } else {
            newCoefficient = parser.tidyCoefficient(newCoefficient);
        }

        //Putting together the differential
        String diff = newCoefficient + "e^" + exponent;
        if (newCoefficient.equals("1")) {
            diff = "e^" + exponent;
        }
        return diff;
    }

    private String naturalLog() {
        String diff = "1/x";
        return diff;
    }

    private String trigFunctions(String term) {
        //ksin(cx), kcos(cx), ktan(cx)
        String diff;
        String[] splitTerm = term.split("");

        //First identify type of function being differentiated
        String function = "";
        String newFunction = "";
        Float multiplier = Float.valueOf(1);
        if (term.contains("sin")) {
            newFunction = "cos";
            function = "sin";
        }
        if (term.contains("cos")) {
            newFunction = "sin";
            function = "cos";
            multiplier = Float.valueOf(-1);
        }
        if (term.contains("tan")) {
            newFunction = "sec^2";
            function = "tan";
        }

        //Identifying k
        String flag = String.valueOf(function.charAt(0));
        String k = parser.identifyCoefficient(term, flag);
        double kVal;
        if (k.contains("/")) {
            kVal = fractionToDecimal(k);
        } else {
            kVal = Float.valueOf(k);
        }

        //Finding cx
        String cx = "";
        //Identifying position of open bracket
        int i = 0;
        while (!splitTerm[i].equals("(")) {
            i++;
        }

        for (int j = i + 1; j < splitTerm.length - 1; j++) {
            cx = cx + splitTerm[j];
        }

        //Identifying c
        String c = parser.identifyCoefficient(cx, "x");
        double cVal;
        if (c.contains("/")) {
            cVal = fractionToDecimal(c);
        } else {
            cVal = Float.valueOf(c);
        }

        //Getting the new coefficient
        double newC = cVal * kVal * multiplier;
        String newCoefficient = String.valueOf(newC);
        if (!newCoefficient.contains(".0")) {
            newCoefficient = decimalToFraction(newCoefficient);
        } else {
            newCoefficient = parser.tidyCoefficient(newCoefficient);
        }

        //Putting together differential
        diff = newCoefficient + newFunction + "(" + cx + ")";

        if (newCoefficient.equals("1")) {
            diff = newFunction + "(" + cx + ")";
        }

        return diff;
    }

    public String powerRule(String term) {
        //Idenitfy coefficient
        String coefficient = parser.identifyCoefficient(term, "x");
        double cVal;
        if (coefficient.contains("/")) {
            cVal = fractionToDecimal(coefficient);
        } else {
            cVal = Float.valueOf(coefficient);
        }

        //Identify exponent
        String exponent = parser.identifyPower(term);
        double eVal;
        if (exponent.contains("/")) {
            eVal = fractionToDecimal(exponent);
        } else {
            eVal = Float.valueOf(exponent);
        }

        //Finding the new coefficient for the differential
        String newCoefficient = String.valueOf(eVal * cVal);
        if (!newCoefficient.contains(".0")) {
            newCoefficient = decimalToFraction(newCoefficient);
        } else {
            newCoefficient = parser.tidyCoefficient(newCoefficient);
        }

        //Finding the new exponent for the differential
        String newExponent = String.valueOf(eVal - 1);
        if (!newExponent.contains(".0")) {
            newExponent = decimalToFraction(newExponent);
        } else {
            newExponent = parser.tidyExponent(newExponent);
        }

        String newTerm;
        if (newExponent.equals("0.0") || newExponent.equals("0")) {
            newTerm = newCoefficient;
        } else if (newExponent.equals("1.0") || newExponent.equals("-1.0") || newExponent.equals("1")) {
            newTerm = newCoefficient + "x";
        } else {
            newTerm = newCoefficient + "x^" + newExponent;
        }

        return newTerm;
    }

    private String chainRule(String term) {
        //c(f)^k
        String[] splitTerm = term.split("");

        //Finding u = f(x) - (f)
        int openB = 0;
        for (int i = 0; i < splitTerm.length; i++) {
            if (splitTerm[i].equals("(")) {
                openB = i;
                break;
            }
        }
        openB++;
        String u = "";
        while (!splitTerm[openB].equals(")")) {
            u = u + splitTerm[openB];
            openB++;
        }

        //Converting y = f(x) into y = f(u)
        String fOfu;
        String coefficient = parser.identifyCoefficient(term, "(");
        String power = "";
        for (int i = openB + 1; i < splitTerm.length; i++) {
            power = power + splitTerm[i];
        }

        /*Used 'x' instead of 'u' so that it can be used in 
        the power rule more easily */
        fOfu = coefficient + "x" + power;

        //Differential of y = f(u)
        String yDiff = powerRule(fOfu);

        //Differential of u = f(x)
        String uDiff;
        ArrayList<String> termsInU = parser.splitEquation(u);
        /*Looping through each term and using the identify rule function         
        to call the relevant rule to differentiate the term*/
        uDiff = identifyRule(termsInU.get(0));
        for (int i = 1; i < termsInU.size(); i++) {
            if (!identifyRule(termsInU.get(i)).equals("0")) {
                String temp = identifyRule(termsInU.get(i));
                uDiff = uDiff + " + " + temp;
            }
        }

        //Differential of y = f(x)
        String diff = "";

        //Replacing u ('x') with original y = f(x)
        //Array represents the differential of y = f(u)
        String[] tempSplit = yDiff.split("");

        for (int i = 0; i < tempSplit.length; i++) {
            //replacing this index with the original function and brackets
            if (tempSplit[i].equals("x")) {
                tempSplit[i] = "(" + u + ")";
            }
        }

        /*replacing variable u ('x') in the differential of y = f(u) 
        with original expression in bracket (u = f(x)) */
        yDiff = "";
        for (int i = 0; i < tempSplit.length; i++) {
            /*As the position of u ('x') had already been replaced aboove in 
            the array, change the string to be this as well.*/
            yDiff = yDiff + tempSplit[i];
        }

        /* Idenitfy open bracket and then put differential of u = f(x) after 
        coefficient of differential of y = f(u) */
        openB = 0;
        //Resplitting it into characters instead of using tempSplit
        String[] fSplit = yDiff.split("");
        for (int i = 0; i < fSplit.length; i++) {
            if (fSplit[i].equals("(")) {
                openB = i;
                break;
            }
        }

        /*Putting the original coefficient, k, of y = k(f(x)) into the 
        differential by looping through the array until the open bracket.*/
        for (int i = 0; i < openB; i++) {
            diff = diff + fSplit[i];
        }

        //Putting the differential of u = f(x) after the original coefficient
        diff = diff + "(" + uDiff + ")";

        //Putting u = f(x) back in (in terms of x) at the end of the differential
        for (int i = openB; i < fSplit.length; i++) {
            diff = diff + fSplit[i];
        }

        return diff;

    }

    private String productRule(String term) {

        // y = f(x)g(x)        
        //Parser will surround f(x) and g(x) in ()
        String[] splitTerm = term.split("");
        f = "";
        g = "";
        String newTerm;

        //Identify f(x)        
        /*Problem with identifying this way is that trig indentities may
        be incorrectly identified*/
        int i = 1;
        while (!splitTerm[i].equals(")")) {
            f = f + splitTerm[i];
            i++;
        }

        //Idenitfy g(x)
        i = i + 2;
        for (int j = i; j < splitTerm.length - 1; j++) {
            g = g + splitTerm[j];
        }

        //Now find f'(x)
        ArrayList<String> fTerms = parser.splitEquation(f);
        fDiff = identifyRule(fTerms.get(0));
        for (int j = 1; j < fTerms.size(); j++) {
            if (!identifyRule(fTerms.get(j)).equals("0")) {
                fDiff = fDiff + " + " + identifyRule(fTerms.get(j));
            }
        }

        //find g'(x)
        ArrayList<String> gTerms = parser.splitEquation(g);
        gDiff = identifyRule(gTerms.get(0));
        for (int j = 1; j < gTerms.size(); j++) {
            if (!identifyRule(gTerms.get(j)).equals("0")) {
                gDiff = gDiff + " + " + identifyRule(gTerms.get(j));
            }
        }

        //Putting together the differential
        newTerm = "(" + f + ")" + "(" + gDiff + ")"
                + "+" + "(" + g + ")" + "(" + fDiff + ")";

        return newTerm;
    }

    private String quotientRule(String term) {
        //y = f(x)/g(x)
        String[] splitTerm = term.split("");
        g = "";
        f = "";
        String newTerm;

        //Identify f(x)
        int i = 1;
        while (!splitTerm[i].equals(")")) {
            f = f + splitTerm[i];
            i++;
        }

        //Find f'(x)
        ArrayList<String> fTerms = parser.splitEquation(f);
        fDiff = identifyRule(fTerms.get(0));
        for (int j = 1; j < fTerms.size(); j++) {
            if (!identifyRule(fTerms.get(j)).equals("0")) {
                fDiff = fDiff + " + " + identifyRule(fTerms.get(j));
            }
        }

        //Idenitfy g(x)
        i = i + 3;
        for (int j = i; j < splitTerm.length - 1; j++) {
            g = g + splitTerm[j];
        }

        //Find g'(x)
        ArrayList<String> gTerms = parser.splitEquation(g);
        gDiff = identifyRule(gTerms.get(0));
        for (int j = 1; j < gTerms.size(); j++) {
            if (!identifyRule(gTerms.get(j)).equals("0")) {
                gDiff = gDiff + " + " + identifyRule(gTerms.get(j));
            }
        }

        //Putting together the differential
        newTerm = "(" + g + ")" + "(" + fDiff + ")" + "-" + "(" + f + ")" 
                + "(" + gDiff + ")" + "/" + "(" + g + ")^2";
        return newTerm;
    }

    public String differential(ArrayList<String> terms) {

        /*Looping through the terms of an equation to differentiate each one
        After differentiating it, concatenate the differentiated terms together
        */
        String differential = identifyRule(terms.get(0));
        if (terms.size() == 1) {
        } else {
            for (int i = 1; i < terms.size(); i++) {
                if (!identifyRule(terms.get(i)).equals("0")) {
                    differential = differential + " + " + identifyRule(terms.get(i));
                }
            }
        }

        return differential;

    }

    public double valueOfEquation(double xVal, String equation) {
        parser.identifyRule(equation);
        double result;

        if (parser.product) {
            //(f)(g)
            result = valueOf2Brackets(equation, xVal);
        } else if (parser.productDiff) {
            //(f1)(f2) + (g1)(g2)
            String[] splitEquation = equation.split("");

            //Finding value of (f1)(f2)
            int i = 0;
            String f = splitEquation[i];
            int twoBrackets = 0;
            i++;
            while (twoBrackets != 2) {
                f = f + splitEquation[i];

                if (splitEquation[i].equals(")")) {
                    twoBrackets++;
                }

                i++;
            }
            i++;
            double fVal = valueOf2Brackets(f, xVal);

            //Finding value of (g1)(g2)
            String g = splitEquation[i];
            i++;
            for (int j = i; j < splitEquation.length; j++) {
                g = g + splitEquation[j];
            }
            double gVal = valueOf2Brackets(g, xVal);

            result = fVal + gVal;

        } else if (parser.quotient) {
            //(f)/(g)
            String[] splitEquation = equation.split("");

            //Finding value of f
            int i = 1;
            String f = splitEquation[i];
            i++;
            while (!splitEquation[i].equals(")")) {
                f = f + splitEquation[i];
                i++;
            }
            double fVal = singleTerms(xVal, f);

            //Finding value of g
            i = i + 3;
            String g = splitEquation[i];
            i++;
            while (!splitEquation[i].equals(")")) {
                g = g + splitEquation[i];
                i++;
            }
            double gVal = singleTerms(xVal, g);

            result = fVal / gVal;
        } else if (parser.quotientDiff) {
            //(f1)(f2) - (g1)(g2)/(h)^2
            String[] splitEquation = equation.split("");

            //Finding value of f
            int i = 0;
            String f = splitEquation[i];
            i++;
            while (!splitEquation[i].equals("-")) {
                f = f + splitEquation[i];
                i++;
            }
            double fVal = valueOf2Brackets(f, xVal);

            //Finding value of g
            i++;
            String g = splitEquation[i];
            i++;
            while (!splitEquation[i].equals("/")) {
                g = g + splitEquation[i];
                i++;
            }
            double gVal = valueOf2Brackets(g, xVal);

            //Finding value of h^2
            i = i + 2;
            String h = splitEquation[i];
            i++;
            while (!splitEquation[i].equals(")")) {
                h = h + splitEquation[i];
                i++;
            }

            double hVal = singleTerms(xVal, h);
            double hSquared = Math.pow(hVal, 2);

            result = fVal - gVal;
            result = result / hSquared;
        } else if (parser.chain) {
            //c(f)^k            
            //Finding c

            String c = parser.identifyCoefficient(equation, "(");

            //Finding f and its value when xVal is substituted in
            String[] splitEquation = equation.split("");
            int i = c.length();
            String f = splitEquation[i];
            i++;
            while (!splitEquation[i].equals(")")) {
                f = f + splitEquation[i];
                i++;
            }
            double fVal = singleTerms(xVal, f);

            //Finding k
            i = i + 2;
            String k = splitEquation[i];
            i++;
            for (int j = i; j < splitEquation.length; j++) {
                k = k + splitEquation[i];
            }
            double kVal = Double.valueOf(k);

            //Raise value of f to k
            result = Math.pow(fVal, kVal);

            //Multiply previous value with coefficient
            double cVal = Double.valueOf(c);
            result = result * cVal;

        } else if (parser.chainDiff) {
            //c(g)(f)^k
            String[] splitEquation = equation.split("");

            //Finding c
            String c = parser.identifyCoefficient(equation, "(");

            //Finding g and its value when xVal is substituted in
            int i = c.length() + 1;
            String g = splitEquation[i];
            i++;
            while (!splitEquation[i].equals(")")) {
                g = g + splitEquation[i];
                i++;
            }
            double gVal = singleTerms(xVal, g);

            //Finding f and its value when xVal is substituted in
            i = i + 2;
            String f = splitEquation[i];
            i++;
            while (!splitEquation[i].equals(")")) {
                f = f + splitEquation[i];
                i++;
            }
            double fVal = singleTerms(xVal, f);

            //Finding k
            i = i + 2;
            String k = splitEquation[i];
            i++;
            for (int j = i; j < splitEquation.length; j++) {
                k = k + splitEquation[i];
                i++;
            }
            double kVal = Double.valueOf(k);

            //Value of f^k
            result = Math.pow(fVal, kVal);
            //Value of c * g
            double cVal = Double.valueOf(c);
            double cg = cVal * gVal;
            //Result = (cg)(f^k)
            result = cg * result;

        } else {
            result = singleTerms(xVal, equation);
        }

        //Rouding the value to 2d.p.
        DecimalFormat df = new DecimalFormat("##0.00");
        String r = df.format(result);
        result = Double.valueOf(r);

        return result;
    }

    //This method is for finding the value of an equation that just adds terms
    private double singleTerms(double xVal, String equation) {
        //Array List of the value of each term when the value for x is substituted in
        ArrayList<Double> valueOfTerms = new ArrayList<>();
        //Split into different terms
        //Find value of each term (if it contains "x")
        ArrayList<String> terms = parser.splitEquation(equation);

        for (int i = 0; i < terms.size(); i++) {
            parser.identifyRule(terms.get(i));
            double temp;
            String term = terms.get(i);

            //If the term is a constant, just get the number value of the constant
            if (parser.constant) {
                temp = Double.valueOf(terms.get(i));
                valueOfTerms.add(temp);
            }

            //If the term is a power of x, use another method to find the value
            if (parser.power) {
                temp = valueOfPower(term, xVal);
                valueOfTerms.add(temp);
            }

            //If the term uses a trig function, use another method
            if (parser.trig) {
                temp = valueOfTrig(term, xVal);
                valueOfTerms.add(temp);
            }

            //If the term uses natural logs, use another method
            if (parser.natLog) {
                temp = valueOfNatLog(xVal);
                valueOfTerms.add(temp);
            }

            //If the term uses eulers number, use another method
            if (parser.eulers) {
                temp = valueOfEulers(term, xVal);
                valueOfTerms.add(temp);
            }
        }

        double result = valueOfTerms.get(0);

        for (int i = 1; i < valueOfTerms.size(); i++) {
            result = result + valueOfTerms.get(i);
        }

        return result;

    }

    private double valueOfPower(String term, double xVal) {
        String exponent = parser.identifyPower(term);
        String coefficient = parser.identifyCoefficient(term, "x");
        double result;

        //If the term is the derivative of ln x, then do 1 divided by the x value
        if (term.equals("1/x")) {
            result = 1 / xVal;
        } else {
            //First raise value of x to the exponent
            double exp = Double.valueOf(exponent);
            result = Math.pow(xVal, exp);
            //Then multiply result with coefficient
            double coeff = Double.valueOf(coefficient);
            result = result * coeff;
        }

        return result;
    }

    private double valueOfTrig(String term, double xVal) {
        String trigCoefficient = "";
        String xCoefficient;

        //csin(kx)
        //Identify c
        if (term.contains("sin")) {
            trigCoefficient = parser.identifyCoefficient(term, "s");
        } else if (term.contains("tan")) {
            trigCoefficient = parser.identifyCoefficient(term, "t");
        } else if (term.contains("cos")) {
            trigCoefficient = parser.identifyCoefficient(term, "c");
        }

        //Idenitfy k
        String[] splitTerm = term.split("");
        int i = 0;
        while (!splitTerm[i].equals("(")) {
            i++;
        }
        i++;
        String x = splitTerm[i];
        i++;
        for (int j = i; j < splitTerm.length - 1; j++) {
            x = x + splitTerm[j];
        }
        xCoefficient = parser.identifyCoefficient(x, "x");

        //Finding the value of the parameter
        double parameter = xVal * Double.valueOf(xCoefficient);
        double trigVal = 1.0;

        /* Finding the value of the trig function when the x value is 
        multiplied by the x coefficient */
        if (term.contains("sin")) {
            trigVal = Math.sin(parameter);
        } else if (term.contains("cos")) {
            trigVal = Math.cos(parameter);
        } else if (term.contains("tan")) {
            trigVal = Math.tan(parameter);
        }

        //Multiplying the value of the trig function by the coefficient
        double result = trigVal * Double.valueOf(trigCoefficient);

        return result;
    }

    private double valueOfNatLog(double xVal) {
        return Math.log(xVal);
    }

    private double valueOfEulers(String term, double xVal) {
        double result;
        String[] splitTerm = term.split("");
        //ke^cx

        //Finding k
        String k = parser.identifyCoefficient(term, "e");

        //Finding c
        int i = 0;
        while (!splitTerm[i].equals("^")) {
            i++;
        }
        i++;
        String x = splitTerm[i];
        i++;
        for (int j = i; j < splitTerm.length; j++) {
            x = x + splitTerm[j];
        }
        String c = parser.identifyCoefficient(x, "x");

        //Raising e to the power of c * xVal
        double power = (Double.valueOf(c)) * xVal;
        result = Math.pow(Math.E, power);
        result = result * (Double.valueOf(k));

        return result;
    }

    private double valueOf2Brackets(String term, double xVal) {
        //Finding 
        //(f)(g)
        double result = 1.0;
        String[] splitTerm = term.split("");

        //Finding f, and its value when the value of x is substituted
        int i = 1;
        String f = splitTerm[i];
        i++;
        while (!splitTerm[i].equals(")")) {
            f = f + splitTerm[i];
            i++;
        }
        double fVal = singleTerms(xVal, f);

        //Finding g, and its value when the value of x is substituted
        i = i + 2;
        String g = splitTerm[i];
        i++;
        while (!splitTerm[i].equals(")")) {
            g = g + splitTerm[i];
            i++;
        }
        double gVal = singleTerms(xVal, g);

        //Multiplying f and g to get the result
        result = fVal * gVal;

        return result;
    }

    public double fractionToDecimal(String fraction) {

        String[] splitFraction = fraction.split("");
        String numerator = splitFraction[0];
        String denominator;

        //Getting numerator
        int i = 1;
        while (!splitFraction[i].equals("/")) {
            numerator = numerator + splitFraction[i];
            i++;
        }
        double n = Double.valueOf(numerator);

        //Getting denominator
        i++;
        denominator = splitFraction[i];
        i++;
        for (int j = i; j < splitFraction.length; j++) {
            denominator = denominator + splitFraction[j];
        }
        double d = Double.valueOf(denominator);

        //Dividing numerator and denominator
        double decimal = n / d;
        return decimal;
    }

    public String decimalToFraction(String decimal) {
        String fraction;
        String n = decimal;
        String[] splitN = decimal.split("");
        String d = "1";
        Boolean negative = false;

        //Getting rid of decimal point to get an integer
        int decimalPoint = decimal.indexOf(".");
        if (n.contains("-")) {
            n = splitN[1];
            negative = true;
        } else {
            n = splitN[0];
        }
        for (int i = 1; i < splitN.length; i++) {
            if (i == decimalPoint) {
            } else {
                n = n + splitN[i];
            }
        }

        // To find the power of 10 that needs to be used to make the decimal an integer
        int multiplier = decimal.length() - (decimalPoint + 1);

        for (int i = 0; i < multiplier; i++) {
            d = d + "0";
        }

        String[] tempSplitN = n.split("");
        if (tempSplitN[0].equals("0")) {
            n = tempSplitN[1];
            for (int i = 2; i < tempSplitN.length; i++) {
                n = n + tempSplitN[i];
            }
        }
        
        //Getting the greatest common factor
        String GCF = GCF(n, d);

        int num = Integer.valueOf(n);
        int den = Integer.valueOf(d);
        //Greatest common factor of numerator and denominator
        int factor = Integer.valueOf(GCF);

        num = num / factor;
        den = den / factor;

        if (negative) {
            fraction = "-" + num + "/" + den;
        } else {
            fraction = num + "/" + den;
        }

        if (decimal.equals("0.0") || decimal.equals("0.00") || decimal.equals("0")) {
            fraction = "0";
        }

        return fraction;
    }

    private String GCF(String n1, String n2) {

        int GCF = 1;

        int nOne = Integer.valueOf(n1);
        int nTwo = Integer.valueOf(n2);

        int modOne;
        int modTwo;

        int midOne = nOne / 2;
        int midTwo = nTwo / 2;
        int mid;

        if (midOne > midTwo) {
            mid = midOne;
        } else {
            mid = midTwo;
        }

        for (int i = 1; i < mid + 1; i++) {
            modOne = nOne % i;
            modTwo = nTwo % i;
            if (modOne == 0 && modTwo == 0) {
                if (i > GCF) {
                    GCF = i;
                }
            }
        }

        return String.valueOf(GCF);
    }

    public ArrayList<Double> solveForX(double val, String equation) {
        //Only works for polynomials up the 2nd degree (quadratic formula)
        ArrayList<String> terms = parser.splitEquation(equation);
        ArrayList<Double> xVals = new ArrayList<>();
        String a = "0";
        String b = "0";
        String c = "0";

        for (int i = 0; i < terms.size(); i++) {
            parser.identifyRule(terms.get(i));
            if (parser.constant) {
                c = terms.get(i);
            } else if (parser.power) {
                if (terms.get(i).contains("^2")) {
                    a = parser.identifyCoefficient(terms.get(i), "x");
                } else {
                    b = parser.identifyCoefficient(terms.get(i), "x");
                }
            }
        }

        //Converting fractions to decimals
        if (a.contains("/")) {
            a = String.valueOf(fractionToDecimal(a));
        } else if (b.contains("/")) {
            b = String.valueOf(fractionToDecimal(b));
        } else if (c.contains("/")) {
            c = String.valueOf(fractionToDecimal(c));
        }

        //Subtracting the value on the left hand side from the right hand side
        if (val != 0) {
            Double temp = Double.valueOf(c);
            temp = temp - val;
            c = String.valueOf(temp);
        }

        if (equation.contains("x^2")) {
            //Finding b^2 - 4ac and its root
            Double bSquared = Double.valueOf(b);
            bSquared = bSquared * bSquared;

            Double fourAC = Double.valueOf(a);
            fourAC = fourAC * Double.valueOf(c);
            fourAC = 4 * fourAC;

            Double root = bSquared - fourAC;
            root = Math.sqrt(root);

            //Using the quadratic formula
            Double minusB = Double.valueOf(b) * (-1);
            Double twoA = Double.valueOf(a) * 2;

            Double temp;

            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    temp = minusB + root;
                } else {
                    temp = minusB - root;
                }
                temp = temp / twoA;
                DecimalFormat df = new DecimalFormat("##0.00");
                String r = df.format(temp);
                temp = Double.valueOf(r);
                xVals.add(temp);
            }
        } else {
            //x + c = v (only of degree 1)
            Double temp = (Double.valueOf(c) * (-1)) / Double.valueOf(b);
            xVals.add(temp);
        }

        return xVals;
    }

    public String tangent(String m, double[] coords) {
        //y - y1 = m(x - x1)
        String line = "y - " + coords[1] + " = " + m + "(x - " + coords[0] + ")";
        return line;
    }

    public String normal(String m, double[] coords) {
        //y - y1 = m(x - x1)
        //Convert m to fraction
        //Find negative reciprocal
        String[] splitM = m.split("");
        String r;

        if (m.contains("/")) {
            r = negativeReciprocal(m);
        } else if (m.contains("-")) {
            m = splitM[1];
            for (int i = 2; i < splitM.length; i++) {
                m = m + splitM[i];
            }
            r = "1/" + m;
        } else {
            r = "-1/" + m;
        }

        String line = "y - " + coords[1] + " = " + r + " (x - " + coords[0] + ")";
        return line;
    }

    public String negativeReciprocal(String m) {
        String[] splitM = m.split("");
        String r;
        String n;
        String d;

        int i = 0;
        if (m.contains("-")) {
            i++;
            n = splitM[i];
        } else {
            n = splitM[i];
        }
        i++;
        while (!splitM[i].equals("/")) {
            n = n + splitM[i];
            i++;
        }
        i++;
        d = splitM[i];
        i++;
        for (int j = i; j < splitM.length; j++) {
            d = d + splitM[j];
        }

        if (m.contains("-")) {
            r = d + "/" + n;
        } else {
            r = "-" + d + "/" + n;
        }

        if (m.equals("0") || m.equals("0.0") || m.equals("0.00")) {
            r = "0";
        }

        return r;
    }

}
