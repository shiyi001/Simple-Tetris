package MyPackage;

import java.util.Observable;
import java.util.Random;

import javax.swing.JOptionPane;

import java.util.Arrays;

public class tetrisModel extends Observable implements Runnable{
	boolean[][] matrix;
	static final int maxX = 20;
	static final int maxY = 25;
	Node base;
	
	int timeInterval = 200;
	double speedChangeRate = 0.80;
	
	int score = 0;
	
	int type = 0;
	int change = 0;
	Node[][][] block;
	static final int typeNum = 6;//共六种形状
	static final int changeNum = 4;//每种形状4中变换
	static final int nodeNum = 4;//每种形状4个点
	
	boolean paused = false;
	
	boolean toTop = false;
	
	public tetrisModel(){
		reset();
	}
	
	private void reset(){
		matrix = new boolean[maxX][];
		for (int i = 0; i < maxX; i++){
			matrix[i] = new boolean[maxY];
			Arrays.fill(matrix[i],false);
		}//初始化界面
		
		block = new Node[typeNum][][];
		for (int i = 0; i < typeNum; i++){
			block[i] = new Node[changeNum][];
			for (int j = 0; j < changeNum; j++){
				block[i][j] = new Node[nodeNum];
			}
		}
		
		for (int i = 0; i < typeNum; i++)
			for (int j = 0; j < changeNum; j++)
				for (int k = 0; k < nodeNum; k++){
					block[i][j][k] = new Node();
				}
		
		fillBlock();
		createBlock();
		
		System.out.println("type="+type);
		System.out.println("change="+change);
	}
		
	public void run(){
		boolean running = true;
		while (running){
			try{
				Thread.sleep(timeInterval);
			}catch(Exception e){
				break;
			}
			if (!paused){
				if (moveOn()){
					setChanged();
					notifyObservers();
				}else{
					JOptionPane.showMessageDialog(null,
							"you failed",
							"Game Over",
							JOptionPane.INFORMATION_MESSAGE);
					break;
				}
			}
		}
		running = false;
	}
	
	public boolean moveOn(){
		if (!toBottom()){
			base.y++; 
			return true;
		}else{
			if (base.y == 0) return false;
			else{
				drawMatrix();
				removeBlock();
				createBlock();
				recoverSpeed();
				return true;
			}
		}
	}
	
	public void changeShape(){
		change = (change+1) % changeNum; 
	}
	
	public void leftMove(){
		if (!toLeft()){
			base.x--;
		}
	}
	
	public void rightMove(){
		if (!toRight()){
			base.x++;
		}
	}
	
	public void speedUp(){
		timeInterval *= speedChangeRate;
	}
	
	public void recoverSpeed(){
		timeInterval = 200;
	}
	
	public void changePauseState(){
		paused = !paused;
	}
	
	private void drawMatrix(){
		for (int i = 0; i < nodeNum; i++){
			int tmp_x = base.x + block[type][change][i].x;
			int tmp_y = base.y + block[type][change][i].y;
			matrix[tmp_x][tmp_y] = true;
		}
	}
	
	private boolean toBottom(){
	//判断是否已经到了最底部
		for (int i = 0; i < nodeNum; i++){
			int tmp_x = base.x + block[type][change][i].x;
			int tmp_y = base.y + block[type][change][i].y;
			if (tmp_y == maxY-1 || matrix[tmp_x][tmp_y+1]){
				return true;
			}
		}
		return false;
	}
	
	private boolean toLeft(){
	//判断是否到了最左边或者左边已有方块
		if (base.x == 0) return true;
		for (int i = 0; i < nodeNum; i++){
			int tmp_x = base.x+block[type][change][i].x;
			int tmp_y = base.y+block[type][change][i].y;
			if (tmp_x > 0){
				if (matrix[tmp_x-1][tmp_y]) return true;
			}
		}
		return false;
	}
	
	private boolean toRight(){
	//判断是否到了最右边
		for (int i = 0; i < nodeNum; i++){
			int tmp_x = base.x+block[type][change][i].x;
			int tmp_y = base.y+block[type][change][i].y;
			if (tmp_x < maxX-1){
				if (matrix[tmp_x+1][tmp_y]) return true;
			}
			if (tmp_x == maxX-1){
				return true;
			}
		}
		return false;
	}
	
	
	private void createBlock(){
		base = new Node(maxX/2, 0);
		do{
			Random r = new Random();
			type = r.nextInt(typeNum);
			change = r.nextInt(changeNum);
		}while (!check());
	}

	private boolean check(){
		for (int i = 0; i < nodeNum; i++){
			int tmp_x = base.x + block[type][change][i].x;
			int tmp_y = base.y + block[type][change][i].y;
			if (matrix[tmp_x][tmp_y]){
				return false;
			}
		}
		return true;
	}
	
	private void removeBlock(){
		int len = maxY-1;
		for (int i = maxY-1; i >= 0; i--){
			boolean flag = true;
			for (int j = 0; j < maxX; j++){
				if (!matrix[j][i]){
					flag = false; break;
				}
			}
			if (flag) score++;
				else{
					for (int j = 0; j < maxX; j++){
						matrix[j][len] = matrix[j][i];
					}
					len--;
				}
		}
		for (int i = len; i >= 0; i--){
			for (int j = 0; j < maxX;j++){
				matrix[j][i] = false;
			}
		}
	}
	
	private void fillBlock(){
	//存入所有可能形状	
		block[0][0][0].x = 0; block[0][0][0].y = 0;
		block[0][0][1].x = 0; block[0][0][1].y = 1;
		block[0][0][2].x = 1; block[0][0][2].y = 1;
		block[0][0][3].x = 1; block[0][0][3].y = 2;
		
		block[0][1][0].x = 0; block[0][1][0].y = 1;
		block[0][1][1].x = 1; block[0][1][1].y = 1;
		block[0][1][2].x = 1; block[0][1][2].y = 0;
		block[0][1][3].x = 2; block[0][1][3].y = 0;
		
		block[0][2][0].x = 0; block[0][2][0].y = 0;
		block[0][2][1].x = 0; block[0][2][1].y = 1;
		block[0][2][2].x = 1; block[0][2][2].y = 1;
		block[0][2][3].x = 1; block[0][2][3].y = 2;
		
		block[0][3][0].x = 0; block[0][3][0].y = 1;
		block[0][3][1].x = 1; block[0][3][1].y = 1;
		block[0][3][2].x = 1; block[0][3][2].y = 0;
		block[0][3][3].x = 2; block[0][3][3].y = 0;
	//第一种方块的所有变形
		block[1][0][0].x = 0; block[1][0][0].y = 0;
		block[1][0][1].x = 0; block[1][0][1].y = 1;
		block[1][0][2].x = 0; block[1][0][2].y = 2;
		block[1][0][3].x = 0; block[1][0][3].y = 3;
		
		block[1][1][0].x = 0; block[1][1][0].y = 0;
		block[1][1][1].x = 1; block[1][1][1].y = 0;
		block[1][1][2].x = 2; block[1][1][2].y = 0;
		block[1][1][3].x = 3; block[1][1][3].y = 0;
		
		block[1][2][0].x = 0; block[1][2][0].y = 0;
		block[1][2][1].x = 0; block[1][2][1].y = 1;
		block[1][2][2].x = 0; block[1][2][2].y = 2;
		block[1][2][3].x = 0; block[1][2][3].y = 3;
		
		block[1][3][0].x = 0; block[1][3][0].y = 0;
		block[1][3][1].x = 1; block[1][3][1].y = 0;
		block[1][3][2].x = 2; block[1][3][2].y = 0;
		block[1][3][3].x = 3; block[1][3][3].y = 0;
	//第二种方块的所有变形
		block[2][0][0].x = 0; block[2][0][0].y = 0;
		block[2][0][1].x = 0; block[2][0][1].y = 1;
		block[2][0][2].x = 1; block[2][0][2].y = 0;
		block[2][0][3].x = 1; block[2][0][3].y = 1;
		
		block[2][1][0].x = 0; block[2][1][0].y = 0;
		block[2][1][1].x = 0; block[2][1][1].y = 1;
		block[2][1][2].x = 1; block[2][1][2].y = 0;
		block[2][1][3].x = 1; block[2][1][3].y = 1;
		
		block[2][2][0].x = 0; block[2][2][0].y = 0;
		block[2][2][1].x = 0; block[2][2][1].y = 1;
		block[2][2][2].x = 1; block[2][2][2].y = 1;
		block[2][2][3].x = 1; block[2][2][3].y = 0;
		
		block[2][3][0].x = 0; block[2][3][0].y = 1;
		block[2][3][1].x = 1; block[2][3][1].y = 1;
		block[2][3][2].x = 1; block[2][3][2].y = 0;
		block[2][3][3].x = 0; block[2][3][3].y = 0;
	//第三种方块的所有变形
		block[3][0][0].x = 0; block[3][0][0].y = 0;
		block[3][0][1].x = 1; block[3][0][1].y = 0;
		block[3][0][2].x = 1; block[3][0][2].y = 1;
		block[3][0][3].x = 1; block[3][0][3].y = 2;
		
		block[3][1][0].x = 0; block[3][1][0].y = 0;
		block[3][1][1].x = 0; block[3][1][1].y = 1;
		block[3][1][2].x = 1; block[3][1][2].y = 0;
		block[3][1][3].x = 2; block[3][1][3].y = 0;
		
		block[3][2][0].x = 0; block[3][2][0].y = 0;
		block[3][2][1].x = 0; block[3][2][1].y = 1;
		block[3][2][2].x = 0; block[3][2][2].y = 2;
		block[3][2][3].x = 1; block[3][2][3].y = 2;
		
		block[3][3][0].x = 0; block[3][3][0].y = 1;
		block[3][3][1].x = 1; block[3][3][1].y = 1;
		block[3][3][2].x = 2; block[3][3][2].y = 0;
		block[3][3][3].x = 2; block[3][3][3].y = 1;
	//第四种情况
		block[4][0][0].x = 0; block[4][0][0].y = 2;
		block[4][0][1].x = 1; block[4][0][1].y = 0;
		block[4][0][2].x = 1; block[4][0][2].y = 1;
		block[4][0][3].x = 1; block[4][0][3].y = 2;
		
		block[4][1][0].x = 0; block[4][1][0].y = 0;
		block[4][1][1].x = 1; block[4][1][1].y = 0;
		block[4][1][2].x = 2; block[4][1][2].y = 0;
		block[4][1][3].x = 2; block[4][1][3].y = 1;
		
		block[4][2][0].x = 0; block[4][2][0].y = 0;
		block[4][2][1].x = 0; block[4][2][1].y = 1;
		block[4][2][2].x = 1; block[4][2][2].y = 0;
		block[4][2][3].x = 0; block[4][2][3].y = 2;
	
		block[4][3][0].x = 0; block[4][3][0].y = 0;
		block[4][3][1].x = 0; block[4][3][1].y = 1;
		block[4][3][2].x = 1; block[4][3][2].y = 1;
		block[4][3][3].x = 2; block[4][3][3].y = 1;
	//第五种情况
		block[5][0][0].x = 1; block[5][0][0].y = 0;
		block[5][0][1].x = 0; block[5][0][1].y = 1;
		block[5][0][2].x = 1; block[5][0][2].y = 1;
		block[5][0][3].x = 0; block[5][0][3].y = 2;
		
		block[5][1][0].x = 0; block[5][1][0].y = 0;
		block[5][1][1].x = 1; block[5][1][1].y = 1;
		block[5][1][2].x = 1; block[5][1][2].y = 0;
		block[5][1][3].x = 2; block[5][1][3].y = 1;
		
		block[5][2][0].x = 1; block[5][2][0].y = 0;
		block[5][2][1].x = 0; block[5][2][1].y = 1;
		block[5][2][2].x = 1; block[5][2][2].y = 1;
		block[5][2][3].x = 0; block[5][2][3].y = 2;
		
		block[5][3][0].x = 0; block[5][3][0].y = 0;
		block[5][3][1].x = 1; block[5][3][1].y = 1;
		block[5][3][2].x = 1; block[5][3][2].y = 0;
		block[5][3][3].x = 2; block[5][3][3].y = 1;
	//第六种情况
	}
}

class Node{
	int x, y;
	public Node(int x, int y){
		this.x = x;
		this.y = y;
	}
	public Node(){
		
	}
}
