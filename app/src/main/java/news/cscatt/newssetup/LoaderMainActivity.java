package news.cscatt.newssetup;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class LoaderMainActivity extends AppCompatActivity {

    ProgressBar pBar;
    ImageView iV;
    TextView tV;

    private ProgressBar progressBar;
    private onbuttonclickHttpPost mTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        downloadPage();

        ListView lv=(ListView)findViewById(R.id.listView);
    }

    private void downloadPage() {

        String csMobile = Context.CONNECTIVITY_SERVICE;  // проверка интернет соеденения
        ConnectivityManager cmMobile = (ConnectivityManager) getSystemService(csMobile);
        cmMobile.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        String csWifi = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cmWifi = (ConnectivityManager) getSystemService(csWifi);
        cmWifi.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        if (cmMobile.getActiveNetworkInfo() == null
                || cmWifi.getActiveNetworkInfo() == null) {
            Toast.makeText(
                    LoaderMainActivity.this,
                    "Отсутствует подключение к интернету. Проверьте настройки",
                    Toast.LENGTH_LONG).show();
        } else {
            if (mTask != null
                    && mTask.getStatus() != onbuttonclickHttpPost.Status.FINISHED) {
                mTask.cancel(true);
            }

            mTask = (onbuttonclickHttpPost) new onbuttonclickHttpPost().execute();

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTask != null
                && mTask.getStatus() != onbuttonclickHttpPost.Status.FINISHED) {
            mTask.cancel(true);
            mTask = null;
        }
    }

    /********/
    public class onbuttonclickHttpPost extends AsyncTask<String, Void, Void> {
        public class execute {
        }

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            byte[] result = null;
            String str = "";

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(
                    "http://24.kg/rss/all/");
            DataBase db = new DataBase(LoaderMainActivity.this);


            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                String responseStr = EntityUtils.toString(response.getEntity());

                StringReader stringReader = new StringReader(responseStr);
                InputSource inputSource = new InputSource(stringReader);
                DocumentBuilderFactory dbfact = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder builder = null;

                DocumentBuilder builderCompany = null;

                try {
                    builder = dbfact.newDocumentBuilder();
                } catch (ParserConfigurationException ex) {
                    ex.printStackTrace();
                }

                Document docCompany = null;
                Document doc = null;

                try {
                    doc = (Document) builder.parse(inputSource);
                    doc.getDocumentElement().normalize();

                    NodeList nodeLstNews = doc.getElementsByTagName("item");

                    for (int s = 0; s < nodeLstNews.getLength(); s++) {

                        Node fstNode = nodeLstNews.item(s);

                        if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element fstElmnt = (Element) fstNode;
                            NodeList fstNmElmntLst = fstElmnt
                                    .getElementsByTagName("title");
                            Element fstNmElmnt = (Element) fstNmElmntLst
                                    .item(0);
                            NodeList fstNm = fstNmElmnt.getChildNodes();

                            NodeList secNmElmntLst = fstElmnt
                                    .getElementsByTagName("link");
                            Element secNmElmnt = (Element) secNmElmntLst
                                    .item(0);
                            NodeList secNm = secNmElmnt.getChildNodes();

                            NodeList thrdNmElmntLst = fstElmnt
                                    .getElementsByTagName("description");
                            Element thrdNmElmnt = (Element) thrdNmElmntLst
                                    .item(0);
                            NodeList thrdNm = thrdNmElmnt.getChildNodes();

                            NodeList frNmElmntLst = fstElmnt
                                    .getElementsByTagName("comments");
                            Element frNmElmnt = (Element) frNmElmntLst.item(0);
                            NodeList frNm = frNmElmnt.getChildNodes();

                            NodeList fvNmElmntLst = fstElmnt
                                    .getElementsByTagName("description");
                            Element fvNmElmnt = (Element) fvNmElmntLst.item(0);
                            NodeList fvNm = fvNmElmnt.getChildNodes();


                            NodeList sixNmElmntLst = fstElmnt
                                    .getElementsByTagName("pubDate");
                            Element sixNmElmnt = (Element) sixNmElmntLst.item(0);
                            NodeList sixNm = sixNmElmnt.getChildNodes();

                            NodeList sevenNmElmntLst = fstElmnt
                                    .getElementsByTagName("guid");
                            Element sevenNmElmnt = (Element) sevenNmElmntLst.item(0);
                            NodeList sevenNm = sevenNmElmnt.getChildNodes();
                            String t;

                            if (((Node) fstNm.item(0))
                                    .getNodeValue() != null) {
                                t = ((Node) fstNm.item(0))
                                        .getNodeValue().toString();
                            } else {
                                t = "";
                            }
                            News newspro = new News(t, ((Node) sixNm.item(0)).getNodeValue()
                                            .toString(),
                                    "",//изображение
                                    "",
                                    "",
                                    "",//категория
                                    "24.kg"//source
                            );
                            Log.d("MainAct", newspro.toString());
                            db.addNews(newspro);
                        }
                    }

                } catch (SAXException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }

            return null;
        }

        /**
         * on getting result
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            progressBar.setProgress(0);

            Intent i = new Intent(LoaderMainActivity.this, NavigationDrawerActivity.class);
            startActivity(i);
            finish();


        }
    }


    public void move(View v) { //Роли не играет
        Intent i = new Intent(LoaderMainActivity.this, NavigationDrawerActivity.class);
        startActivity(i);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_loader_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
