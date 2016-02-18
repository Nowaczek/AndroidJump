package com.game.prezes.game;


import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GameScene extends BaseScene implements  IOnSceneTouchListener
{


    private HUD gameHUD;
    private Text scoreText,moneyText;
    private PhysicsWorld physicsWorld;


    private static final String TAG_ENTITY = "entity";
    private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
    private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
    private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE = "levelComplete";

    private Player player;


    private Text gameOverText,gameOverText1;
    private boolean gameOverDisplayed = false;

    public boolean end=false;


    @Override
    public void createScene()
    {
        createBackground();
        createHUD();
        createPhysics();
        loadLevel();
        createGameOverText();




        setOnSceneTouchListener(this);


    }



    @Override
    public void onBackKeyPressed()
    {
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneManager.SceneType getSceneType()
    {
        return SceneManager.SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
        camera.setHUD(null);
        camera.setChaseEntity(null); //TODO
        camera.setCenter(240, 400);


        // TODO code responsible for disposing scene
        // removing all game scene objects.
    }

    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
    {


        if (pSceneTouchEvent.isActionDown())
        {
            if(end==false) {


                if (player.canRun == false) {
                    player.jump();
                    player.canRun = true;
                } else {
                    if (ResourcesManager.getInstance().activity.money >= 1) {
                        ResourcesManager.getInstance().activity.money -= 1;
                        player.jumpextra();
                    }

                }
            }

        }
        return false;
    }

    private void loadLevel()
    {
        ResourcesManager.getInstance().activity.generatemap();



        final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);

        final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 0);

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL) {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException {
                final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
                final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

                camera.setBounds(0, 0, width, height); // here we set camera bounds
                camera.setBoundsEnabled(true);

                return GameScene.this;
            }
        });

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY) {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException {
                final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
                final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
                final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

                final Sprite levelObject;

                if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1)) {
                    levelObject = new Sprite(x, y, resourcesManager.platform1_region, vbom);
                    PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
                } else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2)) {
                    levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
                    final Body body =
                            PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("platform2");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                } else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3)) {
                    levelObject = new Sprite(x, y, resourcesManager.platform3_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("platform3");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                } else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN)) {
                    levelObject = new Sprite(x, y, resourcesManager.coin_region, vbom) {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed) {
                            super.onManagedUpdate(pSecondsElapsed);

                            if (player.collidesWith(this)) {
                                ResourcesManager.getInstance().activity.money+=1;

                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                } else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
                    player = new Player(x, y, vbom, camera, physicsWorld) {


                        @Override
                        public void onDie() {
                            end=true;




                            if (!gameOverDisplayed) {
                                player.setVisible(false);
                                player.setIgnoreUpdate(true);
                                player.delete();


                                if(ResourcesManager.getInstance().activity.fbchecklogin()==true)
                                {
                                    ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ResourcesManager.getInstance().activity.gameover();
                                        }
                                    });
                                }
                                else
                                {
                                    ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ResourcesManager.getInstance().activity.gameovernoconnect();
                                        }
                                    });
                                }




                                move(-1000f);
                                displayGameOverText();
                            }
                        }
                    };
                    levelObject = player;
                } else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE)) {
                    levelObject = new Sprite(x, y, resourcesManager.complete_stars_region, vbom) {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed) {
                            super.onManagedUpdate(pSecondsElapsed);





                            setResult();
                            if (player.collidesWith(this)) {

                                ResourcesManager.getInstance().activity.lastlevel+=ResourcesManager.getInstance().activity.score;
                                ResourcesManager.getInstance().activity.score=0;
                                /*
                               todo

                               */


                               // SceneManager.getInstance().loadGameScene(engine);
                                ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ResourcesManager.getInstance().activity.levelcomplete();
                                    }
                                });

                                SceneManager.getInstance().loadGameScene(engine);


                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1)));
                } else {
                    throw new IllegalArgumentException();
                }

                levelObject.setCullingEnabled(true);

                return levelObject;
            }
        });



        try {

            levelLoader.loadLevelFromStream(new FileInputStream("/data/data/com.game.prezes.game/files/map.lvl"));

        } catch (FileNotFoundException e) {
            Log.e("error", e.toString()) ;
            e.printStackTrace();
        }








    }

    private void createGameOverText()
    {
        gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
        gameOverText1= new Text(0, 0, resourcesManager.font, "Click 'back' to menu", vbom);
    }

    private void displayGameOverText()
    {


        camera.setChaseEntity(null);
        gameOverText.setPosition(camera.getCenterX(), camera.getCenterY() + 50);
        attachChild(gameOverText);
        gameOverText1.setPosition(camera.getCenterX(), camera.getCenterY() );
        attachChild(gameOverText1);
        gameOverDisplayed = true;

    }

    private void createHUD()
    {
        gameHUD = new HUD();

        scoreText = new Text(10, 740, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setAnchorCenter(0, 0);
        scoreText.setText("Score: 0");
        gameHUD.attachChild(scoreText);
        moneyText = new Text(10, 700, resourcesManager.font, "Money: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        moneyText.setAnchorCenter(0, 0);
        moneyText.setText("Money: 0");
        gameHUD.attachChild(moneyText);
        camera.setHUD(gameHUD);
    }

    private void createBackground()
    {
        setBackground(new Background(Color.BLUE));
        
    }


    private void setResult()
    {

        moneyText.setText("Money: " + ResourcesManager.getInstance().activity.money+" $$");
        Float temp=ResourcesManager.getInstance().activity.score+ResourcesManager.getInstance().activity.lastlevel;
        scoreText.setText("Score: " + temp+" ptk");
    }

    private void createPhysics()
    {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -80), false);
        physicsWorld.setContactListener(contactListener());
        registerUpdateHandler(physicsWorld);
    }

    // ---------------------------------------------
    // INTERNAL CLASSES
    // ---------------------------------------------

    private ContactListener contactListener()
    {
        ContactListener contactListener = new ContactListener()
        {
            public void beginContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
                {
                    if (x2.getBody().getUserData().equals("player"))
                    {
                        player.increaseFootContacts();
                    }

                    if (x1.getBody().getUserData().equals("platform2") && x2.getBody().getUserData().equals("player"))
                    {
                        engine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
                            public void onTimePassed(final TimerHandler pTimerHandler) {
                                pTimerHandler.reset();
                                engine.unregisterUpdateHandler(pTimerHandler);
                                x1.getBody().setType(BodyDef.BodyType.DynamicBody);
                                x1.setSensor(true);
                            }
                        }));
                    }

                    if (x1.getBody().getUserData().equals("platform3") && x2.getBody().getUserData().equals("player"))
                    {
                        if (player.iffalldown())
                        x1.getBody().setType(BodyDef.BodyType.DynamicBody);
                        x1.setSensor(true);
                    }

                }
            }

            public void endContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
                {
                    if (x2.getBody().getUserData().equals("player"))
                    {
                        player.decreaseFootContacts();
                    }
                }
            }

            public void preSolve(Contact contact, Manifold oldManifold)
            {

            }

            public void postSolve(Contact contact, ContactImpulse impulse)
            {

            }
        };
        return contactListener;
    }




}

