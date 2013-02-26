package ralfstx.swt.custom.virtualcanvas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import ralfstx.swt.custom.virtualcanvas.Painter;
import ralfstx.swt.custom.virtualcanvas.VirtualCanvas;


public class VirtualCanvasPainterExample {

  private VirtualCanvas virtualCanvas;

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    new VirtualCanvasPainterExample().createUi( shell );
    shell.setSize( 400, 400 );
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  private void createUi( Composite parent ) {
    parent.setLayout( new GridLayout( 1, false ) );
    virtualCanvas = new VirtualCanvas( parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL );
    virtualCanvas.getHorizontalBar().setVisible( false );
    virtualCanvas.getVerticalBar().setVisible( false );
    virtualCanvas.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    Painter painter = createPainter();
    virtualCanvas.setContentSize( 500, 200 );
    virtualCanvas.setPainter( painter );
    createAlignmentControls( createButtonBar( parent ) );
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

  private static Painter createPainter() {
    return new Painter() {
      @Override
      public void paint( GC gc, int srcX, int srcY, int destX, int destY, int width, int height ) {
        Color black = new Color( gc.getDevice(), 0, 0, 0 );
        Color white = new Color( gc.getDevice(), 200, 200, 200 );
        int w = 50 - srcX % 50;
        int fieldX = srcX / 50;
        System.out.println( srcX + " " + destX + " " + w );
        Image image = new Image( gc.getDevice(), width, height );
        try {
          int x = 0;
          while( x < 500 ) {
            gc.setBackground( fieldX % 2 == 0 ? black : white );
            gc.fillRectangle( destX + x, destY, w, 50 );
            x += w;
            w = 50;
            fieldX++;
          }
//          gc.copyArea( image, destX, destY );
        } finally {
          image.dispose();
          black.dispose();
          white.dispose();
        }
      }
    };
  }

}
