package ralfstx.swt.custom.virtualcanvas;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import ralfstx.swt.custom.virtualcanvas.ImagePainter;
import ralfstx.swt.custom.virtualcanvas.VirtualCanvas;


public class VirtualCanvasExample {

  private VirtualCanvas scrollableCanvas;

  public static void main( String[] args ) throws IOException {
    Display display = new Display();
    Shell shell = new Shell( display );
    new VirtualCanvasExample().createUi( shell );
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
    scrollableCanvas = new VirtualCanvas( parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL );
    scrollableCanvas.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    Image testImage = createImage( parent.getDisplay(), "resources/testimage.jpg" );
    ImagePainter painter = new ImagePainter( testImage );
    scrollableCanvas.setPainter( painter );
    Composite buttonBar = createButtonBar( parent );
    createSizeControls( buttonBar, testImage.getBounds().width, testImage.getBounds().height );
    createAlignmentControls( buttonBar );
  }

  private void createSizeControls( Composite parent, int width, int height ) {
    new Label( parent, SWT.NONE ).setText( "Content Size:" );
    SizeButtons sizeButtons = new SizeButtons( parent ) {
      @Override
      protected void updateSize( int width, int height ) {
        scrollableCanvas.setContentSize( width, height );
      }
    };
    sizeButtons.widthSpinner.setSelection( width );
    sizeButtons.heightSpinner.setSelection( height );
  }

  private void createAlignmentControls( Composite parent ) {
    new Label( parent, SWT.NONE ).setText( "Alignment:" );
    new AlignmentButtons( parent ) {
      @Override
      protected void updateAlignment( int alignment ) {
        scrollableCanvas.setAlignment( alignment );
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
    ClassLoader classLoader = VirtualCanvasExample.class.getClassLoader();
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

  static abstract class SizeButtons {

    private final Spinner widthSpinner;
    private final Spinner heightSpinner;

    public SizeButtons( Composite parent ) {
      Listener listener = createListener();
      widthSpinner = createSpinner( parent, listener );
      new Label( parent, SWT.NONE ).setText( "x" );
      heightSpinner = createSpinner( parent, listener );
    }

    private Spinner createSpinner( Composite parent, Listener listener ) {
      Spinner spinner = new Spinner( parent, SWT.BORDER );
      spinner.setMaximum( 10000 );
      spinner.setPageIncrement( 100 );
      spinner.setIncrement( 10 );
      spinner.setSelection( 100 );
      spinner.addListener( SWT.Modify, listener );
      return spinner;
    }

    private Listener createListener() {
      return new Listener() {

        @Override
        public void handleEvent( Event event ) {
          int width = widthSpinner.getSelection();
          int height = heightSpinner.getSelection();
          updateSize( width, height );
        }
      };
    }

    protected abstract void updateSize( int width, int height );

  }

  static abstract class AlignmentButtons {
    private final Button leftButton;
    private final Button rightButton;
    private final Button topButton;
    private final Button bottomButton;

    public AlignmentButtons( Composite parent ) {
      Listener listener = createListener();
      leftButton = createCheckButton( parent, "LEFT", listener );
      rightButton = createCheckButton( parent, "RIGHT", listener );
      topButton = createCheckButton( parent, "TOP", listener );
      bottomButton = createCheckButton( parent, "BOTTOM", listener );
    }

    private Button createCheckButton( Composite parent, String text, Listener listener ) {
      Button button = new Button( parent, SWT.CHECK );
      button.setText( text );
      button.addListener( SWT.Selection, listener );
      return button;
    }

    private Listener createListener() {
      return new Listener() {

        @Override
        public void handleEvent( Event event ) {
          int alignment = SWT.NONE;
          if( event.widget == leftButton && leftButton.getSelection() ) {
            rightButton.setSelection( false );
          }
          if( event.widget == rightButton && rightButton.getSelection() ) {
            leftButton.setSelection( false );
          }
          if( event.widget == topButton && topButton.getSelection() ) {
            bottomButton.setSelection( false );
          }
          if( event.widget == bottomButton && bottomButton.getSelection() ) {
            topButton.setSelection( false );
          }
          if( leftButton.getSelection() ) {
            alignment |= SWT.LEFT;
          }
          if( rightButton.getSelection() ) {
            alignment |= SWT.RIGHT;
          }
          if( topButton.getSelection() ) {
            alignment |= SWT.TOP;
          }
          if( bottomButton.getSelection() ) {
            alignment |= SWT.BOTTOM;
          }
          updateAlignment( alignment );
        }
      };
    }

    protected abstract void updateAlignment( int alignment );

  }

}
