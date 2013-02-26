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


public interface Painter {

  /**
   * Copy a piece of the virtual image to the painting area.
   *
   * @param gc the GC to use for painting
   * @param srcX the x position of the piece to copy from the virtual image
   * @param srcY the y position of the piece to copy from the virtual image
   * @param destX the x position of the canvas to paint the copied piece
   * @param destY the y position of the canvas to paint the copied piece
   * @param width the width of the piece to copy
   * @param height the height of the piece to copy
   */
  void paint( GC gc, int srcX, int srcY, int destX, int destY, int width, int height );

}
