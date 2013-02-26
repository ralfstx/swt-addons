package ralfstx.swt.custom.imagelabel;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;


public class ImageAnimator_Test {

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

}
