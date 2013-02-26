/*******************************************************************************
 * Copyright (c) 2012 Ralf Sternberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial implementation and API
 ******************************************************************************/
package ralfstx.swt.custom.virtualcanvas;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;


public class ImagePainter implements Painter {

  private final Image image;
  private final Rectangle imageBounds;

  public ImagePainter( Image image ) {
    this.image = image;
    imageBounds = image.getBounds();
  }

  @Override
  public void paint( GC gc, int srcX, int srcY, int dstX, int dstY, int width, int height ) {
    if( srcX < imageBounds.width && srcY < imageBounds.height ) {
      int copyWidth = Math.min( width, imageBounds.width - srcX );
      int copyHeight = Math.min( height, imageBounds.height - srcY );
      gc.drawImage( image, srcX, srcY, copyWidth, copyHeight, dstX, dstY, copyWidth, copyHeight );
    }
  }

}
