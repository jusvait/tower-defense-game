package TD.GameObjects.Shop

/** Purchaseable
 *
 *  A template for purchaseable game objects
 *  @param price    price of the purchase
 *  @param texture  texture of the purchase
 * */
abstract class Purchaseable(val price: Int, val texture: String) {}

/** Tower
 *
 *  Helper class for selling towers
 * */
abstract class Tower(price: Int, texture: String) extends Purchaseable(price, texture) {
  val towerType: String
}
case class FireTower() extends Tower(20, "Icons/fire_tower.png") {
  val towerType = "fire"
}
case class IceTower() extends Tower(30, "Icons/ice_tower.png") {
  val towerType = "ice"
}
case class PoisonTower() extends Tower(30, "Icons/poison_tower.png") {
  val towerType = "poison"
}
case class BoomTower() extends Tower(40, "Icons/boom_tower.png") {
  val towerType = "boom"
}
