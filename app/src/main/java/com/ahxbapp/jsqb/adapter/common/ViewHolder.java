package com.ahxbapp.jsqb.adapter.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ahxbapp.common.ImageLoadTool;
import com.ahxbapp.jsqb.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.image.ImageOptions;
import org.xutils.x;


/**
 * 通用的ViewHolder
 *
 * @author 陈建
 */
public class ViewHolder {

    private SparseArray<View> mviews;
    private View mConverView;
    private int mPosition;
    private Context mContext;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mviews = new SparseArray<View>();
        this.mPosition = position;
        this.mContext = context;

        mConverView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConverView.setTag(this);
    }


    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public static ViewHolder getHolder(View convertView, Context context, ViewGroup parent, int layoutId, int position) {

        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    /**
     * 通过viewId返回控件
     *
     * @param viewId
     * @return View
     */
    public <T extends View> T getView(int viewId) {

        View view = mviews.get(viewId);
        if (view == null) {
            view = mConverView.findViewById(viewId);
            mviews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 给TextView设置文字
     *
     * @param viewId
     * @param text
     * @return ViewHolder
     */
    public ViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int colorRes) {
        TextView textView = getView(viewId);
        textView.setTextColor(mContext.getResources().getColor(colorRes));
        return this;
    }

    public ViewHolder setBackGround(int viewId, Drawable back) {
        TextView textView = getView(viewId);
        textView.setBackground(back);
        return this;
    }

    /**
     * 给ImageView设置图片
     *
     * @param viewId
     * @param resId
     * @return ViewHolder
     */
    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    /**
     * 给ImageView设置图片
     *
     * @param viewId
     * @param bitmap
     * @return ViewHolder
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 通过url给ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return ViewHolder
     */
    public ViewHolder setImageUrl(int viewId, String url, ImageOptions options) {
        ImageView imageView = getView(viewId);
        x.image().bind(imageView, url, options);
        //new BitmapUtils(mContext).display(imageView,url);

        return this;
    }

    /**
     * 通过url给ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return ViewHolder
     */
    public ViewHolder setImageUrl(int viewId, String url) {
        ImageView imageView = getView(viewId);
        ImageLoader.getInstance().displayImage(url, imageView, ImageLoadTool.options);

        //BitmapHelper.getInstance(mContext).displayImage(url,imageView,BitmapHelper.options);
        //new BitmapUtils(mContext).display(imageView,url);

        return this;
    }

    //设置button的上下左右的图片
    //direction： 左 上 下 右 分别代表 1.2.3.4
    public ViewHolder setButtonDrawable(int viewId, int drawableId, int direction) {
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        Button btn = getView(viewId);
        if (direction == 1) {
            btn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else if (direction == 2) {
            btn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else if (direction == 3) {
            btn.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        } else if (direction == 4) {
            btn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        return this;
    }

    public ViewHolder setHide(int viewId) {
        View view = getView(viewId);
        view.setVisibility(View.GONE);
        return this;
    }

    public ViewHolder setShow(int viewId) {
        View view = getView(viewId);
        view.setVisibility(View.VISIBLE);
        return this;
    }

    //点击按钮
    public ViewHolder clickButton(int viewId, final ViewHolderInterface.common_click block) {
        final View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                block.click(mContext, mPosition, view);
            }
        });
        return this;
    }

    public ViewHolder setListView(int viewId, BaseAdapter myadapter) {
        final ListView mylist = getView(viewId);
        mylist.setAdapter(myadapter);
        return this;
    }

    public ViewHolder setGridView(int viewId, BaseAdapter myadapter) {
        final GridView mylist = getView(viewId);
        mylist.setAdapter(myadapter);
        return this;
    }


    public ViewHolder setCheckBoxState(int viewId, boolean isChecked) {
        final CheckBox cb = getView(viewId);
        cb.setChecked(isChecked);
        return this;
    }


    public View getmConverView() {
        return mConverView;
    }

    public interface ViewHolderInterface {
        public interface common_click {
            public void click(Context context, final int position, final View vv);
        }
    }
}
