package com.game.prezes.game;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Prezes on 2015-11-27.
 */
public abstract class Player extends AnimatedSprite
{
    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    private Body body;


    public boolean canRun = false;

    private int footContacts = 0;
    public float x= 0f;




    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------

    public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
    {
        super(pX, pY, ResourcesManager.getInstance().player_region, vbo);
        createPhysics(camera, physicsWorld);
        camera.setChaseEntity(this);



    }



    // ---------------------------------------------
    // CLASS LOGIC
    // ---------------------------------------------

    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
    {
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

        body.setUserData("player");
        body.setFixedRotation(true);

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);


                if(Math.round(getY())-27>Math.round(ResourcesManager.getInstance().activity.score))
                {
                    ResourcesManager.getInstance().activity.score=Math.round(getY()-27);
                }

                odczytpozycjy();
                if (getY() <= 0) {

                    onDie();
                    for (int i = 0; i < getBody().getFixtureList().size(); i++) {
                        this.getBody().getFixtureList().get(i).setSensor(false);

                    }
                }
            if(canRun==true) {
                if (x > 0.5 || x < -0.5) {

                    if (footContacts < 1)
                    {
                        move(x);
                    }

                }
                //Log.d("Predkosc x", "Log Message" + body.getLinearVelocity().y);
                if (body.getLinearVelocity().y < 0) {
                    for (int i = 0; i < getBody().getFixtureList().size(); i++) {
                        this.getBody().getFixtureList().get(i).setSensor(false);

                    }
                    jump();
                } else {
                    for (int i = 0; i < getBody().getFixtureList().size(); i++) {
                        this.getBody().getFixtureList().get(i).setSensor(true);
                    }

                }
                if (body.getLinearVelocity().y < -70) {

                    for (int i = 0; i < getBody().getFixtureList().size(); i++) {
                        this.getBody().getFixtureList().get(i).setSensor(false);

                    }
                    onDie();


                }

            }



                        }

        });
    }

    private void odczytpozycjy() {


            try {
                x = ResourcesManager.getInstance().activity.pozycjax;
                x*=2.7f;
            }
            catch(Exception e)
            {
                x=0;
            }




    }
    public boolean iffalldown()
    {
        if (body.getLinearVelocity().y < 0) {
            return true;
        }
        else {
            return false;
        }

    }

    public void setRunning() {
        canRun = true;

        final long[] PLAYER_ANIMATE = new long[]{100, 100, 100};
    }


    public void move(Float x)
    {

        body.setLinearVelocity(new Vector2(x, body.getLinearVelocity().y));
    }



    public void jump()

        {

        if(footContacts<1)
        {
        return;
        }
        else
        {
        body.setLinearVelocity(new Vector2(body.getLinearVelocity().x,45));
            ResourcesManager.getInstance().jump.play();
        }
        }


    public void jumpextra()
    {
        ResourcesManager.getInstance().sjump.play();
            body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 80));


    }
    public void delete()
    {
this.dispose();
    }



    public void increaseFootContacts()
    {
        footContacts++;
    }

    public void decreaseFootContacts()
    {
        footContacts--;
    }

    public abstract void onDie();







}