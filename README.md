# Freeman-Chain-Codes
A Java application to generate Freeman Chain Codes using binarized image data. 

The following application was created with the intention of generating Freeman Chain Codes (FCC) from handwritten images,
specifically the numerical digits 0 through 9. 

This application accepts images in a 30x30 format, and assumed the image is already black and white.

The application traverses through the image starting at the top left corner until it stumbles upon a black pixel, effectively making it the starting point.
The edges of the written character are traversed in 9 separate quadrants, and returned for further classification. Separating the image
into 9 quadrants allows for a higher classification rate than when traversing the image in a single sweep. 
