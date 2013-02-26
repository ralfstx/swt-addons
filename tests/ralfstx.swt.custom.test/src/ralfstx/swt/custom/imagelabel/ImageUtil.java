package ralfstx.swt.custom.imagelabel;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;


public class ImageUtil {

  public static Image createCheckerImage( Device device ) {
    ImageData data = createCheckerImageData();
    return new Image( device, data );
  }

  private static ImageData createCheckerImageData() {
    PaletteData palette = new PaletteData( new RGB[] { new RGB( 150, 150, 150 ),
                                                       new RGB( 200, 200, 200 ) } );
    ImageData data = new ImageData( 24, 24, 8, palette );
    for( int x = 0; x < data.width; x++ ) {
      for( int y = 0; y < data.height; y++ ) {
        data.setPixel( x, y, x < 12 == y < 12 ? 0 : 1 );
      }
    }
    return data;
  }

  public static ImageLoader loadAnimatedImage( String resourceName ) throws IOException {
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
      inputStream.close();
    }
  }

  public static void main( String[] args ) {
    ImageData imageData = createCheckerImageData();
    ImageLoader loader = new ImageLoader();
    loader.data = new ImageData[] {imageData};
    loader.save( "/tmp/checker.png", SWT.IMAGE_PNG );
  }
}
