package App;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class Quiz extends JFrame implements ActionListener {

    String questions[][] = new String[10][5];
    String answers[][] = new String[10][1];
    String useranswers[][] = new String[10][1];

    JLabel question;
    JRadioButton opt1, opt2, opt3, opt4;
    ButtonGroup group;
    JButton next, previous, submit, help, quit;
    int current = 0;
    JButton dummyFocus;

    // Add after other field declarations
    private Timer timer;
    private JLabel timerLabel;
    private int timeLeft = 300; // 5 minutes in seconds

    public Quiz(String name) {

        dummyFocus = new JButton();
        dummyFocus.setVisible(false);
        add(dummyFocus); // Add to the frame, but not to any layout position
        setLayout(new BorderLayout());

        setSize(800, 600);
        setLayout(new BorderLayout());
        // Load image
        ImageIcon originalIcon = new ImageIcon(ClassLoader.getSystemResource("icons/quiz.png"));

        // Custom panel to paint scaled image
        JLabel imgLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = originalIcon.getImage();

                int imgWidth = img.getWidth(this);
                int imgHeight = img.getHeight(this);
                double imgAspect = (double) imgWidth / imgHeight;

                int newWidth = getWidth();
                int newHeight = (int) (newWidth / imgAspect);

                if (newHeight > getHeight()) {
                    newHeight = getHeight();
                    newWidth = (int) (newHeight * imgAspect);
                }

                int x = (getWidth() - newWidth) / 2;
                int y = (getHeight() - newHeight) / 2;

                g.drawImage(img, x, y, newWidth, newHeight, this);
            }
        };

// 👇 Give it a visible height, so BorderLayout allocates space
        imgLabel.setPreferredSize(new Dimension(800, 200));

        add(imgLabel, BorderLayout.NORTH);

        setupTimer();

        // Main panel for question and options
        // In your constructor, after adding the image and before the centerPanel:
        // Create top panel for timer and help
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        // Create timer panel on the left
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timerPanel.setBackground(Color.WHITE);
        timerPanel.add(timerLabel);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.setBackground(Color.WHITE);
        namePanel.add(new JLabel("Welcome, " + name + "!"));

        help = new JButton("Help");
        help.setBackground(new Color(22, 99, 54));
        help.setForeground(Color.WHITE);
        help.addActionListener(this);
        // Create help panel on the right
        JPanel helpPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        helpPanel.setBackground(Color.WHITE);
        helpPanel.add(help);

        // Add both panels to the top panel
        topPanel.add(timerPanel, BorderLayout.WEST);
        topPanel.add(helpPanel, BorderLayout.EAST);
        topPanel.add(namePanel, BorderLayout.CENTER);

        // Create a main panel to hold both top panel and center panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

// Add both panels to the top panel
        topPanel.add(timerPanel, BorderLayout.WEST);
        topPanel.add(helpPanel, BorderLayout.EAST);

// Add padding to top panel
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

// Add the top panel below the image
        mainPanel.add(topPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel();
// Change centerPanel to be added to the middle
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // Add padding to centerPanel
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));

        // Add vertical spacing after question label
        centerPanel.add(Box.createVerticalStrut(1)); // 20px space

        question = new JLabel();
        question.setFont(new Font("Tahoma", Font.PLAIN, 24));
        centerPanel.add(question);

        opt1 = new JRadioButton();
        opt2 = new JRadioButton();
        opt3 = new JRadioButton();
        opt4 = new JRadioButton();

        opt1.setFont(new Font("Dialog", Font.PLAIN, 20));
        opt2.setFont(new Font("Dialog", Font.PLAIN, 20));
        opt3.setFont(new Font("Dialog", Font.PLAIN, 20));
        opt4.setFont(new Font("Dialog", Font.PLAIN, 20));

        opt1.setBackground(Color.WHITE);
        opt2.setBackground(Color.WHITE);
        opt3.setBackground(Color.WHITE);
        opt4.setBackground(Color.WHITE);

        group = new ButtonGroup();
        group.add(opt1);
        group.add(opt2);
        group.add(opt3);
        group.add(opt4);

        centerPanel.add(opt1);
        centerPanel.add(opt2);
        centerPanel.add(opt3);
        centerPanel.add(opt4);

        // Add top panel and center panel to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        next = new JButton("Next");
        next.setBackground(new Color(22, 99, 54));
        next.setForeground(Color.WHITE);
        next.addActionListener(this);

        previous = new JButton("Previous");
        previous.setBackground(new Color(100, 100, 255));
        previous.setForeground(Color.WHITE);
        previous.addActionListener(this);
        previous.setEnabled(false); // Disabled on first question

        submit = new JButton("Submit");
        submit.setBackground(new Color(255, 215, 0));
        submit.setForeground(Color.BLACK);
        submit.addActionListener(this);

        quit = new JButton("Quit");
        quit.setBackground(Color.RED);
        quit.setForeground(Color.WHITE);
        quit.addActionListener(this);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(next);
        bottomPanel.add(previous);
        bottomPanel.add(submit);
        bottomPanel.add(quit);

        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize questions and answers
        questions[0][0] = "Number of primitive data types in Java are.?";
        questions[0][1] = "6";
        questions[0][2] = "7";
        questions[0][3] = "8";
        questions[0][4] = "9";

        questions[1][0] = "What is the size of float and double in java.?";
        questions[1][1] = "32 and 64";
        questions[1][2] = "32 and 32";
        questions[1][3] = "64 and 64";
        questions[1][4] = "64 and 32";

        questions[2][0] = "Automatic type conversion is possible in which of the possible cases?";
        questions[2][1] = "Byte to int";
        questions[2][2] = "Int to Long";
        questions[2][3] = "Long to int";
        questions[2][4] = "Short to int";

        questions[3][0] = "When an array is passed to a method, what does the method receive?";
        questions[3][1] = "The reference of the array";
        questions[3][2] = "A copy of the array";
        questions[3][3] = "Length of the array";
        questions[3][4] = "Copy of first element";

        questions[4][0] = "Arrays in java are.?";
        questions[4][1] = "Object References";
        questions[4][2] = "Objects";
        questions[4][3] = "Primitive data type";
        questions[4][4] = "None";

        questions[5][0] = "When is the object created with new keyword?";
        questions[5][1] = "At rum time";
        questions[5][2] = "At compile time";
        questions[5][3] = "Depends on the code";
        questions[5][4] = "None";

        questions[6][0] = "Identify the corrected definition of a package.?";
        questions[6][1] = "A package is a collection of editing tools";
        questions[6][2] = "A package is a collection of Classes";
        questions[6][3] = "A package is a collection of Classes and interfaces";
        questions[6][4] = "A package is a collection of interfaces";

        questions[7][0] = "compareTo() returns";
        questions[7][1] = "True";
        questions[7][2] = "False";
        questions[7][3] = "An int value";
        questions[7][4] = "None";

        questions[8][0] = "To which of the following does the class string belong to.";
        questions[8][1] = "java.lang";
        questions[8][2] = "java.awt";
        questions[8][3] = "java.applet";
        questions[8][4] = "java.String";

        questions[9][0] = "Total constructor string class have.?";
        questions[9][1] = "3";
        questions[9][2] = "7";
        questions[9][3] = "13";
        questions[9][4] = "20";

        answers[0][0] = "8";
        answers[1][0] = "32 and 64";
        answers[2][0] = "Int to Long";
        answers[3][0] = "The reference of the array";
        answers[4][0] = "Objects";
        answers[5][0] = "At rum time";
        answers[6][0] = "A package is a collection of Classes and interfaces";
        answers[7][0] = "An int value";
        answers[8][0] = "java.lang";
        answers[9][0] = "13";

        if (current != 0) {
            previous.setEnabled(true);
        }
        // Show the first question after all components are initialized
        displayQuestion(current);

        setSize(1440, 850);
        setLocation(50, 0);
        getContentPane().setBackground(Color.WHITE);

        setVisible(true);

        SwingUtilities.invokeLater(this::removeRadioButtonArrowKeys);

        // Add key bindings AFTER frame is visible
        setupKeyBindings();

        // Disable radio button arrow keys AFTER everything is set up
        SwingUtilities.invokeLater(() -> {
            disableRadioButtonArrowKeys();
            dummyFocus.requestFocusInWindow();
        });

    }

    private void setupKeyBindings() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        // Use KeyEvent constants for more reliable key binding
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "nextQuestion");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "prevQuestion");

        am.put("nextQuestion", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (next.isEnabled()) {
                    next.doClick();
                    dummyFocus.requestFocusInWindow();
                }
            }
        });

        am.put("prevQuestion", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (previous.isEnabled()) {
                    previous.doClick();
                    dummyFocus.requestFocusInWindow();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dummyFocus.requestFocusInWindow();
        if (e.getSource() == next) {
            // Shift focus to dummy button to avoid key event conflicts
            storeUserAnswer();
            current++;
            if (current == questions.length - 1) {
                next.setEnabled(false);
                submit.setEnabled(true);
            }
            previous.setEnabled(true);
            displayQuestion(current);
        } else if (e.getSource() == submit) {
            // Add confirmation dialog
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to submit?",
                    "Confirm Submission",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                storeUserAnswer();
                showScoreBoard();
            }
        } else if (e.getSource() == help) {
            JOptionPane.showMessageDialog(this, "Select the correct answer and click Next. On the last question, click Submit.");
        } else if (e.getSource() == quit) {
            System.exit(0);
        } else if (e.getSource() == previous) {
            storeUserAnswer();
            current--;
            displayQuestion(current);
            next.setEnabled(true);

            if (current == 0) {
                previous.setEnabled(false);
            }
        }
    }

    private void removeRadioButtonArrowKeys() {
        JRadioButton[] buttons = {opt1, opt2, opt3, opt4};
        for (JRadioButton button : buttons) {
            // Remove from InputMap
            InputMap im = button.getInputMap();
            im.put(KeyStroke.getKeyStroke("LEFT"), "none");
            im.put(KeyStroke.getKeyStroke("RIGHT"), "none");

            // Remove from ActionMap
            ActionMap am = button.getActionMap();
            am.put("selectPrevious", null);
            am.put("selectNext", null);
        }

        // Force focus to dummy button
        dummyFocus.requestFocusInWindow();
    }

    private void disableRadioButtonArrowKeys() {
        JRadioButton[] buttons = {opt1, opt2, opt3, opt4};
        for (JRadioButton button : buttons) {
            // Remove from both WHEN_FOCUSED and WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
            InputMap[] inputMaps = {
                button.getInputMap(JComponent.WHEN_FOCUSED),
                button.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            };

            for (InputMap im : inputMaps) {
                im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "none");
                im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "none");
                im.put(KeyStroke.getKeyStroke("LEFT"), "none");
                im.put(KeyStroke.getKeyStroke("RIGHT"), "none");
            }

            // Clear the action map entries for navigation
            ActionMap am = button.getActionMap();
            am.put("selectPrevious", null);
            am.put("selectNext", null);
        }
    }

    private void displayQuestion(int q) {
        question.setText("<html><b>Q" + (q + 1) + ".</b> " + questions[q][0] + "</html>");
        //question.setText("Q" + (q + 1) + ". " + questions[q][0]);
        opt1.setText(questions[q][1]);
        opt2.setText(questions[q][2]);
        opt3.setText(questions[q][3]);
        opt4.setText(questions[q][4]);
        group.clearSelection();
        // If user already answered, select that option
        if (useranswers[q][0] != null) {
            if (useranswers[q][0].equals(opt1.getText())) {
                opt1.setSelected(true);
            } else if (useranswers[q][0].equals(opt2.getText())) {
                opt2.setSelected(true);
            } else if (useranswers[q][0].equals(opt3.getText())) {
                opt3.setSelected(true);
            } else if (useranswers[q][0].equals(opt4.getText())) {
                opt4.setSelected(true);
            }
        }
        removeRadioButtonArrowKeys();

        // Re-disable arrow keys after displaying new question
        SwingUtilities.invokeLater(() -> {
            disableRadioButtonArrowKeys();
            dummyFocus.requestFocusInWindow();
        });
    }

    private void storeUserAnswer() {
        String answer = null;
        if (opt1.isSelected()) {
            answer = opt1.getText();
        } else if (opt2.isSelected()) {
            answer = opt2.getText();
        } else if (opt3.isSelected()) {
            answer = opt3.getText();
        } else if (opt4.isSelected()) {
            answer = opt4.getText();
        }
        useranswers[current][0] = answer;
    }

    private void showScoreBoard() {
        int attempted = 0;
        int correct = 0;
        int wrong = 0;

        for (int i = 0; i < questions.length; i++) {
            if (useranswers[i][0] != null) {
                attempted++;
                if (answers[i][0].equals(useranswers[i][0])) {
                    correct++;
                } else {
                    wrong++;
                }
            }
        }

        int notAttempted = questions.length - attempted;

        // Create an elegant score board using HTML formatting
        String scoreboard = String.format(
                "<html><div style='text-align: center; font-family: Arial; padding: 20px;'>"
                + "<h2 style='color: #2c3e50;'>Quiz Results</h2>"
                + "<hr style='border: 1px solid #bdc3c7'>"
                + "<table style='width: 100%%; margin: 10px;'>"
                + "<tr style='color: #2c3e50;'><td>Total Questions:</td><td>%d</td></tr>"
                + "<tr style='color: #27ae60;'><td>Correct Answers:</td><td>%d</td></tr>"
                + "<tr style='color: #c0392b;'><td>Wrong Answers:</td><td>%d</td></tr>"
                + "<tr style='color: #7f8c8d;'><td>Not Attempted:</td><td>%d</td></tr>"
                + "<tr style='color: #2980b9;'><td>Final Score:</td><td>%d out of %d</td></tr>"
                + "</table>"
                + "<hr style='border: 1px solid #bdc3c7'>"
                + "<p style='color: #2c3e50;'>Percentage: %.1f%%</p>"
                + "</div></html>",
                questions.length, correct, wrong, notAttempted,
                correct, questions.length,
                (correct * 100.0) / questions.length
        );

        // Show the score board in a custom dialog
        JDialog scoreDialog = new JDialog(this, "Score Board", true);
        scoreDialog.setLayout(new BorderLayout());

        // Create a label with the score board
        JLabel scoreLabel = new JLabel(scoreboard);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        scoreDialog.add(scoreLabel, BorderLayout.CENTER);

        // Add an OK button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            scoreDialog.dispose();
            System.exit(0);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        scoreDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog properties
        scoreDialog.pack();
        scoreDialog.setLocationRelativeTo(this);
        scoreDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        scoreDialog.setVisible(true);
    }

    private void setupTimer() {
        timerLabel = new JLabel("Time Left: 5:00");
        timerLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        timerLabel.setForeground(Color.BLACK);

        timer = new Timer(1000, e -> {
            timeLeft--;
            int minutes = timeLeft / 60;
            int seconds = timeLeft % 60;
            timerLabel.setText(String.format("Time Left: %d:%02d", minutes, seconds));

            // Warning at 1 minute
            if (timeLeft == 60) {
                Toolkit.getDefaultToolkit().beep();
                timerLabel.setForeground(Color.RED);
                JOptionPane.showMessageDialog(this,
                        "Warning: 1 minute left!",
                        "Time Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

            // Auto submit at time up
            if (timeLeft <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this,
                        "Time's up! Submitting quiz...",
                        "Time's Up",
                        JOptionPane.INFORMATION_MESSAGE);
                storeUserAnswer();
                showScoreBoard();
            }
        });
        timer.start();
    }

}
