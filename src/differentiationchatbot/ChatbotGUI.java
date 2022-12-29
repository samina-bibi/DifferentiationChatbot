/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package differentiationchatbot;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Samin
 */
public class ChatbotGUI extends JFrame implements ActionListener {

    JPanel main, chatbotScreen, userSol, practQ, practS;
    JButton btnFindX, btnDiffRules, btnStanDiffs, btnStraightLines,
            btnOpenChatbot;
    JButton btnMM1, btnMM2, btnMM3, btnMM4;
    JButton btnPracticeSols;
    JTextField userInput, userInputSol;
    JTextArea chatConvo, userSolutionExplanation, practiceQuestions, practiceSolutions;
    //Titles for each screen
    JLabel lblMenuTitle, lblUserSolTitle, lblPracticeQTitle, lblPracticeSTitle,
            lblChatscreenTitle, lblQType, lblmenuTopics;
    //For main menu
    JLabel lblFindX, lblDiffRules, lblStanDiffs, lblStraightLines;
    //For chatbot screen
    JLabel lblLink = new JLabel();
    //For practice solutions
    JComboBox<String> solutions;

    Font titleFont = new Font("Helvetica", Font.BOLD, 30);
    Font normalFont = new Font("Helvetica", Font.PLAIN, 20);
    int width = 800;
    int height = 650;
    int centreX = 300;
    int centreY = 300;

    String userQuestion = "";

    //For Practice questions and solutions
    ProblemGenerator prob = new ProblemGenerator();

    //Will be used within the action listener.
    Parser parser = new Parser();

    ChatbotGUI() {
        initialise();
    }

    private void initialise() {
        ImageIcon image;
        image = new ImageIcon("image/BMO_Bees.png");
        setIconImage(image.getImage());

        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setTitle("Differentiation Chatbot");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel scrapPanel = new JPanel();
        this.add(scrapPanel);
        mainMenu(scrapPanel);
        this.setResizable(false);
        this.setVisible(true);

    }

    private void mainMenu(JPanel panel) {

        panel.setVisible(false);

        main = new JPanel();
        main.setLayout(null);

        btnFindX = new JButton();
        btnFindX.addActionListener(this);
        btnFindX.setText("Topic 1");
        btnFindX.setBounds(50, 160, 120, 70);
        lblFindX = new JLabel("Finding the value of x when given the gradient");
        lblFindX.setBounds(220, 160, 270, 70);

        btnDiffRules = new JButton();
        btnDiffRules.addActionListener(this);
        btnDiffRules.setText("Topic 2");
        btnDiffRules.setBounds(50, 240, 120, 70);
        lblDiffRules = new JLabel("Using and Recognising Differentiation Rules");
        lblDiffRules.setBounds(220, 240, 270, 70);

        btnStanDiffs = new JButton();
        btnStanDiffs.addActionListener(this);
        btnStanDiffs.setText("Topic 3");
        btnStanDiffs.setBounds(50, 320, 120, 70);
        lblStanDiffs = new JLabel("Standard Differentials");
        lblStanDiffs.setBounds(220, 320, 270, 70);

        btnStraightLines = new JButton();
        btnStraightLines.addActionListener(this);
        btnStraightLines.setText("Topic 4");
        btnStraightLines.setBounds(50, 400, 120, 70);
        lblStraightLines = new JLabel("Tangents and Perpendicular to the curve");
        lblStraightLines.setBounds(220, 400, 270, 70);

        btnOpenChatbot = new JButton();
        btnOpenChatbot.addActionListener(this);
        btnOpenChatbot.setText("Open Chatbot");
        btnOpenChatbot.setFont(normalFont);
        btnOpenChatbot.setBounds((centreX / 2), 490, 500, 50);

        lblMenuTitle = new JLabel();
        lblMenuTitle.setText("Differentiation Chatbot");
        lblMenuTitle.setBounds(centreX / 2 + 50, 20, 330, 50);
        lblMenuTitle.setFont(titleFont);

        lblmenuTopics = new JLabel("Pick a specific topic to practice or click open chatbot to enter your queries.");
        lblmenuTopics.setBounds((centreX / 2), 80, 450, 50);

        main.add(btnFindX);
        main.add(lblFindX);
        main.add(btnDiffRules);
        main.add(lblDiffRules);
        main.add(btnStanDiffs);
        main.add(lblStanDiffs);
        main.add(btnStraightLines);
        main.add(lblStraightLines);
        main.add(btnOpenChatbot);
        main.add(lblMenuTitle);
        main.add(lblmenuTopics);

        this.add(main);

    }

    private void chatScreen(JPanel panel) {
        panel.setVisible(false);
        userQuestion = "";

        chatbotScreen = new JPanel();
        chatbotScreen.setLayout(null);

        lblChatscreenTitle = new JLabel();
        lblChatscreenTitle.setText("Chatbot");
        lblChatscreenTitle.setBounds((centreX / 2) + 190, 10, 150, 50);
        lblChatscreenTitle.setFont(titleFont);

        lblLink.setText("");
        lblLink.setForeground(Color.BLUE.darker());
        lblLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(lblLink.getText()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        lblLink.setVisible(true);
        lblLink.setBounds(50, 400, width - 100, 40);

        chatConvo = new JTextArea();
        chatConvo.setBounds(50, 60, width - 100, 340);
        chatConvo.setBackground(Color.white);
        chatConvo.append("You can give me the questions you're struggling on, and I'll solve them. \n"
                + "If you need further help or resources, just ask for it!");
        chatConvo.setEditable(false);

        userInput = new JTextField();
        userInput.setBounds(50, 450, width - 100, 40);
        userInput.setText("Enter query here...");
        userInput.addActionListener(this);

        btnMM3 = new JButton();
        btnMM3.setBounds(centreX / 2, 500, 500, 50);
        btnMM3.setText("Main Menu");
        btnMM3.addActionListener(this);

        chatbotScreen.add(userInput);
        chatbotScreen.add(chatConvo);
        chatbotScreen.add(btnMM3);
        chatbotScreen.add(lblChatscreenTitle);
        chatbotScreen.add(lblLink);

        this.add(chatbotScreen);
    }

    private void userSol(JPanel panel, String question, int complexity) {

        panel.setVisible(false);

        userSol = new JPanel();
        userSol.setLayout(null);

        lblUserSolTitle = new JLabel("Solution to user question");
        lblUserSolTitle.setBounds((centreX / 2) + 50, 10, 400, 50);
        lblUserSolTitle.setFont(titleFont);

        userInputSol = new JTextField();
        userInputSol.setBounds(40, 450, width - 100, 40);
        userInputSol.setText("Enter query here...");
        userInputSol.addActionListener(this);

        SolutionGenerator sol = new SolutionGenerator();

        userSolutionExplanation = new JTextArea();
        userSolutionExplanation.setEditable(false);
        userSolutionExplanation.setBounds(40, 60, width - 100, 375);
        //Find out how to get complexity
        userSolutionExplanation.setText(sol.solution(question, complexity));

        btnMM1 = new JButton();
        btnMM1.setBounds(centreX / 2, 500, 500, 50);
        btnMM1.setText("Main Menu");
        btnMM1.addActionListener(this);

        userSol.add(userInputSol);
        userSol.add(userSolutionExplanation);
        userSol.add(btnMM1);
        userSol.add(lblUserSolTitle);

        this.add(userSol);
    }

    private void practQ(JPanel panel, String QType) {

        panel.setVisible(false);

        practQ = new JPanel();
        practQ.setLayout(null);

        practiceQuestions = new JTextArea();
        practiceQuestions.setEditable(false);
        practiceQuestions.setBounds(40, 60, width - 100, 375);

        if (QType.equals("Finding x when given the gradient")) {
            prob.generateQuestions(0);
        } else if (QType.equals("Differentiation rules")) {
            prob.generateQuestions(1);
        } else if (QType.equals("Standard differentials")) {
            prob.generateQuestions(2);
        } else if (QType.equals("Straight lines")) {
            prob.generateQuestions(3);
        }

        for (int i = 0; i < prob.questionsAndAnswers.length; i++) {
            practiceQuestions.append("\n Q" + (i + 1) + " " + prob.questionsAndAnswers[i][0]);
            practiceQuestions.append("\n");
        }

        btnPracticeSols = new JButton();
        btnPracticeSols.addActionListener(this);
        btnPracticeSols.setLayout(null);
        btnPracticeSols.setBounds(centreX / 2, 440, 500, 50);
        btnPracticeSols.setText("Click to see solutions");

        btnMM2 = new JButton();
        btnMM2.setBounds(centreX / 2, 500, 500, 50);
        btnMM2.setText("Main Menu");
        btnMM2.addActionListener(this);

        lblQType = new JLabel();
        lblQType.setBounds((centreX / 2), 10, 500, 50);
        lblQType.setText(QType);
        lblQType.setFont(titleFont);

        practQ.add(practiceQuestions);
        practQ.add(btnPracticeSols);
        practQ.add(btnMM2);
        practQ.add(lblQType);

        this.add(practQ);

    }

    private void practS(JPanel panel) {

        panel.setVisible(false);

        /*Has a drop down box to navigate between solutions for each of 
        the questions*/
        practS = new JPanel();
        practS.setLayout(null);

        String[] sol = {"Solution 1", "Solution 2", "Solution 3",
            "Solution 4", "Solution 5", "Solution 6", "Solution 7", "Solution 8",
            "Solution 9", "Solution 10"};

        solutions = new JComboBox<>(sol);
        solutions.addActionListener(this);
        solutions.setBounds((centreX / 2) + 150, 53, 100, 30);

        lblPracticeSTitle = new JLabel("Solutions to practice questions");
        lblPracticeSTitle.setBounds((centreX / 2), 5, 450, 50);
        lblPracticeSTitle.setFont(titleFont);

        practiceSolutions = new JTextArea();
        practiceSolutions.setEditable(false);
        practiceSolutions.setBounds(50, 90, width - 100, 400);
        practiceSolutions.setText("\n Answer to Q1 \n" + prob.questionsAndAnswers[0][1]);

        btnMM4 = new JButton();
        btnMM4.setBounds(centreX / 2, 500, 500, 50);
        btnMM4.setText("Main Menu");
        btnMM4.addActionListener(this);

        practS.add(btnMM4);
        practS.add(practiceSolutions);
        practS.add(lblPracticeSTitle);
        practS.add(solutions);

        this.add(practS);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //Practice Questions Buttons
        if (e.getSource().equals(btnOpenChatbot)) {
            chatScreen(main);
        } else if (e.getSource().equals(btnFindX)) {
            practQ(main, "Finding x when given the gradient");
        } else if (e.getSource().equals(btnDiffRules)) {
            practQ(main, "Differentiation rules");
        } else if (e.getSource().equals(btnStanDiffs)) {
            practQ(main, "Standard differentials");
        } else if (e.getSource().equals(btnStraightLines)) {
            practQ(main, "Straight lines");
        }

        //Main Menu buttons
        if (e.getSource().equals(btnMM1)) {
            mainMenu(userSol);
        } else if (e.getSource().equals(btnMM2)) {
            mainMenu(practQ);
        } else if (e.getSource().equals(btnMM3)) {
            mainMenu(chatbotScreen);
        } else if (e.getSource().equals(btnMM4)) {
            mainMenu(practS);
        } else if (e.getSource().equals(btnPracticeSols)) {
            practS(practQ);
        }

        //User input - on chatbot screen
        if (e.getSource().equals(userInput)) {
            String q = userInput.getText();
            Boolean openSol = parser.checkQuestionValidity(q);
            Boolean queryValidity = parser.checkQueryValidity(q);
            if (openSol) {
                userQuestion = q;

                chatConvo.append("\n Do you want an in depth explanation? (Yes or no).");
                userInput.setText("Enter query here...");

            } else if (parser.checkQuestionValidity(userQuestion)) {
                String ansComplexity = userInput.getText().toUpperCase();

                if (ansComplexity.equals("YES")) {
                    userSol(chatbotScreen, userQuestion, 1);
                } else if (ansComplexity.equals("NO")) {
                    userSol(chatbotScreen, userQuestion, 2);
                }

            } else if (queryValidity) {
                String temp = chatConvo.getText();
                temp = temp + "\n" + parser.response;
                chatConvo.setText(temp);
                lblLink.setText(parser.link);
            } else {
                String temp = chatConvo.getText();
                temp = temp + "\n Invalid input";
                chatConvo.setText(temp);
            }
        }

        //JComboBox for solutions to practice solutions
        if (e.getSource().equals(solutions)) {
            int questionNo = solutions.getSelectedIndex();
            practiceSolutions.setText("\n Answer to Q" + (questionNo + 1) + "\n" + prob.questionsAndAnswers[questionNo][1]);
        }
    }

}
