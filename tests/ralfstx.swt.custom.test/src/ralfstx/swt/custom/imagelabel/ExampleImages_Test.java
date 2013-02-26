package ralfstx.swt.custom.imagelabel;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;


public class ExampleImages_Test {

  @Test
  public void testName() {
    ImageLoader loader = loadAnimatedImage( "replace.gif" );
    System.out.println( "frames: " + loader.data.length );
    System.out.println( "background: " + loader.backgroundPixel );
    System.out.println( "repeat: " + loader.repeatCount );

    RGB[] colors = loader.data[ 0 ].palette.colors;
    for( int i = 0; i < colors.length; i++ ) {
      RGB rgb = colors[i];
      System.out.println( i + ": " + rgb );
    }

    for( int i = 0; i < loader.data.length; i++ ) {
      ImageData imageData = loader.data[ i ];
      System.out.println( "Frame " + i + " (" + imageData.width + "x" + imageData.height + ")" );
      System.out.println( "  offset: " + imageData.x + "," + imageData.y );
      System.out.println( "  delay " + imageData.delayTime + "0ms" );
      System.out.println( "  disposal " + imageData.disposalMethod );
      System.out.println( "  transparent " + imageData.transparentPixel );
    }
  }

  private static ImageLoader loadAnimatedImage( String resourceName ) {
    ClassLoader classLoader = ImageLabelExample.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream( "resources/" + resourceName );
    if( inputStream == null ) {
      throw new IllegalArgumentException( "Failed to load image: " + resourceName );
    }
    try {
      ImageLoader imageLoader = new ImageLoader();
      imageLoader.load( inputStream );
      return imageLoader;
    } finally {
      try {
        inputStream.close();
      } catch( IOException exception ) {
        throw new RuntimeException( exception );
      }
    }
  }

}
