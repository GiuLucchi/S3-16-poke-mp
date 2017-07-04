package app

import java.awt._
import java.util.Optional
import javax.swing._
import javax.imageio.ImageIO
import model.entities.{Owner, PokemonFactory, PokemonWithLife}
import utilities.Settings
import view.BattlePanel

class CreazionePokemon(myPoke: PokemonWithLife, otherPoke: PokemonWithLife) extends BattlePanel {
  val imagePokemonSize: Int = Settings.FRAME_SIDE / 4
  val fontSize: Int = (Settings.FRAME_SIDE * 0.034).toInt
  val fontLifeSize: Int = (Settings.FRAME_SIDE * 0.02).toInt
  val pokemonImgPose: Array[Dimension] = Array(new Dimension((Settings.FRAME_SIDE * 0.65).toInt,(Settings.FRAME_SIDE * 0.10).toInt),
                                          new Dimension((Settings.FRAME_SIDE * 0.11).toInt,(Settings.FRAME_SIDE * 0.48).toInt))
  val pokemonNamePose: Array[Dimension] = Array(new Dimension((Settings.FRAME_SIDE * 0.08).toInt,(Settings.FRAME_SIDE * 0.165).toInt),
                                            new Dimension((Settings.FRAME_SIDE * 0.57).toInt,(Settings.FRAME_SIDE * 0.56).toInt))
  val pokemonLevelPose: Array[Dimension] = Array(new Dimension((Settings.FRAME_SIDE * 0.06).toInt,(Settings.FRAME_SIDE * 0.205).toInt),
                                            new Dimension((Settings.FRAME_SIDE * 0.55).toInt,(Settings.FRAME_SIDE * 0.61).toInt))
  val pokemonProgressBarPose: Array[Dimension] = Array(new Dimension((Settings.FRAME_SIDE * 0.214).toInt,(Settings.FRAME_SIDE * 0.218).toInt),
                                            new Dimension((Settings.FRAME_SIDE * 0.7).toInt,(Settings.FRAME_SIDE * 0.612).toInt))
  val pokemonLifePose: Dimension = new Dimension((Settings.FRAME_SIDE * 0.845).toInt,(Settings.FRAME_SIDE * 0.625).toInt)
  val pokemonLevExpPose: Dimension = new Dimension((Settings.FRAME_SIDE * 0.64).toInt,(Settings.FRAME_SIDE * 0.666).toInt)
  var pokemonEntities: Array[PokemonWithLife] = Array(otherPoke,myPoke)
  var pokemonImages: Array[JButton] = Array(new JButton(),new JButton())
  var pokemonSide: Array[String] = Array(Settings.POKEMON_IMAGES_FRONT_FOLDER,Settings.POKEMON_IMAGES_BACK_FOLDER)
  var pokemonNames: Array[JTextField] = Array(new JTextField(),new JTextField())
  var pokemonLevels: Array[JTextField] = Array(new JTextField(),new JTextField())
  var pokemonProgressBar: Array[JProgressBar] = Array(new JProgressBar(0, 100),new JProgressBar(0, 100))
  var myPokemonLife: JTextField = new JTextField()
  val pokemonLevExpBar: JProgressBar = new JProgressBar(0, 100)

  this.setLayout(null)

  for(i <- pokemonImages.indices) {
    createJTextField(pokemonNames(i),pokemonEntities(i).pokemon.name.toUpperCase)
    pokemonNames(i).setBounds(pokemonNamePose(i).width,pokemonNamePose(i).height,200,30)
    pokemonNames(i).setFont(new Font("Verdana", Font.BOLD, fontSize))
    createJTextField(pokemonLevels(i),"L."+pokemonEntities(i).pokemon.level)
    pokemonLevels(i).setBounds(pokemonLevelPose(i).width,pokemonLevelPose(i).height,70,30)
    pokemonLevels(i).setFont(new Font("Verdana", Font.PLAIN, fontSize))

    var myImage: Image = ImageIO.read(getClass.getResource(pokemonSide(i) + pokemonEntities(i).pokemon.id + ".png"))
    var myImageIcon: ImageIcon = new ImageIcon(myImage.getScaledInstance(imagePokemonSize,imagePokemonSize,java.awt.Image.SCALE_SMOOTH))
    pokemonImages(i).setIcon(myImageIcon)
    pokemonImages(i).setBounds(pokemonImgPose(i).width,pokemonImgPose(i).height,imagePokemonSize,imagePokemonSize)
    pokemonImages(i).setBorder(null)
    pokemonImages(i).setContentAreaFilled(false)
    this.add(pokemonImages(i))

    pokemonProgressBar(i).setBounds(pokemonProgressBarPose(i).width,pokemonProgressBarPose(i).height,150,16)
    pokemonProgressBar(i).setValue(100)
    this.add(pokemonProgressBar(i))
  }
  createJTextField(myPokemonLife,pokemonEntities(1).pokemonLife+"/"+pokemonEntities(1).pokemon.experiencePoints)
  myPokemonLife.setBounds(pokemonLifePose.width,pokemonLifePose.height,100,30)
  myPokemonLife.setFont(new Font("Verdana", Font.PLAIN, fontLifeSize))
  this.add(myPokemonLife)
  pokemonLevExpBar.setValue(23)
  pokemonLevExpBar.setBounds(pokemonLevExpPose.width,pokemonLevExpPose.height,170,8)
  this.add(pokemonLevExpBar)

  private def createJTextField(jTextField: JTextField,text: String): Unit = {
    jTextField.setText(text)
    jTextField.setOpaque(false)
    jTextField.setBorder(null)
    this.add(jTextField)
  }

  def setPokemonLife(life: Int): Unit = {
    myPokemonLife.setText(pokemonEntities(1).pokemonLife+"/"+pokemonEntities(1).pokemon.experiencePoints)
  }

  def setPokemonLifeProgressBar(life: Int, owner: Owner.Value): Unit = {
    if (owner == Owner.TRAINER) pokemonProgressBar(1).setValue(life) else pokemonProgressBar(0).setValue(life)
  }

}

object CreazionePokemonMain extends App {
  val frame: JFrame = new JFrame()
  frame.setSize(Settings.FRAME_SIDE, Settings.FRAME_SIDE)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  var myPokemon: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(15)).get()
  val panel: CreazionePokemon = new CreazionePokemon(myPokemon,
    PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(15)).get())
  frame.add(panel)
  myPokemon.loseLifePoints(30)
  panel.setPokemonLife(myPokemon.pokemonLife)
  val lifeRatio: Double = myPokemon.pokemonLife.toDouble/myPokemon.pokemon.experiencePoints.toDouble
  panel.setPokemonLifeProgressBar((lifeRatio*100).toInt,Owner.TRAINER)
  frame.setVisible(true)
  frame.setResizable(false)
}