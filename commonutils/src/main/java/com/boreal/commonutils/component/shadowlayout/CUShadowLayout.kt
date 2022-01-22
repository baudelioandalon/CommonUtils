package com.boreal.commonutils.component.shadowlayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.FloatRange
import com.boreal.commonutils.R


class CUShadowLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    // Shadow paint
    private val mPaint: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            isDither = true
            isFilterBitmap = true
//            this@CUShadowLayout.visibility = View.GONE
        }
    }


    // Shadow bitmap and canvas
    private var mBitmap: Bitmap? = null
    private val mCanvas: Canvas? = Canvas()

    // View bounds
    private val mBounds = Rect()

    // Check whether need to redraw shadow
    private var mInvalidateShadow = true

    // Detect if shadow is visible
    private var mIsShadowed = false

    // Shadow variables
    private var mShadowColor = 0
    private var mShadowAlpha = 0
    private var mShadowRadius = 0f
    private var mShadowDistance = 0f
    private var mShadowAngle = 0f
    var shadowDx = 0f
        private set
    var shadowDy = 0f
        private set

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Clear shadow bitmap
        if (mBitmap != null) {
            mBitmap!!.recycle()
            mBitmap = null
        }
    }

    var isShadowed: Boolean
        get() = mIsShadowed
        set(isShadowed) {
            mIsShadowed = isShadowed
            postInvalidate()
        }
    var shadowDistance: Float
        get() = mShadowDistance
        set(shadowDistance) {
            mShadowDistance = shadowDistance
            resetShadow()
        }

    @set:FloatRange
    @set:SuppressLint("SupportAnnotationUsage")
    var shadowAngle: Float
        get() = mShadowAngle
        set(shadowAngle) {
            mShadowAngle = Math.max(MIN_ANGLE, Math.min(shadowAngle, MAX_ANGLE))
            resetShadow()
        }

    // Set blur filter to paint
    var shadowRadius: Float
        get() = mShadowRadius
        set(shadowRadius) {
            mShadowRadius = Math.max(MIN_RADIUS, shadowRadius)
            if (isInEditMode) return
            // Set blur filter to paint
            mPaint.maskFilter = BlurMaskFilter(mShadowRadius, BlurMaskFilter.Blur.NORMAL)
            resetShadow()
        }
    var shadowColor: Int
        get() = mShadowColor
        set(shadowColor) {
            mShadowColor = shadowColor
            mShadowAlpha = Color.alpha(shadowColor)
            resetShadow()
        }

    // Reset shadow layer
    private fun resetShadow() {
        // Detect shadow axis offset
        shadowDx = (mShadowDistance * Math.cos(mShadowAngle / 180.0f * Math.PI)).toFloat()
        shadowDy = (mShadowDistance * Math.sin(mShadowAngle / 180.0f * Math.PI)).toFloat()

        // Set padding for shadow bitmap
        val padding = (mShadowDistance + mShadowRadius).toInt()
        setPadding(padding, padding, padding, padding)
        requestLayout()
    }

    private fun adjustShadowAlpha(adjust: Boolean): Int {
        return Color.argb(
            if (adjust) MAX_ALPHA else mShadowAlpha,
            Color.red(mShadowColor),
            Color.green(mShadowColor),
            Color.blue(mShadowColor)
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Set ShadowLayout bounds
        mBounds[0, 0, measuredWidth] = measuredHeight
    }

    override fun requestLayout() {
        // Redraw shadow
        mInvalidateShadow = true
        super.requestLayout()
    }

    override fun dispatchDraw(canvas: Canvas) {
        // If is not shadowed, skip
        if (mIsShadowed) {
            // If need to redraw shadow
            if (mInvalidateShadow) {
                // If bounds is zero
                if (mBounds.width() != 0 && mBounds.height() != 0) {
                    // Reset bitmap to bounds
                    mBitmap = Bitmap.createBitmap(
                        mBounds.width(), mBounds.height(), Bitmap.Config.ARGB_8888
                    )
                    // Canvas reset
                    mCanvas!!.setBitmap(mBitmap)

                    // We just redraw
                    mInvalidateShadow = false
                    // Main feature of this lib. We create the local copy of all content, so now
                    // we can draw bitmap as a bottom layer of natural canvas.
                    // We draw shadow like blur effect on bitmap, cause of setShadowLayer() method of
                    // paint does`t draw shadow, it draw another copy of bitmap
                    super.dispatchDraw(mCanvas)

                    // Get the alpha bounds of bitmap
                    val extractedAlpha = mBitmap?.extractAlpha()
                    // Clear past content content to draw shadow
                    mCanvas.drawColor(0, PorterDuff.Mode.CLEAR)

                    // Draw extracted alpha bounds of our local canvas
                    mPaint.color = adjustShadowAlpha(false)
                    mCanvas.drawBitmap(extractedAlpha!!, shadowDx, shadowDy, mPaint)

                    // Recycle and clear extracted alpha
                    extractedAlpha.recycle()
                } else {
                    // Create placeholder bitmap when size is zero and wait until new size coming up
                    mBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
                }
            }

            // Reset alpha to draw child with full alpha
            mPaint.color = adjustShadowAlpha(true)
            // Draw shadow bitmap
            if (mCanvas != null && mBitmap != null && !mBitmap!!.isRecycled) canvas.drawBitmap(
                mBitmap!!, 0.0f, 0.0f, mPaint
            )
        }

        // Draw child`s
        super.dispatchDraw(canvas)
    }

    companion object {
        // Default shadow values
        private const val DEFAULT_SHADOW_RADIUS = 30.0f
        private const val DEFAULT_SHADOW_DISTANCE = 15.0f
        private const val DEFAULT_SHADOW_ANGLE = 45.0f
        private const val DEFAULT_SHADOW_COLOR = Color.DKGRAY

        // Shadow bounds values
        private const val MAX_ALPHA = 255
        private const val MAX_ANGLE = 360.0f
        private const val MIN_RADIUS = 0.1f
        private const val MIN_ANGLE = 0.0f
    }

    init {
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE, mPaint)

        // Retrieve attributes from xml
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CUShadowLayout)
        try {
            isShadowed = typedArray.getBoolean(R.styleable.CUShadowLayout_sl_shadowed, true)
            shadowRadius = typedArray.getDimension(
                R.styleable.CUShadowLayout_sl_shadow_radius, DEFAULT_SHADOW_RADIUS
            )
            shadowDistance = typedArray.getDimension(
                R.styleable.CUShadowLayout_sl_shadow_distance, DEFAULT_SHADOW_DISTANCE
            )
            shadowAngle = typedArray.getInteger(
                R.styleable.CUShadowLayout_sl_shadow_angle, DEFAULT_SHADOW_ANGLE.toInt()
            )
                .toFloat()
            shadowColor = typedArray.getColor(
                R.styleable.CUShadowLayout_sl_shadow_color, DEFAULT_SHADOW_COLOR
            )
        } finally {
            typedArray.recycle()
        }
    }
}