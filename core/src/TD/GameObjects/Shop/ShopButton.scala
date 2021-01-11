package TD.GameObjects.Shop

import TD.GameObjects.{Cursor, GameState}
import TD.TowerDefense
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.{Event, EventListener, InputEvent, InputListener}
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable


/** Shop button
 *
 * Helper class to create shop HUD
 *
 *  @param selling what item this button is selling
 *  @param shop   which shop this button belongs to
 *  @param game   which game this button is bound to
 *  @param tex    this buttons texture
 * */
class ShopButton(val selling: Purchaseable, val shop: Shop, val game: TowerDefense, tex: String) extends Button {

  this.init()

  this.addListener(new InputListener() {

    override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int ): Boolean = {
      val purchase = shop.purchase(game, selling)

      if (purchase.isDefined) {
        purchase.get match {
          case item: Item => Cursor.placeItem(item)
          case tower: Tower => Cursor.placeItem(tower)
          case _ => {}
        }
      }
      true
    }
  })

  /** Initialize button */
  private def init() = {
    val shopButtonStyle = new ButtonStyle()

    val shopButtonTex = new Texture(tex)
    val shopButtonRegion = new TextureRegion(shopButtonTex, 16, 16)
    val shopButtonDrawable = new TextureRegionDrawable(shopButtonRegion)

    shopButtonStyle.up = shopButtonDrawable
    shopButtonStyle.down = shopButtonDrawable

    this.setStyle(shopButtonStyle)
  }
}
