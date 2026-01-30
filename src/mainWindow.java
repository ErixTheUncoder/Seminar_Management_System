import java.awt.Color;
import javax.swing.*;

public class mainWindow extends JFrame
{
    public static void main(String[] args)
    { 
        JFrame frame = new JFrame(); //Create new Jframe
        
        frame.setTitle("First window");//Title
        frame.setSize(720,640); //Set the window size
        frame.setVisible(true); //Make the window visible
        frame.setResizable(false); //Make the window not resizable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close the application instead of hiding
        frame.getContentPane().setBackground(new Color(0,0,0));
    }
}
