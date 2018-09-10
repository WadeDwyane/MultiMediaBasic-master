# Android多媒体开发之图像开发基础 #

本文将主要介绍有关Android图像捕获、显示、存储的有关知识。首先我们将展示如何通过三种方式来绘制图片；然后介绍如何通过Android内置功能来捕获图像；其次介绍如何展示图像；最后我们将对图像的存储做详细介绍。文章所介绍的图像绘制、捕获、显示、存储为Android的多媒体开发提供了很好的切入点，为我们学习音频、视频开发开发奠定了坚实的基础。

----------
## 1.1 图像绘制 ##
Android平台为我们提供了至少3种方法来绘制一张图像。分别为：`ImageView、SutrfaceView、自定义View`.下面我们将详细介绍每一种方法的具体实现：

### 1.1.1 ImageView绘制图片 ###
这个是最简单的,基本人人都会用过:

	Bitmap bitmap = BitmapFactory.decodeFile(
		Environment.getExternalStorageDirectory().getPath()
			+ File.separator + "beauty.jpg");
	imageView.setImageBitmap(bitmap);

### 1.1.2 SurfaceView绘制图片 ###
SufaceView绘制图片稍微复杂一点:

	SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
	surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
	    @Override
	    public void surfaceCreated(SurfaceHolder surfaceHolder) {

	        if (surfaceHolder == null) {
	            return;
	        }

	        Paint paint = new Paint();
	        paint.setAntiAlias(true);
	        paint.setStyle(Paint.Style.STROKE);

			//获取bitmap
	        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() +
					File.separator + "11.jpg");  
			//先锁定当前surfaceView的画布
	        Canvas canvas = surfaceHolder.lockCanvas();
			//执行绘制操作
	        canvas.drawBitmap(bitmap, 0, 0, paint);
			//解除锁定并显示在界面上
	        surfaceHolder.unlockCanvasAndPost(canvas);
	    }

	    @Override
	    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

	    }

	    @Override
	    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

	    }
	});

### 1.1.3 自定义View绘制图片 ###
在自定义View中,主要涉及到的方法是创建Canvas,Paint对象,重写onDraw(),绘制的代码写在onDraw()方法里面.具体的代码如下:

	public class CustomView extends View {

	    Paint paint = new Paint();
	    Bitmap bitmap;

	    public CustomView(Context context) {
	        super(context);
	        paint.setAntiAlias(true);
	        paint.setStyle(Paint.Style.STROKE);
			//获取bitmap
	        bitmap = BitmapFactory.decodeFile(
				Environment.getExternalStorageDirectory().getPath() + File.separator +"11.jpg");  
	    }

	    @Override
	    protected void onDraw(Canvas canvas) {
	        super.onDraw(canvas);

	        // 不建议在onDraw做任何分配内存的操作
	        if (bitmap != null) {
	            canvas.drawBitmap(bitmap, 0, 0, paint);
	        }
	    }
	}

## 1.2 图像捕获 ##
### 1.2.1 使用Intent实现图像捕获 ###
Android上很多简单的操作都可以Intent(意图）来调用设备内置的软件来完成。Android设备所提供的硬件——摄像头，保证了我们可以使用Camera程序来捕获图像。

Camera程序中的manifest.xml文件中指定了以下意图过滤器：

	<intent-filter>
		<action android:name="android.media.action.IMAGE_CAPTURE"/>
		<category android:name="android.intent.category.DEFAULT"/>
	</intent-filter>

我们只需通过构建一个由以上过滤器捕获的意图，即可实现图像捕获。

	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE")

在实际开发中，我们很少直接用字符串创建意图，而是采用MediaStore中的常量
`ACTION_IMAGE_CAPTURE`来创建。

	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
	startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE)

### 1.2.2 处理Carema返回数据 ###
使用Camera捕获到图像后，通过重写`onActivityResult`方法来处理返回的数据。

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK) {
			//获取附加值
			Bundle extras = intent.getExtras();
			//从附加值中获取Bitmap数据
			Bitmap bmp = (Bitmap) extras.get("data");
			imv = (ImageView) findViewById(R.id.imv);
			//展示Bitmap
			imv.setImageBitmap(bmp);
		}
	}

### 1.2.3 捕获大图像 ###
有时候我们需要获取较大的图像，这时候我们只需要传递一个附加值给使用Carema的意图。这个附加值的名称是`MediaStore`中的`EXTRA_OUTPUT`,我们只需要制定捕获的图像存储的位置即可。

	//存储在sd卡上
	String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Beauty.jpg";
	File file = new File(path);
    Uri uri = Uri.fromFile(file);
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	//设置捕获图像输出位置
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    startActivityForResult(intent, REQUEST_CAPTURE_BIG_CODE);

### 1.2.4 展示大图像 ###
由于移动设备的内存限制，大图像的显示一直都是需要考虑的问题。为了解决大图像显示带来的对内存消耗严重的问题，Android提供了`BitmapFactory`来操作图像。它提供了多种静态方法，使得我们能够通过不同的数据源来加载图像。它提供的`BitmapFactory.Options`类，使得我们能够指定Bitmap如何读入内存。加载图像时，我们指定图像采样大小`inSampleSize`,它表示要加载的图像所占的比例。例如，`inSampleSize = 8，`那么就生成一张是原始图像的1/8的新图像。我们在实际开发中，通常要考虑实际图像的大小从而来决定采样率。下面的的代码展示了如何将大图缩放至屏幕大小。

	//获取屏幕的相关尺寸
	Display currentDisplay = getWindowManager().getDefaultDisplay();
	int dw = currentDisplay.getWidth();
	int dh = currentDisplay.getHeight();

	//设置Bitmap的相关参数
	BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeFile(imageFilePath,
					bmpFactoryOptions);

	//获取高度比率
	int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight/(float)dh);
	//获取宽度比率
	int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth/(float)dw);

	如果宽、高比率都超过1，那么图像的一条边将超出屏幕
	if (heightRatio > 1 && widthRatio > 1) {
		if (heightRatio > widthRatio) {
			// 高度比率更大，则根据它来缩放
			bmpFactoryOptions.inSampleSize = heightRatio;
		} else {
			// 宽度比率更大，则根据它来缩放
			bmpFactoryOptions.inSampleSize = widthRatio;
		}
	}

	//实现真正的解码
	bmpFactoryOptions.inJustDecodeBounds = false;
	bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);

## 1.3 图像存储及元数据 ##
Android提供了一个基本组件——`ContentProvider`来实现程序间的资源共享。图像(音频或视频)的内容提供者是`MediaStore`，它为文件的存储和检索提供了很多便利。而元数据是关于数据本身的基本数据：文件的大小和名称。但`MediaStore`还允许设置一些其他的信息，如标题、描述、经度、纬度等。

### 1.3.1 获得图像的Uri ###
为了获取MediaStore的标准引用，先使用一个内容解析器。内容解析器用于提供访问内容提供者的方法。
我们想通过`insert`方法，将图片存储在sd卡中，并且提供给其他应用使用。那么

	Uri imageFileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());

	//启动捕获图片的应用
	Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,imageFileUri);
	startActivityForResult(i, CAMERA_RESULT);

	//预填充元数据
	ContentValues contentValues = new ContentValues(3);
	contentValues.put(Media.DISPLAY_NAME, titleEditText.getText().toString());
	contentValues.put(Media.DESCRIPTION, descriptionEditText.getText().toString());

	//更新元数据内容
	getContentResolver().update(imageFileUri, contentValues, null, null);


### 1.3.2 使用MediaStore检索数据 ###
当第三方应用想使用我们的数据时，就需要用到内容提供者。首先我们通过`MediaStore.Images.Media`来创建我们的检索列，然后使用`managedQuery()`执行查询，执行查询完毕会返回查询的`Cursor`。通过`Cursor`我们就能获取到查询的结果。

	String[] columns = {Media.DATA, Media._ID, Media.TITLE, Media.DISPLAY_NAME};
	cursor = managedQuery(Media.EXTERNAL_CONTENT_URI, columns, null, null,
				null);

	fileColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
	displayColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

	if (cursor.moveToFirst()) {
		// titleTextView.setText(cursor.getString(titleColumn));
		titleTextView.setText(cursor.getString(displayColumn));

		imageFilePath = cursor.getString(fileColumn);
		bmp = getBitmap(imageFilePath);

		// Display it
		imageButton.setImageBitmap(bmp);
	}

## 1.4 总结 ##
本文主要介绍了Android中如何生成、捕获、存储图像，同时针对大图的捕获和显示提供了实际的解决方案。相信它会为探索Android多媒体提供一个良好的开始。
