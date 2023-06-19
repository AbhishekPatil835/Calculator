package com.example.calculator

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var b1: Button? = null
    private var b2: Button? = null
    private var b3: Button? = null
    private var b4: Button? = null
    private var b5: Button? = null
    private var b6: Button? = null
    private var b7: Button? = null
    private var b8: Button? = null
    private var b9: Button? = null
    private var b0: Button? = null
    private var b_equal: Button? = null
    private var b_multi: Button? = null
    private var b_divide: Button? = null
    private var b_add: Button? = null
    private var b_sub: Button? = null
    private var b_clear: Button? = null
    private var b_dot: Button? = null
    private var b_para1: Button? = null
    private var b_para2: Button? = null
    private var t1: TextView? = null
    private var t2: TextView? = null
    private val ADDITION = '+'
    private val SUBTRACTION = '-'
    private val MULTIPLICATION = '*'
    private val DIVISION = '/'
    private val EQU = '='
    private val EXTRA = '@'
    private val MODULUS = '%'
    private var ACTION = 0.toChar()
    private var val1 = Double.NaN
    private var val2 = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewSetup()
        setNumberButtonClickListeners()
        setOperatorButtonClickListeners()
        setClearButtonClickListeners()
        setEqualButtonClickListeners()
    }

    private fun viewSetup() {
        b1 = findViewById(R.id.button1)
        b2 = findViewById(R.id.button2)
        b3 = findViewById(R.id.button3)
        b4 = findViewById<Button>(R.id.button4)
        b5 = findViewById<Button>(R.id.button5)
        b6 = findViewById<Button>(R.id.button6)
        b7 = findViewById<Button>(R.id.button7)
        b8 = findViewById<Button>(R.id.button8)
        b9 = findViewById<Button>(R.id.button9)
        b0 = findViewById<Button>(R.id.button0)
        b_equal = findViewById<Button>(R.id.button_equal)
        b_multi = findViewById<Button>(R.id.button_multi)
        b_divide = findViewById<Button>(R.id.button_divide)
        b_add = findViewById<Button>(R.id.button_add)
        b_sub = findViewById<Button>(R.id.button_sub)
        b_clear = findViewById<Button>(R.id.button_clear)
        b_dot = findViewById<Button>(R.id.button_dot)
        b_para1 = findViewById<Button>(R.id.button_para1)
        b_para2 = findViewById<Button>(R.id.button_para2)
        t1 = findViewById(R.id.input)
        t2 = findViewById<TextView>(R.id.output)
    }

    private fun setNumberButtonClickListeners() {
        val numberButtons = listOf(b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b_dot)
        numberButtons.forEach { button ->
            button?.setOnClickListener {
                ifErrorOnOutput()
                exceedLength()
                t1!!.text = t1!!.text.toString() + button.text.toString()
            }
        }
    }

    private fun setOperatorButtonClickListeners() {
        val operatorButtons = listOf(b_add, b_sub, b_multi, b_divide, b_para1, b_para2)
        operatorButtons.forEach { button ->
            button?.setOnClickListener {
                if (t1!!.text.length > 0) {
                    saveInput(t1!!.text.toString()) // Save the input
                    val input = t1!!.text.toString()
                    if (isNumeric(input)) {
                        operation()
                        ACTION = button.text.toString()[0]
                        t2!!.text = val1.toString()
                        t1!!.setText(null)
                    } else {
                        t2!!.text = "Error"
                    }
                } else {
                    t2!!.text = "Error"
                }
            }
        }
    }

    private fun setClearButtonClickListeners() {
        b_clear?.setOnClickListener {
            if (t1!!.text.length > 0) {
                val name: CharSequence = t1!!.text.toString()
                t1!!.text = name.subSequence(0, name.length - 1)
            } else {
                val1 = Double.NaN
                val2 = Double.NaN
                t1!!.text = ""
                t2!!.text = ""
            }
        }
    }

    private fun setEqualButtonClickListeners() {
        b_equal?.setOnClickListener {
            if (t1!!.text.length > 0) {
                saveInput(t1!!.text.toString()) // Save the input
                operation()
                ACTION = EQU
                if (!ifReallyDecimal()) {
                    t2!!.text = val1.toString()
                } else {
                    t2!!.text = val1.toInt().toString()
                }
                t1!!.setText(null)
            } else {
                t2!!.text = "Error"
            }
        }
    }

    private fun operation() {
        if (!java.lang.Double.isNaN(val1)) {
            val2 = t1!!.text.toString().toDouble()
            when (ACTION) {
                ADDITION -> val1 += val2
                SUBTRACTION -> val1 -= val2
                MULTIPLICATION -> val1 *= val2
                DIVISION -> val1 /= val2
                EXTRA -> {
                    val1 *= 100
                    val1 += val2
                }
                MODULUS -> val1 %= val2
            }
        } else {
            try {
                val1 = t1!!.text.toString().toDouble()
            } catch (e: Exception) {
            }
        }
    }

    private fun ifReallyDecimal(): Boolean {
        return val1 % 1.0 != 0.0
    }

    private fun saveInput(input: String) {
        val sharedPref = getSharedPreferences("CalculatorInput", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("input", input)
        editor.apply()
    }

    private fun ifErrorOnOutput() {
        if (t2!!.text.toString() == "Error") {
            t2!!.text = ""
            t1!!.text = ""
            val1 = Double.NaN
            val2 = Double.NaN
        }
    }

    private fun exceedLength() {
        val pixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            100f,
            resources.displayMetrics
        ).toInt()

        if (t1!!.text.length * 12 >= pixels) {
            t1!!.textSize = 16f
        } else {
            t1!!.textSize = 30f
        }
    }

    private fun isNumeric(str: String): Boolean {
        return try {
            str.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}
