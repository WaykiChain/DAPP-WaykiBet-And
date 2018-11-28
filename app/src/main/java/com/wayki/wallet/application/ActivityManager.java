package com.wayki.wallet.application;

import android.app.Activity;
import android.content.Context;

import java.util.Iterator;
import java.util.Stack;

/**
 * activity管理
 */
public class ActivityManager {
    private static Stack<Activity> activityStack = new Stack<>();

    private ActivityManager() {

    }

    private static class ManagerHolder {
        private static final ActivityManager instance = new ActivityManager();
    }

    public static ActivityManager create() {
        return ManagerHolder.instance;
    }

    /**
     * 获取当前栈里面activity的数量
     * @return
     */
    public int getCount() {
        if(activityStack!=null&&!activityStack.isEmpty()) {
            return activityStack.size();
        } else {
            return 0;
        }
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }
    /**
     * 从栈中移除Activity
     */
    public void removeActivity(Activity activity){
        if(activity!=null){
            activityStack.remove(activity);
        }
    }
    /**
     * 获取当前Activity（栈顶Activity）
     */
    public Activity topActivity() {
        if (activityStack == null) {
            throw new NullPointerException(
                    "Activity stack is Null,your Activity must extend BaseActivity");
        }
        if (activityStack.isEmpty()) {
            return null;
        }
        return activityStack.lastElement();
    }

    /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    public Activity findActivity(Class<?> cls) {
        Activity activity = null;
        for (Activity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return (Activity) activity;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
        //如果它的大小为0的话,则会报NoSuchElement异常,已经没有元素了
        if(!activityStack.isEmpty()){
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        Iterator<Activity> itr = activityStack.iterator();
        while (itr.hasNext()) {
            Activity activity = (Activity) itr.next();
            if(activity.getClass().equals(cls)) {
                itr.remove();
                activity.finish();
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        Iterator<Activity> itr = activityStack.iterator();
        while (itr.hasNext()) {
            Activity activity = (Activity) itr.next();
            if(!activity.getClass().equals(cls)) {
                itr.remove();
                activity.finish();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack==null) {
            return;
        }
        Iterator<Activity> itr = activityStack.iterator();
        while (itr.hasNext()) {
            Activity activity = (Activity) itr.next();
            if(activity!=null)
                activity.finish();
        }
        activityStack.clear();
    }

    /**
     * 应用程序退出
     *
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            Runtime.getRuntime().exit(-1);
        }
    }
}
