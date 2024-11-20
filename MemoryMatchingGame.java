/************************************************************************
*@Date: 11/08/2024 
*@Name: Shayden Nair
*@Author: Andrew Carreiro
*@Brief Description: This is a 5x5 grid game you have to match words with
*                    each other based on memory. 
************************************************************************/

import javax.swing.*; // GUI components
import java.awt.*; // Layout and graphics
import java.awt.event.*; // Event handling
import java.util.*; // Collections

public class MemoryMatchingGame extends JFrame implements ActionListener {
   private final int gridSize = 5; // Size of the grid (5x5)
   private final JButton[][] buttons = new JButton[gridSize][gridSize]; // Button grid
   private final String[][] words = new String[gridSize][gridSize]; // Word grid
   private JButton firstButton, secondButton; // Buttons for selected cards
   private int firstRow, firstCol; // Positions of first button
   private boolean isChecking = false; // Prevent clicks during checking
   private int pairsFound = 0; // Count of matched pairs

   private final HashMap<String, String> wordMap = new HashMap<>(); // Word mapping

   public MemoryMatchingGame() {
      initializeGame(); // Initialize the game setup
   }

   private void initializeGame() {
      setTitle("Word Matching Game"); // Set window title
      setSize(500, 500); // Set window size
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit on close
      setLayout(new BorderLayout()); // Set layout

      // Title panel setup
      JPanel titlePanel = new JPanel();
      JLabel titleLabel = new JLabel("Memory Game");
      titleLabel.setFont(new Font("Ink Free", Font.BOLD, 50));
      titleLabel.setForeground(new Color(255, 255, 0));
      titlePanel.add(titleLabel); // Add title to panel
      add(titlePanel, BorderLayout.NORTH); // Add title panel to frame

      initializeWordMap(); // Load word pairs
      initializeWords(); // Initialize word grid
      createGrid(); // Create game grid
      setVisible(true); // Show frame
   }

   private void initializeWordMap() {
      // Populate word map with pairs
      wordMap.put("apple", "apple");
      // (Other word mappings)
   }

   private void initializeWords() {
      ArrayList<String> wordPairs = new ArrayList<>(); // List for word pairs

      // Create pairs and shuffle them
      for (String name : wordMap.keySet()) {
         String word = wordMap.get(name);
         wordPairs.add(word); // Add word twice for pairs
         wordPairs.add(word);
      }
      Collections.shuffle(wordPairs); // Shuffle pairs

      // Assign shuffled words to the grid
      int index = 0;
      for (int row = 0; row < gridSize; row++) {
         for (int col = 0; col < gridSize; col++) {
            words[row][col] = wordPairs.get(index++); // Fill grid with words
         }
      }
   }

   private void createGrid() {
      Font wordFont = new Font("Ariel", Font.PLAIN, 48); // Font for buttons
      JPanel gridPanel = new JPanel(new GridLayout(gridSize, gridSize)); // Grid layout

      // Create buttons for the grid
      for (int row = 0; row < gridSize; row++) {
         for (int col = 0; col < gridSize; col++) {
            JButton button = new JButton();
            button.setFont(wordFont); // Set font
            button.setFocusPainted(false); // Disable focus paint
            button.addActionListener(this); // Add action listener
            buttons[row][col] = button; // Store button
            gridPanel.add(button); // Add button to grid
         }
      }
      add(gridPanel, BorderLayout.CENTER); // Add grid to frame
   }

   public void actionPerformed(ActionEvent e) {
      if (isChecking) return; // Prevent clicks during checking

      JButton clickedButton = (JButton) e.getSource(); // Get clicked button
      int row = -1, col = -1; // Initialize position variables

      // Find position of clicked button
      for (int i = 0; i < gridSize; i++) {
         for (int j = 0; j < gridSize; j++) {
            if (buttons[i][j] == clickedButton) { // Match found
               row = i; 
               col = j; 
               break;
            }
         }
      }

      if (clickedButton.getText().equals(words[row][col])) return; // Ignore if already revealed

      clickedButton.setText(words[row][col]); // Show word on button

      if (firstButton == null) {
         // First button selected
         firstButton = clickedButton;
         firstRow = row;
         firstCol = col;
      } else {
         // Second button selected
         secondButton = clickedButton;
         isChecking = true; // Set checking flag

         // Check for match
         if (words[firstRow][firstCol].equals(words[row][col])) {
            pairsFound++; // Increment matched pairs
            firstButton = null; // Reset buttons
            secondButton = null;
            isChecking = false;

            // Check for win condition
            if (pairsFound == (gridSize * gridSize) / 2) {
               int restart = JOptionPane.showConfirmDialog(this, 
                  "Congratulations! You've matched all pairs! Would you like to play again?", 
                  "Game Over", JOptionPane.YES_NO_OPTION);
               if (restart == JOptionPane.YES_OPTION) {
                  restartGame(); // Restart game
               } else {
                  System.exit(0); // Exit game
               }
            }
         } else {
            // No match: reset buttons after delay
            Timer timer = new Timer(500, new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  firstButton.setText(""); // Clear first button
                  secondButton.setText(""); // Clear second button
                  firstButton = null; // Reset
                  secondButton = null;
                  isChecking = false; // Reset checking flag
               }
            });
            timer.setRepeats(false); // Timer only runs once
            timer.start(); // Start timer
         }
      }
   }

   private void restartGame() {
      pairsFound = 0; // Reset match count
      firstButton = null; // Clear selections
      secondButton = null;
      isChecking = false; // Reset checking flag
      initializeWords(); // Reshuffle words

      // Clear button texts
      for (int i = 0; i < gridSize; i++) {
         for (int j = 0; j < gridSize; j++) {
            buttons[i][j].setText(""); 
         }
      }
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(MemoryMatchingGame::new); // Launch the game
   }
}
