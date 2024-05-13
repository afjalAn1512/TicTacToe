import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TicTacToeGui extends JFrame implements ActionListener {


    //xScore - holds the score value for the x player
    //oScore - holds the score value for the o player
    //moveCounter - counts the number of moves (used to determine if there is a draw)
    private int xScore, oScore, moveCounter;

    // isPlayerOne - flag to indicate if the current player is player x or not
    private boolean isPlayerOne;

    private JLabel turnLabel, scoreLabel, resultLabel;
    private JButton[][] board;
    private JDialog resultDialog;

    public TicTacToeGui() {
        super("Tic Tac Toe");
        setSize(CommonConstant.FRAME_SIZE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(CommonConstant.BACKGROUND_COLOR);

        ImageIcon image = new ImageIcon("image/logo.png");
        setIconImage(image.getImage());


        //init vars
        createResultDialog();
        board = new JButton[3][3];

        //player x starts first
        isPlayerOne = true;

        addGuiComponent();
    }

    private void addGuiComponent() {

        //bar label
        JLabel barLabel = new JLabel();
        barLabel.setOpaque(true);
        barLabel.setBackground(CommonConstant.BAR_COLOR);
        barLabel.setBounds(0, 0, CommonConstant.FRAME_SIZE.width, 25);

        //turn label
        turnLabel = new JLabel(CommonConstant.X_LABEL);
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        turnLabel.setFont(new Font("Dialog", Font.PLAIN, 25));
        turnLabel.setPreferredSize(new Dimension(100, turnLabel.getPreferredSize().height));
        turnLabel.setOpaque(true);
        turnLabel.setBackground(CommonConstant.X_COLOR);
        turnLabel.setForeground(CommonConstant.BOARD_COLOR);
        turnLabel.setBounds(
                (CommonConstant.FRAME_SIZE.width - turnLabel.getPreferredSize().width) / 2,
                0,
                turnLabel.getPreferredSize().width,
                turnLabel.getPreferredSize().height
        );


        //score label

        scoreLabel = new JLabel(CommonConstant.SCORE_LABEL);
        scoreLabel.setFont(new Font("Dialog", Font.PLAIN, 25));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setForeground(CommonConstant.BOARD_COLOR);
        scoreLabel.setBounds(
                0,
                turnLabel.getY() + turnLabel.getPreferredSize().height + 25,
                CommonConstant.FRAME_SIZE.width,
                scoreLabel.getPreferredSize().height
        );

        //game board
        GridLayout gridLayout = new GridLayout(3, 3);
        JPanel boardPanel = new JPanel(gridLayout);
        boardPanel.setBounds(
                0,
                scoreLabel.getY() + scoreLabel.getPreferredSize().height + 35,
                CommonConstant.BOARD_SIZE.width,
                CommonConstant.BOARD_SIZE.height
        );


        //create board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                JButton button = new JButton();
                button.setFont(new Font("Dialog", Font.PLAIN, 100));
                button.setPreferredSize(CommonConstant.BUTTON_SIZE);
                button.setBackground(CommonConstant.BACKGROUND_COLOR);
                button.addActionListener(this);
                button.setBorder(BorderFactory.createLineBorder(CommonConstant.BOARD_COLOR));

                //add button to board
                board[i][j] = button;
                boardPanel.add(board[i][j]);

            }
        }


        //reset button
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Dialog", Font.PLAIN, 16));
        resetButton.addActionListener(this);
        resetButton.setBackground(CommonConstant.BOARD_COLOR);
        resetButton.setBounds(
                (CommonConstant.FRAME_SIZE.width - resetButton.getPreferredSize().width) / 2,
                CommonConstant.FRAME_SIZE.height - 100,
                resetButton.getPreferredSize().width,
                resetButton.getPreferredSize().height
        );


        getContentPane().add(turnLabel);
        getContentPane().add(barLabel);
        getContentPane().add(scoreLabel);
        getContentPane().add(boardPanel);
        getContentPane().add(resetButton);


    }

    private void createResultDialog() {
        resultDialog = new JDialog();
        resultDialog.getContentPane().setBackground(CommonConstant.BACKGROUND_COLOR);
        resultDialog.setResizable(false);
        resultDialog.setTitle("Result");
        resultDialog.setSize(CommonConstant.RESULT_DIALOG_SIZE);
        resultDialog.setLocationRelativeTo(this);
        resultDialog.setModal(true);
        resultDialog.setLayout(new GridLayout(2, 1));
        resultDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resetGame();
            }
        });

        //result label
        resultLabel = new JLabel();
        resultLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        resultLabel.setForeground(CommonConstant.BOARD_COLOR);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);


        // restart button
        JButton restartButton = new JButton("Play Again");
        restartButton.setBackground(CommonConstant.BOARD_COLOR);
        restartButton.addActionListener(this);

        resultDialog.add(resultLabel);
        resultDialog.add(restartButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Reset") || command.equals("Play Again")) {
            //reset the game
            resetGame();

            // only reset the score when pressing reset
            if (command.equals("Reset"))
                xScore = oScore = 0;

            if (command.equals("Play Again"))
                resultDialog.setVisible(false);

        } else {
            //player move
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("")) {
                moveCounter++;

                // mark the button with x/o only if not hasn't been selected yet
                if (isPlayerOne) {
                    //player one (x player)
                    button.setText(CommonConstant.X_LABEL);
                    button.setForeground(CommonConstant.X_COLOR);

                    //update turn label
                    turnLabel.setText(CommonConstant.O_LABEL);
                    turnLabel.setBackground(CommonConstant.O_COLOR);


                    //update turn
                    isPlayerOne = false;
                } else {
                    // player two (o player)
                    button.setText(CommonConstant.O_LABEL);
                    button.setForeground(CommonConstant.O_COLOR);


                    //update turn label
                    turnLabel.setText(CommonConstant.X_LABEL);
                    turnLabel.setForeground(CommonConstant.X_COLOR);

                    // update turn
                    isPlayerOne = true;
                }

                // check win condition
                if (isPlayerOne) {
                    // check to see if the last move from O was the winning move
                    checkOWin();
                }
                {
                    //check to see if the last move from X was the winning move
                    checkXWin();
                }

                //check for draw conditions
                checkDraw();

                // update score label
                scoreLabel.setText("X: "+ xScore + " | O: "+ oScore);

            }

            repaint();
            revalidate();
        }
    }

    private void checkXWin() {

        String result = "X wins!";

        //check rows
        for (int row = 0; row < board.length; row++) {
            if (board[row][0].getText().equals("X") && board[row][1].getText().equals("X") && board[row][2].getText().equals("X")) {
                resultLabel.setText(result);

                //display result dialog
                resultDialog.setVisible(true);


                //update score
                xScore++;

            }
        }

        //check columns
        for (int col = 0; col < board.length; col++) {
            if (board[col][0].getText().equals("X") && board[col][1].getText().equals("X") && board[col][2].getText().equals("X")) {
                resultLabel.setText(result);

                //display result dialog
                resultDialog.setVisible(true);


                //update score
                xScore++;

            }
        }

        //check diagonals
        if (board[0][0].getText().equals("X") && board[1][1].getText().equals("X") && board[2][2].getText().equals("X")) {
            resultLabel.setText(result);

            //display result dialog
            resultDialog.setVisible(true);


            //update score
            xScore++;

        }

        if (board[2][0].getText().equals("X") && board[1][1].getText().equals("X") && board[0][2].getText().equals("X")) {
            resultLabel.setText(result);

            //display result dialog
            resultDialog.setVisible(true);


            //update score
            xScore++;

        }


    }

    private void checkOWin() {

        String result = "O wins!";

        //check rows
        for (int row = 0; row < board.length; row++) {
            if (board[row][0].getText().equals("O") && board[row][1].getText().equals("O") && board[row][2].getText().equals("O")) {
                resultLabel.setText(result);

                //display result dialog
                resultDialog.setVisible(true);

                //update score
                oScore++;

            }
        }

        //check columns
        for (int col = 0; col < board.length; col++) {
            if (board[col][0].getText().equals("O") && board[col][1].getText().equals("O") && board[col][2].getText().equals("O")) {
                resultLabel.setText(result);

                //display result dialog
                resultDialog.setVisible(true);


                //update score
                oScore++;

            }
        }

        //check diagonals
        if (board[0][0].getText().equals("O") && board[1][1].getText().equals("O") && board[2][2].getText().equals("O")) {
            resultLabel.setText(result);

            //display result dialog
            resultDialog.setVisible(true);


            //update score
            oScore++;

        }

        if (board[2][0].getText().equals("O") && board[1][1].getText().equals("O") && board[0][2].getText().equals("O")) {
            resultLabel.setText(result);

            //display result dialog
            resultDialog.setVisible(true);


            //update score
            oScore++;

        }

    }

    private void checkDraw(){
        //if there a total of 9 moves and no player has won yet then it means there is a draw

        if (moveCounter >= 9){
            resultLabel.setText("Draw !");
            resultDialog.setVisible(true);
        }

    }

    private void resetGame() {
        //reset player to back to x_player
        isPlayerOne = true;
        turnLabel.setText(CommonConstant.X_LABEL);
        turnLabel.setBackground(CommonConstant.X_COLOR);

        //reset score display
        scoreLabel.setText(CommonConstant.O_LABEL);


        //reset move counter
        moveCounter = 0;


        //reset board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j].setText("");
            }
        }
    }
}
