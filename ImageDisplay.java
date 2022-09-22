
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.lang.Math;
import java.util.ArrayList;


public class ImageDisplay {

	JFrame frame;
	JFrame frame2;
	JLabel lbIm1;
	JLabel lbIm2;
	BufferedImage imgOne;
	BufferedImage imgTwo;
	// default image width and height
	int width = 1920; 
	int height = 1080;
	
	//Global Variables.
	int aliasing;
	
	//RGB to YUV conversion matrix
	double [][] RGB_to_YUV = {{0.299, 0.587, 0.114},{0.596, -0.274, -0.322},{0.211, -0.523, 0.312}};
	//YUV to RGB conversion matrix
	double [][] YUV_to_RGB = {{1,0.956,0.621},{1,-0.272,-0.647},{1,-1.106,1.703}};
	
	// Array to store initial RGB data.
	int[][] initialImg_R = new int [height+1][width+1];
	int[][] initialImg_G = new int [height+1][width+1];
	int[][] initialImg_B = new int [height+1][width+1];
	
	//Array to store initial YUV data
	double[][] initialImg_Y = new double [height+1][width+1];
	double[][] initialImg_U = new double [height+1][width+1];
	double[][] initialImg_V = new double [height+1][width+1];
	
	//Array to store RGB values after sub sampling
	int[][] outputImg_R = new int [height+1][width+1];
	int[][] outputImg_G = new int [height+1][width+1];
	int[][] outputImg_B = new int [height+1][width+1];
	
	//Image after Scaling 
	int[][] finalImg_R; //new in t [height+1][width+1];
	int[][] finalImg_G; //new in t [height+1][width+1];
	int[][] finalImg_B; //new in t [height+1][width+1];
	
	//Image after Aliasing
	int[][] AlisImg_R; //new in t [height+1][width+1];
	int[][] AlislImg_G; //new in t [height+1][width+1];
	int[][] AlislImg_B; //new in t [height+1][width+1];
	
    
	/**
     *  Function to perfrom sub-sampling depending on the input parameters Y, U and V.
     *   if Y > 1, Replaces every value from 1 to Y, with average of the neighbouring value.
     */
	private void subSampling(int Y, int U, int V){
		// Y Space
		if(Y > 1) {
			for(int y = 0; y<height; y++)
			{
				for(int x = 0; x < (width-Y); x+=Y) {
				//Setting the value between x and x+n to average of the two.
					for(int s = x+1; s < x+Y; s++) {
						initialImg_Y[y][s]= (initialImg_Y[y][x]+ initialImg_Y[y][x+Y])/2;
					}
				}
			}
	    }
		
		// U Space
		if(U > 1) {
			for(int y = 0; y<height; y++)
			{
				for(int x = 0; x < (width-U); x+=Y) {
					//Setting the value between x and x+n to average of the two.
					 for(int s = x+1; s < x+U; s++) {
						initialImg_U[y][s]= (initialImg_U[y][x]+ initialImg_U[y][x+U])/2;
						
					}
					
				}
			}
		}
		
		// V Space
				if(V > 1) {
					for(int y = 0; y<height; y++)
					{
						for(int x = 0; x < (width-V); x+=V) {
							//Setting the value between x and x+n to average of the two.
							 for(int s = x+1; s < x+V; s++) {
								initialImg_V[y][x]= (initialImg_V[y][x]+ initialImg_V[y][x+V])/2;	
							}
							
						}
					}
			    }
		
	}

	/**
     *  Function to convert the YUV space to RGB space using the YUV_to_RGB conversion matrix.
     *
     */
	private void convertYUV_RGB() {
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				outputImg_R[y][x] = (int)(Math.round(initialImg_Y[y][x]*YUV_to_RGB[0][0])+Math.round(initialImg_U[y][x]*YUV_to_RGB[0][1])+ Math.round(initialImg_V[y][x]*YUV_to_RGB[0][2]));
				if(outputImg_R[y][x] < 0 || outputImg_R[y][x] > 255)
				outputImg_R[y][x] = clamp(outputImg_R[y][x]);
				
				outputImg_G[y][x] = (int)(Math.round(initialImg_Y[y][x]*YUV_to_RGB[1][0])+Math.round(initialImg_U[y][x]*YUV_to_RGB[1][1])+Math.round(initialImg_V[y][x]*YUV_to_RGB[0][2])); 
				if(outputImg_G[y][x] < 0 || outputImg_G[y][x] > 255)
					outputImg_G[y][x] = clamp(outputImg_G[y][x]);
				
				outputImg_B[y][x]=  (int)(Math.round(initialImg_Y[y][x]*YUV_to_RGB[2][0])+Math.round(initialImg_U[y][x]*YUV_to_RGB[2][1])+Math.round(initialImg_V[y][x]*YUV_to_RGB[0][2])); 
				if(outputImg_B[y][x] < 0 || outputImg_B[y][x] > 255)
					outputImg_B[y][x] = clamp(outputImg_B[y][x]);
			}
		}
	}

	/**
	 * Function to round the RGB values between 0-255.
	 * @return return the value between 0 and 255.
     */
	private int clamp(int val){
		int min = 0;
		int max = 255;
		
		if(val < min) {
			val = min;
		}
		else {
			val = max;
		}
		return val;
	}
	
	
	private int computeRedVal(int h, int w){
		 if(h >=0 && h <= height && w >=0 && w <= width) {
		ArrayList<Integer> SumList = new ArrayList<>();
		
		//Top
		if(h-1 >= 0)
			SumList.add(outputImg_R[h-1][w]);
		//down
		if(h+1 <= height )
			SumList.add(outputImg_R[h+1][w]);
		//left
		if(w-1 >= 0)
			SumList.add(outputImg_R[h][w-1]);
		//right
		if(w+1 <= width)
			SumList.add(outputImg_R[h][w+1]);
		
		//leftDiagonal Top
		if(w-1 >=0 && h-1 >= 0)
			SumList.add(outputImg_R[h-1][w-1]);
		
		//Right Diagonal Top
		if(w+1 <=width && h-1 >= 0)
			SumList.add(outputImg_R[h-1][w+1]);
		
		//Left Diagonal down
		if(h+1 <= height && w-1 >=0)
			SumList.add(outputImg_R[h+1][w-1]);
		
		//Right Diagonal down
		if(h+1 <= height && w+1 <= width)
			SumList.add(outputImg_R[h+1][w+1]);
		
		//center
		SumList.add(outputImg_R[h][w]);
	
		int Sum = 0;
		for(int s:SumList) {
			Sum = Sum+ s;
			//System.out.print(s+"LIST");
		}
		//System.out.println("Sum"+Sum+" Size"+ SumList.size());
		return Sum/SumList.size();
		 }
		 return outputImg_R[h][w];
	}
	
    private int computeGreenVal(int h, int w){
    ArrayList<Integer> SumList = new ArrayList<>();
		
    if(h >=0 && h <= height && w >=0 && w <= width) {
		//Top
		if(h-1 >= 0)
			SumList.add(outputImg_G[h-1][w]);
		//down
		if(h+1 <= height )
			SumList.add(outputImg_G[h+1][w]);
		//left
		if(w-1 >= 0)
			SumList.add(outputImg_G[h][w-1]);
		//right
		if(w+1 <= width)
			SumList.add(outputImg_G[h][w+1]);
		
		//leftDiagonal Top
		if(w-1 >=0 && h-1 >= 0)
			SumList.add(outputImg_G[h-1][w-1]);
		
		//Right Diagonal Top
		if(w+1 <=width && h-1 >= 0)
			SumList.add(outputImg_G[h-1][w+1]);
		
		//Left Diagonal down
		if(h+1 <= height && w-1 >=0)
			SumList.add(outputImg_G[h+1][w-1]);
		
		//Right Diagonal down
		if(h+1 <= height && w+1 <= width)
			SumList.add(outputImg_G[h+1][w+1]);
		
		SumList.add(outputImg_G[h][w]);
		
		
		int Sum = 0;
		for(int s:SumList) {
			Sum = Sum + s;
		}
		
		
		return Sum/SumList.size();
    }
    return outputImg_G[h][w];
	}

    private int computeBlueVal(int h, int w){
    	 if(h >=0 && h <= height && w >=0 && w <= width) {
    	 ArrayList<Integer> SumList = new ArrayList<>();
 		
 		//Top
 		if(h-1 >= 0)
 			SumList.add(outputImg_B[h-1][w]);
 		//down
 		if(h+1 <= height )
 			SumList.add(outputImg_B[h+1][w]);
 		//left
 		if(w-1 >= 0)
 			SumList.add(outputImg_B[h][w-1]);
 		//right
 		if(w+1 <= width)
 			SumList.add(outputImg_B[h][w+1]);
 		
 		//leftDiagonal Top
 		if(w-1 >=0 && h-1 >= 0)
 			SumList.add(outputImg_B[h-1][w-1]);
 		
 		//Right Diagonal Top
 		if(w+1 <=width && h-1 >= 0)
 			SumList.add(outputImg_B[h-1][w+1]);
 		
 		//Left Diagonal down
 		if(h+1 <= height && w-1 >=0)
 			SumList.add(outputImg_B[h+1][w-1]);
 		
 		//Right Diagonal down
 		if(h+1 <= height && w+1 <= width)
 			SumList.add(outputImg_B[h+1][w+1]);
 		
 		SumList.add(outputImg_B[h][w]);
 		
 		
 		int Sum = 0;
 		for(int s:SumList) {
 			Sum = Sum+ s;
 		}
 		return Sum/SumList.size();
    	 }
    	 return outputImg_B[h][w];
    }
	
	/**
     * Scales the Image to the New height and width
     */
	private void scaling(float sh, float sw){
		int new_Height = (int)(height*sh);
		int new_Width = (int)(width*sw);
		
		finalImg_R = new int [new_Height +1][new_Width +1];
		finalImg_G = new int [new_Height +1][new_Width +1];
		finalImg_B = new int [new_Height +1][new_Width +1];
		
		for(int y = 0; y< new_Height; y++)
		{
			for(int x = 0; x <new_Width; x++){
				finalImg_R[y][x] = outputImg_R[(int)(y/sh)][(int)(x/sw)];
				finalImg_G[y][x] = outputImg_G[(int)(y/sh)][(int)(x/sw)];
				finalImg_B[y][x] = outputImg_B[(int)(y/sh)][(int)(x/sw)];	
			}
		}
		displayImageResult(new_Height, new_Width, finalImg_R, finalImg_G, finalImg_B); 
	}
	
	/**
     * Scales the Image to the New height and width with Aliasing
     */
	private void scalingA(float sh, float sw){
		imgTwo = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int new_Height = (int)(height*sh);
		int new_Width = (int)(width*sw);
		
		finalImg_R = new int [new_Height +1][new_Width +1];
		finalImg_G = new int [new_Height +1][new_Width +1];
		finalImg_B = new int [new_Height +1][new_Width +1];
		byte a = 0;

		for(int y = 0; y< new_Height; y++)
		{
			for(int x = 0; x <new_Width; x++){

				finalImg_R[y][x] = computeRedVal((int)(y/sh), (int)(x/sw));
				finalImg_G[y][x] = computeGreenVal((int)(y/sh),(int)(x/sw));
				finalImg_B[y][x] = computeBlueVal((int)(y/sh),(int)(x/sw));	
				
				int pix = ((a << 24) + (finalImg_R[y][x] << 16) + (finalImg_G[y][x] << 8) + finalImg_B[y][x]);
                imgTwo.setRGB(x, y, pix);
			}
		}
	}
	
	private void displayImageResult(int height, int width, int[][] red, int[][] green, int[][]blue){
		imgTwo = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				byte a = 0;
				int pix = ((a << 24) + (red[y][x] << 16) + (green[y][x] << 8) + blue[y][x]);
				imgTwo.setRGB(x,y,pix);
				
			}
		}
	}
	
	
	
	
	
	
	/** Read Image RGB
	 *  Reads the image of given width and height at the given imgPath into the provided BufferedImage.
	 */
	private void readImageRGB(int width, int height, String imgPath, BufferedImage img)
	{
		try
		{
			int frameLength = width*height*3;
     
			File file = new File(imgPath);
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(0);

			long len = frameLength;
			byte[] bytes = new byte[(int) len];

			raf.read(bytes);

			int ind = 0;
			for(int y = 0; y < height; y++)
			{
				for(int x = 0; x < width; x++)
				{
					byte a = 0;
					byte r = bytes[ind];
					byte g = bytes[ind+height*width];
					byte b = bytes[ind+height*width*2]; 
					
					//Storing the initial image RGB values
					initialImg_R[y][x] = (r & 0xff);
					initialImg_G[y][x] = (g & 0xff);
					initialImg_B[y][x] = (b & 0xff);
					
					//Converting to YUV space.
					initialImg_Y[y][x] = (r & 0xff)*RGB_to_YUV[0][0] + (g & 0xff)*RGB_to_YUV[0][1] + (b & 0xff)*RGB_to_YUV[0][2]; 
					initialImg_U[y][x] = (r & 0xff)*RGB_to_YUV[1][0] + (g & 0xff)*RGB_to_YUV[1][1] + (b & 0xff)*RGB_to_YUV[1][2]; 
					initialImg_V[y][x] = (r & 0xff)*RGB_to_YUV[2][0] + (g & 0xff)*RGB_to_YUV[2][1] + (b & 0xff)*RGB_to_YUV[2][2]; 
					
					int pix = ((a << 24) + (initialImg_R[y][x] << 16) + (initialImg_G[y][x] << 8) + initialImg_B[y][x]);
					//original Image.
					img.setRGB(x,y,pix);
					ind++;
				}
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void showIms(String[] args){

		// Read a parameter from command line
		String fileName = args[0];
		int Y  = Integer.parseInt(args[1]);
		int U  = Integer.parseInt(args[2]);
		int V  = Integer.parseInt(args[3]);

		float Sw = Float.parseFloat(args[4]);
		float Sh = Float.parseFloat(args[5]);
		aliasing = Integer.parseInt(args[6]);
	
		// Read in the specified image
		imgOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		readImageRGB(width, height, fileName, imgOne);
		subSampling(Y,U,V);
		//convert YUV to RGB
		convertYUV_RGB();
		
		//SCALING AND ANTIALISING
		// Scaling with no antialising
		if(Sw < 1.0 && Sh < 1.0 && aliasing == 0)  
			scaling(Sh, Sw);
		else
		//Scaling with antianlising.
			scalingA(Sh, Sw);
		
		// Use label to display the image
		frame = new JFrame();
		frame2 = new JFrame();
		
		GridBagLayout gLayout = new GridBagLayout();
		GridBagLayout gLayout2 = new GridBagLayout();
		
		frame.getContentPane().setLayout(gLayout);
		frame2.getContentPane().setLayout(gLayout2);
		

		lbIm1 = new JLabel(new ImageIcon(imgOne));
		lbIm2 = new JLabel(new ImageIcon(imgTwo));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.anchor = GridBagConstraints.CENTER;
		c1.weightx = 0.5;
		c1.gridx = 0;
		c1.gridy = 0;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		frame.getContentPane().add(lbIm1, c);
		
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridx = 0;
		c1.gridy = 1;
		frame2.getContentPane().add(lbIm2, c1);

		frame.pack();
		frame2.pack();
		
		frame.setVisible(true);
		frame2.setVisible(true);
	}

	public static void main(String[] args) {
		ImageDisplay ren = new ImageDisplay();
		ren.showIms(args);
	}

}
