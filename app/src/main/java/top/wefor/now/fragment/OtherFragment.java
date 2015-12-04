package top.wefor.now.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import top.wefor.now.R;
import top.wefor.now.WebActivity;
import top.wefor.now.utils.Constants;

/**
 * Created by ice on 15/10/28.
 */
public class OtherFragment extends BaseFragment {

    ObservableScrollView mScrollView;
    private View mColumnSelectView, mHeadPictureView;
    private SharedPreferences mPreferences;

    @OnClick(R.id.wiki_imageButton)
    void openWiki() {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra(WebActivity.EXTRA_TITLE, getString(R.string.wiki_title));
        intent.putExtra(WebActivity.EXTRA_URL, getString(R.string.wiki_url));
        startActivity(intent);
    }

    @Bind(R.id.js_checkBox)
    CheckBox mJsCB;
    @Bind(R.id.js_textView)
    TextView mJsTv;

    @OnCheckedChanged(R.id.js_checkBox)
    void jsChanged(boolean isChecked) {
        SharedPreferences.Editor editor = mPreferences.edit();
        if (isChecked) {
            editor.putBoolean(Constants.JAVA_SCRIPT_ENABLED, true);
            mJsTv.setText(R.string.js_close_description);
        } else {
            editor.putBoolean(Constants.JAVA_SCRIPT_ENABLED, false);
            mJsTv.setText(R.string.js_open_description);
        }
        editor.apply();
    }

    @OnClick(R.id.columnSelect_textView)
    void columnSelect() {
        if (mColumnSelectView == null) {
            mColumnSelectView = getActivity().getLayoutInflater().inflate(R.layout.dialog_column_select, null);
            LinearLayout linearLayout = (LinearLayout) mColumnSelectView.findViewById(R.id.linearLayout);
            linearLayout.addView(checkBox(getString(R.string.zcool)));
            linearLayout.addView(checkBox(getString(R.string.ng)));
            linearLayout.addView(checkBox(getString(R.string.zhihu)));
            linearLayout.addView(checkBox(getString(R.string.moment)));
        } else {
            ViewGroup parent = (ViewGroup) mColumnSelectView.getParent();
            parent.removeView(mColumnSelectView);
        }

        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.column_select))
                .setView(mColumnSelectView)
                .create().show();
    }

    private View checkBox(final String name) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.item_column_select, null);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        checkBox.setText(name);
        checkBox.setChecked(mPreferences.getBoolean(name, true));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(name, isChecked);
                editor.apply();
            }
        });
        return view;
    }

    @Bind(R.id.headPicture_textView)
    TextView mHeadPictureTv;

    @OnClick(R.id.headPicture_linearLayout)
    void choiceHeadPictureSource() {
        if (mHeadPictureView == null) {
            mHeadPictureView = getActivity().getLayoutInflater().inflate(R.layout.dialog_head_picture, null);
            RadioGroup radioGroup = (RadioGroup) mHeadPictureView.findViewById(R.id.radioGroup);
            radioGroup.check(radioGroup.getChildAt(mPreferences.getInt(Constants.COVER_SOURCE, 0)).getId());
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putInt(Constants.COVER_SOURCE, group.indexOfChild(radioButton));
                    editor.apply();
                    mHeadPictureTv.setText(radioButton.getText());
                }
            });
        } else
            ((ViewGroup) mHeadPictureView.getParent()).removeView(mHeadPictureView);

        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.head_picture))
                .setView(mHeadPictureView)
                .create().show();
    }

    @OnClick(R.id.about_textView)
    void about() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.about))
                .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_about, null))
                .setPositiveButton("个人网站", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(getString(R.string.my_website)));
                        startActivity(intent);
                    }
                })
                .create().show();
    }

    @OnClick(R.id.thanks_textView)
    void thanks() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.thanks))
                .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_thanks, null))
                .setPositiveButton("GitHub", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(getString(R.string.my_github)));
                        startActivity(intent);
                    }
                })
                .create().show();
    }

    @OnClick(R.id.suggest_linearLayout)
    void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.MY_EMAIL_QQ, Constants.MY_EMAIL_GOOGLE});
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        try {
            startActivity(Intent.createChooser(i, getString(R.string.send_email)));
        } catch (android.content.ActivityNotFoundException ex) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.send_email_failed).create().show();
        }

    }

    public static OtherFragment newInstance() {
        return new OtherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        ButterKnife.bind(this, view);

        mPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
        boolean isJSEnabled = mPreferences.getBoolean(Constants.JAVA_SCRIPT_ENABLED, true);
        //视图默认为打开  default is checked in view
        if (isJSEnabled) {
            mJsCB.setChecked(true);
            mJsTv.setText(R.string.js_close_description);
        }
        int index = mPreferences.getInt(Constants.COVER_SOURCE, 0);
        mHeadPictureTv.setText(getResources().getStringArray(R.array.head_picture_source)[index]);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScrollView = (ObservableScrollView) view.findViewById(R.id.scrollView);

        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);

    }
}
