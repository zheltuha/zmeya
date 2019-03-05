import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener { //implements ActionListener интерфейс

    private final int SIZE = 336;           //размер поля
    private final int DOT_SIZE = 16;        //размер точки(змейки(яблок))
    private final int ALL_DOTS = 441;       //масимальный размер змейки
    private Image dot;                      //изображение змейки
    private Image apple;                    //изображение яблока
    private int appleX;                     //координаты яблока по X
    private int appleY;                     //                  по Y
    private int[] x = new int[ALL_DOTS];    //для хранения положения змейки в каждую секунду(тик)
    private int[] y = new int[ALL_DOTS];    //  по X и Y, и не больше максимального количества точек "ALL_DOTS"
    private int dots;                       //размер змейки в начале игры
    private Timer timer;                    //таймер для обновения экрана
    private boolean left = false;           //элементы управления змейкой
    private boolean right = true;           //
    private boolean up = false;             //
    private boolean down = false;           //
    private boolean inGame = true;          //сосотояние в игре

    public GameField(){
        setBackground(Color.white);
        loadImage();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }
    public void initGame(){                                 //начало игры

        dots = 4;                                           //количество начальных точек(длина змейки)
        for (int i=0; i<dots; i++){
                    x[i] = 48-i*DOT_SIZE;                   //координаты начала змейки
            y[i] = 48;
        }
        timer = new Timer(150,this);            //частота обновления таймера
        timer.start();
        createApple();
    }
    public void createApple(){                              //создание яблока на поле
        appleX = new Random().nextInt(22)*DOT_SIZE;
        appleY = new Random().nextInt(22)*DOT_SIZE;
    }
    public void checkApple(){                               //чек яблока
        if (x[0] == appleX && y[0] == appleY){              //если голова на координатах яблока,
            dots++;                                         //увеличивается змейка
            createApple();                                  //и создается новое яблоко
        }
    }

    public void loadImage(){                                 //загрузка изображений

        ImageIcon iia = new ImageIcon("dotA.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dotZ.png");
        dot = iid.getImage();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){                                              //если все еще в игре
            g.drawImage(apple,appleX,appleY,this);       //рисуем яблоко по координатом
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot,x[i],y[i],this);
            }
        }
        else {                                                  //если игра хакончилась
            String str = "press F to рестарт";                  //вывод надписи,
            g.setColor(Color.white);
            g.drawString(str, 120, SIZE/2);
            setBackground(Color.black);
        }
    }

    public void rstrt(){                                    //рестарт игры
        left = false;
        right = true;
        up = false;
        down = false;
        inGame = true;
        setBackground(Color.white);
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void move(){                                     //управление замейкай
        for (int i = dots; i > 0 ; i--) {
            x[i] = x[i-1];                                  //все точки встают на место прдыдущей точки(головы)
            y[i] = y[i-1];
        }
        if(left){                                           //направление движения
            x[0]-= DOT_SIZE;
        }
        if(right){
            x[0]+= DOT_SIZE;
        }
        if(up){
            y[0]-= DOT_SIZE;
        }
        if(down){
            y[0]+= DOT_SIZE;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {             //тик таймера
        if (inGame){
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }
    public void checkCollisions(){                           //проверка колизии, вышла ли змейка за игровое поле
        for (int i = dots; i > 0 ; i--) {
            if(i>4 && x[0] == x[i] && y[0] == y[i]){         //если врезалась в себя
                inGame = false;
            }
        }
        if (x[0]>SIZE){                                      //проверка на воход за рамки поля
            inGame = false;
        }
        if (x[0]<0){
            inGame = false;
        }
        if (y[0]>SIZE){
            inGame = false;
        }
        if (y[0]<0){
            inGame = false;
        }
    }
    class FieldKeyListener extends KeyAdapter{               //обработка нажатия клавишь
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !right){
                left = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_UP && !down){
                up = true;
                left = false;
                right = false;
            }
            if(key == KeyEvent.VK_DOWN && !up){
                down = true;
                left = false;
                right = false;
            }
            if(key == KeyEvent.VK_F && inGame == false){    //когда игра законченя, кнопка F начинает игру заново
                timer.stop();
                rstrt();
            }
        }
    }

}
