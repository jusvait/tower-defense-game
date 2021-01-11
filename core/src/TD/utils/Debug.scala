package TD.utils

import TD.TowerDefense
import com.badlogic.gdx.Gdx

/** Debug
 *
 * small debug object with a few methods for printing information to console
 * */

object Debug {
  def printEntityCount(game: TowerDefense) = {
      println(s"Existing entities: ${game.ecs.pool.entities.size}")
  }

  def printFPS(game: TowerDefense) = {
    println(s"Frames: ${Gdx.graphics.getFramesPerSecond}")
  }



}
