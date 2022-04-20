//從其他Activivty得到值 新版Main傳到Second  舊版Second傳到Main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.activityandfragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.button.setOnClickListener {
            val intent = SecondActivity.createIntent(this, "I am from MainActivity")
            startActivityForResult(intent , REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    val information = data?.getStringExtra(EXTRA_INFORMATION).orEmpty()
                    Toast.makeText(this,"I receive $information",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 100
        private const val EXTRA_INFORMATION = "information"
        fun createResultIntent(information : String):Intent{
            return Intent().putExtra(EXTRA_INFORMATION,information)
        }
    }
}



//____________________________________________________________________________________________________
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.activityandfragment.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //從MainActivity傳入值
        binding.textView2.text = intent?.getStringExtra(EXTRA_TITLE).orEmpty()

        binding.button2.setOnClickListener {
            setResult(Activity.RESULT_OK,MainActivity.createResultIntent("back from Second."))
            //返回
            onBackPressed()
        }
    }

    companion object {
        private const val EXTRA_TITLE = "title"

        fun createIntent(context: Context, title: String): Intent {
            return Intent(context, SecondActivity::class.java)
                .putExtra(EXTRA_TITLE, title)
        }
    }
}
