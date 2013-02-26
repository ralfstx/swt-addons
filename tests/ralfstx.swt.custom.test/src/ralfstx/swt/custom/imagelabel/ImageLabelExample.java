package ralfstx.swt.custom.imagelabel;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import ralfstx.swt.custom.imagelabel.ImageLabel;


public class ImageLabelExample {

  public static void main( String[] args ) throws IOException {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setBackgroundImage( ImageUtil.createCheckerImage( display ) );
    shell.setBackgroundMode( SWT.INHERIT_DEFAULT );
    shell.setLayout( new GridLayout() );

    createImageLabel( shell, "loading.gif" );
    createImageLabel( shell, "test.gif" );
    createImageLabel( shell, "replace.gif" );
    createImageLabel( shell, "combine.gif" );
    createImageLabel( shell, "Gallop_animated.gif" );
    createImageLabel( shell, "Steam_wheel_animated_transparent.gif" );

    shell.layout();
    shell.pack();
    shell.setMinimumSize( 300, 300 );
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  private static void createImageLabel( Composite parent, String resourceName ) throws IOException {
    ImageLoader loader = ImageUtil.loadAnimatedImage( resourceName );
    Label label = new Label( parent, SWT.NONE );
    label.setText( resourceName );
    label.setLayoutData( new GridData( SWT.CENTER, SWT.TOP, true, false ) );
    Label label2 = new Label( parent, SWT.NONE );
    label2.setImage( new Image( label2.getDisplay(), loader.data[ 0 ] ) );
    label2.setLayoutData( new GridData( SWT.CENTER, SWT.TOP, true, false ) );
    ImageLabel imageLabel = new ImageLabel( parent, SWT.NONE );
    imageLabel.setLayoutData( new GridData( SWT.CENTER, SWT.TOP, true, false ) );
    imageLabel.setImageLoader( loader );
  }

}
