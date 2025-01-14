package learner.sandman.ntcc3;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.android.globalactionbarservice.R;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.video.Video;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MyAccessibilityService extends AccessibilityService implements CameraBridgeViewBase.CvCameraViewListener2 {
	//****************Variables for the cursorview**************//
	FrameLayout cursorFrameLayout;
	WindowManager.LayoutParams cursorParams;

	//*****Variables for setting up the faceview***********//
	View faceView;
	WindowManager myWindowManager;
	WindowManager.LayoutParams faceParams;
	//**********variables for the cameraview and cameraview listener
	CameraBridgeViewBase cameraView;
	//******************handler********************//
	Handler handler;
	//****************ALL CLASSIFIER VARIABLES***************//
	CascadeClassifier haarCascadeClassifierForFace,haarCascadeClassifierForTeeth;


	@Override
	protected void onServiceConnected() {
		super.onServiceConnected();
		//************************ENABLING ALL THE OPEN CV LIBRARIES***************************//
		if(OpenCVLoader.initDebug()){
			Log.d("TAG1","OpenCv started successfully");
		}
		//************************SETTING UP THE FACEVIEW***************************************//

		faceView= LayoutInflater.from(this).inflate(R.layout.face_layout,null);
		myWindowManager= (WindowManager) getSystemService(WINDOW_SERVICE);
		faceParams= new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT
		);
		faceParams.gravity= Gravity.TOP|Gravity.LEFT;
		faceParams.x=0;
		faceParams.y=0;
		//faceParams.alpha= (float) ;
		myWindowManager.addView(faceView,faceParams);
		//************************SETTING UP THE CURSOR VIEW************************//
		//**********************MAKING A LAYOUT FOR THE CURSOR************************//
		cursorFrameLayout=new FrameLayout(this);
		cursorParams=new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT
		);
		cursorParams.gravity=Gravity.TOP|Gravity.LEFT;
		LayoutInflater inflater=LayoutInflater.from(this);

		inflater.inflate(R.layout.cursor_layout,cursorFrameLayout);




		//use cursorFramlayout instead of cursor view
		myWindowManager.addView(cursorFrameLayout,cursorParams);

		//***********Getting and storing the screen width and screen height*****************//
		DisplayMetrics displayMetrics = new DisplayMetrics();
		myWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
		final int screenHeight = displayMetrics.heightPixels;
		final int screenWidth = displayMetrics.widthPixels;


		//********************THIS BLOCK CONSISTS OF THE HANDLER*************************//
		handler=new Handler(Looper.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				ImageView cusor=cursorFrameLayout.findViewById(R.id.cursor_view);

				switch (msg.what){
					//****************FOR LEFT MOVEMENT*******************//
					case 1://left

						cusor.setBackgroundColor(Color.BLACK);
						cursorParams.x+=30;
						if(cursorParams.x>screenWidth){
							cursorParams.x=screenWidth;
						}
						Log.d("TAG5","should go left");
						break;
					case 2://left

						cusor.setBackgroundColor(Color.BLACK);
						cursorParams.x+=10;
						if(cursorParams.x>screenWidth){
							cursorParams.x=screenWidth;
						}
						Log.d("TAG5","should go left");
						break;
					//********************FOR RIGHT MOVEMENT******************//
					case 3://right

						cusor.setBackgroundColor(Color.BLACK);
						cursorParams.x-=30;
						if(cursorParams.x<0){
							cursorParams.x=0;
						}
						Log.d("TAG5","should go right");
						break;
					case 4://right

						cusor.setBackgroundColor(Color.BLACK);
						cursorParams.x-=10;
						if(cursorParams.x<0){
							cursorParams.x=0;
						}
						Log.d("TAG5","should go right");
						break;
					//********************FOR DOWN MOVEMENT*******************//
					case 5://down
						cusor.setBackgroundColor(Color.BLACK);
						cursorParams.y+=30;
						if(cursorParams.y>screenHeight){
							cursorParams.y=screenHeight;
						}
						Log.d("TAG5","should go down");
						break;
					case 6://down
						cusor.setBackgroundColor(Color.BLACK);
						cursorParams.y+=10;
						if(cursorParams.y>screenHeight){
							cursorParams.y=screenHeight;
						}
						Log.d("TAG5","should go down");
						break;
					//******************FOR UP MOVEMENT*******************//
					case 7://up
						cusor.setBackgroundColor(Color.BLACK);
						cursorParams.y-=30;
						if(cursorParams.y>screenHeight){
							cursorParams.y=0;
						}
						Log.d("TAG5","should go up");
						break;
					case 8://up
						cusor.setBackgroundColor(Color.BLACK);
						cursorParams.y-=10;
						if(cursorParams.y>screenHeight){
							cursorParams.y=0;
						}
						Log.d("TAG5","should go up");
						break;
					default:
						cusor.setBackgroundColor(Color.WHITE);

						Log.d("TAG5","Hadnler does nothing");
						break;


				}

				myWindowManager.updateViewLayout(cursorFrameLayout,cursorParams);
				if(cursorParams.y<screenHeight/2){
					faceParams.y=screenHeight;
					faceParams.x=screenWidth;
					myWindowManager.updateViewLayout(faceView,faceParams);
				}else{
					faceParams.y=0;
					faceParams.x=0;
					myWindowManager.updateViewLayout(faceView,faceParams);

				}



			}
		};
		//**************************OPENING THE FACE DETECTION FILES***********************//



		//**************************MAKING THE FACEVIEW SHOW FACE******************************//
		cameraView=faceView.findViewById(R.id.cameraView);
		cameraView.setVisibility(SurfaceView.VISIBLE);
		cameraView.setCvCameraViewListener(this);
		cameraView.enableView();


	}
	void bringInTheCascadeFileForFaceDetection() throws IOException {
		InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);//smile er jonno cascade file ta nilam
		File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);//directory banalam jeta private
		File mCascadeFile = new File(cascadeDir,"cascade.xml");//directoryr moddhe cascade.xml file ta rakhlam
		FileOutputStream os = new FileOutputStream(mCascadeFile);
		//it is stream to write to my new cascade.xml
		//file
		byte[] buffer = new byte[4096];//ekta byte array banalam buffer naame
		int bytesRead;//this will collect a  byte of data from input stream

		while((bytesRead = is.read(buffer)) != -1)//is.read reads  from file and puts in buffer and returns koy byte porlo
		{
			os.write(buffer, 0, bytesRead);//buffer theke data niye write korche
		}
		is.close();
		os.close();
		haarCascadeClassifierForFace = new CascadeClassifier(mCascadeFile.getAbsolutePath());//ekta cascade classifier banalam using the file
		if(!haarCascadeClassifierForFace.empty()){
			Log.d("TAG1","The haar Cascde object ain't empty");
		}
	}
	void bringInTheCascadeFileForTeethDetection() throws IOException {
		InputStream is = getResources().openRawResource(R.raw.cascadefina);//smile er jonno cascade file ta nilam
		File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);//directory banalam jeta private
		File mCascadeFile = new File(cascadeDir,"cascade.xml");//directoryr moddhe cascade.xml file ta rakhlam
		FileOutputStream os = new FileOutputStream(mCascadeFile);
		//it is stream to write to my new cascade.xml
		//file
		byte[] buffer = new byte[4096];//ekta byte array banalam buffer naame
		int bytesRead;//this will collect a  byte of data from input stream

		while((bytesRead = is.read(buffer)) != -1)//is.read reads  from file and puts in buffer and returns koy byte porlo
		{
			os.write(buffer, 0, bytesRead);//buffer theke data niye write korche
		}
		is.close();
		os.close();
		haarCascadeClassifierForTeeth = new CascadeClassifier(mCascadeFile.getAbsolutePath());//ekta cascade classifier banalam using the file
		if(!haarCascadeClassifierForTeeth.empty()){
			Log.d("TAG2","The haar Cascde object for eyes ain't empty");
		}
	}



	//*******************THESE VARIABLES ARE RELATED  TO OPENCV************************//

	Mat mRgba,mGray;
	int frameCount;
	Point nosePoint,detectedNosePoint;

	//these are variables related to optical flow
	Point centrePoint;
	MatOfPoint2f prevFeatures;
	MatOfPoint2f presentFeatures;
	MatOfPoint2f prevFeaturesStorer;
	Mat prevmGray;
	MatOfByte status;
	MatOfFloat err;
	boolean prevFeaturesAvailable;
	Point pstorer;
	boolean firstTeethDetected=false;
	int frameCountForDebouncing=0;

	@Override
	public void onCameraViewStarted(int width, int height) {
		mRgba=new Mat();
		mGray=new Mat();
		prevmGray=new Mat();
		nosePoint=new Point();
		frameCount=0;
		//******************OPRNING THE FILES***************************//
		try {
			bringInTheCascadeFileForFaceDetection();
			bringInTheCascadeFileForTeethDetection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//******************initializing optical flow variables****************//
		status=new MatOfByte();
		err=new MatOfFloat();

		presentFeatures=new MatOfPoint2f();
		prevFeatures=new MatOfPoint2f();
		//prevFeaturesStorer=new MatOfPoint2f();
		prevFeaturesAvailable=false;

		pstorer=new Point();

	}

	@Override
	public void onCameraViewStopped() {

	}

	@Override
	public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
		//****************I AM INCREASING THE FRAME COUNT******************************//
		frameCount=(frameCount+1)%200;
		//*****************I AM COLLECTING THE TWO MATS FROM CAMERA**********************//
		mGray=inputFrame.gray().t();
		Core.flip(mGray,mGray,-1);
		Imgproc.resize(mGray,mGray,inputFrame.gray().size());

		mRgba=inputFrame.rgba().t();
		Core.flip(mRgba,mRgba,-1);
		Imgproc.resize(mRgba,mRgba,inputFrame.rgba().size());


		//*********************THE CENTRE POINT AND CENTRE RECTANGLES ARE BEING ET UP**********************//
		centrePoint =new Point();
		centrePoint.x=mRgba.cols()/2;
		centrePoint.y=mRgba.rows()/2;


		Rect centralRectangle=new Rect(new Point(centrePoint.x-30,centrePoint.y-30),
				new Point(centrePoint.x+30,centrePoint.y+30));


		Imgproc.rectangle(mRgba,centralRectangle.tl(),centralRectangle.br(),new Scalar(100),3);

		Rect centralRectangleOuter=new Rect(new Point(centrePoint.x-50,centrePoint.y-50),
				new Point(centrePoint.x+50,centrePoint.y+50));


		Imgproc.rectangle(mRgba,centralRectangleOuter.tl(),centralRectangleOuter.br(),new Scalar(100),3);



		//*******************FRAME HANDLING NUMERICALLY********************//
		if(frameCount%20==0){

			//****************DETECTING FACE USING VIOLA JONES********************//
			//matofrect  will collect the detected faces
			MatOfRect faces=new MatOfRect();
			//classifier detectsa face and stores the faces detected in faces object
			haarCascadeClassifierForFace.detectMultiScale(mGray, faces, 1.1, 2,
					2, new Size(100,100), new Size());
			//this array stores all the faces
			Rect[]facesArray = faces.toArray();
			//if there is atleast one frame detected
			if(facesArray.length>0){
				//i show the face rectangle on the faceview
				Imgproc.rectangle(mRgba,facesArray[0].tl(),facesArray[0].br(),new Scalar(100),3);
				//i obtain the nose co-ordinates
				nosePoint=new Point(facesArray[0].tl().x/2+facesArray[0].br().x/2,
						facesArray[0].tl().y/2+facesArray[0].br().y/2
				);
				//this will only make a difference in case of the very first frame
				prevFeaturesAvailable=true;
				//i am going to store a previous matrix when the face is detected
				prevmGray=mGray.clone();
				//i am going to store the nosePoint as previois features
				prevFeatures.fromArray(nosePoint);
				//also i am going to draw the nosepoint drawn here
				if(prevFeatures.toArray().length>0){
					Imgproc.circle(mRgba,prevFeatures.toArray()[0],5,new Scalar(100),20);
				}

			}
		} else{//this block is for when violaJones is not being applied
			//if i have a prev matrix and a prev nose point
			if(prevFeaturesAvailable){
				//************IN THIS BLOCK MY INTENTION IS TO GET THE NEW FEATURE OR MOVED NOSE POSITION*********//

				//pstorer stores the prevFeatures
				pstorer=new Point();
				pstorer.x=prevFeatures.toArray()[0].x;
				pstorer.y=prevFeatures.toArray()[0].y;

				//after this presentFeatures will have the latest noise points
				Video.calcOpticalFlowPyrLK(prevmGray,mGray,prevFeatures, presentFeatures, status, err);
				Imgproc.circle(mRgba,presentFeatures.toArray()[0],5,new Scalar(100),20);
				//i am gonna store the features of this frame as prevFeatures now
				prevmGray=mGray.clone();
				prevFeatures=presentFeatures;
				prevFeaturesAvailable=true;

			}
		}

		if(firstTeethDetected==false){
			//*******************IF THERE ARE PREVIOUS FEATURES STH MAGICAL IS ABOUT TO HAPPEN**************//
			Message msg=new Message();
			if(presentFeatures.toArray().length!=0){

				//***********FOR LEFT MOVEMENT******************
				if(centrePoint.x-presentFeatures.toArray()[0].x<-50 ){
					//o means left
					Log.d("TAG5","LEFT");
					msg.what=1;
				}else if(centrePoint.x-presentFeatures.toArray()[0].x<-30 ){
					//o means left
					Log.d("TAG5","LEFT");
					msg.what=2;
				}
				//***********FOR RIGHT MOVEMENT******************
				else if(centrePoint.x-presentFeatures.toArray()[0].x>50){
					//1 means right
					Log.d("TAG5","RIGHT");
					msg.what=3;
				}
				else if(centrePoint.x-presentFeatures.toArray()[0].x>30){
					//1 means right
					Log.d("TAG5","RIGHT");
					msg.what=4;
				}
				//******************FOR DOWN MOVEMENT*************************//
				else if(centrePoint.y-presentFeatures.toArray()[0].y<-50){
					//o means down
					Log.d("TAG5","DOWN");
					msg.what=5;
				}
				else if(centrePoint.y-presentFeatures.toArray()[0].y<-30){
					//o means down
					Log.d("TAG5","DOWN");
					msg.what=6;
				}
				//*********************FOR UP MOVEMENT**************************//
				else if(centrePoint.y-presentFeatures.toArray()[0].y>50){
					//1 means up
					Log.d("TAG5","UP");
					msg.what=7;
				}
				else if(centrePoint.y-presentFeatures.toArray()[0].y>30){
					//1 means up
					Log.d("TAG5","UP");
					msg.what=8;
				}
				//**************************IF NO MOVEMENT******************************//
				else{
					msg.what=-1;
				}
			}
			handler.sendMessage(msg);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//************HANDLER MESSAGING ENDS HERE**********************//

		}

		//******************THIS IS THE CODE FOR CLICKING*************************//
		MatOfRect teeth = new MatOfRect();
		if(haarCascadeClassifierForTeeth != null) {
			haarCascadeClassifierForTeeth.detectMultiScale
					(mGray.submat((int) centrePoint.y,mGray.rows(),mGray.cols()*1/4,mGray.cols()*3/4),
							teeth,
							1.1,
							4,
							2,
							new Size(),
							new Size());
		}
		//********************TEETH DETECTION CODE STARTS HERE*************************//
		Rect[] teethArray = teeth.toArray();
		//if there is any teeth in the image
		if(teethArray.length>0 && firstTeethDetected==false){
			//i indicate the teeth in the image
			Imgproc.rectangle(mRgba.submat((int) centrePoint.y,mRgba.rows(),mRgba.cols()*1/4,mRgba.cols()*3/4)
					, teethArray[0].tl(),teethArray[0].br(), new Scalar(100), 3);
			//i keep log that the first teeth has been detected
			firstTeethDetected=true;
			//i set count for debouncing =0;
			frameCountForDebouncing=0;
		}else if(firstTeethDetected==true){
			frameCountForDebouncing+=1;
			// i have waited for 20 frames
			if(frameCountForDebouncing>=10){
				//since 20 frames have passed i set the count to zero and also stop entering this block
				firstTeethDetected=false;
				frameCountForDebouncing=0;
				//if there is still teeth showing
				if(teethArray.length>0){
					Path swipePath = new Path();
					swipePath.moveTo(cursorParams.x+40, cursorParams.y+110);

					GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
					gestureBuilder.addStroke(new GestureDescription.StrokeDescription(swipePath, 0, 150));
					dispatchGesture(gestureBuilder.build(), null, null);
				}
			}

		}
		//************CODE FOR TEETH DETECTION AND CLICKING ENDS HERE***********************//
		return mRgba;
	}
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
	}
	@Override
	public void onInterrupt() {
	}

}
