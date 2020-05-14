package com.aptoide.uploader.account.view;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.aptoide.uploader.R;
import com.aptoide.uploader.UploaderApplication;
import com.aptoide.uploader.view.android.FragmentView;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MaintenanceFragment extends FragmentView implements MaintenanceView {

  private View progressbar;
  private View maintenanceView;
  private TextView title;
  private TextView message_first;
  private TextView message_second;
  private TextView blog;
  private ImageView blogNextButton;

  public static MaintenanceFragment newInstance() {
    return new MaintenanceFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    progressbar = view.findViewById(R.id.fragment_maintenance_progressbar);
    maintenanceView = view.findViewById(R.id.fragment_maintenance_view);
    title = view.findViewById(R.id.fragment_maintenance_title);
    message_first = view.findViewById(R.id.fragment_maintenance_message1);
    message_second = view.findViewById(R.id.fragment_maintenance_message2);
    blog = view.findViewById(R.id.fragment_maintenance_blog);
    blogNextButton = view.findViewById(R.id.fragment_maintenance_blognext);
    new MaintenancePresenter(this,
        new MaintenanceNavigator(getContext().getApplicationContext(), getFragmentManager()),
        ((UploaderApplication) getContext().getApplicationContext()).getMaintenanceManager(),
        new CompositeDisposable(), AndroidSchedulers.mainThread()).present();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    progressbar = null;
    title = null;
    message_first = null;
    message_second = null;
    blog = null;
    blogNextButton = null;
    maintenanceView = null;
  }

  @Nullable @Override
  public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_maintenance, container, false);
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override public void showNoLoginView() {
    progressbar.setVisibility(View.GONE);
    maintenanceView.setVisibility(View.VISIBLE);
    title.setText(getString(R.string.login_disclaimer_unavailable_title));
    message_first.setText(getString(R.string.login_disclaimer_unavailable_body_1));
    message_second.setText(getString(R.string.login_disclaimer_unavailable_body_2));
    setupBlogTextView();
  }

  @Override public Observable<Integer> clickOnBlog() {
    return Observable.merge(RxView.clicks(blog), RxView.clicks(blogNextButton))
        .map(__ -> 1);
  }

  @Override public void hideProgressBar() {
    progressbar.setVisibility(View.GONE);
  }

  private void setupBlogTextView() {
    SpannableString content = new SpannableString(getString(R.string.login_disclaimer_blog_button));
    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
    blog.setText(content);
  }
}