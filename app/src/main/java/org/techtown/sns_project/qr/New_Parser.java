package org.techtown.sns_project.qr;

import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class New_Parser {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    String URL;

    public New_Parser(FirebaseAuth firebaseAuth, FirebaseUser user, FirebaseFirestore db, String URL) {
        this.firebaseAuth = firebaseAuth;
        this.user = user;
        this.db = db;
        this.URL = URL;
        this.getData();
    }

    private void getData() {
        CodiJsoup jsoupAsyncTask = new CodiJsoup();
        jsoupAsyncTask.execute();
    }

    private class CodiJsoup extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Document doc = null;
            try {
                doc = Jsoup.connect(URL).get();
            } catch (IOException e) {
                e.printStackTrace();
            }


            final Elements productImg = doc.select("div[class=product-img] img");
            //상품 사진

            final Elements productINFO= doc.select("span[class=product_title]");
            //상품 제품명
            System.out.println("제품 명 : " +productINFO.text());

            final Elements productBrand= doc.select("div[class=explan_product product_info_section] ul p[class=product_article_contents] a");
            //상품 회사 및 태그
            String title = null;
            int count=0;
            for (Element element : productBrand){
                if(count==0){
                    title =  element.text();}
                else if(count>1)
                {
                    System.out.println("HashTag : "+element.text());
                }
                count++;
            }

            final Elements productPrice= doc.select("div[class=member_price] ul li");

            String product_price = productPrice.select("span[class=txt_price_member m_list_price]").first().text();
            //상품 가격



            ProductInfo pi = new ProductInfo(URL, "https:"+productImg.attr("src"), title
                    , productINFO.text(), product_price);
            db.collection("InfoFromURL").document(user.getUid()).set(pi);


            return null;
        }
    }
}