package ralfstx.swt.custom.imagelabel;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ralfstx.swt.custom.imagelabel.ImageLabel;

import static org.junit.Assert.*;


public class ImageLabel_Test {

  private Display display;
  private Shell shell;

  @Before
  public void setUp() {
    display = Display.getDefault();
    shell = new Shell( display );
  }

  @After
  public void tearDown() {
    shell.dispose();
  }

  @Test
  public void create() {
    Composite parent = new Composite( shell, SWT.NONE );
    ImageLabel label = new ImageLabel( parent, SWT.BORDER );

    assertSame( parent, label.getParent() );
    assertEquals( SWT.BORDER, label.getStyle() & SWT.BORDER );
  }

  @Test
  public void setImageLoader_null() {
    ImageLabel label = new ImageLabel( shell, SWT.NONE );

    try {
      label.setImageLoader( null );
      fail();
    } catch( NullPointerException exception ) {
    }
  }

  @Test
  public void setImageLoader() {
    ImageLabel label = new ImageLabel( shell, SWT.BORDER );
    ImageLoader imageLoader = createAnimatedImage( "loading.gif" );

    label.setImageLoader( imageLoader );

    assertSame( imageLoader, label.getImageLoader() );
  }

  // TODO empty loader, load image later
  // TODO load another image at runtime

  @Test
  public void computeSize_default() {
    ImageLabel label = new ImageLabel( shell, SWT.NONE );
    Canvas canvas = new Canvas( shell, SWT.NONE );

    assertEquals( canvas.computeSize( SWT.DEFAULT, SWT.DEFAULT ),
                  label.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
  }

  @Test
  public void computeSize_animatedGif() {
    ImageLabel label = new ImageLabel( shell, SWT.BORDER );
    ImageLoader imageLoader = createAnimatedImage( "loading.gif" );

    label.setImageLoader( imageLoader );

    int borderWidth = label.getBorderWidth() * 2;
    Point size = label.computeSize( SWT.DEFAULT, SWT.DEFAULT );
    assertEquals( 32 + borderWidth, size.x );
    assertEquals( 32 + borderWidth, size.y );
  }

  @Test
  public void computeSize_singlePng() {
    ImageLabel label = new ImageLabel( shell, SWT.BORDER );
    ImageLoader imageLoader = createAnimatedImage( "100x50.png" );

    label.setImageLoader( imageLoader );

    int borderWidth = label.getBorderWidth() * 2;
    Point size = label.computeSize( SWT.DEFAULT, SWT.DEFAULT );
    assertEquals( 100 + borderWidth, size.x );
    assertEquals( 50 + borderWidth, size.y );
  }

  private ImageLoader createAnimatedImage( String resourceName ) {
    ClassLoader classLoader = ImageLabel_Test.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream( "resources/" + resourceName );
    if( inputStream == null ) {
      throw new IllegalArgumentException( "Failed to load image: " + resourceName );
    }
    try {
      ImageLoader imageLoader = new ImageLoader();
      imageLoader.load( inputStream );
      return imageLoader;
    } finally {
      try {
        inputStream.close();
      } catch( IOException exception ) {
        throw new RuntimeException( exception );
      }
    }
  }

}
