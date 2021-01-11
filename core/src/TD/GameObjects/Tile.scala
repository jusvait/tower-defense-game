package TD.GameObjects

/** Tile
 *
 *  A helper class to describe tiles
 * */
class Tile(tileName: String) {
  var tileType: String = tileName
  val size = 32

  def changeTileType(newType: String) = this.tileType = newType
}