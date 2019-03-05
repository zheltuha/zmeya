import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow(){

     setTitle("Змейка");
     setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  //выход(закрытие), по нажетию *крестика
     setSize(368,392);                              //размеры окна
     setLocation(700,400);                                //позиция появление окна на мониторе
     add(new GameField());
     setVisible(true);

    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();

    }
}
