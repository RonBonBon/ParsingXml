package hackeru.edu.parsingxml;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class YnetArticleFragment extends Fragment {
    WebView webView;

    public YnetArticleFragment() {
        // Required empty public constructor
    }

    public static YnetArticleFragment newInstance(String link) {

        Bundle args = new Bundle();
        args.putString("link", link);
        YnetArticleFragment fragment = new YnetArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String link = getArguments().getString("link");
        View v = inflater.inflate(R.layout.fragment_ynet_article, container, false);
        webView = (WebView) v.findViewById(R.id.webView);

        config(webView);

        webView.loadUrl(link);
        return v;
    }

    private void config(final WebView webView) {
        //Enable JavaScript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                webView.loadUrl(url);
                return true;
            }
        });
    }

}
