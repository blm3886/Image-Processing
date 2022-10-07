# Image-Processing

<div width = "50%">
https://user-images.githubusercontent.com/97884167/194514996-4ec36c54-c508-4cce-be36-17a9fa35701c.mp4
</div>
<div width = "50%">
 https://user-images.githubusercontent.com/97884167/194514958-35339422-a61c-405d-b6ef-b8ca847cb2bd.mp4
</div>



https://user-images.githubusercontent.com/97884167/194514891-cbf197e6-e236-43c7-bc5c-87974c620a72.mp4



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
