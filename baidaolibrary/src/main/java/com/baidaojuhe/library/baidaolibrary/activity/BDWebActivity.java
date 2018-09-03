/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;
import com.baidaojuhe.library.baidaolibrary.util.BDLayout;
import com.baidaojuhe.library.baidaolibrary.util.BDUtils;

import net.box.app.library.util.IAppUtils;

/**
 * 2015年12月10日 下午5:03:17
 * <p>
 * 用来显示网页
 */
@SuppressWarnings("deprecation")
public class BDWebActivity extends BDActionBarActivity implements DownloadListener, WebView.FindListener {

    private WebView mWebView;

    @Override
    public void onInitViews(@NonNull Bundle arguments, Bundle savedInstanceState) {
        mWebView = IViewCompat.findById(this, R.id.bd_vb_content);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onInitDatas(@NonNull Bundle arguments, Bundle savedInstanceState) {
        setTitle(getBundle().getString(BDConstants.BDKey.KEY_TITLE, getString(R.string.app_name)));
        String dir = getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(dir);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        setWebViewCommonAttribute(mWebView);

        mWebView.setFocusableInTouchMode(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受证书
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showText(error.getDescription());
                } else {
                    showText(R.string.bd_prompt_webview_load_failure);
                }
                loadDismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showText(description);
                loadDismiss();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    showText(errorResponse.getReasonPhrase());
                } else {
                    showText(R.string.bd_prompt_webview_load_failure);
                }
                loadDismiss();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadDismiss();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    loadDismiss();
                }
                super.onProgressChanged(view, newProgress);
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
                //    Log.e(APP_CACHE, "onReachedMaxAppCacheSize reached, increasing space: " + spaceNeeded);
                quotaUpdater.updateQuota(spaceNeeded * 2);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                loadDismiss();
            }
        });
        mWebView.setFindListener(this);
        mWebView.setDownloadListener(this);

        loadUrl(getBundle().getString(BDConstants.BDKey.KEY_CONTENT));
    }

    @Override
    public void onInitListeners(@NonNull Bundle arguments, Bundle savedInstanceState) {
    }

    @Override
    public BDLayout getContainerLayout(@NonNull Bundle arguments, @Nullable Bundle savedInstanceState) {
        return BDLayout.create(R.layout.bd_activity_web);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        loadDismiss();
        Uri uri = Uri.parse(url);
        Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(downloadIntent);
        finish();
    }

    @Override
    public void onFindResultReceived(int activeMatchOrdinal, int numberOfMatches, boolean isDoneCounting) {
        loadDismiss();
    }

    private void loadUrl(String url) {
        if (TextUtils.isEmpty(url) || !IAppUtils.isNetPath(url)) {
            showText(R.string.bd_prompt_webview_load_failure);
            finish();
            return;
        }
        try {
            Uri uri = Uri.parse(url);
            if (uri.getHost().contains("weixin.qq.com")) {
                BDUtils.startWechat(this);
                finish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showLoad().show();
        mWebView.loadUrl(url);
    }
}
