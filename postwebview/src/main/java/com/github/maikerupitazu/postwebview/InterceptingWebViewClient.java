package com.github.maikerupitazu.postwebview;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public abstract class InterceptingWebViewClient extends WebViewClient {
    public static final String TAG = "InterceptingWebViewClient";

    public void init(WebView webView) {
        webView.addJavascriptInterface(new PostInterceptJavascriptInterface(this), "interception");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        mNextAjaxRequestContents = null;
        mNextFormRequestContents = null;

        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    final public WebResourceResponse shouldInterceptRequest(final WebView view, final WebResourceRequest request) {
        if (mNextAjaxRequestContents != null) {
            return shouldInterceptAjaxRequest(view, request, mNextAjaxRequestContents);
        } else if (mNextFormRequestContents != null) {
            return shouldInterceptFormRequest(view, request, mNextFormRequestContents);
        } else {
            return shouldInterceptNormalRequest(view, request);
        }
    }

    public WebResourceResponse shouldInterceptNormalRequest(final WebView view, final WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    public WebResourceResponse shouldInterceptAjaxRequest(final WebView view, final WebResourceRequest request, final PostInterceptJavascriptInterface.AjaxRequestContents ajaxRequest) {
        return super.shouldInterceptRequest(view, request);
    }

    public WebResourceResponse shouldInterceptFormRequest(final WebView view, final WebResourceRequest request, final PostInterceptJavascriptInterface.FormRequestContents formRequest) {
        return super.shouldInterceptRequest(view, request);
    }

    private PostInterceptJavascriptInterface.FormRequestContents mNextFormRequestContents = null;

    void nextMessageIsFormRequest(PostInterceptJavascriptInterface.FormRequestContents formRequestContents) {
        mNextFormRequestContents = formRequestContents;
    }

    private PostInterceptJavascriptInterface.AjaxRequestContents mNextAjaxRequestContents = null;

    void nextMessageIsAjaxRequest(PostInterceptJavascriptInterface.AjaxRequestContents ajaxRequestContents) {
        mNextAjaxRequestContents = ajaxRequestContents;
    }
}