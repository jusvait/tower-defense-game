package TD.GameObjects.Shop

/**  Item
 *
 *  Helper class for item purchases
 *  @param price   Price of the item
 *  @param texture Texture of the item
 *
 *  */
class Item(price: Int, texture: String) extends Purchaseable(price, texture) {}

/** Upgrade
 *
 *
 *  Helper class for upgrade purchases
 * @param price   Price of the upgrade
 * @param texture Texture of the upgrade
 * */
class Upgrade(price:Int, texture: String) extends Purchaseable(price, texture){}