/*******************************************************************************
 * Copyright (c) 2012 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial implementation and API
 ******************************************************************************/
package ralfstx.swt.custom.virtualcanvas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;


public class VirtualCanvas extends Canvas {

  private static final int FIXED_STYLES = SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE;
  private static final int OPTIONAL_STYLES = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
  private final ScrollBar hBar;
  private final ScrollBar vBar;
  private final Rectangle contentArea;
  private int alignment;
  private Painter painter;

  public VirtualCanvas( Composite parent, int style ) {
    super( parent, FIXED_STYLES | style & OPTIONAL_STYLES );
    contentArea = new Rectangle( 0, 0, 100, 100 );
    alignment = SWT.CENTER;
    hBar = getHorizontalBar();
    vBar = getVerticalBar();
    addHScrollListener();
    addVScrollListener();
    addResizeListener();
    addPaintListener();
  }

  public void setAlignment( int alignment ) {
    this.alignment = alignment;
    updateLayout();
    update();
  }

  public void setContentSize( int width, int height ) {
    contentArea.width = width;
    contentArea.height = height;
    updateLayout();
    update();
  }

  public void setPainter( Painter painter ) {
    this.painter = painter;
    redraw();
    update();
  }

  public void setImage( Image image ) {
    this.painter = new ImagePainter( image );
    Rectangle bounds = image.getBounds();
    setContentSize( bounds.width, bounds.height );
    redraw();
    update();
  }

  private void addHScrollListener() {
    if( hBar != null ) {
      hBar.addListener( SWT.Selection, new Listener() {
        @Override
        public void handleEvent( Event e ) {
          int hSelection = hBar.getSelection();
          int destX = -hSelection - contentArea.x;
          scroll( destX, contentArea.y, 0, contentArea.y, contentArea.width, contentArea.height,
                  false );
          contentArea.x = -hSelection;
        }
      } );
    }
  }

  private void addVScrollListener() {
    if( vBar != null ) {
      vBar.addListener( SWT.Selection, new Listener() {
        @Override
        public void handleEvent( Event e ) {
          int vSelection = vBar.getSelection();
          int destY = -vSelection - contentArea.y;
          scroll( contentArea.x, destY, contentArea.x, 0, contentArea.width, contentArea.height,
                  false );
          contentArea.y = -vSelection;
        }
      } );
    }
  }

  private void addResizeListener() {
    addListener( SWT.Resize, new Listener() {
      @Override
      public void handleEvent( Event e ) {
        updateLayout();
      }
    } );
  }

  private void updateLayout() {
    Rectangle clientArea = getClientArea();
    updateHScrollBar( clientArea );
    updateVScrollBar( clientArea );
    updateHOrigin( clientArea );
    updateVOrigin( clientArea );
    redraw();
  }

  private void updateHScrollBar( Rectangle clientArea ) {
    if( hBar != null ) {
      boolean isRightAligned = ( alignment & SWT.RIGHT ) != 0
                               && hBar.getMaximum() == hBar.getSelection() + hBar.getThumb();
      hBar.setMaximum( contentArea.width );
      hBar.setThumb( Math.min( contentArea.width, clientArea.width ) );
      if( isRightAligned ) {
        hBar.setSelection( contentArea.width - clientArea.width );
      }
    }
  }

  private void updateVScrollBar( Rectangle clientArea ) {
    if( vBar != null ) {
      boolean isBottomAligned = ( alignment & SWT.BOTTOM ) != 0
                                && vBar.getMaximum() == vBar.getSelection() + vBar.getThumb();
      vBar.setMaximum( contentArea.height );
      vBar.setThumb( Math.min( contentArea.height, clientArea.height ) );
      if( isBottomAligned ) {
        vBar.setSelection( contentArea.height - clientArea.height );
      }
    }
  }

  private void updateHOrigin( Rectangle clientArea ) {
    int hPage = contentArea.width - clientArea.width;
    int hScroll = getHScroll( clientArea );
    if( hPage <= 0 ) {
      if( ( alignment & SWT.RIGHT ) != 0 ) {
        hScroll = hPage;
      } else if( ( alignment & SWT.LEFT ) != 0 ) {
        hScroll = 0;
      } else {
        hScroll = hPage / 2;
      }
    }
    contentArea.x = clientArea.x - hScroll;
  }

  private void updateVOrigin( Rectangle clientArea ) {
    int vPage = contentArea.height - clientArea.height;
    int vScroll = getVScroll( clientArea );
    if( vPage <= 0 ) {
      if( ( alignment & SWT.BOTTOM ) != 0 ) {
        vScroll = vPage;
      } else if( ( alignment & SWT.TOP ) != 0 ) {
        vScroll = 0;
      } else {
        vScroll = vPage / 2;
      }
    }
    contentArea.y = clientArea.y - vScroll;
  }

  private int getHScroll( Rectangle clientArea ) {
    if( hBar != null ) {
      return hBar.getSelection();
    }
    // if we don't have a horizontal scrollbar, but RIGHT alignment, simulate scrolled to max
    return ( alignment & SWT.RIGHT ) != 0 ? contentArea.width - clientArea.width : 0;
  }

  private int getVScroll( Rectangle clientArea ) {
    if( vBar != null ) {
      return vBar.getSelection();
    }
    // if we don't have a vertical scrollbar, but BOTTOM alignment, simulate scrolled to max
    return ( alignment & SWT.BOTTOM ) != 0 ? contentArea.height - clientArea.height : 0;
  }

  private void addPaintListener() {
    addListener( SWT.Paint, new Listener() {
      @Override
      public void handleEvent( Event e ) {
        paint( e );
      }
    } );
  }

  private void paint( Event e ) {
    GC gc = e.gc;
    Rectangle clientArea = getClientArea();
    gc.setBackground( getBackground() );
    if( painter != null ) {
      int srcX = -Math.min( 0, contentArea.x );
      int srcY = -Math.min( 0, contentArea.y );
      Rectangle destArea = getDestArea( clientArea );
      fillFrame( gc, clientArea, destArea );
      gc.setClipping( contentArea );
      painter.paint( gc, srcX, srcY, destArea.x, destArea.y, destArea.width, destArea.height );
    } else {
      fillArea( gc, clientArea );
    }
  }

  private Rectangle getDestArea( Rectangle clientArea ) {
    Rectangle destArea = new Rectangle( 0, 0, 0, 0 );
    destArea.x = Math.max( 0, contentArea.x );
    destArea.y = Math.max( 0, contentArea.y );
    destArea.width = Math.min( clientArea.width, contentArea.width );
    destArea.height = Math.min( clientArea.height, contentArea.height );
    return destArea;
  }

  private void fillFrame( GC gc, Rectangle clientArea, Rectangle destArea ) {
    Rectangle topBar = new Rectangle( clientArea.x, clientArea.y, clientArea.width, destArea.y );
    Rectangle left = new Rectangle( clientArea.x, destArea.y, destArea.x, destArea.height );
    Rectangle right = new Rectangle( destArea.x + destArea.width, destArea.y,
                                     clientArea.width - destArea.width, destArea.height );
    Rectangle bottomBar = new Rectangle( clientArea.x, destArea.y + destArea.height,
                                         clientArea.width, clientArea.height - destArea.height );
    fillArea( gc, topBar );
    fillArea( gc, left );
    fillArea( gc, right );
    fillArea( gc, bottomBar );
  }

  private void fillArea( GC gc, Rectangle area ) {
    if( !area.isEmpty() ) {
      gc.fillRectangle( area );
    }
  }

}
