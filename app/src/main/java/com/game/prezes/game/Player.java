package com.game.prezes.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Prezes on 2015-11-27.
 */
public abstract class Player extends AnimatedSprite
{
    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    private Body body;


    private boolean canRun = false;

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

                odczytpozycjy();

                if (getY() <= 0) {
                    onDie();
                }
                if (x >0.5 || x <-0.5) {

                    if (footContacts < 1)
                    {
                        move(x);
                    }

                }
                jump();

            }

        });
    }

    private void odczytpozycjy() {

        try {
            FileInputStream fis = new FileInputStream( "data/data/com.game.prezes.game/files/pozycja.txt");

            StringBuilder builder = new StringBuilder();
            int ch;
            while((ch = fis.read()) != -1){
                builder.append((char)ch);
            }
            String a= String.valueOf(builder);
            try {
                x = Float.parseFloat(a);
                x*=2.5f;
            }
            catch(Exception e)
            {
                x=0;
            }


            fis.close();


            //Toast.makeText(getApplicationContext(), builder, Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            e.printStackTrace();
            // Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setRunning()
    {
        canRun = true;

        final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };

        animate(PLAYER_ANIMATE, 0, 2, true);
    }

    public void move(Float x)
    {

        body.setLinearVelocity(new Vector2(x, body.getLinearVelocity().y));
          }

    public void jump()
    {
        if (footContacts < 1)
        {
            return;
        }
        else
        {
        body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 45));
        }
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