package TD.GameObjects

import com.badlogic.gdx.Gdx

/** Game state
 *
 * Helper class to describe the game's state
 *
 * @param playerHP Player's current health
 * @param playerMoney Player's current money
 * @param moneyGain How much money is gained every second
 * @param killReward How much the player is rewarded for a single enemy killed
 * @param timePlayed How long the player has been playing a level
 * @param toughTime Time when enemies are being spawned twice as fast
 * */
class GameState(var playerHP: Float, var playerMoney: Float, var moneyGain: Float, var killReward: Float, var timePlayed: Float, val toughTime: Float) {

  def update() = {
    this.timePlayed += Gdx.graphics.getDeltaTime
    this.playerMoney += Gdx.graphics.getDeltaTime * moneyGain
  }

  def alive = this.playerHP > 0

}
