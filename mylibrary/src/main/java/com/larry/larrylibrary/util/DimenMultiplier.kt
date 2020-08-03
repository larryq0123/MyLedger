package com.larry.larrylibrary.util

import android.content.Context
import kotlin.math.roundToInt
import java.io.*


/**
 * Created by user on 2018/10/25.
 */
object DimenMultiplier {

    private val TAG = DimenMultiplier::class.java.simpleName
    private lateinit var linesProcessed: MutableList<String>

    fun multiplyDimens(context: Context, assetName: String, multiplier: Float, fileName: String){
        linesProcessed = ArrayList()
        var reader: BufferedReader? = null

        try {
            reader = BufferedReader(InputStreamReader(context.assets.open(assetName)))

            var mLine: String?
            while (true) {
                mLine = reader.readLine()
                if(mLine == null){
                    break
                }

                linesProcessed.add(processLine(mLine, multiplier))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        //todo 將linesProcessed寫進新的文件檔案裡
        writeLinesToFile(linesProcessed, fileName)
    }

    /**
     * 將含有dp, sp的數字乘上倍數
     */
    private fun processLine(line: String, multiplier: Float): String{
        if(!line.contains("<dimen"))
            return line

        val start = line.indexOf("\">") + 2
        val end = line.indexOf("</dimen>")
        val prefix = line.substring(0, start)
        val suffix = "</dimen>"
        val clean = line.removePrefix(prefix).removeSuffix(suffix)

        BaseLogUtil.logd(TAG, "in processLine, line = $line, clean = $clean")

        val unit = if(clean.contains("dp")) "dp" else "sp"
        val number = (clean.removeSuffix(unit).toInt() * multiplier).roundToInt()
        val sb = StringBuilder()
        sb.append(prefix).append(number).append(unit).append(suffix)

        return sb.toString()
    }

    private fun writeLinesToFile(lines: MutableList<String>, fileName: String){
        val directory = FileUtil.getAPPRootDirectory()
        val file = File(directory, fileName)

        try {
            file.createNewFile()
            val fOut = FileOutputStream(file)
            val writer = OutputStreamWriter(fOut)

            lines.forEach {
                writer.append(it)
                writer.append("\n")
            }

            writer.close()
            fOut.flush()
            fOut.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}