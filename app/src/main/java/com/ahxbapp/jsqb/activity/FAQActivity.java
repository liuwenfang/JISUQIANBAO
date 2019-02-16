package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.model.FAQModel;
import com.ahxbapp.jsqb.utils.DensityUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.List;
/**
 * 常见问题
 */
@EActivity(R.layout.activity_faq)
public class FAQActivity extends BaseActivity {

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    ExpandableListView expand_list;


    private FAQModel[] modelArray;

    private ExpandableListAdapter adapter;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("常见问题");
        showDialogLoading();
        APIManager.getInstance().member_getProblem(this, new APIManager.APIManagerInterface.common_FAQlist() {
            @Override
            public void Success(Context context, List list) {
                hideProgressDialog();
                modelArray = new FAQModel[list.size()];
                int inde = 0;
                for (FAQModel m : (List<FAQModel>) list) {
                    modelArray[inde] = m;
                    inde++;
                }
                expand_list.setAdapter(adapter);
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
        adapter = new BaseExpandableListAdapter() {
            TextView getTextView_Group() {
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(FAQActivity.this, 45));
                TextView textView = new TextView(FAQActivity.this);
                //设置 textView控件的布局
                textView.setLayoutParams(lp);
                //设置该textView中的内容相对于textView的位置
                textView.setGravity(Gravity.CENTER_VERTICAL);
                //设置txtView的内边距
                textView.setPadding(80, 0, 0, 0);
                //设置文本颜色
                textView.setTextColor(getResources().getColor(R.color.text_black));
                return textView;
            }

            TextView getTextView() {
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView textView = new TextView(FAQActivity.this);
                //设置 textView控件的布局
                textView.setLayoutParams(lp);
                //设置该textView中的内容相对于textView的位置
                textView.setGravity(Gravity.CENTER_VERTICAL);
                //设置txtView的内边距

                textView.setPadding(50, 10, 0, 10);
                //设置文本颜色
                textView.setTextColor(getResources().getColor(R.color.text_grey));
                return textView;
            }

            //取得分组数
            @Override
            public int getGroupCount() {
                return modelArray.length;
            }

            //取得指定分组的子元素数
            @Override
            public int getChildrenCount(int groupPosition) {
                return 1;
            }

            //取得与给定分组关联的数据
            @Override
            public Object getGroup(int groupPosition) {
                FAQModel model = modelArray[groupPosition];
                return model.getTitle();
            }

            //取得与给定分组子视图关联的数据
            @Override
            public Object getChild(int groupPosition, int childPosition) {
                FAQModel model = modelArray[groupPosition];
                return model.getContents();
            }

            //取得指定分组的ID.该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            //取得给定分组中给定子视图的ID. 该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                //定义一个TextView
                TextView textView = getTextView_Group();
                textView.setTextSize(16);
                textView.setText(modelArray[groupPosition].getTitle());
                return textView;
            }

            //取得显示给定分组给定子位置的数据用的视图
            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                //定义一个TextView
                TextView textView = getTextView();
                textView.setTextSize(16);
                textView.setText(modelArray[groupPosition].getContents());
                return textView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }
        };


        //为ExpandableListView的子列表单击事件设置监听器
        expand_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                // TODO Auto-generated method stub
//                Toast.makeText(FAQActivity.this, "你单击了："
//                        +adapter.getChild(groupPosition, childPosition), Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }
}
