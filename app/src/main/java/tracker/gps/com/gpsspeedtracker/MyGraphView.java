package tracker.gps.com.gpsspeedtracker;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class MyGraphView extends View{
    private Paint greenLine = new Paint();
    private Paint redLine = new Paint();
    private Paint whiteLine = new Paint();
    private int widthOfView;
    private int moveXPostion;
    private int heightOfRow;

    public MyGraphView(Context context) {
        super(context);
        init(context);
    }

    public MyGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        greenLine.setStrokeWidth(2);
        greenLine.setColor(Color.GREEN);
        redLine.setStrokeWidth(2);
        redLine.setColor(Color.RED);
        whiteLine.setStrokeWidth(3);
        whiteLine.setColor(Color.WHITE);

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        widthOfView = display.getWidth();
        moveXPostion = widthOfView / 100;
        heightOfRow = widthOfView / 6;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int xPostionStart = 0;
        int yPositionStart = widthOfView;
        int xPositionStop = moveXPostion;
        int yPositionStop;
        int pixelsRate = widthOfView / 60;

        canvas.drawLine(0, heightOfRow, widthOfView, heightOfRow, whiteLine);
        canvas.drawLine(0, heightOfRow * 2, widthOfView, heightOfRow * 2, whiteLine);
        canvas.drawLine(0, heightOfRow * 3, widthOfView, heightOfRow * 3, whiteLine);
        canvas.drawLine(0, heightOfRow * 4, widthOfView, heightOfRow * 4, whiteLine);
        canvas.drawLine(0, heightOfRow * 5, widthOfView, heightOfRow * 5, whiteLine);

        for (Integer actualSpeed : (Iterable<Integer>) MainActivity.arrayDeque) {
            yPositionStop = widthOfView - actualSpeed * pixelsRate;
            canvas.drawLine(xPostionStart, yPositionStart, xPositionStop, yPositionStop, greenLine);
            xPostionStart = xPositionStop;
            yPositionStart = yPositionStop;
            xPositionStop = xPositionStop + moveXPostion;
        }

        int averageSpeed = widthOfView - MainActivity.averageSpeed * pixelsRate;
        canvas.drawLine(0, averageSpeed, widthOfView, averageSpeed, redLine);

        super.onDraw(canvas);
    }
}
