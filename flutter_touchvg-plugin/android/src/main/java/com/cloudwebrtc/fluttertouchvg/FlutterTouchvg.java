package com.cloudwebrtc.fluttertouchvg;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.platform.PlatformView;
import rhcad.touchvg.IViewHelper;
import rhcad.touchvg.ViewFactory;

/**
 * @author kevin kang
 * FlutterTouchvg for android
 */
public class FlutterTouchvg implements PlatformView,MethodCallHandler {

  private static final String TAG = "FlutterTouchvg";
  private Registrar registrar = null;
  private MethodChannel channel = null;
  private EventChannel.EventSink eventSink = null;

  TouchVGView vgview;
  IViewHelper helper;

  public FlutterTouchvg(Context context, Registrar registrar, int id) {
    this.registrar = registrar;

    helper = ViewFactory.createHelper();
    vgview = new TouchVGView(this.getContext(), helper);

    vgview.helper().setBackgroundColor(100);

    //初始事件
    EventChannel eventChannel = new EventChannel(registrar.messenger(), "flutter_touchvg_" + id);
    eventChannel.setStreamHandler(streamHandler);

    channel = new MethodChannel(registrar.messenger(), "flutter_touchvg_" + id);
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("setCommand")) {
      String command = call.argument("command");
      Log.d(TAG, "command:" + command);
      this.setCommand(this.vgview,command);
      result.success(null);
    } else if (call.method.equals("eraseView")){
      Log.d(TAG, "android:eraseView:");
      this.eraseView();
    } else if (call.method.equals("undo")){
        Log.d(TAG, "android:undo:");
        this.undo();
    } else if (call.method.equals("redo")){
        Log.d(TAG, "android:redo:");
        this.redo();
    } else if (call.method.equals("setLineWidth")){
        Log.d(TAG, "android:setLineWidth:");
        Integer lineWidth = call.argument("lineWidth");
        Log.d(TAG, "lineWidth:" + lineWidth);
        this.setLineWidth(this.vgview,lineWidth);
        result.success(null);
    } else if (call.method.equals("setLineColor")){
        Log.d(TAG, "android:setLineColor:");
        Integer r = call.argument("r");
        Integer g = call.argument("g");
        Integer b = call.argument("b");
        Integer a = call.argument("a");
        this.setLineColor(this.vgview,r,g,b,a);
        result.success(null);
    }
    else {
      result.notImplemented();
    }
  }

  private EventChannel.StreamHandler streamHandler = new EventChannel.StreamHandler() {
    @Override
    public void onListen(Object o, EventChannel.EventSink sink) {
      eventSink = sink;
    }

    @Override
    public void onCancel(Object o) {
      eventSink = null;
    }
  };

  public Registrar registrar() {
    return this.registrar;
  }

  public Activity getActivity() {
    return registrar.activity();
  }

  public Context getContext() {
    return registrar.context();
  }

  @Override
  public View getView() {
    return vgview;
  }

  @Override
  public void dispose() {

  }

  public String getName() {
    return "TouchVGView";
  }

  public boolean canUndo() {
    return  this.helper.canUndo();
  }

  public boolean canRedo() {
    return  this.helper.canRedo();
  }

  public void undo() {
    this.helper.undo();
  }

//  @ReactMethod
//  public void snapshot(Promise promise) {
//    Bitmap image = this.helper.snapshot(false);
//    ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
//    image.compress(Bitmap.CompressFormat.JPEG, 100, imageStream);
//    WritableMap response = Arguments.createMap();
//    response.putString("base64", Base64.encodeToString(imageStream.toByteArray(), Base64.DEFAULT));
//    promise.resolve(response);
//  }

  public void redo() {
    this.helper.redo();
  }

  public void eraseView() {
    this.helper.eraseView();
  }

  public void setLineColor(int r, int g, int b, int a) {
    this.helper.setLineColor(r,g,b,a);
  }

  public void setCommand(TouchVGView view, String cmd) {
    view.helper().setCommand(cmd);
  }

  public void setLineWidth(TouchVGView view, int w) {
    view.helper().setLineWidth(w);
  }

  public void setStrokeWidth(TouchVGView view, int w) {
    view.helper().setStrokeWidth(w);
  }

  public void setLineColor(TouchVGView view,int r, int g, int b, int a) {
      view.helper().setLineColor(r,g,b,a);
  }

}
