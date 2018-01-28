package honey.popcon.mykotlin

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.QUEUE_ADD
import android.support.annotation.NonNull
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(){
    //declare the possible moves in array so we can use later
    var moves = arrayOf("stone","paper","scissor")
   lateinit var cpuScoreView:TextView
   //
   lateinit var  playerScoreView:TextView
   lateinit var  cpuResponseView:TextView
   lateinit var playerResponseView:TextView
    //animation are optional or replace this with you own animation
   lateinit var zoomOutAnim:Animation
   lateinit var blinkAnim:Animation
   lateinit var zoomInAnim:Animation
   lateinit var winAudio: MediaPlayer
   lateinit var loseAudio:MediaPlayer
   lateinit var tts:TextToSpeech


    var cpuScore:Int=0
    var playerScore:Int=0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cpuScoreView=findViewById<TextView>(R.id.cpu_score) as TextView
        playerScoreView=findViewById<TextView>(R.id.player_score) as TextView
        cpuResponseView=findViewById<TextView>(R.id.cpu_response) as TextView
        playerResponseView=findViewById<TextView>(R.id.player_response) as TextView
        zoomOutAnim=AnimationUtils.loadAnimation(this,R.anim.zoomout)
        zoomInAnim=AnimationUtils.loadAnimation(this,R.anim.zoomin)
        blinkAnim=AnimationUtils.loadAnimation(this,R.anim.abc_slide_out_top)
        winAudio= MediaPlayer.create(this,R.raw.win)
        loseAudio= MediaPlayer.create(this,R.raw.lose)
       //implenting tts is optional
        tts= TextToSpeech(this, TextToSpeech.OnInitListener {
        tts.setLanguage(Locale.US)

        });

//When user select stonebutton
        val stoneButton=findViewById<ImageButton>(R.id.stone_button)
         stoneButton.setOnClickListener({
             var cpuResponse=getCpuResponse()
             var playerResponse=moves[0]
             speakResponse(cpuResponse,playerResponse)
            displayResult(cpuResponse,playerResponse)


         })
//when user press paper button
        val paperButton=findViewById<ImageButton>(R.id.paper_button)
        paperButton.setOnClickListener({
            var cpuResponse=getCpuResponse()
            var playerResponse=moves[1]
            speakResponse(cpuResponse,playerResponse)
            displayResult(cpuResponse,playerResponse)
        })
//when user select scissor button
        val scissorButton=findViewById<ImageButton>(R.id.scissor_button)
        scissorButton.setOnClickListener({
            var cpuResponse=getCpuResponse()
            var playerResponse=moves[2]
            speakResponse(cpuResponse,playerResponse)
           displayResult(cpuResponse,playerResponse)
        })
//reload score or restart the game button
       val resetButton=findViewById<ImageButton>(R.id.reload_button)
        resetButton.setOnClickListener({
            cpuScore=0;
            playerScore=0;
            updateUi()
            cpuScoreView.startAnimation(blinkAnim)
            playerScoreView.startAnimation(blinkAnim)

            cpuResponseView.setText("CPU RESPONSE")
            cpuResponseView.startAnimation(zoomInAnim)
            playerResponseView.setText("YOUR RESPONSE")
            playerResponseView.startAnimation(zoomInAnim)
        })

    }
//create random cpu response
    fun getCpuResponse():String{

        val random = Random()
        var pos=random.nextInt(3)
        return moves[pos]

    }
    //change UI when user made choice
 fun displayResult(cpuRes: String,playerRes: String){

     cpuResponseView.setText(cpuRes.capitalize())
     cpuResponseView.startAnimation(zoomOutAnim)
     playerResponseView.setText(playerRes.capitalize())
     playerResponseView.startAnimation(zoomOutAnim)
     matchResult(cpuRes,playerRes)


 }

   //function to speak the choices made by player and cpu

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun speakResponse(cpuRes: String, playerRes: String){
        tts.speak(playerRes, TextToSpeech.QUEUE_ADD, null)
        tts.playSilentUtterance(200,QUEUE_ADD,null)
        tts.speak(cpuRes, TextToSpeech.QUEUE_ADD, null)

    }


    //logic to find who wins player or cpu
    fun matchResult(cpuRes:String,playerRes:String){
        if(cpuRes.equals(playerRes)){

        }
        else if (cpuRes.equals(moves[0])){
            if (playerRes.equals(moves[1])){
                win()

            }
            else if (playerRes.equals(moves[2])){
                lose()
            }
        }

        else if (cpuRes.equals(moves[1])){
            if (playerRes.equals(moves[2])){
               win()

            }
            else if (playerRes.equals(moves[0])){
                lose()
            }
        }

        else if (cpuRes.equals(moves[2])){
            if (playerRes.equals(moves[1])){
                lose()
            }
            else if (playerRes.equals(moves[0])){
               win()
            }
        }

    }

    //if user wins upgrade score
    fun win(){
        playerScore++
        playerScoreView.startAnimation(blinkAnim)
       /*
        try {
            if (mp.isPlaying){
                mp.stop()
                mp.reset()
                mp.release()
                mp=MediaPlayer.create(this,R.raw.win)
            }
            mp.start()
        }
        catch (e:Exception){
            Log.e("Some error"," catch sayst "+e.stackTrace)

        }
        */
        winAudio.start()
        updateUi()
    }
//if cpu wins upgrade cpuscore
    fun lose(){
        cpuScore++
        cpuScoreView.startAnimation(blinkAnim)
        loseAudio.start()
        updateUi()
    }

    //update ui
    private fun updateUi() {
      cpuScoreView.setText("CPU "+cpuScore)
      playerScoreView.setText("SCORE "+playerScore)
    }

}
