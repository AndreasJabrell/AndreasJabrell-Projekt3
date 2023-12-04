import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class TicTacToe extends JFrame {
    static boolean Terminator = false;
    //Padding för att knapparna skall se mer ordnade ut
    static final int UNI_PADDING = 10;
    public static String winner;
    public static String sound;
    //TextField som visar meddelanden till spelare
    static JTextField outputTF;
    //Array för att plocka in värden på våra knappar
    static JButton[] arrayJB = new JButton[9];
    //För att få igång if-satser som byter vilken spelare som spelar - byt till bättre namn på denna!!
    static boolean playerGo;
    //Player mode JD variabler
    static JPanel players = new JPanel();
    static JLabel p1 = new JLabel(" ");
    static JLabel p2 = new JLabel(" ");
    //För att kolla om game=draw
    static int counter = 0;
    //Vårt spel
    private static TicTacToe ticTacToe;
    //ActionListener till våra knappar för att kunna köra spelet
    static ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //For-loop för att köra if-statements 9 gånger(alla knappar) som sätter X/O på knapparna och byter spelare efter varje klick.
            for (int i = 0; i < 9; i++) {
                if (e.getSource() == arrayJB[i]) {
                    if(!Terminator)  //not terminator
                        if ((arrayJB[i].getText().isEmpty()) && playerGo) {
                            arrayJB[i].setText("O");
                            p2.setEnabled(false);
                            p1.setEnabled(true);
                            playerGo = false;
                            checkWinOrDraw();
                        }else {
                            arrayJB[i].setText("X");  // player 1 väljer
                            p1.setEnabled(false);
                            p2.setEnabled(true);
                            playerGo = true;
                            checkWinOrDraw();
                        }

                    else if (playerGo)  {   // Ai väljer O
                        // loop om med while loop
                        Random random = new Random();
                        int r= random.nextInt(9);
                        System.out.println(r);

                        while (arrayJB[r].getText()!=""){
                            System.out.println(arrayJB[r].getText());
                            System.out.println(r);
                            r = random.nextInt(9);
                            arrayJB[r].setText("O");
                        }
                        arrayJB[r].setText("0");
                        p2.setEnabled(false);
                        p1.setEnabled(true);
                        playerGo = false;
                        checkWinOrDraw();
                    }
                    else {
                        arrayJB[i].setText("X");
                        p1.setEnabled(false);
                        p2.setEnabled(true);
                        playerGo = true;
                        checkWinOrDraw();
                        arrayJB[i].doClick();
                    }
                }
            }
        }
    };

    public TicTacToe() {
        setSize(500, 500);
        setVisible(true);
    }

    public static void main(String[] args) {
        //Application eller frame
        ticTacToe = new TicTacToe();
        ticTacToe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ticTacToe.setResizable(false);
        ticTacToe.setLayout(new BorderLayout());

        //JPanel som rymmer JButtons
        JPanel panelJB = new JPanel();
        panelJB.setBorder(new EmptyBorder(UNI_PADDING, UNI_PADDING, UNI_PADDING, UNI_PADDING));
        panelJB.setBackground(Color.lightGray);
        panelJB.setSize(300, 300);

        //Gridlayout för att JButtons skall visas rätt
        GridLayout gl = new GridLayout(3, 3);
        gl.setHgap(UNI_PADDING);
        gl.setVgap(UNI_PADDING);
        panelJB.setLayout(gl);

        //TextField för att visa vems tur det är och så vidare.
        outputTF = new JTextField("TicTacToe");
        outputTF.setHorizontalAlignment(SwingConstants.CENTER);
        Font bigFont = outputTF.getFont().deriveFont(Font.PLAIN, 30f);
        outputTF.setFont(bigFont);
        outputTF.setBorder(BorderFactory.createLineBorder(Color.black, 5));
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BorderLayout());
        panelTop.add(outputTF, BorderLayout.NORTH);
        panelTop.add(players, BorderLayout.SOUTH);
        players.setLayout(new FlowLayout());
        players.add(p1);
        players.add(p2);
        ticTacToe.add(panelTop, BorderLayout.NORTH);

        //For-loop som bygger våra JButtons
        for (int i = 0; i < 9; i++) {
            Font f = new Font("Open sans", Font.BOLD, 75);
            arrayJB[i] = new JButton();
            arrayJB[i].addActionListener(listener);
            arrayJB[i].setFont(f);
            arrayJB[i].setActionCommand(i + "");
            panelJB.add(arrayJB[i]);
        }
        ticTacToe.add(panelJB);
        ticTacToe.setVisible(true);
        changeGameMode();

    }

    public static void changeGameMode() {
        // skapa en panel med er custom game mode
        JDialog frame = new JDialog(ticTacToe, "Game mode", true);
        JPanel dialog = new JPanel();
        frame.setLocation(150, 250);
        frame.add(dialog);
        dialog.setSize(100, 100);
        LayoutManager FlowLayout = new FlowLayout();
        dialog.setLayout(FlowLayout);
        JButton gameMode1 = new JButton("PvP");
        gameMode1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputTF.setHorizontalAlignment(SwingConstants.CENTER);
                p1.setText("PLAYER 1 X");
                p2.setText("PLAYER 2 O");
                sound = "src/LETSGO.wav";
                Sound();
                frame.setVisible(false);
            }
        });
        JButton gameMode2 = new JButton("PvTerminator");
        gameMode2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputTF.setHorizontalAlignment(SwingConstants.CENTER);
                p1.setText("PLAYER 1 X");
                p2.setText("TERMINATOR O");
                sound = "src/LIVE.wav";
                Terminator=true;
                Sound();
                frame.setVisible(false);
            }
        });
        dialog.add(gameMode1);
        dialog.add(gameMode2);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkWinOrDraw() {
        counter++;
        if (counter == 9) {
            winner = "It's a draw";
            Winner();
        }
        //Dessa är vinnarkombinationerna i en array
        int[][] winningCombinations = {
                {1, 4, 7}, {0, 4, 8}, {2, 4, 6},
                {3, 4, 5}, {0, 3, 6}, {2, 5, 8},
                {0, 1, 2}, {6, 7, 8}
        };
        //Initiatar som false för att kunna sätta true om kombinationen är en vinnarkombination
        boolean winnerIsX = false;
        //If-satser som letar efter vinnarkombinationer
        for (int[] combination : winningCombinations) {
            if (arrayJB[combination[0]].getText().equals("X") &&
                    arrayJB[combination[1]].getText().equals("X") &&
                    arrayJB[combination[2]].getText().equals("X")) {
                winnerIsX = true;
                break;
            }
        }
        //Här lägger vi in vår vinnarmetod
        if (winnerIsX) {
            winner = "Player X has won";
            Winner();
        }
        boolean winnerIsO = false;
        for (int[] combination : winningCombinations) {
            if (arrayJB[combination[0]].getText().equals("O") &&
                    arrayJB[combination[1]].getText().equals("O") &&
                    arrayJB[combination[2]].getText().equals("O")) {
                winnerIsO = true;
                break;
            }
        }
        if (winnerIsO) {
            winner = "Player O has won";
            Winner();
        }
    }

    public static void Sound() {
        try {
            // Open an audio input stream.
            File soundFile = new File(sound);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException a) {
            a.printStackTrace();
        }
    }

    public static void Winner() {
        JDialog d = new JDialog(ticTacToe, "dialog Box");
        JLabel l = new JLabel(winner + "! Want to play again?");
        d.add(l);
        d.setLayout(new FlowLayout());
        JButton yes = new JButton("Yes");
        d.add(yes);
        sound = "src/WOHO.wav";
        Sound();
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reset();
                d.setVisible(false);
            }
        });
        JButton no = new JButton("No");
        d.add(no);
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ticTacToe.dispose();
                sound = "src/Hasta.wav";
                Sound();
            }
        });
        d.setSize(250, 100);
        d.setLocation(125, 250);
        for (int i = 0; i < 9; i++) {
            arrayJB[i].setEnabled(false);
        }
        d.setVisible(true);
    }
    public static void Reset() {
        counter = 0;
        for (int i = 0; i < 9; i++) {
            arrayJB[i].setEnabled(true);
            arrayJB[i].setText("");
        }
    }
}