# CheckCreator

This console application generates a check and displays it on the screen; if necessary, the check is additionally written to a file.

All parameters are passed as command line arguments. Types of parameters and the corresponding argument format: 
1) product: <productId-productQuantity>
2) discount card: <'Card'-cardNumber>
3) file name for loading initial data about all goods: <'File'-'ProductsInput'-absoluteFilename>
4) file name for loading initial data about all discount cards: <'File'-'CardsInput'-absoluteFilename>
5) file name for saving a receipt: <'File'-'CheckOutput'-absoluteFilename>. 
The order of the passed parameters does not matter.

To build the application, JDK version 17.0.1, Gradle version 7.3.3, were used.
