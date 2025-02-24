package com.github.migi_1.Context.main;

import java.util.HashMap;

import com.github.migi_1.Context.score.ScoreController;
import com.github.migi_1.Context.utility.ProjectAssetManager;
import com.github.migi_1.ContextMessages.PlatformPosition;
import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;


/**
 * Controls the GUI elements of the LobbyEnvironment.
 */
public class LobbyHUDController {

	private static final String TITLEFONT_LOCATION = "Interface/Fonts/myfont.fnt";
	private static final String MENUFONT_LOCATION = "Interface/Fonts/myfont2.fnt";

    private BitmapText title, instruction;
    private HashMap<PlatformPosition, BitmapText> players = new HashMap<>(4);

    private BitmapFont menuFont;

    private Node guiNode;
    private AssetManager assetManager;
    private AppSettings settings;

    /**
     * Constructor. Generates initial score and displays it.
     * @param app Application
     */

    public LobbyHUDController(Application app) {
    	this.guiNode = ((Main) app).getGuiNode();
        assetManager = ProjectAssetManager.getInstance().getAssetManager();
        BitmapFont titleFont = assetManager.loadFont(TITLEFONT_LOCATION);
        menuFont = assetManager.loadFont(MENUFONT_LOCATION);
        title = new BitmapText(titleFont, false);
        title.setSize(titleFont.getCharSet().getRenderedSize());
        title.setColor(ColorRGBA.White);
        title.setText("CarriedfAway");
        settings = ((Main) app).getSettings();
        setTextPosition(title, 0.5f, 0.8f);
        guiNode.attachChild(title);
        addPlayers();
        addInstruction();
        addHighScore();
    }

    /**
     * Adds a text box for the HighScore.
     */
    private void addHighScore() {
    	ScoreController scoreController = new ScoreController();
    	int highScore = scoreController.getHighScore();
    	BitmapText highScoreText = new BitmapText(menuFont, true);
    	highScoreText.setSize(menuFont.getCharSet().getRenderedSize());
    	highScoreText.setColor(ColorRGBA.Green);
    	highScoreText.setText("Highscore\n\t " + highScore);
    	setTextPosition(highScoreText, 0.3f, 0.6f);
    	guiNode.attachChild(highScoreText);
    }

    /**
     * Adds the text fields for the players.
     */
    private void addPlayers() {
        float textPosition = 0.65f;
        final float distanceBetweenText = 0.05f;

        for (PlatformPosition position : PlatformPosition.values()) {
        	BitmapText player = new BitmapText(menuFont);
            player.setSize(menuFont.getCharSet().getRenderedSize());
            player.setColor(ColorRGBA.White);
            setTextPosition(player, 0.5f, textPosition);
            player.setText("|");

        	players.put(position, player);
        	guiNode.attachChild(player);
        	textPosition -= distanceBetweenText;
        }
    }

    /**
     * Adds the "press space to start" instruction.
     */
    private void addInstruction() {
        BitmapFont titleFont = assetManager.loadFont(TITLEFONT_LOCATION);
        instruction = new BitmapText(titleFont, false);
        instruction.setSize(titleFont.getCharSet().getRenderedSize() * 0.75f);
        instruction.setColor(ColorRGBA.White);
        instruction.setText("Pressfspaceftofstart");
        setTextPosition(instruction, 0.5f, 0.3f);
        guiNode.attachChild(instruction);
    }

    /**
     * Sets the text for a certain player.
     *
     * @param position
     * 		The position the player has assigned.
     * @param text
     * 		The text.
     */
    public void setPlayerText(PlatformPosition position, String text) {
    	players.get(position).setText(text);
    }

    /**
     * Sets the position of a text field.
     * @param text
     *              The text field.
     * @param widthFactor
     *              The width value at which the center of the text field
     *              will be placed.
     * @param heightFactor
     *              The height value at which the text field will be placed.
     */
    private void setTextPosition(BitmapText text, float widthFactor,
            float heightFactor) {
        float width = widthFactor * settings.getWidth() - 0.5f * text.getLineWidth();
        float height = heightFactor * settings.getHeight();
        text.setLocalTranslation(width, height, 0);
    }

    /**
     * Getter for the players.
     * Used in testing ONLY.
     * @return the players hashmap.
     */
    protected HashMap<PlatformPosition, BitmapText> getPlayers() {
        return players;
    }
}
