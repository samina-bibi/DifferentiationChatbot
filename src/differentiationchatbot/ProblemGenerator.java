/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package differentiationchatbot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Samin
 */
public class ProblemGenerator {

    Random rand = new Random();
    SolutionGenerator sol = new SolutionGenerator();
    Maths maths = new Maths();
    Parser parser = new Parser();
    //Rows - Questions, Columns - Answers
    String question;
    String solution;
    String[][] questionsAndAnswers = new String[10][2];

    ProblemGenerator() {

    }

    public String[][] generateQuestions(int questionType) {
        switch (questionType) {
            case 0:
                valueOfXGivenM();
                break;
            case 1:
                diffRules();
                break;
            case 2:
                stanDiffs();
                break;
            case 3:
                lines();
                break;
            default:
                break;
        }

        return questionsAndAnswers;
    }

    private String polynomialTerm(Boolean random, int e) {

        int lengthOfC = rand.nextInt(3);
        String coefficient = String.valueOf(rand.nextInt(10 - 1) + 1);
        for (int i = 0; i < lengthOfC - 1; i++) {
            coefficient = coefficient + String.valueOf(rand.nextInt(10 - 1) + 1);
        }

        //Largest exponent will be 4
        String exponent = String.valueOf(rand.nextInt(4 - 1) + 1);

        if (!random) {
            exponent = String.valueOf(e);
        }

        String polynomial = coefficient + "x^" + exponent;

        if (exponent.equals("") || exponent.equals("1")) {
            polynomial = coefficient + "x";
        } else if (exponent.equals("0")) {
            polynomial = coefficient;
        }

        return polynomial;

    }

    private String stanDiffTerm() {

        String stanDiff = "";

        int typeToUse = rand.nextInt(3);
        String type = "";

        int lengthOfC = rand.nextInt(3);
        String termCoefficient = String.valueOf(rand.nextInt(10 - 1) + 1);
        for (int i = 0; i < lengthOfC - 1; i++) {
            termCoefficient = termCoefficient + String.valueOf(rand.nextInt(10 - 1) + 1);
        }

        lengthOfC = rand.nextInt(3);
        String xCoefficient = String.valueOf(rand.nextInt(10 - 1) + 1);
        for (int i = 0; i < lengthOfC - 1; i++) {
            xCoefficient = xCoefficient + String.valueOf(rand.nextInt(10 - 1) + 1);
        }

        switch (typeToUse) {
            case 0:
                type = "e";
                stanDiff = termCoefficient + type + "^" + xCoefficient + "x";
                break;
            case 1:
                stanDiff = "lnx";
                break;
            case 2:
                typeToUse = rand.nextInt(3);
                switch (typeToUse) {
                    case 0:
                        type = "sin";
                        break;
                    case 1:
                        type = "cos";
                        break;
                    case 2:
                        type = "tan";
                        break;
                    default:
                        break;
                }
                stanDiff = termCoefficient + type + "(" + xCoefficient + "x)";
                break;

            default:
                break;
        }

        return stanDiff;
    }

    private String randomEquation(Boolean stanDiff) {
        String equation;
        int typeOfEquation = rand.nextInt(3);
        switch (typeOfEquation) {
            case 1: {
                //Make an equation that needs the product rule
                //(f)(g)
                //randomize number of terms in f - up to 3 terms
                int termsInF = rand.nextInt(4);
                while (termsInF == 0) {
                    termsInF = rand.nextInt(4);
                }       //randomize number of terms in g - up to 3 terms
                int termsInG = rand.nextInt(4);
                while (termsInG == 0) {
                    termsInG = rand.nextInt(4);
                }       //Form the equation
                String f = polynomialTerm(true, -1);
                for (int i = 0; i < termsInF - 1; i++) {
                    f = f + " + " + polynomialTerm(true, -1);
                }
                String g = polynomialTerm(true, -1);
                for (int i = 0; i < termsInG - 1; i++) {
                    g = g + " + " + polynomialTerm(true, -1);
                }
                equation = "(" + f + ")(" + g + ")";
                break;
            }
            case 2: {
                //Make an equation that needs the chain rule
                //k(f)^p
                //Randomize number of terms in f - up to 3
                int termsInF = rand.nextInt(4);
                while (termsInF == 0 || termsInF == 1) {
                    termsInF = rand.nextInt(4);
                }       //Randomize length of coefficient
                int lengthOfC = rand.nextInt(3);
                String coefficient = String.valueOf(rand.nextInt(10 - 1) + 1);
                for (int i = 0; i < lengthOfC - 1; i++) {
                    coefficient = coefficient + String.valueOf(rand.nextInt(10 - 1) + 1);
                }       //Randomize length of exponent
                int lengthOfE = rand.nextInt(3);
                while (lengthOfE == 0) {
                    lengthOfE = rand.nextInt(3);
                }
                String exponent = "";
                for (int i = 0; i < lengthOfE; i++) {
                    exponent = exponent + String.valueOf(rand.nextInt(10 - 1) + 1);
                }       //Form the equation
                String f = polynomialTerm(true, -1);
                for (int i = 0; i < termsInF - 1; i++) {
                    f = f + " + " + polynomialTerm(true, -1);
                }
                equation = coefficient + "(" + f + ")^" + exponent;
                break;
            }
            default: {
                int termsInF = rand.nextInt(4);
                while (termsInF == 0) {
                    termsInF = rand.nextInt(4);
                }
                int typeOfTerm = rand.nextInt(2);
                if (!stanDiff) {
                    typeOfTerm = 0;
                }
                if (typeOfTerm == 0) {
                    equation = polynomialTerm(true, -1);
                } else {
                    equation = stanDiffTerm();
                }
                for (int i = 0; i < termsInF - 1; i++) {
                    typeOfTerm = rand.nextInt(2);
                    if (!stanDiff) {
                        typeOfTerm = 0;
                    }
                    if (typeOfTerm == 0) {
                        equation = equation + " + " + polynomialTerm(true, -1);
                    } else {
                        equation = equation + " + " + stanDiffTerm();
                    }
                }
                break;
            }
        }

        return equation;
    }

    // Topic 1
    public void valueOfXGivenM() {
        //Store questions, along with solutions, in a text file.
        for (int i = 0; i < 10; i++) {
            //Forming the quadratic equation
            String equation = polynomialTerm(false, 2);
            for (int j = 1; j > -1; j--) {
                equation = equation + " + " + polynomialTerm(false, j);
            }

            //Get a value for the gradient
            int xVal = rand.nextInt(10);

            //Find the gradient given the x value
            ArrayList<String> terms = parser.splitEquation(equation);
            String differential = maths.differential(terms);

            double gradient = maths.valueOfEquation(xVal, differential);
            String m;
            while (gradient == Double.NEGATIVE_INFINITY || gradient == Double.POSITIVE_INFINITY || gradient == Double.NaN) {
                xVal = rand.nextInt(10);
                gradient = maths.valueOfEquation(xVal, equation);
            }

            String tempG = String.valueOf(gradient);
            if (!tempG.contains(".0")) {
                tempG = maths.decimalToFraction(tempG);
                m = tempG;
            } else {
                m = String.valueOf(gradient);
            }

            //Forming the question
            question = "Find the point on the curve y = " + equation + " when the gradient is equal to " + m + ".";
            
            //Getting the solution
            solution = sol.solution(question, 2);
            
            questionsAndAnswers[i][0] = question;
            questionsAndAnswers[i][1] = solution;
        }
    }

    // Topic 2
    private void diffRules() {
        //Quotient, chain, product
        int ruleToBeUsed;

        for (int i = 0; i < 10; i++) {
            ruleToBeUsed = rand.nextInt(3);

            switch (ruleToBeUsed) {
                case 0:
                    productQuestion();
                    break;
                case 1:
                    quotientQuestion();
                    break;
                case 2:
                    chainQuestion();
                    break;
                default:
                    break;
            }
            questionsAndAnswers[i][0] = question;
            questionsAndAnswers[i][1] = solution;

        }

    }

    private void productQuestion() {
        //(f)(g)
        //randomize number of terms in f - up to 3 terms
        int termsInF = rand.nextInt(4);
        while (termsInF == 0) {
            termsInF = rand.nextInt(4);
        }
        //randomize number of terms in g - up to 3 terms
        int termsInG = rand.nextInt(4);
        while (termsInG == 0) {
            termsInG = rand.nextInt(4);
        }

        //Form the equation
        String f = polynomialTerm(true, -1);
        for (int i = 0; i < termsInF - 1; i++) {
            f = f + " + " + polynomialTerm(true, -1);
        }

        String g = polynomialTerm(true, -1);
        for (int i = 0; i < termsInG - 1; i++) {
            g = g + " + " + polynomialTerm(true, -1);
        }

        String product = "(" + f + ")(" + g + ")";

        //Put in form seen in Appendix 3 - Differentiate the following with respect to x, equation         
        question = "Differentiate the following with respect to x, " + product + ".";

        //Use solution generator
        solution = sol.solution(question, 2);
    }

    private void quotientQuestion() {
        //(f)/(g)
        //randomize number of terms in f - up to 3 terms
        int termsInF = rand.nextInt(4);
        while (termsInF == 0) {
            termsInF = rand.nextInt(4);
        }
        //randomize number of terms in g - up to 3 terms
        int termsInG = rand.nextInt(4);
        while (termsInG == 0) {
            termsInG = rand.nextInt(4);
        }

        //Form the equation
        String f = polynomialTerm(true, -1);
        for (int i = 0; i < termsInF - 1; i++) {
            f = f + " + " + polynomialTerm(true, -1);
        }

        String g = polynomialTerm(true, -1);
        for (int i = 0; i < termsInG - 1; i++) {
            g = g + " + " + polynomialTerm(true, -1);
        }

        String quotient = "(" + f + ")/(" + g + ")";

        //Put in form seen in Appendix 3 - Differentiate the following with respect to x,
        question = "Differentiate the following with respect to x, " + quotient + ".";

        //Use solution generator
        solution = sol.solution(question, 2);
    }

    private void chainQuestion() {
        //k(f)^p
        //Randomize number of terms in f - up to 3
        int termsInF = rand.nextInt(4);
        while (termsInF == 0) {
            termsInF = rand.nextInt(4);
        }

        //Randomize length of coefficient
        int lengthOfC = rand.nextInt(3);
        String coefficient = "";
        for (int i = 0; i < lengthOfC; i++) {
            coefficient = coefficient + String.valueOf(rand.nextInt(10 - 1) + 1);
        }

        //Largest exponent will be 4
        String exponent = String.valueOf(rand.nextInt(4 - 1) + 1);

        //Form the equation
        String f = polynomialTerm(true, -1);
        for (int i = 0; i < termsInF - 1; i++) {
            f = f + " + " + polynomialTerm(true, -1);
        }

        String chain = coefficient + "(" + f + ")^" + exponent;

        //Put in form seen in Appendix 3 - Differentiate the following with respect to x,
        question = "Differentiate the following with respect to x, " + chain + ".";

        //Use solution generator
        solution = sol.solution(question, 2);
    }

    // Topic 3
    private void stanDiffs() {
        //10 questions which require the user to use standard differentials
        //3 will be single terms
        //Rest will have a varying number of terms between 1 and 3.

        for (int i = 0; i < 3; i++) {
            //Put in form seen in Appendix 3 - Differentiate the following with respect to x,
            question = "Differentiate the following with respect to x, " + stanDiffTerm() + ".";
            //Store question in Array
            questionsAndAnswers[i][0] = question;
            //Store solution in Array
            solution = sol.solution(question, 2);
            questionsAndAnswers[i][1] = solution;
        }

        int termsInEquation = rand.nextInt(4);
        while (termsInEquation == 0) {
            termsInEquation = rand.nextInt(4);
        }
        for (int i = 0; i < 7; i++) {
            //Form equation
            String f = stanDiffTerm();
            for (int j = 1; j < termsInEquation; j++) {
                f = f + " + " + stanDiffTerm();
            }
            //Put in form seen in Appendix 3 - Differentiate the following with respect to x,
            question = "Differentiate the following with respect to x, " + f + ".";
            //Store question in Array
            questionsAndAnswers[i + 3][0] = question;
            //Store solution in Array
            solution = sol.solution(question, 2);
            questionsAndAnswers[i + 3][1] = solution;
        }

    }

    // Topic 4
    private void lines() {
        //Perpendicular and tangent
        for (int i = 0; i < 10; i++) {
            int typeOfLine = rand.nextInt(2);
            String line = "tangent";

            if (typeOfLine == 0) {
                line = "perpendicular";
            }

            //No standard differentials
            //No quotients
            String equation = randomEquation(false);

            double[] coordinates = new double[2];

            //Max val of 10
            double xVal = rand.nextInt(11);

            double yVal = maths.valueOfEquation(xVal, equation);

            while (yVal == Double.POSITIVE_INFINITY || yVal == Double.NEGATIVE_INFINITY || yVal == Double.NaN || yVal > 500) {
                xVal = rand.nextInt(11);
                yVal = maths.valueOfEquation(xVal, equation);
            }

            //Rounding y value to 2 decimal places
            DecimalFormat df = new DecimalFormat("###.##");
            String tempY = df.format(yVal);
            yVal = Double.valueOf(tempY);

            coordinates[0] = xVal;
            coordinates[1] = yVal;

            //Put in form seen in Appendix 3 - Find the equation of the perpendicular to the curve y = ### at the point (##, ##).
            question = "Find the equation of the " + line + " to the curve y = " + equation + " at the point (" + coordinates[0] + ", " + coordinates[1] + ").";
            questionsAndAnswers[i][0] = question;

            //Use solution generator
            solution = sol.solution(question, 2);
            questionsAndAnswers[i][1] = solution;
        }
    }

}
