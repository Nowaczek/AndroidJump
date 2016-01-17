package com.game.prezes.game;

import android.os.AsyncTask;
import android.util.Log;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

/**
 * Created by Prezes on 2015-11-16.
 */
public class MainMenuScene extends BaseScene implements MenuScene.IOnMenuItemClickListener
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------

    private MenuScene menuChildScene;


    private final int MENU_PLAY = 0;
    private final int MENU_LOGIN = 1;

    //-----------------
    //      0-pierwsze
    //      1-pokaz ze zalogowany
    //      2-pokaz ze nie zalogowany
    //---------------------------------------------
    // METHODS FROM SUPERCLASS
    //---------------------------------------------

    @Override
    public void createScene()
    {

        createBackground();


            if(ResourcesManager.getInstance().activity.fbchecklogin()==true)
            {
                ResourcesManager.getInstance().activity.iflogin=1;

            }
            else
            {
                ResourcesManager.getInstance().activity.iflogin=2;

            }

        if(ResourcesManager.getInstance().activity.iflogin==1)
        {
            createMenuChildScene(1);


            ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ResourcesManager.getInstance().activity.hello();
                }
            });


        }
        if(ResourcesManager.getInstance().activity.iflogin==2)
        {
            createMenuChildScene(0);
        }


    }

    @Override
    public void onBackKeyPressed()
    {
        System.exit(0);
    }

    @Override
    public SceneManager.SceneType getSceneType()
    {
        return SceneManager.SceneType.SCENE_MENU;
    }


    @Override
    public void disposeScene()
    {
        // TODO Auto-generated method stub
    }


    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
    {
        switch(pMenuItem.getID())
        {
            case MENU_PLAY:
                //Load Game Scene!
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            case MENU_LOGIN:
                if(ResourcesManager.getInstance().activity.iflogin==1)
                {

                    ResourcesManager.getInstance().activity.fblogout();
                    createScene();
                }
                else
                {



                    ResourcesManager.getInstance().activity.fblogin();
                    new waiting().execute();









                }



                return true;
            default:
                return false;
        }
    }

    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    private void createBackground()
    {
        attachChild(new Sprite(240, 400, resourcesManager.menu_background_region, vbom)
        {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera)
            {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        });
    }

    private void createMenuChildScene(Integer iflogin)
    {




        menuChildScene = new MenuScene(camera);
        menuChildScene.setPosition(0, 0);

        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
        final IMenuItem loginMenuItem;


        if(iflogin==1)
        {

             loginMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LOGIN, resourcesManager.logout_region, vbom), 1.2f, 1);
        }
        else
        {
             loginMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LOGIN, resourcesManager.login_region, vbom), 1.2f, 1);
        }
        menuChildScene.addMenuItem(playMenuItem);
        menuChildScene.addMenuItem(loginMenuItem);

        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() - 100);
        loginMenuItem.setPosition(loginMenuItem.getX(), loginMenuItem.getY() - 110);

        menuChildScene.setOnMenuItemClickListener(this);

        setChildScene(menuChildScene);



    }
    private class waiting extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            do {
                Log.i("waitin", "forerrorcode");


            }
            while (ResourcesManager.getInstance().activity.errorcode == 0) ;
            return "1";
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            if (ResourcesManager.getInstance().activity.errorcode == 1) {
                createScene();
            } else {

            }



        }
    }

}
