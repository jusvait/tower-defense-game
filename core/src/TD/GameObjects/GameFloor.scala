package TD.GameObjects


import com.badlogic.gdx.math.Vector2

/** Game floor
 *
 *  Helper class for game's floor
 *
 *  @param width  width of floor in tiles
 *  @param height height of floor in tiles
 *  @param floor  a string representation of the floor
 *  @param path   an array containing path targets for enemies
 * */
class GameFloor(val width: Int, val height: Int, floor: String, val path: Array[Vector2]) {
  val tiles = Array.ofDim[Tile](width, height)
  initTiles()

  private def initTiles() = {
    val floorTiles = floor.split(";").grouped(width).toArray.reverse

    for {
      y <- 0 until height
      x <- 0 until width
    } {
      val tile = new Tile(floorTiles(y)(x))
      tiles(x)(y) = tile
    }
  }
}
