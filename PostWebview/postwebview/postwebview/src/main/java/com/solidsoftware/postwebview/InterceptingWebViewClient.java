package com.solidsoftware.postwebview;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public abstract class InterceptingWebViewClient extends WebViewClient {
    public static final String TAG = "InterceptingWebViewClient";

    private WebView mWebView;

    public InterceptingWebViewClient(WebView webView) {
        mWebView = webView;
        mWebView.addJavascriptInterface(new PostInterceptJavascriptInterface(this), "interception");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        mNextAjaxRequestContents = null;
        mNextFormRequestContents = null;

        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(final WebView view, final WebResourceRequest request) {
        if (mNextAjaxRequestContents != null) {
            return shouldInterceptAjaxRequest(view, mNextAjaxRequestContents);
        } else if (mNextFormRequestContents != null) {
            return shouldInterceptFormRequest(view, mNextFormRequestContents);
        } else {
            return shouldInterceptNormalRequest(view, request);
        }
    }

    abstract WebResourceResponse shouldInterceptNormalRequest(final WebView view, final WebResourceRequest request);

    abstract WebResourceResponse shouldInterceptAjaxRequest(final WebView view, final PostInterceptJavascriptInterface.AjaxRequestContents request);

    abstract WebResourceResponse shouldInterceptFormRequest(final WebView view, final PostInterceptJavascriptInterface.FormRequestContents request);

    private PostInterceptJavascriptInterface.FormRequestContents mNextFormRequestContents = null;

    public void nextMessageIsFormRequest(PostInterceptJavascriptInterface.FormRequestContents formRequestContents) {
        mNextFormRequestContents = formRequestContents;
    }

    private PostInterceptJavascriptInterface.AjaxRequestContents mNextAjaxRequestContents = null;

    public void nextMessageIsAjaxRequest(PostInterceptJavascriptInterface.AjaxRequestContents ajaxRequestContents) {
        mNextAjaxRequestContents = ajaxRequestContents;
    }
}
