package TD.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{BitmapFont, TextureRegion}
import com.badlogic.gdx.graphics.{Color,Texture}
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle
import com.badlogic.gdx.scenes.scene2d.ui.{ Slider }
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

/** UITools
 *
 *  A helper object to create buttons & sliders more easily
 *  Currently only has functionality to create sliders
 * */
object UITools {
  var buttonFont = new BitmapFont()

  /** Volume slider */
  def createSlider(upTex: String, downTex: String,
                   beforeTex: String, afterTex: String,
                   width: Int, height: Int): Slider = {
    var backUp    = new Texture(Gdx.files.internal("Textures/button_1.png"))
    var backDown  = new Texture(Gdx.files.internal("Textures/button_1_down.png"))
    var backButtonStyle = new TextButtonStyle()
    var backUpRegion    = new TextureRegion(backUp,128, 64)
    var backDownRegion  = new TextureRegion(backDown, 128, 64)

    var sliderStyle       = new SliderStyle()
    var sliderKnob        = new Texture(Gdx.files.internal("Textures/knob.png"))
    var sliderKnobDown    = new Texture(Gdx.files.internal("Textures/knob_down.png"))
    var sliderBG          = new Texture(Gdx.files.internal("Textures/slider_knob_after.png"))
    var sliderKnobBefore  = new Texture(Gdx.files.internal("Textures/slider_knob_before.png"))

    var sliderKnobRegion        = new TextureRegion(sliderKnob, 32,32)
    var sliderKnobDownRegion    = new TextureRegion(sliderKnobDown, 32,32)
    var sliderBGRegion          = new TextureRegion(sliderBG, 128, 32)
    var sliderKnobBeforeRegion  = new TextureRegion(sliderKnobBefore, 128, 32)

    sliderStyle.knob        = new TextureRegionDrawable(sliderKnobRegion)
    sliderStyle.knobDown    = new TextureRegionDrawable(sliderKnobDownRegion)
    sliderStyle.background  = new TextureRegionDrawable(sliderBGRegion)
    sliderStyle.knobBefore  = new TextureRegionDrawable(sliderKnobBeforeRegion)

    /** Volume slider label */
    var labelStyle = new LabelStyle()
    labelStyle.background = new TextureRegionDrawable(backUpRegion)
    labelStyle.font = buttonFont
    labelStyle.fontColor = Color.WHITE

    var slider = new Slider(0f, 1f, 0.01f, false, sliderStyle)
    slider
  }


}
