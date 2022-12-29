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
public class SolutionGenerator {

    Maths maths = new Maths();
    Parser parser = new Parser();

    SolutionGenerator() {

    }

    public String solution(String input, int complexity) {
        //complexity = 2, simplest
        //complexity = 1, most in depth
        parser.checkQuestionValidity(input);
        String solution = "";

        if (parser.ifDerivQ) {
            solution = derivative(input, complexity);
        } else if (parser.ifTangQ) {
            solution = tangent(input, complexity);
        } else if (parser.ifPerpendQ) {
            solution = perpendicular(input, complexity);
        } else if (parser.ifCoordQ) {
            solution = coordinates(input, complexity);
        } else if (parser.ifMaxQ || parser.ifMinQ || parser.ifStatQ) {
            solution = coordinates(input, complexity);
        }

        return solution;
    }

    private String derivative(String input, int complexity) {
        String solution = input + "\n";
        String equation = parser.identifyEquation(input);

        //Finding the differential of the equation and 'tidying it up'
        ArrayList<String> terms = parser.splitEquation(equation);
        String differential = maths.differential(terms);

        solution = solution + "The derivative of " + equation + " is " + differential + ".\n";

        //Step by step for each term.
        for (int i = 0; i < terms.size(); i++) {
            parser.identifyRule(terms.get(i));
            if (parser.constant) {
                solution = solution + constant(terms.get(i), complexity) + "\n";
            } else if (parser.power) {
                solution = solution + power(terms.get(i), complexity) + "\n";
            } else if (parser.eulers) {
                solution = solution + stanDiff(terms.get(i), 1, complexity) + "\n";
            } else if (parser.trig) {
                solution = solution + stanDiff(terms.get(i), 2, complexity) + "\n";
            } else if (parser.product) {
                solution = solution + product(terms.get(i), complexity) + "\n";
            } else if (parser.quotient) {
                solution = solution + quotient(terms.get(i), complexity) + "\n";
            } else if (parser.chain) {
                solution = solution + chain(terms.get(i), complexity) + "\n";
            } else if (parser.natLog) {
                solution = solution + stanDiff(terms.get(i), 3, complexity) + "\n";
            }
        }

        //return differential;
        return solution;
    }

    private String constant(String term, int complexity) {
        String s = "none";
        if (complexity == 1) {
            s = "\n The rule required for the term, " + term + " is the constant rule. "
                    + "\n Therefore, the differential of " + term + " is " + maths.identifyRule(term);
        } else if (complexity == 2) {
            s = "\n Constant rule: " + term + " -> " + maths.identifyRule(term);
        }
        return s;
    }

    private String power(String term, int complexity) {
        String diff = maths.identifyRule(term);
        String exp = parser.identifyPower(term);

        String s = "\n" + diff;
        if (complexity == 1) {
            s = "\n The rule required for the term, " + term + " is the power rule."
                    + "\n The power rule involves identifying the coefficent: " + parser.identifyCoefficient(term, "x")
                    + " and the exponent: " + parser.identifyPower(term) + ". After these are identified, the coefficient \n and the exponent"
                    + "of the derivative can then be found. \n The new coefficient is " + parser.identifyCoefficient(diff, "x")
                    + " and this is found by multiplying the coefficient and exponent. \n The new exponent is " + parser.identifyPower(diff)
                    + " and this is found by subtracting one from  the exponent. \n Therefore, the differential of " + term + " is " + diff + ".";
        } else if (complexity == 2) {
            s = "\n Power Rule: Coefficient = " + parser.identifyCoefficient(term, "x")
                    + "    Exponent = " + parser.identifyPower(term)
                    + "\n New Coefficient = " + parser.identifyCoefficient(diff, "x") + "    New Exponent = " + parser.identifyPower(diff)
                    + "\n Differential = " + diff;
        }
        return s;
    }

    private String stanDiff(String term, int type, int complexity) {
        String diff = maths.identifyRule(term);
        String s = "";

        switch (type) {
            case 1:
                {
                    //e^x or e^kx
                    
                    //To put the coefficent in the answer
                    String[] splitTerm = term.split("");
                    String x = "";
                    for (int i = 2; i < splitTerm.length; i++) {
                        x = x + splitTerm[i];
                    }       if (complexity == 1) {
                        s = "\n Standard differentials need to be used for the term, " + term
                                + ", specifically, the standard differential for e^x."
                                + "\n This involves finding the coefficient of x, which is "
                                + parser.identifyCoefficient(x, "x") + ". \n Once this is found, "
                                + "the coefficient of x is multiplied with the coefficient of e, which is 1. "
                                + "\n The new coefficient is then " + parser.identifyCoefficient(x, "x") + "\n "
                                + "Therefore, the differential of " + term + " is " + diff + ".";
                    } else if (complexity == 2) {
                        s = "\n Standard differential for e^x: "
                                + "\n New coefficient = " + parser.identifyCoefficient(x, "x")
                                + "\n Differential = " + diff + ".";
                    }       break;
                }
            case 2:
                {
                    //Solution for trig functions
                    String fC = "", diffC = "", xC;
                    if (term.contains("sin")) {
                        fC = parser.identifyCoefficient(term, "s");
                    } else if (term.contains("cos")) {
                        fC = parser.identifyCoefficient(term, "c");
                    } else if (term.contains("tan")) {
                        fC = parser.identifyCoefficient(term, "t");
                    }       if (diff.contains("sin")) {
                        diffC = parser.identifyCoefficient(diff, "s");
                    } else if (diff.contains("cos")) {
                        diffC = parser.identifyCoefficient(diff, "c");
                    } else if (diff.contains("tan")) {
                        diffC = parser.identifyCoefficient(diff, "t");
                    } else if (diff.contains("sec")) {
                        diffC = parser.identifyCoefficient(diff, "s");
                    }       String[] splitTerm = term.split("");
                    
                    //Getting whats in the brackets
                    int i = 0;
                    while (!splitTerm[i].equals("(")) {
                        i++;
                    }       
                    i++;
                    String x = splitTerm[i];
                    i++;
                    while (!splitTerm[i].equals(")")) {
                        x = x + splitTerm[i];
                        i++;
                    }      
                    xC = parser.identifyCoefficient(x, "x");
                    
                    String function = "";
                    String newFunction = "";
                    if (term.contains("sin")) {
                        function = "sin";
                        newFunction = "cos";
                    } else if (term.contains("cos")) {
                        function = "cos";
                        newFunction = "-sin";
                    } else if (term.contains("tan")) {
                        function = "tan";
                        newFunction = "sec^2";
                    }       if (complexity == 1) {
                        s = "\n Standard differentials need to be used for the term, " + term
                                + ", specifically, the standard differential for trigonometric functions."
                                + "\n This involves identifying the coefficient of the function, " + fC
                                + "\n The coefficient of the x term, " + xC + ", is then found."
                                + "\n These values are then multiplied to get the new coefficient of the differential, "
                                + diffC
                                + "\n Therefore, the differential of " + term + " is " + diff + ".";
                        
                    } else if (complexity == 2) {
                        s = "\n Standard differential for " + function + ". New function = " + newFunction + "."
                                + "\n New coefficient = " + diffC + "\n Differential = " + diff;
                    }       break;
                }
            case 3:
                //Natural log, lnx
                if (complexity == 1) {
                    s = "\n Standard differentials need to be used for the term, " + term
                            + ", specifically, the standard differential for the natural logarithm."
                            + "\n This involves finding the differential of the parameter, 1."
                            + "\n This is then put over the parameter, x."
                            + "\n Therefore, the differential of " + term + " is " + diff + ".";
                } else if (complexity == 2) {
                    s = "\n Standard differentials for natural log need to be used."
                            + "\n Differential = 1/x";
                }   break;
            default:
                break;
        }

        return s;
    }

    private String product(String term, int complexity) {
        String s = "";

        String diff = maths.identifyRule(term);
        String[] splitTerm = term.split("");

        //Finding f, g, f', g'
        String f, g, fDiff, gDiff;

        int i = 1;
        f = splitTerm[i];
        i++;
        while (!splitTerm[i].equals(")")) {
            f = f + splitTerm[i];
            i++;
        }

        i = i + 2;
        g = splitTerm[i];
        i++;
        while (!splitTerm[i].equals(")")) {
            g = g + splitTerm[i];
            i++;
        }

        ArrayList<String> fTerms = parser.splitEquation(f);
        ArrayList<String> gTerms = parser.splitEquation(g);

        fDiff = maths.differential(fTerms);
        gDiff = maths.differential(gTerms);

        if (complexity == 1) {
            s = "\n The product rule needs to be used for the term, " + term + "."
                    + "\n This involves finding the equation in the first bracket, labelled 'f', this is, "
                    + f + ". \n The equation in the second bracket, labelled 'g' is then found, this is, "
                    + g + ". \n These equations are then differentiated to get get f' = " + fDiff + " and g' = "
                    + gDiff;
        } else if (complexity == 2) {
            s = "\n Product Rule: g = " + g + ". f = " + f
                    + "\n g' = " + gDiff + ". f' = " + fDiff
                    + "\n dy/dx = (g)(f') + (f)(g') = " + diff;
        }
        return s;
    }

    private String chain(String term, int complexity) {
        String diff = maths.identifyRule(term);
        String c = parser.identifyCoefficient(term, "(");

        int i = c.length() + 1;
        String[] splitTerm = term.split("");
        String f = splitTerm[i];
        i++;
        while (!splitTerm[i].equals(")")) {
            f = f + splitTerm[i];
            i++;
        }
        i = i + 2;
        String e = splitTerm[i];
        i++;
        for (int j = i; j < splitTerm.length; j++) {
            e = e + splitTerm[i];
        }

        ArrayList<String> fTerms = parser.splitEquation(f);
        String fDiff = maths.differential(fTerms);

        String s = "\n not done - chain rule";

        if (complexity == 1) {
            s = "\n The chain rule needs to be used for the term, " + term
                    + ". \n This involves finding the equation in the brackets, " + f
                    + " and its derivative, " + fDiff + "."
                    + "\n The coefficient, " + c + ", and the exponent, " + e
                    + ", is then found and multiplied together for the new coefficient."
                    + "\n The differential is then found by putting the differential "
                    + "of the equation and the new coefficient in front of the "
                    + "original equation, and taking 1 from the exponent"
                    + "\n Therefore, the differential is " + diff;
        } else if (complexity == 2) {
            s = "\n Chain Rule: f = " + f
                    + " Coefficient = " + c + " Exponent = " + e
                    + "\n Differential = " + diff;
        }

        return s;
    }

    private String quotient(String term, int complexity) {
        String s = "";
        String diff = maths.identifyRule(term);
        String[] splitTerm = term.split("");

        int i = 1;
        String f = splitTerm[i];
        i++;
        while (!splitTerm[i].equals(")")) {
            f = f + splitTerm[i];
            i++;
        }

        ArrayList<String> fTerms = parser.splitEquation(f);
        String fDiff = maths.differential(fTerms);

        i = i + 3;
        String g = splitTerm[i];
        i++;
        for (int j = i; j < splitTerm.length - 1; j++) {
            g = g + splitTerm[j];
        }

        ArrayList<String> gTerms = parser.splitEquation(g);
        String gDiff = maths.differential(gTerms);

        if (complexity == 1) {
            s = "\n The quotient rule is needed for the term, " + term + "."
                    + "\n This involves finding the equation on the numerator (f), " + f + ", and its differential(f'), " + fDiff + "."
                    + "\n The denominator (g), " + g + ", is also found, as well as its differential (g'), " + gDiff + "."
                    + "\n The differential is then put in the form (g)(f') - (f)(g')/(g)^2"
                    + "\n Therefore, the differential is " + diff;
        } else if (complexity == 2) {
            s = "\n Quotient Rule:"
                    + "\n f = " + f + "  f' = " + fDiff
                    + "\n g = " + g + "  g' = " + gDiff
                    + "\n Differential = " + diff;
        }

        return s;
    }

    private String perpendicular(String input, int complexity) {
        /*
        * Finding the prependicular of a curve at a given point
        * Firstly, find the gradient of the curve at the point
        * To do this, differentiate the equation of the curve
        * Substitute the x value into the equation and find the value of the gradient
        * Then put the equation in the form y - y1 = m(x - x1)
        * Can simplify into y = mx + c by expanding brackets
         */
        String s = input + "\n";
        //Idenitifying coordinates and equation
        String equation = parser.identifyEquation(input);
        String[] splitEquation = equation.split("");
        if (splitEquation[splitEquation.length - 1].equals(" ")) {
            equation = splitEquation[0];
            for (int i = 1; i < splitEquation.length - 1; i++) {
                equation = equation + splitEquation[i];
            }
        }
        double[] coordinates = parser.identifyCoords(input);

        //Finding the gradient
        ArrayList<String> terms = parser.splitEquation(equation);
        String differential = maths.differential(terms);
        double gradient = maths.valueOfEquation(coordinates[0], differential);
        // Get the negative reciprocal
        DecimalFormat df = new DecimalFormat("##0.00");
        String m = df.format(gradient);
        //Work on converting decimal to fraction (doesn't work for negative or 0)
        m = maths.decimalToFraction(m);
        //Forming the straight line
        String line = maths.normal(m, coordinates);

        //Step by step for each term.
        if (complexity == 1) {
            s = s + "\nFirstly, the differential of the equation is found, this is " + differential + "."
                    + "\nThen, the gradient when x = " + coordinates[0] + " is found, this is " + m + "."
                    + "\nThen, find the negative reciprocal of m."
                    + "\nFinally, the equation of the perpendicular can be written in the form y - y1 = m(x - x1)."
                    + "\nTherefore, the perpendicular is " + line + ".";
        } else if (complexity == 2) {
            s = s + "\nDifferential = " + differential
                    + "\nGradient at x = " + coordinates[0] + " is " + m
                    + "\nThen find negative reciprocal."
                    + "\nPerpendicular = " + line;
        }
        return s;
    }

    private String tangent(String input, int complexity) {
        /*
        * Finding the tangent of a curve at a given point
        * Firstly, find the gradient of the curve at the point
        * To do this, differentiate the equation of the curve
        * Substitute the x value into the equation and find the value of the gradient
        * Then put the equation in the form y - y1 = m(x - x1)
        * Can simplify into y = mx + c by expanding brackets
         */
        String s = input + "\n";
        //Idenitifying coordinates and equation
        String equation = parser.identifyEquation(input);
        String[] splitEquation = equation.split("");
        if (splitEquation[splitEquation.length - 1].equals(" ")) {
            equation = splitEquation[0];
            for (int i = 1; i < splitEquation.length - 1; i++) {
                equation = equation + splitEquation[i];
            }
        }
        double[] coordinates = parser.identifyCoords(input);

        //Finding the gradient
        ArrayList<String> terms = parser.splitEquation(equation);
        String differential = maths.differential(terms);
        double gradient = maths.valueOfEquation(coordinates[0], differential);
        // Get the negative reciprocal
        DecimalFormat df = new DecimalFormat("##0.00");
        String m = df.format(gradient);
        //Work on converting decimal to fraction (doesn't work for negative or 0)
        m = maths.decimalToFraction(m);

        //Forming the straight line
        String straightLine = maths.tangent(m, coordinates);

        if (complexity == 1) {
            s = s + "\nFirstly, find the differential of the equation, which is dy/dx = " + differential + " ."
                    + "\nThen, find the value of the gradient at the given coordinates, this value is " + m + "."
                    + "\nThen, form an equation in the form y - y1 = m(x - x1)"
                    + "\nTherefore, the tangent is " + straightLine + ".";
        } else if (complexity == 2) {
            s = s + "\nDifferential = " + differential
                    + "\nValue of gradient at x = " + coordinates[0] + " is " + m
                    + "\nTangent = " + straightLine;
        }

        //Step by step for each term - should I do it?        
        return s;

    }

    private String coordinates(String input, int complexity) {
        String solution = input + "\n";

        String equation = parser.identifyEquation(input);
        String[] splitEquation = equation.split("");
        if (splitEquation[splitEquation.length - 1].equals(" ")) {
            equation = splitEquation[0];
            for (int i = 1; i < splitEquation.length - 1; i++) {
                equation = equation + splitEquation[i];
            }
        }
        double gradient = 0;
        parser.checkQuestionValidity(input);
        if (parser.ifCoordQ) {
            gradient = parser.identifyGradientVal(input);
        }

        //Finding the differential
        ArrayList<String> terms = parser.splitEquation(equation);
        String differential = maths.differential(terms);

        //Solving for x
        ArrayList<Double> xVals = maths.solveForX(gradient, differential);

        //Getting y coordinates
        ArrayList<Double> yVals = new ArrayList<>();

        for (int i = 0; i < xVals.size(); i++) {
            Double y = maths.valueOfEquation(xVals.get(i), equation);
            yVals.add(y);
        }

        //Putting together the coordinates (to put in solution string)
        String coords = "(" + xVals.get(0) + ", " + yVals.get(0) + ")";
        for (int i = 1; i < xVals.size(); i++) {
            coords = coords + " (" + xVals.get(i) + ", " + yVals.get(i) + ")";
        }

        //Putting together what the x coordinates are for the solution
        String xs = String.valueOf(xVals.get(0));
        for (int i = 1; i < xVals.size(); i++) {
            xs = xs + ", " + xVals.get(i);
        }

        //Putting together what the y coordinates are for the solution
        String ys = String.valueOf(yVals.get(0));
        for (int i = 1; i < yVals.size(); i++) {
            ys = ys + ", " + yVals.get(i);
        }

        //Step by step for each term.
        if (complexity == 1) {
            solution = solution + "\nFirstly, find the differential of the equation, " + differential
                    + "\nThen, set the equation to equal " + gradient
                    + "\nAfter doing this, solve for x, either by factorising or the quadratic formula,"
                    + " the solution(s) for x are " + xs
                    + "\nSubstitute the value(s) for x into the original equation to get solution(s) for y, "
                    + ys
                    + "\nTherefore, the coordinates are " + coords;
        } else if (complexity == 2) {
            solution = solution + "\ndy/dx = " + differential
                    + "\nValue of dy/dx = " + gradient
                    + "\nSolutions for x = " + xs
                    + "\nSolutions for y = " + ys
                    + "\nTherefore, the coordinates are " + coords;
        }

        return solution;
    }

}
