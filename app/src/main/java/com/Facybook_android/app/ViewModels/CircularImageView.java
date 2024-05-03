    package com.Facybook_android.app.ViewModels;

    import android.content.Context;
    import android.graphics.Canvas;
    import android.graphics.Path;
    import android.util.AttributeSet;

    import androidx.appcompat.widget.AppCompatImageView;

    public class CircularImageView extends AppCompatImageView {

        public CircularImageView(Context context) {
            super(context);
        }

        public CircularImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // Get the width and height of the view
            int viewWidth = getWidth();
            int viewHeight = getHeight();

            // Calculate the radius to draw the circle
            int radius = Math.min(viewWidth, viewHeight) / 2;

            // Create a path to draw a circle
            Path path = new Path();
            path.addCircle(viewWidth / 2f, viewHeight / 2f, radius, Path.Direction.CCW);

            // Clip the canvas to the circular path
            canvas.clipPath(path);

            // Call the superclass method to draw the image
            super.onDraw(canvas);
        }
    }
