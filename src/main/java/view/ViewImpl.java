package view;
import controller.BattleController;
import controller.Controller;
import controller.GameController;
import controller.GameViewObserver;
import database.remote.DBConnect;
import model.entities.PokemonWithLife;
import model.entities.Trainer;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

public class ViewImpl extends JFrame implements View {

    private static final String WINDOW_TITLE = "Pokemon MP";
    private Controller controller;
    private BuildingPanel buildingPanel;
    private Dimension frameDim;
    private BattleView battlePanel;

    public ViewImpl(Controller controller) {
        this.controller = controller;
        this.setTitle(WINDOW_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frameDim = new Dimension(Settings.FRAME_SIDE(), Settings.FRAME_SIDE());
        this.setSize(frameDim);
        this.setMinimumSize(frameDim);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.getContentPane().add(new MenuPanel(this, this.controller));
        this.validate();
        this.setVisible(true);
    }

    private void setPanel(final JPanel panel) {
        this.getContentPane().removeAll();
        this.getContentPane().add(panel);
        panel.setBackground(Color.black);
        this.revalidate();
        this.repaint();
    }

    private void setDialogue(final JPanel panel){
        final DialoguePanel dialoguePanel = (DialoguePanel) panel;
        this.getContentPane().add(dialoguePanel, BorderLayout.SOUTH);
        dialoguePanel.setPreferredSize(new Dimension(0, Settings.SCREEN_WIDTH()/12));
        this.revalidate();
        this.repaint();
    }

    private void setGameMenuPanel(JPanel gameMenuPanel) {
        this.getContentPane().add(gameMenuPanel, BorderLayout.EAST);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void setController(Controller controller) { this.controller = controller; }
    

    @Override
    public void showMenu() {
        this.setPanel(new MenuPanel(this, this.controller));
    }

    @Override
    public void showLogin() { this.setPanel(new LoginPanel(this, this.controller)); }

    @Override
    public void showSignIn() { this.setPanel(new SignInPanel(this, this.controller)); }

    @Override
    public void showPanel(JPanel gamePanel) {
        this.setPanel(gamePanel);
    }

    @Override
    public void showBattle (PokemonWithLife myPokemon, PokemonWithLife otherPokemon, BattleController battleController) {
        this.battlePanel = new BattlePanel(myPokemon,otherPokemon,this,battleController);
        BattlePanel panel = (BattlePanel) this.battlePanel;
        this.setPanel(panel);
    }

    @Override
    public  BattleView getBattlePanel(){
        return this.battlePanel;
    }

    @Override
    public void showPokemonChoice(BattleController battleController, Trainer trainer) { this.setPanel(new PokemonChoicePanel(battleController, trainer)); }

    @Override
    public void showDialogue(JPanel dialoguePanel){
        this.setDialogue(dialoguePanel);
    }

    @Override
    public void showPokedex(Trainer trainer, GameViewObserver gameController) {
        this.setPanel(new PokedexPanel(trainer, gameController));
    }

    @Override
    public void showTeamPanel(Trainer trainer, GameViewObserver gameController) {
        this.setPanel(new TeamPanel(trainer, gameController));
    }

    @Override
    public void showKeyboardPanel(GameViewObserver gameController) {
        this.setPanel(new KeyboardPanel(gameController));
    }

    @Override
    public void showGameMenuPanel(GameViewObserver controller) {
        this.setGameMenuPanel(new GameMenuPanel(controller));
    }

    @Override
    public void showPause() {

    }

    @Override
    public void showError(final String error, final String title) {
        JOptionPane.showMessageDialog(this, error, title, JOptionPane.ERROR_MESSAGE);
    }

}
