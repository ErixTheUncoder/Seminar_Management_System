import javax.swing.*;

public class mainWindow extends JFrame
{
  public mainWindow(){
  //Create frame for mainWindow
  JFrame mainWindow = new JFrame();

  //setup panel for textbox
  JPanel textbox = new JPanel();
  JLabel chooseRoleStr = new JLabel("Choose your role");
  textbox.add(chooseRoleStr); //Add JLabel text into panel
  this.add(textbox);

  //TODO: setup panel for button





  //Setup window
    this.setTitle("Seminar Management System"); //Set Title
    this.setSize(720,640); //Set the window size
    this.setVisible(true); //Make the window visible
    this.setResizable(true); //Make the window resizable
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close the application instead of hiding
  }
    public static void main(String[] args)
    {  //Create new Jframe called window
        mainWindow window =new mainWindow();

        

    }
}
