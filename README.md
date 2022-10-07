# Image-Processing
Output Video:


https://user-images.githubusercontent.com/97884167/194515996-5c72e2a4-32ab-42b1-8f92-fce188288e6d.mp4



Input Video:



https://user-images.githubusercontent.com/97884167/194516107-6989ca62-4e2b-457b-b765-21b2909e0621.mp4


https://user-images.githubusercontent.com/97884167/194516141-c88b52d1-55a6-4f99-bceb-fa8796070390.mp4



A course work project on Quantization and Subsampling of image  to analyze how it affects visual media types like images and video.

Your program will be invoked using seven parameters where
YourProgram.exe C:/myDir/myImage.rgb Y U V Sw Sh A
- The first parameter is the name of the image, which will be provided in an 8 bit per channel RGB format (Total 24 bits per pixel). 
All of the images will be of the same size for this assignment (HD size = 1920wx1080h).
- The next three parameters are integers control the subsampling of your Y U and V spaces respectively. For sake of simplicity, we will follow the convention that subsampling occurs only along the width dimension and not the height. Each of these parameters can take on values from 1 to n for some n, 1 suggesting no sub sampling and n suggesting a sub sampling by n
- The next two parameters are single precision floats Sw and Sh which take positive values < 1.0 and control the scaled output image width and height independently.
- Finally a integer A ( 0 or 1) to suggest that antialiasing (prefiltering needs to be performed). 0 indicates no antialiasing and vice versa

The flow of the code:
1) Reading the image in RGB format.
2) Converting to YUV space 
3) Processing YUV sub-sampling 
4) Adjusting YUV sub-sampled data for display 
5) Converting back to RGB space
6)  Scale RGB image with Sw & Sh respecting choice of A.
7) Display Image output.

In the Image below, the inital image in the background shows the original image, the image on the right shows the image after the Quantization, Sub Sampling and Scaling process.

![result](https://github.com/blm3886/Image-Processing/blob/main/result.png)
