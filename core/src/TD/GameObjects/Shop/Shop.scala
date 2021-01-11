package TD.GameObjects.Shop

import TD.GameObjects.Cursor
import TD.TowerDefense

import scala.collection.mutable

/** Shop helper class */

class Shop {
  /** Purchase a tower/item from the shop
   *
   * @return Bought item or None if player doesn't have enough gold
   */
  def purchase(game: TowerDefense, item: Purchaseable)  = {
    if (item.price <= game.gameState.playerMoney && !Cursor.hasItem) {
      game.gameState.playerMoney -= item.price
      Some(item)
    } else {
      None
    }
  }
}
