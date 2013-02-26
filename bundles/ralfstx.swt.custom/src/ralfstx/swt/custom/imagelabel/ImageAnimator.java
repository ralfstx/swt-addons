package ralfstx.swt.custom.imagelabel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.RGB;


class ImageAnimator implements Runnable {

  private final ImageLoader loader;
  private final Device device;
  private Image bufferImage;
  private GC bufferGC;
  private ImageData currImgData;
  private Thread animateThread;
  private Runnable redrawListener;
  private final int imageWidth;
  private final int imageHeight;

  public ImageAnimator( ImageLoader loader, Device device ) {
    this.loader = loader;
    this.device = device;
    imageWidth = loader.data.length == 1 ? loader.data[ 0 ].width : loader.logicalScreenWidth;
    imageHeight = loader.data.length == 1 ? loader.data[ 0 ].height : loader.logicalScreenHeight;
  }

  public void setRedrawListener( Runnable runnable ) {
    this.redrawListener = runnable;
  }

  public void start() {
    animateThread = new Thread( this, "ImageAnimator" );
    animateThread.setDaemon( true );
    animateThread.start();
  }

  @Override
  public void run() {
    initializeBuffer();
    try {
      int repeatCount = 0;
      while( repeatCount < loader.repeatCount || loader.repeatCount == 0 ) {
        currImgData = loader.data[ 0 ];
        fillBackground( 0, 0, imageWidth, imageHeight );
        for( int i = 0; i < loader.data.length; i++ ) {
          currImgData = loader.data[ i ];
          drawCurrentImageOnBuffer();
          notifyListener();
          delay();
          drawDisposalOnBuffer();
        }
        repeatCount++;
      }
    } finally {
      dispose();
    }
  }

  private void initializeBuffer() {
    if( bufferImage != null ) {
      bufferImage.dispose();
    }
    bufferImage = new Image( device, imageWidth, imageHeight );
    bufferGC = new GC( bufferImage );
  }

  private void drawDisposalOnBuffer() {
    if( currImgData.disposalMethod == SWT.DM_FILL_BACKGROUND ) {
      fillBackground();
    } else if( currImgData.disposalMethod == SWT.DM_FILL_PREVIOUS ) {
      // TODO not sure what to do here, drawing the previous image again doesn't seem to make sense
    }
  }

  private void fillBackground() {
    fillBackground( currImgData.x, currImgData.y, currImgData.width, currImgData.height );
  }

  private void fillBackground( int x, int y, int width, int height ) {
    if( loader.backgroundPixel != -1 ) {
      RGB rgb = currImgData.palette.getRGB( loader.backgroundPixel );
      Color bgColor = new Color( bufferGC.getDevice(), rgb );
      try {
        bufferGC.setBackground( bgColor );
      } finally {
        bgColor.dispose();
      }
      bufferGC.fillRectangle( x, y, width, height );
    }
  }

  private void drawCurrentImageOnBuffer() {
    drawImageOnBuffer( currImgData );
  }

  private void drawImageOnBuffer( ImageData data ) {
    Image currentImage = new Image( bufferImage.getDevice(), currImgData );
    try {
      bufferGC.drawImage( currentImage,
                          0, 0, data.width, data.height,
                          data.x, data.y, data.width, data.height );
    } finally {
      currentImage.dispose();
    }
  }

  private void notifyListener() {
    if( redrawListener != null ) {
      redrawListener.run();
    }
  }

  private void delay() {
    // Sleep for the specified delay time (adding commonly-used slow-down fudge factors).
    try {
      int ms = currImgData.delayTime * 10;
      if( ms < 20 ) {
        ms += 30;
      }
      if( ms < 30 ) {
        ms += 10;
      }
      Thread.sleep( ms );
    } catch( InterruptedException e ) {
    }
  }

  private void dispose() {
    if( bufferGC != null && !bufferGC.isDisposed() ) {
      bufferGC.dispose();
    }
  }

  public Image getBufferedImage() {
    return bufferImage;
  }

}
