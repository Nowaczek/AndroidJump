package com.game.prezes.game;

import android.graphics.Color;
import android.util.Log;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import java.io.IOException;


public class ResourcesManager
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------

    private static final ResourcesManager INSTANCE = new ResourcesManager();

    public Engine engine;
    public GameActivity activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;

    public Font font;

    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------

    public ITextureRegion splash_region;
    public ITextureRegion menu_background_region;
    public ITextureRegion play_region;
    public ITextureRegion login_region;
    public ITextureRegion logout_region;
    public ITextureRegion howto_region;


    // Game Texture
    public BuildableBitmapTextureAtlas gameTextureAtlas;

    // Game Texture Regions
    public ITextureRegion platform1_region;
    public ITextureRegion platform2_region;
    public ITextureRegion platform3_region;
    public ITextureRegion coin_region;
    public ITiledTextureRegion player_region;
    public ITextureRegion game_background_region;
    private BitmapTextureAtlas splashTextureAtlas;
    private BuildableBitmapTextureAtlas menuTextureAtlas;

    // Level Complete Window

    public ITiledTextureRegion complete_stars_region;


    // Music game

    public Music jump,coin,win,lose,sjump,gamemusic;

    //Music menu

    public Music menumusic;

    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();

    }

    public void loadGameResources()
    {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();

    }


    private void loadMenuGraphics()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
        play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
        login_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "fblogin.png");
        logout_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "fblogout.png");
        howto_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "howtoplay.png");
        try
        {
            this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.menuTextureAtlas.load();
        }
        catch (final ITextureAtlasBuilder.TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }

    private void loadMenuAudio()
    {

        try {
            menumusic = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "music/menumusic.wav");
            menumusic.setVolume(0.2f);
            menumusic.setLooping(true);

        } catch (IOException e) {
            Log.e("error sound",e.toString());
        }
    }

    private void loadMenuFonts()
    {
        FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
        font.load();
    }

    private void loadGameGraphics()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        //game_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
        platform1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform1.png");
        platform2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform2.png");
        platform3_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform3.png");
        coin_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin.png");
        player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 2, 1);


        complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "star.png", 2, 1);

        try
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
        }
        catch (final ITextureAtlasBuilder.TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }

    private void loadGameFonts()
    {

    }

    private void loadGameAudio()
    {
        try {
            jump = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "music/jump.wav");
            jump.setVolume(0.5f);
            coin = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "music/coin.wav");
            coin.setVolume(0.5f);
            win = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "music/win.wav");
            win.setVolume(0.5f);
            lose = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "music/lose.wav");
            lose.setVolume(0.5f);
            sjump = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "music/sjump.wav");
            sjump.setVolume(0.5f);
            gamemusic = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "music/gamemusic.wav");
            gamemusic.setVolume(0.5f);
            gamemusic.setLooping(true);
        } catch (IOException e) {
            Log.e("error sound",e.toString());
        }
    }

    public void unloadGameTextures()
    {
        // TODO (Since we did not create any textures for game scene yet)
    }

    public void loadSplashScreen()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
        splashTextureAtlas.load();
    }

    public void unloadSplashScreen()
    {
        splashTextureAtlas.unload();
        splash_region = null;
    }

    public void unloadMenuTextures()
    {
        menuTextureAtlas.unload();
    }

    public void loadMenuTextures()
    {
        menuTextureAtlas.load();
    }

    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }

    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------

    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
}