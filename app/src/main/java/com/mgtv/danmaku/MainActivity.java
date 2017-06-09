package com.mgtv.danmaku;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mgtv.danmaku.entity.DanmakuListEntity;
import com.mgtv.danmaku.loader.DanmakuJSONLoaderFactory;
import com.mgtv.danmaku.parser.DanmakuListParser;
import com.mgtv.danmaku.suffer.MGSpannedCacheStuffer;
import com.mgtv.danmaku.util.GlideCircleTransform;
import com.mgtv.danmaku.util.RequestCountDownTimer;
import com.mgtv.danmaku.util.RoundedCornersTransformation;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText editBarrage;

    private IDanmakuView danmakuView;
    private DanmakuContext danmakuContext;
    private BaseDanmakuParser parser;
    private ILoader loader;
    private Context context;
    private RequestCountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        initDanmakuView();
//        danmakuView.prepare(parser, danmakuContext);
    }

    private void initDanmakuView() {
        danmakuView = (IDanmakuView) findViewById(R.id.danmakuView);
        editBarrage = (EditText) findViewById(R.id.editBarrage);

        danmakuContext = DanmakuContext.create();
        // 设置不重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        // 设置最大显示行数
//        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
//        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        danmakuContext
//                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter); // 图文混排使用SpannedCacheStuffer
                .setCacheStuffer(new MGSpannedCacheStuffer(context), mCacheStufferAdapter)
//                .setCacheStuffer(new MGSpannedCacheStuffer(context), mCacheStufferAdapter)
                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(10);
//                .setMaximumLines(maxLinesPair);

        /** 弹幕点击事件 */
        danmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
            @Override
            public boolean onDanmakuClick(IDanmakus danmakus) {

                BaseDanmaku last = danmakus.last();
                if (null != last){
                    // 17/5/16 修改颜色 和 点赞数
                    increasePraise(last);
                    Log.e(TAG, "onDanmakuClick: " + last.text);
                    return true;
                }

                return false;
            }

            @Override
            public boolean onDanmakuLongClick(IDanmakus danmakus) {
                return false;
            }

            @Override
            public boolean onViewClick(IDanmakuView view) {
                /** danmakuView 被点击了 (非弹幕) */
                return false;
            }
        });

//        parser = createParser(this.getResources().openRawResource(R.raw.comments));
//        parser = createParser("http://galaxy.person.mgtv.com/rdbarrage?vid=7001&time=60&device=2");
//        parser = createParser(null);
        createParser();

        danmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
            @Override
            public void updateTimer(DanmakuTimer timer) {
            }

            @Override
            public void drawingFinished() {
            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                DanmakuRendererEntity tag = (DanmakuRendererEntity) danmaku.tag;
                Log.e(TAG, "danmakuShown: " + tag.content);
            }

            @Override
            public void prepared() {
                danmakuView.start();
            }
        });
    }

    private Handler mMainThreadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            final BaseDanmaku danmaku = (BaseDanmaku) msg.obj;
            final DanmakuRendererEntity danmakuInfo = (DanmakuRendererEntity) danmaku.tag;
            Glide.with(context)
                    .load("http://p1.hunantv.com/2/ava2_6SoyOBRDsYNkD5yOiHZAK85QbCiiuvyR.jpg")
                    .asBitmap()
                    .transform(new RoundedCornersTransformation(context, 50, 0))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            danmakuInfo.avatarBitmap = resource;
                            danmakuView.invalidateDanmaku(danmaku, false);
                        }
                    });
        }
    };

    int count = 0;
    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
            count++;
            final DanmakuRendererEntity danmakuInfo = (DanmakuRendererEntity) danmaku.tag;
            Log.e(TAG, "prepareDrawing count: " + count + ", content: " + danmakuInfo.content);


            // 加载图片
            Message msg = Message.obtain();
            msg.obj = danmaku;
            msg.what = 1;
            mMainThreadHandler.sendMessage(msg);

        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // tag包含bitmap，一定要清空
            danmaku.tag = null;
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.screenWidthDp > newConfig.screenHeightDp) {
            // 横屏
//            editBarrage.setFocusable(true);
//            editBarrage.setFocusableInTouchMode(true);
            editBarrage.requestFocus();
            InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editBarrage, InputMethodManager.SHOW_FORCED);
        } else {
            // 竖屏

        }
    }

    private void createParser() {

//        if (url == null) {
//            return new BaseDanmakuParser() {
//
//                @Override
//                protected Danmakus parse() {
//                    return new Danmakus();
//                }
//            };
//        }

        parser = new DanmakuListParser();
        loader = DanmakuJSONLoaderFactory.create(DanmakuJSONLoaderFactory.TAG_JSON);
    }
//    private BaseDanmakuParser createParser(InputStream stream) {
//
//        if (stream == null) {
//            return new BaseDanmakuParser() {
//
//                @Override
//                protected Danmakus parse() {
//                    return new Danmakus();
//                }
//            };
//        }
//
//        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
//
//        try {
//            loader.load(stream);
//        } catch (IllegalDataException e) {
//            e.printStackTrace();
//        }
//        BaseDanmakuParser parser = new BiliDanmukuParser();
//        IDataSource<?> dataSource = loader.getDataSource();
//        parser.load(dataSource);
//        return parser;
//
//    }

    public void loadBarrage(){
        final String url = "http://beta.galaxy.person.mgtv.com/rdbarrage?vid=10001&time=60";
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                try {
                    loader.load(url);
                    e.onNext("1");
                } catch (IllegalDataException error) {
                    error.printStackTrace();
                }

            }
        }).subscribeOn(Schedulers.io())               //在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  //回到主线程;
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        IDataSource<?> dataSource = loader.getDataSource();
                        parser.load(dataSource);
                        danmakuView.prepare(parser, danmakuContext);

                        DanmakuListEntity data = (DanmakuListEntity) dataSource.data();
                        DanmakuListEntity.Data danmakuData = data.data;
                        if (null == danmakuData)
                            return;

                        timer = new RequestCountDownTimer(danmakuData.interval * 1000, 1000) {
                            @Override
                            public void onFinish() {
                                // TODO: 17/5/18 请求弹幕数据
                                Log.e(TAG, "onFinish");
                            }
                        };
                        timer.start();
                    }
                });
    }

    public void reset(){
        parser.release();
    }

    public void doClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.add:
                reset();
                count = 0;
                loadBarrage();

            break;

            case R.id.pause:
                danmakuView.pause();
                timer.pause();
                break;

            case R.id.resume:
                danmakuView.resume();
                timer.resume();
                break;

            case R.id.clean:
                danmakuView.clearDanmakusOnScreen();
                break;

            case R.id.reset:
                // TODO: 17/6/7 reset
                danmakuView.removeAllDanmakus(true);
                break;
        }

//        parser = createParser(this.getResources().openRawResource(R.raw.barrage));

        Window window = getWindow();
        WindowManager windowManager = window.getWindowManager();
        Point outSize = new Point();
        windowManager.getDefaultDisplay().getSize(outSize);
        int screenWidth = outSize.x;
        int screenHeight = outSize.y;
//
        // 1066.66f : 以 1920 屏幕宽度为基准，scrollSpeedFactor 为 1.8
        float scrollSpeedFactor = screenWidth / 1066.66f;
        danmakuContext.setScrollSpeedFactor(scrollSpeedFactor);
        Log.e(TAG, "screenWidth: " + screenWidth + ", screenHeight: " + screenHeight
                + ", scrollSpeedFactor: " + scrollSpeedFactor);

        /** 不管屏幕多大， 每条弹幕生命周期为 7 秒 */
//        float scrollSpeedFactor = 7000f / DanmakuFactory.COMMON_DANMAKU_DURATION;
//        danmakuContext.setScrollSpeedFactor(scrollSpeedFactor);
//
//        addDanmaKuCustomerStuffer();
    }

    private void addDanmaKuCustomerStuffer(){
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || danmakuView == null) {
            return;
        }

        Bitmap avatarBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        // 组装需要传递给danmaku的数据
        DanmakuRendererEntity info = new DanmakuRendererEntity();
        info.content = "hello, zane";
        info.avatarBitmap = avatarBitmap;
        info.type = DanmakuRendererEntity.TYPE_AVATAR;

        danmaku.text = "";
        danmaku.tag = info;


        // 新增弹幕登场时间
        long time = danmakuView.getCurrentTime();
        danmaku.setTime(time);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.WHITE;
        danmaku.borderColor = Color.GREEN;
        danmakuView.addDanmaku(danmaku);
    }

    private void increasePraise(BaseDanmaku danmaku){
        if (!(danmaku.tag instanceof DanmakuRendererEntity)){
            Log.e(TAG, "danmaku.tag 类型必须是 DanmakuRendererInfo");
            return;
        }
        DanmakuRendererEntity tag = (DanmakuRendererEntity) danmaku.tag;
        tag.increasePraiseCount();
        tag.contentColor = Color.RED;
                danmakuView.invalidateDanmaku(danmaku, false);
    }

    private SpannableStringBuilder createSpannable(Drawable drawable) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("图文混排");
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    private void addDanmaKuShowTextAndImage(boolean islive) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
        drawable.setBounds(0, 0, 100, 100);
        SpannableStringBuilder spannable = createSpannable(drawable);
        danmaku.text = spannable;
        danmaku.padding = 5;
        danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = islive;
        danmaku.setTime(danmakuView.getCurrentTime() + 1200);
        danmaku.textSize = 25f * (parser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.WHITE;
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        danmaku.underlineColor = Color.GREEN;
        danmakuView.addDanmaku(danmaku);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (danmakuView != null) {
            // dont forget release!
            danmakuView.release();
            danmakuView = null;
        }
    }
}
