import java.awt.Color;
import javax.swing.*;

public class mainWindow extends JFrame
{

  public mainWindow(){
    JPanel mainPanel = new JPanel();
    mainPanel.setBackground(Color.BLACK);

    this.add(mainPanel);

    super.setTitle("Hello World"); //Set Title
    super.setSize(720,640); //Set the window size
    super.setVisible(true); //Make the window visible
    super.setResizable(false); //Make the window not resizable
    super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close the application instead of hiding
  }
    public static void main(String[] args)
    {  //Create new Jframe
        new mainWindow();
    }
}
