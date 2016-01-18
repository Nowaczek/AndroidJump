package com.game.prezes.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;


public class GameActivity extends BaseGameActivity implements IAccelerationListener {
    private BoundCamera camera;
    private ResourcesManager resourcesManager;
    public Float pozycjax=0f;
    public Integer iflogin=0;
    public Integer errorcode=0;


    //level varable
    public float score = 0;
    public float lastlevel = 0;




    ///////FB

    String name_profile;
    String id_profile;

    public ITextureRegion profilepicture;



public CallbackManager mCallbackManager;

    public EngineOptions onCreateEngineOptions()
    {
        camera = new BoundCamera(0, 0, 480, 800);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR, new RatioResolutionPolicy(480, 800), this.camera);
        engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return engineOptions;
    }



    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
    {
        ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
        resourcesManager = ResourcesManager.getInstance();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
        this.enableAccelerationSensor(this);
    }

    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
    {

        score = 0;
        Log.d("savefolder", getFilesDir().toString());
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();


        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);

    }

    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
    {
        mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMenuScene();
            }
        }));
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions)
    {
        return new LimitedFPSEngine(pEngineOptions, 60);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        System.exit(0);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false;
    }




    public boolean fbchecklogin()
    {
        if(AccessToken.getCurrentAccessToken() != null)
        {
            return true;
        }
        else
        {

            return false;
        }
    }

    public void fbloadprofile()
    {
        if(AccessToken.getCurrentAccessToken() != null){
            try {
                FileInputStream fis = new FileInputStream(getFilesDir()+"/profil.txt");
                Log.d("savefolder",getFilesDir().toString());
                StringBuilder builder = new StringBuilder();
                int ch;
                while((ch = fis.read()) != -1){
                    builder.append((char)ch);
                }
                String[] open= String.valueOf(builder).split(";");
                name_profile=open[1];
                id_profile=open[0];



                fis.close();

                Log.e("fbload profile",open[0]+"+"+open[1]);



            } catch (IOException e) {
                e.printStackTrace();
               Log.e("error fbload",e.toString());
            }


        }
    }
    public void fblogin()
    {
        errorcode=0;
        Log.d("FB", "klasa otwarta");


        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        RequestData();
                        errorcode = 1;

                    }

                    @Override
                    public void onCancel() {
                        Log.d("FB", "cancel");
                        errorcode = 2;
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("FB", "error");
                        errorcode = 2;
                    }

                });

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));


    }
    public static Bitmap getFacebookProfilePicture(String userID){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        try {
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");

            Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            return bitmap;
        } catch (Exception e) {
            Log.e("error download picture",e.toString());
        }

        return null;

    }
    public void fblogout()
    {
        LoginManager.getInstance().logOut();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mCallbackManager.onActivityResult(requestCode, resultCode, data)) {

            return;
        }
    }

    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if(json != null){

                        String id=(json.getString("id"));
                        String name = json.getString("name");
                        FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), "profil.txt"));
                        String saveprofile = id+";"+name;
                        fos.write(saveprofile.getBytes());
                        fos.close();
                        Log.d("saved", "profile saved");
                        Bitmap bitmap = getFacebookProfilePicture(json.getString("id"));
                        SaveImage(bitmap);



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        request.executeAsync();

    }

    private void SaveImage(Bitmap finalBitmap) {


        File myDir = new File(String.valueOf("data/data/com.game.prezes.game/files/"));

        myDir.mkdirs();

        String fname = "profile";
        File file = new File(myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
            out.flush();
            out.close();
            Log.i("img","saved");

        } catch (Exception e) {
           Log.e("image",e.toString());
        }
        //"data/data/com.example.hello.klik/files/pozycja.jpg"





    }


    public void hello()
    {

        fbloadprofile();
        try {
            final Dialog dialog = new Dialog(this);
            Log.i("dialog", "dialog was created");
            dialog.setContentView(R.layout.custom);

            dialog.setTitle("HI "+name_profile+"!!");

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("It's nice to see you!");
            ImageView image = (ImageView) dialog.findViewById(R.id.image);
            image.setImageURI(Uri.parse("data/data/com.game.prezes.game/files/profile"));

            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            Log.i("dialog", "show");


        }
            catch(Exception e)
            {
                Log.e("error", e.toString());
            }


    }

    public void alertdialog() {

        final Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Error");

        // set dialog message
        alertDialogBuilder
                .setMessage("testdialog")
                .setCancelable(false)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "kliknales tak", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "kliknales tak", Toast.LENGTH_SHORT).show();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }
    public void generatemap()
    {
        try {
            FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), "map.lvl"));
            //string okresjalacy mape platform
            String levelmap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "\t<level width=\"480\" height=\"20000\">\n"+
                    "\t<entity x=\"60\" y=\"16\" type=\"platform1\"/>\n";//dodanie wstepu pliku w formacie XML podanie rozmiaru mapy i pierwszej platformy na ktorej bedzie stal playter
            //string okreslajacy mape monet
            String coinmap="";

            Random rand = new Random();

            Integer counterplatform;        //liczba platform na danym poziomie
            Integer platform;               //liczba okreslajaca typ platformy
            Integer x=0;                    //pozycja x
            Integer lastx=40;               //pozycja ostatniej platformy w osi x
            Integer y=40;                   //pozycja w osi y

            Integer y_temp;                 //zmienna ktora poprawia pozycje w osi y
            Integer x_temp;                 //zmienna ktora poprawia pozycje w osi x
            Integer coin;                   //zmienna okresjalaca stworzenie monety

            for(int i =0;i<10;i++)                                                                                       //petla odpowiedzialna za liczbe "pieter"
            {
                y+=rand.nextInt((270 - 139) + 1) + 139;                                                                  //kolejna wysokosc "pietra" z przedzialu 140 - 270
                counterplatform =rand.nextInt((2 - 1) + 1) + 1;                                                          //ilosc platform na danym poziomie
                for(int j =0;j<counterplatform;j++)                                                                      //petla odpowiedzialna za liczbe platform na danym poziomie
                {
                    if(j==0)                                                                                             //pierwsza platforma na nowym poziomie
                    {
                        do {
                            x = rand.nextInt((450 - 39) + 1) + 41;                                                        // losowanie pozycji x danej platformy
                        }
                        while ((x<lastx-100)&&(x>lastx+100));                                                             // waronek sprawdzajacy aby ta platforma nie byla zbyt daleko ostatniej platformy z poprzedniego poziomu
                        lastx=x;
                        platform =rand.nextInt((3 - 1) + 1) + 1;                                                          // rodzaj platformy 1.stala 2.spadajaca po czasie 3. spadajaca od razu
                        coin= rand.nextInt((100 - 1) + 1) + 1;                                                            // losowanie prawdopodobienstwa wystapienia monety

                        if(platform==1)                                                                                    //jezeli platforma stala
                        {
                            if(coin>0)                                                                                     //ustalanie ptawdopodobienstwa wystapienia monety
                            {
                                levelmap+="\t<entity x=\""+x+"\" y=\""+y+"\" type=\"platform"+platform+"\"/>\n";           //dodanie do stringu mapy kojna platforme
                                y_temp=y+50;                                                                                //temp ustala poprawiona pozycje
                                coinmap+= "\t<entity x=\""+x+"\" y=\""+y_temp+"\" type=\"coin\"/>\n" ;                      //dodanie do stringu monet pozycje danej monety
                            }
                            else
                            {
                                levelmap+="\t<entity x=\""+x+"\" y=\""+y+"\" type=\"platform"+platform+"\"/>\n";            //bez monety jest dodawana tylko platforma

                            }
                        }
                        else                                                                                                //to samo co powyzej, lecz tutaj sa tworzone platformy rodzaju 2 i 3 ktore wymagaja poprawionej pozycji, inaczej nakladaly by sie
                        {
                            if(coin>0)                                                                                      //platforma z moneta
                            {
                                x_temp=x+52;
                                y_temp=y+11;
                                levelmap+="\t<entity x=\""+x_temp+"\" y=\""+y_temp+"\" type=\"platform"+platform+"\"/>\n";

                                y_temp=y+50;
                                coinmap+= "\t<entity x=\""+x+"\" y=\""+y_temp+"\" type=\"coin\"/>\n" ;
                            }
                            else                                                                                            //bez monety
                            {
                                x_temp=x+52;
                                y_temp=y+11;
                                levelmap+="\t<entity x=\""+x_temp+"\" y=\""+y_temp+"\" type=\"platform"+platform+"\"/>\n";

                            }

                        }
                    }
                    else                                                                                                    //tutaj jest to samo co dla loowania pierwszej platformy w danym "poziomie" tyle, ze pozycja x...
                    {
                        do {
                            x = rand.nextInt((450 - 39) + 1) + 41;
                        }
                        while ((x<(lastx+150))&&(x>(lastx-150)));                                                          //...jest losowana tak aby platformy nie nakladaly sie
                        lastx=x;
                        platform =rand.nextInt((3 - 1) + 1) + 1;
                        coin= rand.nextInt((100 - 1) + 1) + 1;

                        if(platform==1)
                        {
                            if(coin>0)
                            {
                                levelmap+="\t<entity x=\""+x+"\" y=\""+y+"\" type=\"platform"+platform+"\"/>\n";
                                y_temp=y+50;
                                coinmap+= "\t<entity x=\""+x+"\" y=\""+y_temp+"\" type=\"coin\"/>\n" ;
                            }
                            else
                            {
                                levelmap+="\t<entity x=\""+x+"\" y=\""+y+"\" type=\"platform"+platform+"\"/>\n";

                            }
                        }
                        else
                        {
                            if(coin>0)
                            {
                                x_temp=x+52;
                                y_temp=y+11;
                                levelmap+="\t<entity x=\""+x_temp+"\" y=\""+y_temp+"\" type=\"platform"+platform+"\"/>\n";

                                y_temp=y+50;
                                coinmap+= "\t<entity x=\""+x+"\" y=\""+y_temp+"\" type=\"coin\"/>\n" ;
                            }
                            else
                            {
                                x_temp=x+52;
                                y_temp=y+11;
                                levelmap+="\t<entity x=\""+x_temp+"\" y=\""+y_temp+"\" type=\"platform"+platform+"\"/>\n";

                            }

                        }
                    }











                }

            }
            y+=150;//y jest zwiekszony po to aby gwiazda zakonczenia byla troche wyzej niz ostatni "poziom"
            levelmap+=coinmap+                                                                  // kolejnosc jest scisle ustawiona najpierw platformy, potem monety
                                "\t<entity x=\"240\" y=\""+y+"\" type=\"levelComplete\"/>\n" +
                                "\t<entity x=\"60\" y=\"30\" type=\"player\"/>\n" +
                                "</level>";                                                     //dodanie zakonczenia czyli gwiazdy zakonczenia i pozycja playera


            fos.write(levelmap.getBytes());
            fos.close();
            Log.d("saved", "profile saved");
        }
        catch(Exception e)
        {

        }

    }
    public void show(String text)
    {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {

    }

    @Override
    public void onAccelerationChanged(AccelerationData pAccelerationData) {
       pozycjax  = pAccelerationData.getValues()[0];




    }

}
