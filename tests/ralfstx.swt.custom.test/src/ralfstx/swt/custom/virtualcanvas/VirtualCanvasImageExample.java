package ralfstx.swt.custom.virtualcanvas;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import ralfstx.swt.custom.virtualcanvas.VirtualCanvas;


public class VirtualCanvasImageExample {

  private VirtualCanvas virtualCanvas;

  public static void main( String[] args ) throws IOException {
    Display display = new Display();
    Shell shell = new Shell( display );
    new VirtualCanvasImageExample().createUi( shell );
    shell.setSize( 800, 600 );
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  private void createUi( Composite parent ) throws IOException {
    parent.setLayout( new GridLayout( 1, false ) );
    virtualCanvas = new VirtualCanvas( parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL );
    virtualCanvas.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    Image image = createImage( parent.getDisplay(), "resources/testimage.jpg" );
    virtualCanvas.setImage( image );
    Composite buttonBar = createButtonBar( parent );
    createAlignmentControls( buttonBar );
  }

  private void createAlignmentControls( Composite parent ) {
    new Label( parent, SWT.NONE ).setText( "Alignment:" );
    new AlignmentButtons( parent ) {
      @Override
      protected void updateAlignment( int alignment ) {
        virtualCanvas.setAlignment( alignment );
      }
    };
  }

  private static Composite createButtonBar( Composite parent ) {
    Composite buttonBar = new Composite( parent, SWT.NONE );
    buttonBar.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    RowLayout layout = new RowLayout( SWT.HORIZONTAL );
    layout.center = true;
    layout.spacing = 5;
    buttonBar.setLayout( layout );
    return buttonBar;
  }

  private static Image createImage( Display display, String name ) throws IOException {
    ClassLoader classLoader = VirtualCanvasImageExample.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream( name );
    if( inputStream == null ) {
      throw new IllegalArgumentException( "Image not found: " + name );
    }
    try {
      return new Image( display, inputStream );
    } finally {
      inputStream.close();
    }
  }

}
