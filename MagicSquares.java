import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class MagicSquares extends JFrame {
    
    private JPanel panel;
    private JTextField[][] grid;

    public MagicSquares() {
        super("Magic Squares");
        setSize(900, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 9));
        grid = new JTextField[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = new JTextField();
                grid[i][j].setHorizontalAlignment(JTextField.CENTER);
                grid[i][j].setFont(new Font("Sans Serif", Font.PLAIN, 30));
                grid[i][j].setDocument(new PlainDocument() {
                    @Override
                    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                        if (getLength() > 0)
                            return;
                        super.insertString(offs, str, a);
                    }
                });
                panel.add(grid[i][j]);
            }
        }
        add(panel);
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        MagicSquares.setLookAndFeel();
        MagicSquares frame = new MagicSquares();
        frame.setVisible(true);
    }
}
