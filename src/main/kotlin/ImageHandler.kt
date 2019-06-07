import javax.imageio.ImageIO
import java.io.IOException
import java.io.File
import java.awt.image.BufferedImage
import java.awt.image.BufferedImageFilter
import java.lang.Exception
import java.net.URL
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int
import java.awt.Color


fun loadImage (imagePath:String): BufferedImage {
    var img = BufferedImage(1200,1200, BufferedImage.TYPE_3BYTE_BGR)

    if(imagePath.contains("http".toRegex())){
        val url = URL(imagePath)
        try {
            img = ImageIO.read(url)
        } catch (e: IOException) {
        }
    }
    else {
        try {
            img = ImageIO.read(File(imagePath))
        } catch (ex: IOException) {
            ex.stackTrace
        }
    }

    return img
}

fun saveImage(imageFileName: String,format:String , image : BufferedImage){

    try {
        // retrieve image
        val  outputfile = File(imageFileName);
        ImageIO.write(image, format, outputfile);
    } catch (ex: Exception) {
        ex.stackTrace
    }
}

fun convertImageToIntegralArray(image:BufferedImage){
    val integralArray =calculateLuminanceOfImage(image)
}



/**
 *Яркость (стандартная для определенных цветовых пространств): (0.2126*R + 0.7152*G + 0.0722*B) [1]
 * Luminance (воспринимаемый вариант 1): (0.299*R + 0.587*G + 0.114*B) [2]
 * Luminance (воспринимаемый вариант 2, медленнее для вычисления): sqrt( 0.241*R^2 + 0.691*G^2 + 0.068*B^2 ) → sqrt( 0.299*R^2 + 0.587*G^2 + 0.114*B^2 ) (благодаря @MatthewHerbst) [3]
 */

fun calculateLuminanceOfImage(image : BufferedImage): Array<LongArray> {
    val integralArray = Array(image.height) { LongArray(image.width) }

    for(y in 0 until image.height){
        val xLine = LongArray(image.width)
        for(x in 0 until image.width){
            xLine[x] = calculateLuminanceOfPixel(image.getRGB(x,y)).toLong()
        }
        integralArray[y] = xLine
    }
    return integralArray
}

fun calculateLuminanceOfPixel(rgb: Int): Double {
    val color = Color(rgb)
    val R = color.red
    val G = color.green
    val B = color.blue
    val luminance = 0.299*R + 0.587*G + 0.114*B
    return luminance
}
