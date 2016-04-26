package MyPackage;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class tetrisView implements Observer {
	tetrisControl control = null;
	tetrisModel model = null;
	
	JFrame mainFrame;
	JPanel panel1,panel2;
	Canvas paintCanvas;
	JTextArea showScore;
	JTextArea showIntroduction;
	
	public static final int canvasWidth = 400;
	public static final int canvasHeight = 500;
	
	public static final int nodeWidth = 20;
	public static final int nodeHeight = 20;
	
	public tetrisView(tetrisModel model, tetrisControl control){
		this.model = model;
		this.control = control;
		
		mainFrame = new JFrame("Tetris");

		paintCanvas = new Canvas();
		paintCanvas.setSize(canvasWidth+1,canvasHeight+1);
		paintCanvas.addKeyListener(control);
		mainFrame.add(paintCanvas);
		
		showScore = new JTextArea();
		showIntroduction = new JTextArea();
		float tmp = (float) 0.5;
		showScore.setAlignmentX(tmp);
		showIntroduction.setAlignmentX(tmp);
		//mainFrame.add(showScore);
		//mainFrame.add(showIntroduction);
		
		mainFrame.addKeyListener(control);
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
	}
	
	private void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, canvasWidth, canvasHeight);

        g.setColor(Color.WHITE);
        for (int i = 0; i < tetrisModel.maxX; i++){
        	for (int j = 0; j < tetrisModel.maxY; j++){
        		if (model.matrix[i][j]){
        			drawNode(g, new Node(i,j));
        		}
        	}
        }
        int baseX = model.base.x;
        int baseY = model.base.y;
        for (int i = 0; i < tetrisModel.nodeNum; i++){
        	int x = baseX + model.block[model.type][model.change][i].x;
        	int y = baseY + model.block[model.type][model.change][i].y;
        	drawNode(g, new Node(x,y));
        }
        show();
    }
	
	private void show(){
		String str = "您的分数为：\n"+model.score;
		showScore.setText(str);
		String str2 = "W变形\nA向左\nD向右\nS向下\nP暂停";
		showIntroduction.setText(str2);
	}
	
	private void drawNode(Graphics g, Node n) {
        g.fillRect(n.x * nodeWidth,
                n.y * nodeHeight,
                nodeWidth - 1,
                nodeHeight - 1);
    }
	

    public void update(Observable o, Object arg) {
        repaint(paintCanvas.getGraphics());
    }
    
    private Image iBuffer;
    private Graphics gBuffer;
    private void repaint(Graphics g) {
            if (iBuffer == null) {
                iBuffer = paintCanvas.createImage(paintCanvas.getSize().width, paintCanvas.getSize().height);
                gBuffer = iBuffer.getGraphics();
            }
            gBuffer.setColor(paintCanvas.getBackground());
            gBuffer.fillRect(0, 0, paintCanvas.getSize().width, paintCanvas.getSize().height);
            paint(gBuffer);
            g.drawImage(iBuffer, 0, 0, paintCanvas);
        }
}
