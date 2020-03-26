package com.anupam.androidtflitemodelmaker

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.anupam.androidtflitemodelmaker.ml.NsfwClassifier
import kotlinx.android.synthetic.main.activity_main.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel


class MainActivity : AppCompatActivity() {

    val dataArray: ArrayList<DataModel> = ArrayList()
    val labelsList = arrayListOf("NSFW", "SFW")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnClassifier.setOnClickListener {
            val iterate = dataArray.listIterator()
            while (iterate.hasNext()) {
                val oldValue = iterate.next()
                runImageClassifier(oldValue)
            }

            recyclerView.adapter?.notifyDataSetChanged()

        }

        populateData()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerViewAdapter(dataArray, this)

    }

    fun populateData()
    {
        dataArray.add(DataModel(R.drawable.sfw_1))
        dataArray.add(DataModel(R.drawable.nsfw))
        dataArray.add(DataModel(R.drawable.nsfw2))
        dataArray.add(DataModel(R.drawable.sfw))
        dataArray.add(DataModel(R.drawable.scarlet))
    }


    fun runImageClassifier(data: DataModel)
    {

        val bitmap =
            BitmapFactory.decodeResource(applicationContext.resources, data.drawableID)

        try {

            val probabilityProcessor =
                TensorProcessor.Builder().add(NormalizeOp(0f, 255f)).build()

            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                .build()

            var tImage = TensorImage(DataType.FLOAT32)


            tImage.load(bitmap)
            tImage = imageProcessor.process(tImage)
            val model = NsfwClassifier.newInstance(this@MainActivity)
            val outputs =
                model.process(probabilityProcessor.process(tImage.tensorBuffer))
            val outputBuffer = outputs.outputFeature0AsTensorBuffer
            val tensorLabel = TensorLabel(labelsList, outputBuffer)

            val nsfwProbability = tensorLabel.mapWithFloatValue.get("NSFW")
            if (nsfwProbability?.compareTo(0.5)!! < 0){
                data.isNSFW = false
            }
            data.prediction =  "NSFW : "+ tensorLabel.mapWithFloatValue.get("NSFW")


        } catch (e: Exception) {
            Log.d("TAG", "Exception is " + e.localizedMessage)
        }
    }
}


data class DataModel(var drawableID: Int, var isNSFW: Boolean = true, var prediction: String = "")