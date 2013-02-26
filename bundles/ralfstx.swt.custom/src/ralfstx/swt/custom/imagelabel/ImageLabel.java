package ralfstx.swt.custom.imagelabel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;


public class ImageLabel extends Canvas {

  private static final int DEFAULT_WIDTH = 64; // see Widget.DEFAULT_WIDTH
  private static final int DEFAULT_HEIGHT = 64; // see Widget.DEFAULT_HEIGHT
  private ImageLoader loader;
  private int imageWidth;
  private int imageHeight;
  private ImageAnimator animator;

  public ImageLabel( Composite parent, int style ) {
    super( parent, style | SWT.NO_BACKGROUND );
    addPaintListener( new PaintListener() {
      @Override
      public void paintControl( PaintEvent event ) {
        paint( event );
      }
    } );
  }

  public void setImageLoader( ImageLoader loader ) {
    if( loader == null ) {
      throw new NullPointerException( "image is null" );
    }
    this.loader = loader;
    determineImageWidth();
    showImage();
  }

  public Object getImageLoader() {
    return loader;
  }

  @Override
  public Point computeSize( int wHint, int hHint, boolean changed ) {
    checkWidget();
    int width = DEFAULT_WIDTH;
    int height = DEFAULT_HEIGHT;
    if( wHint == SWT.DEFAULT ) {
      width = loader != null ? imageWidth : DEFAULT_WIDTH;
    } else if( wHint < 0 ) {
      width = 0;
    }
    if( hHint == SWT.DEFAULT ) {
      height = loader != null ? imageHeight : DEFAULT_HEIGHT;
    } else if( hHint < 0 ) {
      height = 0;
    }
    width += getBorderWidth() * 2;
    height += getBorderWidth() * 2;
    return new Point( width, height );
  }

  @Override
  public void dispose() {
    super.dispose();
  }

  protected void paint( PaintEvent event ) {
    Image bufferImage = animator.getBufferedImage();
    if( bufferImage != null && !bufferImage.isDisposed() ) {
      event.gc.drawImage( bufferImage, 0, 0 );
    }
  }

  private void determineImageWidth() {
    if( loader.data.length == 1 ) {
      imageWidth = loader.data[ 0 ].width;
      imageHeight = loader.data[ 0 ].height;
    } else {
      imageWidth = loader.logicalScreenWidth;
      imageHeight = loader.logicalScreenHeight;
    }
  }

  private void showImage() {
    animator = new ImageAnimator( loader, getDisplay() );
    animator.setRedrawListener( new Runnable() {
      @Override
      public void run() {
        getDisplay().asyncExec( new Runnable() {
          @Override
          public void run() {
            redraw();
          }
        } );
      }
    } );
    animator.start();
  }

}
