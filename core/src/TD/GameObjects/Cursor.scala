package TD.GameObjects

import TD.CONSTANTS
import TD.GameObjects.Shop.Purchaseable
import com.badlogic.gdx.Gdx.input


/** Cursor
 *
 *  Helper object to track player mouse/touch
 * */
object Cursor {
  /** The cursor has an inventory, if something is bought from the shop it is placed here until put on the field */
  private var inventory: Option[Purchaseable] = None

  def getItem = inventory.get

  def hasItem = inventory.isDefined

  def placeItem(item: Purchaseable) = this.inventory = Some(item)

  def removeItem() = this.inventory = None

  /** x-coordinate of the cursor in tiles */
  def x = {
    var x = (input.getX / 2) / 32

    if (x > 14) {x = 14}
    x
  }

  /** y-coordinate of the cursor in tiles */
  def y = {
    var y = (((CONSTANTS.WINDOW_HEIGHT - input.getY) / 2) - CONSTANTS.HUD_HEIGHT) / 32

    if (y > 5) {y = 5}
    y
  }

  /** Tile coordinates the cursor is hovering over */
  def hoverTileCoordinates: Option[(Int, Int)] = {
    val mouseX = input.getX / 2
    val mouseY = (CONSTANTS.WINDOW_HEIGHT - input.getY) / 2

    if (
      mouseX >= CONSTANTS.WINDOW_WIDTH
        || mouseY >= CONSTANTS.WINDOW_HEIGHT
        || mouseX <= 0
        || mouseY <= CONSTANTS.HUD_HEIGHT
    ) { None } else {Some(Cursor.x, Cursor.y)}
  }

  /** Tile the cursor is hovering over */
  def hoverTile(floor: GameFloor): Option[Tile] = hoverTileCoordinates match {
    case Some((x, y)) => Some(floor.tiles(x)(y))
    case None => None
  }

}
