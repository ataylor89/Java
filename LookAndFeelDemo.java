import javax.swing.*;
import javax.swing.plaf.metal.*;
import java.awt.*;
import java.awt.event.*;


public class LookAndFeelDemo extends JFrame implements ActionListener {
    
    private JPanel panel;
    private JButton nimbus, system, metal, ocean;

    public LookAndFeelDemo() {
        super("Look and Feel Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        panel = new JPanel(); 
        nimbus = new JButton("Nimbus");
        nimbus.addActionListener(this);
        system = new JButton("System");
        system.addActionListener(this);
        metal = new JButton("Metal");
        metal.addActionListener(this);
        ocean = new JButton("Ocean");
        ocean.addActionListener(this);
        panel.add(nimbus); 
        panel.add(system);
        panel.add(metal);
        panel.add(ocean);
        add(panel);
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
        } catch (Exception e) {
            System.err.println(e);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void setLookAndFeel(LookAndFeel lookAndFeel) {
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            System.err.println(e);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nimbus) 
            setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        else if (e.getSource() == system)
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        else if (e.getSource() == metal) {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
            setLookAndFeel(new MetalLookAndFeel());
        }
        else if (e.getSource() == ocean) {
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
            setLookAndFeel(new MetalLookAndFeel());
        }
    }

    public static void main(String[] args) {
        LookAndFeelDemo.setLookAndFeel(); 
        LookAndFeelDemo demo = new LookAndFeelDemo();
        demo.setVisible(true);
    }
}
