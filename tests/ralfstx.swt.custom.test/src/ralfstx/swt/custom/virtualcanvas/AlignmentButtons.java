package ralfstx.swt.custom.virtualcanvas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


abstract class AlignmentButtons {

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

  protected abstract void updateAlignment( int alignment );

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

}
