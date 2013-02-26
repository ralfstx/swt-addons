package ralfstx.swt.custom.imagelabel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import static ralfstx.swt.custom.imagelabel.ImageUtil.createCheckerImage;


public class Transparent_Test {

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new GridLayout( 1, false ) );

    Image image = createCheckerImage( display );
    shell.setBackgroundImage( image );
    shell.setBackgroundMode( SWT.INHERIT_DEFAULT );

    final Image bufferImage = createTransparentImage( display );

    Canvas canvas = new Canvas( shell, SWT.NONE );
    canvas.addPaintListener( new PaintListener() {
      @Override
      public void paintControl( PaintEvent e ) {
        e.gc.drawImage( bufferImage, 0, 0 );
      }
    } );

    Label label = new Label( shell, SWT.NONE );
    label.setImage( bufferImage );

    shell.pack();
    shell.setMinimumSize( 200, 200 );
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  private static Image createTransparentImage( Display display ) {
    PaletteData palette = new PaletteData( new RGB[] { new RGB( 200, 50, 50 ),
                                                       new RGB( 255, 0, 255 ) } );
    ImageData data = new ImageData( 64, 64, 8, palette );
    data.transparentPixel = 1;
    for( int x = 0; x < data.width; x++ ) {
      for( int y = 0; y < data.height; y++ ) {
        data.setPixel( x, y, x < 32 == y < 32 ? 0 : 1 );
      }
    }
    return new Image( display, data );
  }

}
