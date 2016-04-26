package MyPackage;

public class Tetris {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		tetrisModel model = new tetrisModel();
		tetrisControl control = new tetrisControl(model);
		tetrisView view = new tetrisView(model, control);
		model.addObserver(view);
		
		(new Thread(model)).start();
	}

}
